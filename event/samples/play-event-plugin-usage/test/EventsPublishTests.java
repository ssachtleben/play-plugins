import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Checks all eventbus publish functionality.
 * 
 * @author Sebastian Sachtleben
 */
public class EventsPublishTests extends EventTest {

	@Test
	public void publishObjectEvent() {
		// TODO: Implement publish async events tests...
		assertThat(true).isTrue();
	}

	/**
	 * Publishes an event without any subscriber...
	 */
	@Test
	public void publishWithoutSubscriber() {
		events().unregisterAll();
		events().publish("Test");
	}
}
