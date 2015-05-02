package com.ssachtleben.play.plugin.auth.filters;

import com.ssachtleben.play.plugin.base.filters.JavaFilter;
import com.ssachtleben.play.plugin.base.filters.ResultAdapter;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Play;
import play.api.mvc.RequestHeader;
import play.api.mvc.Result;
import play.mvc.Results;
import scala.Option;

public class BasicAuthFilter extends JavaFilter {
  
  @Override
  public Result Apply(Result currentResult, RequestHeader requestHeader) {
    boolean hasMethodAnnotation = false;
    boolean hasClassAnnotation = false;
    String username = "";
    String password = "";
    if (!requestHeader.tags().get("ROUTE_ACTION_METHOD").isEmpty() && !requestHeader.tags().get("ROUTE_CONTROLLER").isEmpty()) {
      try {
        final Class<?> clazz = Class.forName(requestHeader.tags().get("ROUTE_CONTROLLER").get());
        for (final Method method : clazz.getMethods()) {
          if (method.getName().equals(requestHeader.tags().get("ROUTE_ACTION_METHOD").get()) && method.isAnnotationPresent(BasicAuth.class)) {
            final BasicAuth methodBasicAuth = method.getAnnotation(BasicAuth.class);
            hasMethodAnnotation = true;
            username = Play.application().configuration().getString(methodBasicAuth.username());
            password = Play.application().configuration().getString(methodBasicAuth.password());
          }
        }
        final BasicAuth classBasicAuth = clazz.getAnnotation(BasicAuth.class);
        if (!hasMethodAnnotation && classBasicAuth != null) {
          hasClassAnnotation = true;
          username = Play.application().configuration().getString(classBasicAuth.username());
          password = Play.application().configuration().getString(classBasicAuth.password());
        }
        //Logger.debug("hasAuth=" + (hasMethodAnnotation || hasClassAnnotation) + ",user=" + username + ",pass=" + password);
      } catch (Exception e) {
        Logger.error("Failed to instantiate class", e);
      }
    }
    if (!(hasMethodAnnotation || hasClassAnnotation)) {
      return currentResult;
    }
    final Option<String> auth = requestHeader.headers().get("authorization");
    if (!auth.isEmpty()) {
      final String baStr = auth.get().replaceFirst("Basic ", "");
      try {
        final String[] decodedAuthSt = new String(Base64.decodeBase64(baStr), "UTF-8").split(":");
        if (decodedAuthSt.length >= 2 && StringUtils.equals(username, decodedAuthSt[0]) && StringUtils.equals(password, decodedAuthSt[1])) {
          return currentResult;
        }
        
      } catch (IOException e) {
        Logger.error("Failed to decode authorization header", e);
      }
    }    
    return new ResultAdapter(Results.unauthorized().toScala()).WithHeader("WWW-Authenticate", "Basic realm=\"my-realm\"!");
  }
}