import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.ssachtleben.play.plugin.event.EventBus;

/**
 * Checks all eventbus unregister functionality.
 * 
 * @author Sebastian Sachtleben
 */
public class EventsUnregisterTests extends EventTest {

	/**
	 * Adds two new subscriber, unregister one and check if one subscriber is left.
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@Test
	public void unregisterObject() throws NoSuchMethodException, SecurityException {
		unregisterAll();
		Method method1 = getObserver("observeString", String.class);
		events().register(method1);
		Method method2 = getObserver("observeString2", String.class);
		events().register(method2);
		assertThat(events().getSubscribers()).hasSize(1);
		assertThat(events().getSubscribers().get(EventBus.EMPTY_TOPIC)).hasSize(2);
		events().unregister(method1);
		assertThat(events().getSubscribers().get(EventBus.EMPTY_TOPIC)).hasSize(1);
		assertThat(events().getSubscribers().get(EventBus.EMPTY_TOPIC).contains(method1)).isFalse();
		assertThat(events().getSubscribers().get(EventBus.EMPTY_TOPIC).contains(method2)).isTrue();
	}

	/**
	 * Adds new subscriber, unregister all subscribers and check the subscribers amount which should be 0.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Test
	public void checkUnregisterAll() throws NoSuchMethodException, SecurityException {
		events().register(getObserver("observeString", String.class));
		events().register(TEST_TOPIC, getObserver("observeString2", String.class));
		assertThat(events().getSubscribers().isEmpty()).isFalse();
		unregisterAll();

	}

	/**
	 * Unregister all subscribers and validate subscribers map afterwards.
	 */
	protected void unregisterAll() {
		events().unregisterAll();
		assertThat(events().getSubscribers().isEmpty()).isTrue();
	}
}
