package com.github.baloise.rocketchatrestclient;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;

import com.github.baloise.rocketchatrestclient.model.ServerInfo;
import com.github.baloise.rocketchatrestclient.model.User;
import com.github.baloise.rocketchatrestclient.util.Oauth2Login;

@Ignore
public class RocketChatClientTest {
    private RocketChatClient rc;
    
    @Before
    public void setUp() {
        //rc = BasicLogin.createClient();
		rc = Oauth2Login.createClient();
    }

	@Test
	public void testRocketCatExists() throws Exception {
		ServerInfo info = rc.getServerInformation();
		assertFalse("The Rocket.Chat Version is empty, when it shouldn't be.", info.getVersion().isEmpty());
		
		User rocketCat = rc.getUsersApi().getInfo("rocket.cat");
		assertTrue("The Rocket.Cat user's id doesn't match what it should be.", "rocket.cat".equals(rocketCat.getId()));
	}
	
	@Test
	public void testChannelGetting() throws Exception {
	    assertEquals("The default count changed?", 50, rc.getChannelsApi().list().length);
	    assertEquals("The channel count wasn't one?", 1, rc.getChannelsApi().list(new RocketChatQueryParams().setCount(1)).length);
	}
}
