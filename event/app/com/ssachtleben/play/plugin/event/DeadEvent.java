package com.ssachtleben.play.plugin.event;

/**
 * Wraps a published event which couldn't delivered due no matching subscribers.
 * <p/>
 * Subscribing a handler to observe published DeadEvent's is useful for
 * detecting misconfiguration or logic errors.
 * 
 * @author Sebastian Sachtleben
 */
public class DeadEvent {

  private final Object source;
  private final Object event;

  /**
   * Creates a new DeadEvent instance.
   * 
   * @param source
   *          The publishing {@link EventService}.
   * @param event
   *          The event without subscribers.
   */
  public DeadEvent(Object source, Object event) {
    this.source = source;
    this.event = event;
  }

  /**
   * Returns the {@link EventService} instance which tried to publish the
   * undelivered event.
   * 
   * @return The source of the event.
   */
  public Object getSource() {
    return source;
  }

  /**
   * Returns the undelievered event because the {@link EventService} couldn't
   * find any subscribed handler.
   * 
   * @return The event without subscribers.
   */
  public Object getEvent() {
    return event;
  }
}
