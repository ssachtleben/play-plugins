package com.ssachtleben.play.plugin.json;

/**
 * Provides array of mixins for {@link JsonFactory}.
 * 
 * @author Sebastian Sachtleben
 */
public interface MixinProvider {

  Class<?>[] mixins();

}
