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

	/**
	 * Publishes an event without any subscriber...
	 */
	@Test
	public void publishWithoutSubscriber() {
		events().unregisterAll();
		EventResult result = events().publish("Test");
		assertThat(result.isPublished()).isFalse();
		assertThat(result.getReceivers()).hasSize(0);
	}
}
