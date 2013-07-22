package com.ssachtleben.play.plugin.event;

/**
 * Provides via {@link #instance()} the current used {@link EventService}
 * implementation which allows to publishing events, register new and unregister
 * existing subscribers.
 * <p/>
 * Its planed to allow custom implementations of {@link EventService} and serve
 * them here, but its not possible right now. The implemented instance is always
 * {@link EventBus}.
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
