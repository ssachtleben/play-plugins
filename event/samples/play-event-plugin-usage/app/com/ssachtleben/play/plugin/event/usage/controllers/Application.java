package com.ssachtleben.play.plugin.event.usage.controllers;

import play.mvc.Controller;
import play.mvc.Result;

import com.ssachtleben.play.plugin.event.Events;
import com.ssachtleben.play.plugin.event.usage.ActivityHistory;
import com.ssachtleben.play.plugin.event.usage.views.html.index;

public class Application extends Controller {

  public static Result index() {
    Events.instance().publish("Index view called");
    return ok(index.render(ActivityHistory.getHistory()));
  }

}
