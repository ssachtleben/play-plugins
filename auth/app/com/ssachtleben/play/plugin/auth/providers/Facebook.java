package com.ssachtleben.play.plugin.auth.providers;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import play.Application;
import play.api.templates.Html;

import com.ssachtleben.play.plugin.auth.models.FacebookAuthUser;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthInfo;

/**
 * Provides authentication with Facebook oauth2 interface.
 * 
 * @author Sebastian Sachtleben
 */
public class Facebook extends OAuth2Provider<FacebookAuthUser, OAuthAuthInfo> {
	public static final String KEY = "facebook";

	public Facebook(Application app) throws Exception {
		super(app);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#key()
	 */
	@Override
	public String key() {
		return KEY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#provider()
	 */
	@Override
	public Class<? extends Api> provider() {
		return FacebookApi.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#info(org.scribe.model.Token)
	 */
	@Override
	protected OAuthAuthInfo info(Token token) {
		// TODO: Read data from facebook for creating new account or comparing current values.
		return new OAuthAuthInfo(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(com.ssachtleben.play.plugin.auth.models.OAuthAuthInfo)
	 */
	@Override
	protected FacebookAuthUser transform(OAuthAuthInfo info) {
		return new FacebookAuthUser(userId(info.token()), info.token());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#popup()
	 */
	@Override
	protected Html popup() {
		return views.html.popups.facebook.render();
	}

	/**
	 * TODO: UGLY !!! UGLY !!! UGLY !!! UGLY !!!
	 * 
	 * @param token
	 * @return
	 */
	private String userId(Token token) {
		// TODO: so ugly here :(
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = null;
		try {
			node = mapper.readTree(request(token, Verb.GET, "https://graph.facebook.com/me?fields=id").getBody());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return node.get("id").asText();
	}
}
