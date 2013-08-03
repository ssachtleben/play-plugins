import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.ssachtleben.play.plugin.event.EventBinding;
import com.ssachtleben.play.plugin.event.EventBus;

/**
 * Checks all eventbus unregister functionality.
 * 
 * @author Sebastian Sachtleben
 */
public class EventsUnregisterTests extends EventsTest {

	/**
	 * Adds two new subscriber, unregister one and check if one subscriber is left.
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@Test
	public void unregisterObject() throws NoSuchMethodException, SecurityException {
		unregisterAll();
		EventBinding binding1 = events().register(getObserver("observeString", String.class));
		EventBinding binding2 = events().register(getObserver("observeString2", String.class));
		assertThat(events().getSubscribers()).hasSize(1);
		assertThat(events().getSubscribers().get(EventBus.EMPTY_TOPIC)).hasSize(2);
		events().unregister(binding1.method());
		assertThat(events().getSubscribers().get(EventBus.EMPTY_TOPIC)).hasSize(1);
		assertThat(events().getSubscribers().get(EventBus.EMPTY_TOPIC).contains(binding1)).isFalse();
		assertThat(events().getSubscribers().get(EventBus.EMPTY_TOPIC).contains(binding2)).isTrue();
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
