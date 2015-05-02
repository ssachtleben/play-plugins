package com.ssachtleben.play.plugin.base.filters;

import play.api.mvc.EssentialAction;
import play.api.mvc.Filter;
import play.api.mvc.Filter$class;
import play.api.mvc.RequestHeader;
import play.api.mvc.Result;
import scala.Function1;
import scala.concurrent.Future;
import scala.runtime.AbstractFunction1;

public abstract class JavaFilter implements Filter {

  @Override
  public Future<Result> apply(Function1<RequestHeader, Future<Result>> nextFilter, final RequestHeader requestHeader) {
    return nextFilter.apply(requestHeader).map(new AbstractFunction1<Result, Result>() {
      @Override
      public Result apply(Result currentResult) {
        return Apply(currentResult, requestHeader);
      }
    }, play.api.libs.concurrent.Execution.defaultContext());
  }

  @Override
  public EssentialAction apply(EssentialAction next) {
    return Filter$class.apply(this, next);
  }

  public abstract Result Apply(Result currentResult, RequestHeader requestHeader);
  
}
