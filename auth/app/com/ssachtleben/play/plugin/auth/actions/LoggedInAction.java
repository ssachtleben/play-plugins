package com.ssachtleben.play.plugin.auth.actions;

import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;

import com.ssachtleben.play.plugin.auth.Auth;

/**
 * This action allows only HTTP request from logged in users.
 * 
 * @author Sebastian Sachtleben
 */
public class LoggedInAction extends Action<LoggedIn> {

  @Override
  public Promise<Result> call(final Context ctx) throws Throwable {
    if (!Auth.isLoggedIn(ctx.session())) {
      Result unauthorized = onUnauthorized(ctx);
      return F.Promise.pure(unauthorized);
    }
    return delegate.call(ctx);
  }

  public Result onUnauthorized(Context ctx) {
    return unauthorized(views.html.defaultpages.unauthorized.render());
  }

}
