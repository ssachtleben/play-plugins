package com.ssachtleben.play.plugin.event.usage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import play.Logger;

import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * The ActivityHistory saves the latest application activity by event
 * subscription.
 * 
 * @author Sebastian Sachtleben
 */
public class ActivityHistory {
  private static final Logger.ALogger log = Logger.of(ActivityHistory.class);

  /**
   * The application activity history list.
   */
  private static List<String> history = new LimitedQueue<String>(10);

  /**
   * Returns the current application activity history.
   * 
   * @return Returns a list of strings.
   */
  public static List<String> getHistory() {
    return history;
  }

  /**
   * Saves a new activity in the history.
   * 
   * @param event
   *          The published event.
   */
  @Observer
  public static void save(Object event) {
    DateFormat df = SimpleDateFormat.getDateTimeInstance();
    history.add(df.format(new Date()) + " - " + event.toString());
    log.info("Saved: " + event.toString());
  }

  /**
   * The LimitedQueue is a LinkedList with a given limit.
   * 
   * @author Sebastian Sachtleben
   * 
   * @param <E>
   *          the type of elements held in this collection
   */
  @SuppressWarnings("serial")
  public static class LimitedQueue<E> extends LinkedList<E> {

    private final int limit;

    public LimitedQueue(int limit) {
      this.limit = limit;
    }

    @Override
    public boolean add(E o) {
      super.add(o);
      while (size() > limit) {
        super.remove();
      }
      return true;
    }
  }
}
