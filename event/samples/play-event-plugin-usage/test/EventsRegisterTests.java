import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.ssachtleben.play.plugin.event.EventBus;
import com.ssachtleben.play.plugin.event.Events;
import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * Checks all eventbus register functionality.
 * 
 * @author Sebastian Sachtleben
 */
public class EventsRegisterTests {

	/**
	 * Register new subscriber and check subscriber map if entry is available.
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@Test
	public void registerObject() throws NoSuchMethodException, SecurityException {
		assertThat(Events.instance()).isInstanceOf(EventBus.class);
		EventBus events = (EventBus) Events.instance();
		events.unregisterAll();
		assertThat(events.getSubscribers().isEmpty()).isTrue();
		Method method = this.getClass().getMethod("observeString", String.class);
		assertThat(method).isNotNull();
		events.register(method);
		assertThat(events.getSubscribers().get(EventBus.EMPTY_TOPIC)).hasSize(1);
		Method method2 = this.getClass().getMethod("observeString2", String.class);
		assertThat(method2).isNotNull();
		events.register(method2);
		assertThat(events.getSubscribers().get(EventBus.EMPTY_TOPIC)).hasSize(2);
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
