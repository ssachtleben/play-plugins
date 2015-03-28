package com.ssachtleben.play.plugin.event;

import java.util.ArrayList;
import java.util.List;

public class EventResult {

  private List<EventBinding> receivers = new ArrayList<>();
  private boolean published = false;

  /**
   * @return the receivers
   */
  public List<EventBinding> getReceivers() {
    return receivers;
  }

  /**
   * @param receivers
   *          the receivers to set
   */
  public void setReceivers(List<EventBinding> receivers) {
    this.receivers = receivers;
  }

  /**
   * @return the published
   */
  public boolean isPublished() {
    return published;
  }

  /**
   * @param published
   *          the published to set
   */
  public void setPublished(boolean published) {
    this.published = published;
  }
}
