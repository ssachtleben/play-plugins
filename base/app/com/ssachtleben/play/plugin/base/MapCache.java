package com.ssachtleben.play.plugin.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MapCache<K, V> {

  private ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<K, V>();

  public Map<K, V> cache() {
    return cache;
  }

}
