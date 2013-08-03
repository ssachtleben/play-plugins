import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.ssachtleben.play.plugin.event.EventResult;

/**
 * Checks all eventbus publish functionality.
 * 
 * @author Sebastian Sachtleben
 */
public class EventsPublishTests extends EventTest {

	@Test
	public void publishObjectEvent() throws NoSuchMethodException, SecurityException {
		events().unregisterAll();
		events().register(getObserver("observeString", String.class));
		EventResult result = events().publish("Test");
		assertThat(result.isPublished()).isTrue();
		assertThat(result.getReceivers()).hasSize(1);
	}

	@Test
	public void publishTopicEvent() throws NoSuchMethodException, SecurityException {
		events().unregisterAll();
		events().register(TEST_TOPIC, getObserver("observeString", String.class));
		EventResult result = events().publish(TEST_TOPIC, "Test");
		assertThat(result.isPublished()).isTrue();
		assertThat(result.getReceivers()).hasSize(1);
	}

	/**
	 * Publishes an event without any subscriber...
	 */
	@Test
	public void publishObjectEventWithoutSubscriber() {
		events().unregisterAll();
		EventResult result = events().publish("Test");
		assertThat(result.isPublished()).isFalse();
		assertThat(result.getReceivers()).hasSize(0);
	}

	/**
	 * Publishes an event to a specific topic without any subscriber...
	 */
	@Test
	public void publishTopicEventWithoutSubscriber() {
		events().unregisterAll();
		EventResult result = events().publish(TEST_TOPIC, "Test");
		assertThat(result.isPublished()).isFalse();
		assertThat(result.getReceivers()).hasSize(0);
	}
}
