# Play Event Plugin - A eventbus plugin for Play Framework 2.3.x

**IMPORTANT: The plugin is still in develop and maybe cause unexpected behaviors**

This plugin adds a eventbus to your application.

## Plugin Installation

Add the library dependency and the repository resolver to the build.sbt:

```
libraryDependencies += "com.ssachtleben" %% "play-event-plugin" % "3.0-SNAPSHOT"

resolvers += Resolver.url("ssachtleben repository (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns)
```

The event plugin has to be added in the conf/play.plugins:

```
1300:com.ssachtleben.play.plugin.event.EventPlugin
```

## Plugin Usage

The com.ssachtleben.play.plugin.event.Events class provides the publish and publishAsync methods to fire an event. The parameters are optional. Here are some examples:

```
Events.instance().publish("my-first-event", param1, param2);
Events.instance().publishAsync("my-first-event", param1, param2);
```

To observe an event create a method with a matching parameter signature.

```
@Observer("my-first-event")
public static observeFirstEvent(final String param1, final String param2) {
	...
}
```

## Custom Event Implementation

The event plugin uses Akka to transport the events. It is possible to override the default implementation.

Create a new events class an implement the EventService interface.

```
public MyEvents implements com.ssachtleben.play.plugin.event.EventService {
	...
}
```

Add the class path to the application.conf:

```
eventService=my.package.MyEvents
```

The event plugin should now use your event implementation.