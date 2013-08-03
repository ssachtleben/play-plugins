import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.apache.commons.lang.RandomStringUtils;

import com.ssachtleben.play.plugin.event.EventBus;
import com.ssachtleben.play.plugin.event.EventService;
import com.ssachtleben.play.plugin.event.Events;
import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * Provides important functionality for all {@link Events} releated tests.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class EventTest {

	public static final String TEST_TOPIC = RandomStringUtils.randomAlphabetic(10);

	/**
	 * Keeps instance of {@link EventBus}.
	 */
	private EventBus events;

	/**
	 * Provides events instance.
	 * <p>
	 * Usually {@link Events} provides a instance of {@link EventService}, but for the tests we use {@link EventBus} for accessing additional
	 * informations.
	 * </p>
	 * 
	 * @return Provides events instance as {@link EventBus}.
	 */
	protected EventBus events() {
		if (events == null) {
			assertThat(Events.instance()).isInstanceOf(EventBus.class);
			events = (EventBus) Events.instance();
		}
		return events;
	}

	/**
	 * Get observer method from this class.
	 * 
	 * @param name
	 *          The name to set
	 * @param params
	 *          The params to set
	 * @return A {@link Method}.
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	protected Method getObserver(String name, Class<?> params) throws NoSuchMethodException, SecurityException {
		Method method = this.getClass().getMethod(name, params);
		assertThat(method).isNotNull();
		return method;
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
