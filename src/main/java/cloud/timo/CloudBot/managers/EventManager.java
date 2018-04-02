package cloud.timo.CloudBot.managers;

import cloud.timo.CloudBot.CloudBot;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.sun.prism.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static cloud.timo.CloudBot.CloudBot.info;

public class EventManager {


    public EventManager() {
        loadEvents();
    }

    public void loadEvents() {
        CloudBot.getInstance().getTeamSpeakManager().getApi().registerAllEvents();
        CloudBot.getInstance().getTeamSpeakManager().getApi().addTS3Listeners(new TS3Listener() {
            public void onTextMessage(TextMessageEvent textMessageEvent) {

            }

            public void onClientJoin(ClientJoinEvent event) {
                Client client = CloudBot.getInstance().getTeamSpeakManager().getApi().getClientInfo(event.getClientId());
                if (client.isInServerGroup(CloudBot.getInstance().getFileManager().getInt("defaultGroupID"))) {
                    CloudBot.getInstance().getTeamSpeakManager().getApi().addClientToServerGroup(
                            CloudBot.getInstance().getFileManager().getInt("userGroupID"), client.getDatabaseId());
                    MessageManager.sendPrivateMessage(client,
                            CloudBot.getInstance().getFileManager().getString("firstJoinMessage").replace("{client}", client.getNickname()));
                    info(client.getNickname() + " is new on the TeamSpeak server.");
                }
                MessageManager.sendPrivateMessage(client, CloudBot.getInstance().getFileManager().getString("joinMessage").replace("{client}", client.getNickname()));
                info(client.getNickname() + " has joined the TeamSpeak server.");
            }
            public void onClientLeave(ClientLeaveEvent event) {
                Client client = CloudBot.getInstance().getTeamSpeakManager().getApi().getClientInfo(event.getClientId());
                info(client.getNickname() + " has left the TeamSpeak server.");
            }

            public void onServerEdit(ServerEditedEvent event) {
                info("The server has edited.");
            }

            public void onChannelEdit(ChannelEditedEvent event) {
                info("The channel " + event.getChannelId() + " were edited.");
            }

            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent event) {
                info("The channel " + event.getChannelId() + " were edited. (Channel Description)");
            }
            public void onClientMoved(ClientMovedEvent event) {
                if (event.getTargetChannelId() == 295) {
                    Client client = CloudBot.getInstance().getTeamSpeakManager().getApi().getClientInfo(event.getClientId());
                    MessageManager.sendPrivateMessage(client, "[B]The Staff has been informed![/B]");
                    for (Client clients : CloudBot.getInstance().getTeamSpeakManager().getApi().getClients()) {
                        if (clients.isInServerGroup(73) || clients.isInServerGroup(74) || clients.isInServerGroup(75) || clients.isInServerGroup(60) || clients.isInServerGroup(6) || clients.isInServerGroup(59)) {
                            MessageManager.sendPrivateMessage(clients, "[color=RED][B]" + client.getNickname() + "[/B][/COLOR] is waiting in the Support-Channel!");
                            info(clients.getNickname() + " is waiting in the supportWait-room.");
                        }
                    }
                }
            }

            public void onChannelCreate(ChannelCreateEvent event) {
                info("The channel " + event.getChannelId() + " has been created.");
            }

            public void onChannelDeleted(ChannelDeletedEvent event) {
                info("The channel " + event.getChannelId() + " has been deleted.");
            }

            public void onChannelMoved(ChannelMovedEvent event) {
                info("The channel " + event.getChannelId() + " has been moved.");

            }

            public void onChannelPasswordChanged(ChannelPasswordChangedEvent event) {
                info("The channel " + event.getChannelId() + " has been changed his password.");
            }

            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent event) {
            }
        });
    }
}
