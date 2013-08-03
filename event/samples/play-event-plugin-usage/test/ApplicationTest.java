import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.ssachtleben.play.plugin.event.EventBus;
import com.ssachtleben.play.plugin.event.Events;
import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * Checks all eventbus functionality and keeps secure that everything is working as excepted.
 * 
 * @author Sebastian Sachtleben
 */
public class ApplicationTest {

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
		assertThat(events.getSubscribers().isEmpty()).isFalse();
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

}
