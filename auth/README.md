# Play Auth Plugin - Simple user authentication plugin for Play Framework 2.3.x

**IMPORTANT: The plugin is still in develop and maybe cause unexpected behaviors**

## Provider Configuration

The following providers are supported:

- facebook
- google
- linkedin
- twitter
- yahoo

Here is a list of all properties which can be defined for each provider:

```
auth.facebook.key=""
auth.facebook.secret=""
auth.facebook.scope=""
auth.facebook.fields=""
auth.facebook.callback=""
auth.facebook.success=""
auth.facebook.error=""
```

All providers with a valid configuration are active by default.

## Authentication Events

It's possible to execute custom code before or after the autentication. There are also events if the authentication was successful or failed.

### Event Types

Here is a list with all events:

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

The auth plugin depends on the event plugin which is used to trigger them. It's very easy to observe them:

```
@Observer(topic = AuthEvents.AUTHENTICATION_SUCCESSFUL)
public static void handleAuthenticationSuccess(final Context ctx, final Object user, final String provider) {
    log.info(String.format("Authentication success event [provider=%s, user=%s]", provider, user));
}
```
