import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.ssachtleben.play.plugin.event.EventResult;
import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * Checks all eventbus publish functionality.
 * 
 * @author Sebastian Sachtleben
 */
public class EventsPublishTests extends EventsTest {

	@Test
	public void publishObjectEvent() throws NoSuchMethodException, SecurityException {
		events().unregisterAll();
		events().register(getObserver("observeString", String.class));
		EventResult result = events().publish("Test");
		assertThat(result.isPublished()).isTrue();
		assertThat(result.getReceivers()).hasSize(1);
	}

	@Test
	public void publishObjectEventAdvanced() throws NoSuchMethodException, SecurityException {
		events().unregisterAll();
		events().register(getObserver("observeString", String.class));
		events().register(getObserver("observeIntLongString", Integer.class, Long.class, String.class));
		events().register(TEST_TOPIC, getObserver("observeIntLongString2", Integer.class, Long.class, String.class));
		// Publish event with topic and signature which has one subscribers...
		EventResult result = events().publish("Test");
		assertThat(result.isPublished()).isTrue();
		assertThat(result.getReceivers()).hasSize(1);
		// Publish event with topic and signature which has two subscribers...
		result = events().publish(TEST_TOPIC, 1, 10l, "Test");
		assertThat(result.isPublished()).isTrue();
		assertThat(result.getReceivers()).hasSize(1);
		// Publish event with topic and signature which has no subscribers...
		result = events().publish(TEST_TOPIC, 1);
		assertThat(result.isPublished()).isFalse();
		assertThat(result.getReceivers()).hasSize(0);
		// Publish event without topic, it has no subscriber and should not be delivered...
		result = events().publish(1);
		assertThat(result.isPublished()).isFalse();
		assertThat(result.getReceivers()).hasSize(0);
	}

	@Test
	public void publishTopicEvent() throws NoSuchMethodException, SecurityException {
		events().unregisterAll();
		events().register(TEST_TOPIC, getObserver("observeString", String.class));
		EventResult result = events().publish(TEST_TOPIC, "Test");
		assertThat(result.isPublished()).isTrue();
		assertThat(result.getReceivers()).hasSize(1);
	}

	@Test
	public void publishTopicEventAdvanced() throws NoSuchMethodException, SecurityException {
		events().unregisterAll();
		events().register(TEST_TOPIC, getObserver("observeString", String.class));
		events().register(TEST_TOPIC, getObserver("observeIntLongString", Integer.class, Long.class, String.class));
		events().register(TEST_TOPIC, getObserver("observeIntLongString2", Integer.class, Long.class, String.class));
		// Publish event with topic and signature which has one subscribers...
		EventResult result = events().publish(TEST_TOPIC, "Test");
		assertThat(result.isPublished()).isTrue();
		assertThat(result.getReceivers()).hasSize(1);
		// Publish event with topic and signature which has two subscribers...
		result = events().publish(TEST_TOPIC, 1, 10l, "Test");
		assertThat(result.isPublished()).isTrue();
		assertThat(result.getReceivers()).hasSize(2);
		// Publish event with topic and signature which has no subscribers...
		result = events().publish(TEST_TOPIC, 1);
		assertThat(result.isPublished()).isFalse();
		assertThat(result.getReceivers()).hasSize(0);
		// Publish event without topic, it has no subscriber and should not be delivered...
		result = events().publish(1);
		assertThat(result.isPublished()).isFalse();
		assertThat(result.getReceivers()).hasSize(0);
	}

	// @Test
	public void publishTopicEventWeakReference() throws NoSuchMethodException, SecurityException {
		events().unregisterAll();
		events().register(TEST_TOPIC, getObserver("observeWeakReference", Integer.class, Long.class, String.class));
		EventResult result = events().publish(TEST_TOPIC, 1, "Test", "Test");
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

	@Observer
	public static void observeIntLongString(Integer i, Long l, String s) {
		assertThat(i).isEqualTo(1);
		assertThat(l).isEqualTo(10l);
		assertThat(s).isEqualTo("Test");
	}

	@Observer
	public static void observeIntLongString2(Integer i, Long l, String s) {
		assertThat(i).isEqualTo(1);
		assertThat(l).isEqualTo(10l);
		assertThat(s).isEqualTo("Test");
	}

	@Observer(strength = ReferenceStrength.WEAK)
	public static void observeWeakReference(Integer i, Long l, String s) {
		assertThat(i).isEqualTo(1);
		assertThat(l).isEqualTo(null);
		assertThat(s).isEqualTo("Test");
	}

	@Observer(strength = ReferenceStrength.STRONG)
	public static void observeStrongReference(Integer i, Long l, String s) {
		assertThat(i).isEqualTo(1);
		assertThat(l).isEqualTo(10l);
		assertThat(s).isEqualTo("Test");
	}
}
