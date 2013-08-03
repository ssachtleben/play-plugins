import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.apache.commons.lang.RandomStringUtils;

import com.ssachtleben.play.plugin.event.EventBus;
import com.ssachtleben.play.plugin.event.Events;
import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * Provides important functionality for all {@link Events} releated tests.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class EventsTest {

	public static final String TEST_TOPIC = RandomStringUtils.randomAlphabetic(10);

	private EventBus events;

	protected EventBus events() {
		if (events == null) {
			assertThat(Events.instance()).isInstanceOf(EventBus.class);
			events = (EventBus) Events.instance();
		}
		return events;
	}

	protected Method getObserver(String name, Class<?> params) throws NoSuchMethodException, SecurityException {
		return getObserver(name, new Class<?>[] { params });
	}

	protected Method getObserver(String name, Class<?>... params) throws NoSuchMethodException, SecurityException {
		Method method = this.getClass().getMethod(name, params);
		assertThat(method).isNotNull();
		return method;
	}

	@Observer(referenceStrength = ReferenceStrength.STRONG)
	public static void observeString(String val) {
		assertThat(val).isEqualTo("Test");
	}

	@Observer(referenceStrength = ReferenceStrength.STRONG)
	public static void observeString2(String val) {
		assertThat(val).isEqualTo("Test");
	}

}
