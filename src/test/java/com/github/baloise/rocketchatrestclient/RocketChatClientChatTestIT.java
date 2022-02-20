package com.github.baloise.rocketchatrestclient;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.github.baloise.rocketchatrestclient.model.Attachment;
import com.github.baloise.rocketchatrestclient.model.Message;
import com.github.baloise.rocketchatrestclient.model.Room;
import com.github.baloise.rocketchatrestclient.util.Oauth2Login;
import com.github.baloise.rocketchatrestclient.util.TestConnectionInfo;
import com.vdurmont.emoji.EmojiManager;

public class RocketChatClientChatTestIT {
    private RocketChatClient rc;

    @Before
    public void setUp() throws Exception {
        this.rc = Oauth2Login.createClient();

        assertNotNull("An error occured setting up Rocket.Chat Client, it is null.", this.rc);
    }
    @Test
    public void testListRooms() throws IOException {
        Room[] rooms = this.rc.getChannelsApi().list();

        System.out.println("Rooms:");
        Arrays.stream(rooms).forEach(room -> System.out.println(room.getName()) );

        assertNotNull("The returned rooms array is null?", rooms);
        assertTrue("The returned rooms array is empty?", rooms.length > 0);
    }
    @Test
    public void testPostMessage() throws IOException {
        Room room = new Room("#Teste", false);
        Message msg = this.rc.getChatApi().postMessage(room, new Message("Testing this out."));

        assertNotNull("The returned message is null?", msg);
        assertEquals("The message text is not the same.", "Testing this out.", msg.getText());
        
        Message attachmentMsg = new Message("Testing with attachment.");
        attachmentMsg.addAttachment(new Attachment("Testing with text attachment"));
        attachmentMsg.setEmojiAvatar(EmojiManager.getForAlias("smirk"));
        
        Message returnedAttachmentMsg = this.rc.getChatApi().postMessage(room, attachmentMsg);
        assertNotNull("The returned attachment message is null?", returnedAttachmentMsg);
        assertEquals("The attachment message text is not the same.", "Testing with attachment.", returnedAttachmentMsg.getText());
        assertEquals("The attachment message doesn't have attachments?", 1, returnedAttachmentMsg.getAttachments().length);
    }
}
