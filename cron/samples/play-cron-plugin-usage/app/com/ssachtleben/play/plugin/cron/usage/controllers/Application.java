package com.ssachtleben.play.plugin.cron.usage.controllers;

import play.mvc.Controller;
import play.mvc.Result;

import com.ssachtleben.play.plugin.cron.usage.views.html.index;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

}
