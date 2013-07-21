package com.ssachtleben.play.plugin.event;

/**
 * Events provides publication and subscription services.
 * 
 * @author Sebastian Sachtleben
 */
public class Events {

  /**
   * Static instance of the current EventBus service.
   */
  private static EventService instance = new EventBus();

  /**
   * Returns current EventBus instance.
   * 
   * @return The EventBus instance.
   */
  public static EventService instance() {
    return instance;
  }

}
