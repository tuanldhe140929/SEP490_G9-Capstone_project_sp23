package com.SEP490_G9.service.serviceImpls;

import java.io.IOException;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;

import com.SEP490_G9.common.Constant;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.exception.InternalServerException;
import com.SEP490_G9.service.GoogleAuthService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

	@Override
	public User getUserInfo(final String code) {
		String userInfoJson;
		try {
			userInfoJson = Request.Get(Constant.GOOGLE_LINK_GET_USER_INFO + getToken(code)).execute().returnContent()
					.asString();
		} catch (IOException e) {
			throw new InternalServerException("Can't get token for code: " + code);
		}
		JsonObject userJsonObject = new Gson().fromJson(userInfoJson, JsonObject.class);
		User user = new User(userJsonObject.get("email").getAsString());

		return user;
	}

	@Override
	public String getToken(final String code) {
		String response = "";
		try {
			response = Request.Post(Constant.GOOGLE_LINK_GET_TOKEN)
					.bodyForm(Form.form().add("client_id", Constant.GOOGLE_CLIENT_ID)
							.add("client_secret", Constant.GOOGLE_CLIENT_SECRET)
							.add("redirect_uri", Constant.GOOGLE_REDIRECT_URI).add("code", code)
							.add("grant_type", Constant.GOOGLE_GRANT_TYPE).build())
					.execute().returnContent().asString();
		} catch (IOException e) {
			throw new InternalServerException("Can't get information for user's code: " + code);
		}

		JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
		String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
		return accessToken;
	}
}
