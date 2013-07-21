package com.ssachtleben.play.plugin.event;

/**
 * Events provides an instance of {@link EventService for publishing events,
 * register new and unregister existing subscribers.
 * <p/>
 * Its planed to allow swapping the EventService implementation via a
 * configuration property.
 * 
 * @author Sebastian Sachtleben
 * @see EventService
 */
public class Events {

  /**
   * Static instance of the current EventService implementation.
   */
  private static EventService instance = new EventBus();

  /**
   * Returns current EventService implementation instance which is @{link
   * EventBus}.
   * 
   * @return The EventService instance.
   */
  public static EventService instance() {
    return instance;
  }

}
