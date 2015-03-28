# Play Auth Plugin - Simple user authentication plugin for Play Framework 2.3.x

**IMPORTANT: The plugin is still in develop and maybe cause unexpected behaviors**

This plugin adds a simple user authentication layer to your application. It depends on Scribe for the oauth process.

## Provider Configuration

The following auth providers are supported:

| Name     | Type   | Configuration Key |
| ---------| -------| ----------------- |
| Facebook | OAuth2 | facebook          |
| Google   | OAuth2 | google			|
| LinkedIn | OAuth2 | linkedin			|
| Twitter  | OAuth1 | twitter			|
| Yahoo    | OAuth1 | yahoo				|

Each provider needs to be configurated in the application.conf:

| Name     | Description                                                     |
| ---------| --------------------------------------------------------------- |
| key      | The application id                                              |
| secret   | The appication secret                                           |
| scope    | The oauth scope                                                 |
| fields   | Name of the fields which will be provided in a json data object |
| callback | The callback url                                                |
| success  | The success url                                                 |
| error    | The error url                                                   |

This is an example configuration for facebook:

```
auth.facebook.key="123456789"
auth.facebook.secret="123456789123456789"
auth.facebook.scope="email,publish_actions"
auth.facebook.fields="id,name,email,first_name,picture"
auth.facebook.callback="http://localhost:9000/login/facebook/auth"
auth.facebook.success="http://localhost:9000/login/facebook/success"
auth.facebook.error="http://localhost:9000/login/facebook/error"
```

All providers with a valid configuration are active by default. If a provider is not working as excepted change the log level in the application.conf and check the log files for warnings:

```
logger.com.ssachtleben.play.plugin.auth.Providers=WARN
```

## Authentication Events

It's possible to execute custom code during the authentication process. There are multiple events which can be observed.

### Event Types

Here is a list with all authentication events:

| Name                                 | Description                                                       | Parameter      							                |
| ------------------------------------ | ----------------------------------------------------------------- | ---------------------------------------------------------- |
| AuthEvents.AUTHENTICATION_BEFORE     | This event will be fired before the auth process starts.          | play.mvc.Http.Context ctx, String providerKey              |
| AuthEvents.AUTHENTICATION_AFTER      | This event will be fired after the auth process.                  | play.mvc.Http.Context ctx, String providerKey              |
| AuthEvents.AUTHENTICATION_ERROR      | This event will be fired if the user can't properly authenticate. | play.mvc.Http.Context ctx, String providerKey              |
| AuthEvents.AUTHENTICATION_SUCCESS    | This event will be executed on a sucessful auth.                  | play.mvc.Http.Context ctx, Object user, String providerKey |

All events are sync by default, if you want to execute time consuming operations you should schedule a task with Akka.

Its possible to change all events to async via application.conf: 

```
auth.events.async = true
```

### Observe Events

The auth plugin depends on the event plugin which is used to trigger them. It's very easy to observe them. Here is an example:

```
@Observer(topic = AuthEvents.AUTHENTICATION_SUCCESSFUL)
public static void handleAuthenticationSuccess(final Context ctx, final Object user, final String provider) {
    log.info(String.format("Authentication success event [provider=%s, user=%s]", provider, user));
}
```
