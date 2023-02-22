package com.SEP490_G9.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Component;

import com.SEP490_G9.entity.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component
public class GoogleUtil {
	private String getToken(final String code) throws ClientProtocolException, IOException {
		String response = Request.Post(Constant.GOOGLE_LINK_GET_TOKEN)
				.bodyForm(Form.form().add("client_id", Constant.GOOGLE_CLIENT_ID)
						.add("client_secret", Constant.GOOGLE_CLIENT_SECRET)
						.add("redirect_uri", Constant.GOOGLE_REDIRECT_URI).add("code", code)
						.add("grant_type", Constant.GOOGLE_GRANT_TYPE).build())
				.execute().returnContent().asString();

		JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
		String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
		return accessToken;
	}
	
	public User getUserInfo(final String code) throws ClientProtocolException, IOException {
		String userInfoJson = Request.Get(Constant.GOOGLE_LINK_GET_USER_INFO + getToken(code)).execute()
				.returnContent().asString();
		JsonObject userJsonObject = new Gson().fromJson(userInfoJson, JsonObject.class);
		User user = new User(userJsonObject.get("email").getAsString());
		
		return user;
	}

}