package com.github.baloise.rocketchatrestclient.util;

import static org.junit.Assert.assertNotNull;

import com.github.baloise.rocketchatrestclient.RocketChatClient;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static com.github.baloise.rocketchatrestclient.util.TestConnectionInfo.*;

public class Oauth2Login {
    public static RocketChatClient createClient()  {
        return new RocketChatClient(ServerUrl,ServiceName,Secret,ExpiresIn, oauth2AccessToken());
    }

    private static String oauth2AccessToken() {
        JsonNode jsonResponse = null;
        try {
            jsonResponse = Unirest.post(Oauth2ServerUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("scope", "openid")
                    .field("grant_type", "password")
                    .field("client_id", ClientId)
                    .field("client_secret", Secret)
                    .field("username", User)
                    .field("password", Password)
                    .asJson()
                    .getBody();
        } catch (UnirestException e) {
            throw new RuntimeException("Invalid Oauth2 login", e);
        }
        return jsonResponse.getObject().getString("access_token");
    }
}
