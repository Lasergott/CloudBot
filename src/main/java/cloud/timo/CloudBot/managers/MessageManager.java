package cloud.timo.CloudBot.managers;

import cloud.timo.CloudBot.CloudBot;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class MessageManager {

      public static void sendPrivateMessage(Client client, String message) {
          CloudBot.getInstance().getTeamSpeakManager().getApi().sendPrivateMessage(client.getId(), message);
      }

      public static void broadcast(String message) {
          CloudBot.getInstance().getTeamSpeakManager().getApi().broadcast(message);
      }
}
