package com.ssachtleben.play.plugin.auth;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import play.Application;

import com.ssachtleben.play.plugin.auth.annotations.Authenticates;
import com.ssachtleben.play.plugin.auth.models.Identity;
import com.ssachtleben.play.plugin.auth.providers.BaseProvider;
import com.ssachtleben.play.plugin.base.ExtendedPlugin;

/**
 * Registers providers during application start.
 * 
 * @author Sebastian Sachtleben
 */
public class AuthPlugin extends ExtendedPlugin {

  /**
   * Default constructor.
   * 
   * @param app
   *          The app to set
   */
  public AuthPlugin(Application app) {
    super(app);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.base.ExtendedPlugin#name()
   */
  @Override
  public String name() {
    return "auth";
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.base.ExtendedPlugin#start()
   */
  @Override
  public void start() {
    final Set<BaseProvider<Identity>> providers = AuthUtils.findProviders(app);
    final Set<Method> authMethods = AuthUtils.findAuthMethods();
    Iterator<BaseProvider<Identity>> iter = providers.iterator();
    while (iter.hasNext()) {
      BaseProvider<Identity> provider = iter.next();
      Method authMethod = null;
      Iterator<Method> iter2 = authMethods.iterator();
      while (iter2.hasNext()) {
        Method currMethod = iter2.next();
        Authenticates authAnnotation = currMethod.getAnnotation(Authenticates.class);
        if (provider.key().equals(authAnnotation.provider())) {
          // TODO: validate if method got matching parameters...
          authMethod = currMethod;
          log.info(String.format("Found auth method for %s provider: %s", provider.key(), authMethod));
          break;
        }
      }
      Providers.register(provider.key(), provider, authMethod);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.base.ExtendedPlugin#stop()
   */
  @Override
  public void stop() {
    Providers.clear();
  }
}
