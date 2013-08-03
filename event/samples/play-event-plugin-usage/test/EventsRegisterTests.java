import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.ssachtleben.play.plugin.event.EventBus;

/**
 * Checks all eventbus register functionality.
 * 
 * @author Sebastian Sachtleben
 */
public class EventsRegisterTests extends EventsTest {

	/**
	 * Register new subscriber and check subscriber map if entry is available.
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@Test
	public void registerObject() throws NoSuchMethodException, SecurityException {
		int size = events().getSubscribers().containsKey(EventBus.EMPTY_TOPIC) ? events().getSubscribers().get(EventBus.EMPTY_TOPIC).size() : 0;
		events().register(getObserver("observeString", String.class));
		assertThat(events().getSubscribers().get(EventBus.EMPTY_TOPIC)).hasSize(size + 1);
	}

	/**
	 * Register new subscriber to a specific topic and check subscriber map if entry is available.
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@Test
	public void registerTopic() throws NoSuchMethodException, SecurityException {
		assertThat(TEST_TOPIC).isNotEmpty().isNotNull();
		int size = events().getSubscribers().containsKey(TEST_TOPIC) ? events().getSubscribers().get(TEST_TOPIC).size() : 0;
		events().register(TEST_TOPIC, getObserver("observeString", String.class));
		assertThat(events().getSubscribers().get(TEST_TOPIC)).hasSize(size + 1);
	}
}
