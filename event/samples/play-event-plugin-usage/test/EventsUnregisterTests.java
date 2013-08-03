import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.ssachtleben.play.plugin.event.EventBus;
import com.ssachtleben.play.plugin.event.Events;
import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * Checks all eventbus unregister functionality.
 * 
 * @author Sebastian Sachtleben
 */
public class EventsUnregisterTests {

	/**
	 * Adds two new subscriber, unregister one and check if one subscriber is left.
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@Test
	public void unregisterObject() throws NoSuchMethodException, SecurityException {
		assertThat(Events.instance()).isInstanceOf(EventBus.class);
		EventBus events = (EventBus) Events.instance();
		// Unregister all events
		events.unregisterAll();
		assertThat(events.getSubscribers().isEmpty()).isTrue();
		// Register subscriber 1
		Method method = this.getClass().getMethod("observeString", String.class);
		assertThat(method).isNotNull();
		events.register(method);
		assertThat(events.getSubscribers().isEmpty()).isFalse();
		// Register subscriber 2
		Method method2 = this.getClass().getMethod("observeString2", String.class);
		assertThat(method2).isNotNull();
		events.register(method2);
		assertThat(events.getSubscribers()).hasSize(1);
		assertThat(events.getSubscribers().get(EventBus.EMPTY_TOPIC)).hasSize(2);
		// Unregister subscriber 1
		events.unregister(method);
		assertThat(events.getSubscribers().get(EventBus.EMPTY_TOPIC)).hasSize(1);
		assertThat(events.getSubscribers().get(EventBus.EMPTY_TOPIC).contains(method)).isFalse();
		assertThat(events.getSubscribers().get(EventBus.EMPTY_TOPIC).contains(method2)).isTrue();
	}

	/**
	 * Adds new subscriber, unregister all subscribers and check the subscribers amount which should be 0.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Test
	public void checkUnregisterAll() throws NoSuchMethodException, SecurityException {
		assertThat(Events.instance()).isInstanceOf(EventBus.class);
		EventBus events = (EventBus) Events.instance();
		events.unregisterAll();
		assertThat(events.getSubscribers().isEmpty()).isTrue();
		Method method = this.getClass().getMethod("observeString", String.class);
		assertThat(method).isNotNull();
		events.register(method);
		Method method2 = this.getClass().getMethod("observeString2", String.class);
		assertThat(method2).isNotNull();
		events.register(method2);
		assertThat(events.getSubscribers().isEmpty()).isFalse();
		events.unregisterAll();
		assertThat(events.getSubscribers().isEmpty()).isTrue();

	}

	/**
	 * Publishes an event without any subscriber...
	 */
	@Test
	public void publishWithoutSubscriber() {
		Events.instance().unregisterAll();
		Events.instance().publish("Test");
	}

	@Observer
	public static void observeString(String val) {
		assertThat(val).isEqualTo("Test");
	}

	@Observer
	public static void observeString2(String val) {
		assertThat(val).isEqualTo("Test");
	}
}
