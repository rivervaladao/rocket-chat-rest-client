package com.github.baloise.rocketchatrestclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.github.baloise.rocketchatrestclient.model.ServerInfo;
import com.mashape.unirest.http.Unirest;

/**
 * Client for Rocket.Chat which relies on the REST API v1.
 * <p>
 * Please note, this client does <strong>not</strong> cache any of the results.
 *
 * @version 0.1.0
 * @since 0.0.1
 */
public class RocketChatClient {
    private RocketChatClientCallBuilder callBuilder;
    private RocketChatRestApiV1Channels channels;
    private RocketChatRestApiV1Chat chat;
    private RocketChatRestApiV1Groups groups;
    private RocketChatRestApiV1Users users;
    private RocketChatRestApiV1Settings settings;

    public RocketChatClient(String serverUrl,
                            String serviceName,
                            String secret,
                            int expiresIn,
                            String accessToken){
        this.callBuilder = new RocketChatClientCallBuilder(serverUrl, serviceName, secret, expiresIn, accessToken);
        this.channels = new RocketChatRestApiV1Channels(this.callBuilder);
        this.chat = new RocketChatRestApiV1Chat(this.callBuilder);
        this.groups = new RocketChatRestApiV1Groups(this.callBuilder);
        this.users = new RocketChatRestApiV1Users(this.callBuilder);
        this.settings = new RocketChatRestApiV1Settings(this.callBuilder);
    }

    /**
     * Initialize a new instance of the client providing the server's url along
     * with username and password to use.
     *
     * @param serverUrl
     *            of the Rocket.Chat server, with or without it ending in
     *            "/api/"
     * @param user
     *            which to authenticate with
     * @param password
     *            of the user to authenticate with
     */
    public RocketChatClient(String serverUrl, String user, String password) {
        this.callBuilder = new RocketChatClientCallBuilder(serverUrl, user, password);
        this.channels = new RocketChatRestApiV1Channels(this.callBuilder);
        this.chat = new RocketChatRestApiV1Chat(this.callBuilder);
        this.groups = new RocketChatRestApiV1Groups(this.callBuilder);
        this.users = new RocketChatRestApiV1Users(this.callBuilder);
		this.settings = new RocketChatRestApiV1Settings(this.callBuilder);
    }
    
    /**
     * Trust self-signed certificates on the rocketchat server url.
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
	public void trustSelfSignedCertificates()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		Unirest.setHttpClient(httpclient);
	}

    /**
     * Forces a logout and clears the auth token if no exception happened.
     *
     * @throws IOException
     *             is thrown if there was a problem connecting, including if the
     *             result wasn't successful
     */
    public void logout() throws IOException {
        this.callBuilder.logout();
    }

    /**
     * Gets the {@link ServerInfo} from the server, containing the version.
     *
     * @return the {@link ServerInfo}
     * @throws IOException
     *             is thrown if there was a problem connecting, including if the
     *             result wasn't successful
     */
    public ServerInfo getServerInformation() throws IOException {
        RocketChatClientResponse res = this.callBuilder.buildCall(RocketChatRestApiV1.Info);

        if (!res.isSuccessful())
            throw new IOException("The call out to get the server information was unsuccessful: \"" + res.getError() + "\"");

        if (!res.hasServerInfo())
            throw new IOException("The server information was not retrieved from the server.");

        return res.getServerInfo();
    }

    public RocketChatRestApiV1Channels getChannelsApi() {
        return this.channels;
    }

    public RocketChatRestApiV1Chat getChatApi() {
        return this.chat;
    }
    
    public RocketChatRestApiV1Groups getGroupsApi() {
        return this.groups;
    }
    
    public RocketChatRestApiV1Users getUsersApi() {
        return this.users;
    }
    
    public RocketChatRestApiV1Settings getSettingsApi() {
    	return this.settings;
    }
}