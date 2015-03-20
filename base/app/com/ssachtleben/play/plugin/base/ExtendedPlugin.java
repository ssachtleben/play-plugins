package com.ssachtleben.play.plugin.base;

import java.util.concurrent.TimeUnit;

import play.Application;
import play.Logger;
import play.Plugin;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

/**
 * @author Sebastian Sachtleben
 */
public abstract class ExtendedPlugin extends Plugin {

  /**
   * The {@link Logger} instance.
   */
  protected final Logger.ALogger log = Logger.of(getClass());

  /**
   * The {@link Application} instance.
   */
  protected Application app;

  /**
   * Default constructor for {@link ExtendedPlugin}.
   * 
   * @param app
   *          The {@link Application} instance.
   */
  public ExtendedPlugin(final Application app) {
    log.debug("Create Plugin");
    this.app = app;
  }

  public abstract String name();

  public abstract void start();

  public abstract void stop();

  /*
   * (non-Javadoc)
   * 
   * @see play.Plugin#onStart()
   */
  @Override
  public void onStart() {
    if (!app.configuration().getBoolean(String.format("%s.active", name()), Boolean.TRUE)) {
      log.info(String.format("Plugin not started (Setting key '%s.active' is false)", name()));
      return;
    }
    if (app.configuration().getBoolean(String.format("%s.async", name()), Boolean.TRUE)) {
      log.debug("Start Plugin (async)");
      Akka.system().scheduler().scheduleOnce(Duration.create(0, TimeUnit.MILLISECONDS), new Runnable() {
        public void run() {
          start();
        }
      }, Akka.system().dispatcher());
    } else {
      log.debug("Start Plugin (sync)");
      start();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see play.Plugin#onStop()
   */
  @Override
  public void onStop() {
    log.debug("Stop Plugin");
    stop();
  }
}
