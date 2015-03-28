package com.ssachtleben.play.plugin.auth.actions;

import java.lang.reflect.Method;

import play.Logger;
import play.mvc.Controller;

import com.ssachtleben.play.plugin.auth.annotations.Restricts;

/**
 * Provides restricted access to {@link Controller} methods.
 * <p>
 * The {@link Restricts} annotation declares a controller method as restricted
 * and the current logged in user needs to be logged in and own the requested
 * role. Otherwise the request will be forbidden.
 * </p>
 * 
 * @author Sebastian Sachtleben
 */
public class RestrictedAction { // extends Action<Simple> {

  /**
   * The logger for {@link RestrictedAction} class.
   */
  private static final Logger.ALogger log = Logger.of(RestrictedAction.class);

  /**
   * The action {@link Method} to invoke after restriction check.s
   */
  private Method actionMethod;

  /**
   * The default contructor for {@link RestrictedAction} needs the invoked
   * {@code actionMethod} to access to the {@link Restricts} annotation.
   * 
   * @param actionMethod
   *          The {@link Method} to invoke.
   */
  public RestrictedAction(Method actionMethod) {
    this.actionMethod = actionMethod;
  }

  /**
   * Checks if the current logged in user is allowed to access to the requested
   * path.
   * 
   * @param ctx
   *          The current {@link Context}.
   * 
   * @see play.mvc.Action#call(play.mvc.Http.Context)
   */
  // @Override
  // public Result call(Context ctx) throws Throwable {
  // Restricts annotation = actionMethod.getAnnotation(Restricts.class);
  // log.info(String.format("called - Found: %s", annotation));
  // return delegate.call(ctx);
  // }

}
