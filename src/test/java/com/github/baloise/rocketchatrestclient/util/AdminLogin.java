package com.github.baloise.rocketchatrestclient.util;

import com.github.baloise.rocketchatrestclient.RocketChatClient;
import static com.github.baloise.rocketchatrestclient.util.TestConnectionInfo.*;
public class AdminLogin {
    public static RocketChatClient createClient() {
        return new RocketChatClient(ServerUrl, AdminUser, AdminPassword);
    }
}
