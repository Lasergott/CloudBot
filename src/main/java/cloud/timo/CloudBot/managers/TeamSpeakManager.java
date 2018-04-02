package cloud.timo.CloudBot.managers;

import cloud.timo.CloudBot.CloudBot;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TeamSpeakManager {

    private final TS3Config config = new TS3Config();
    private final TS3Query query = new TS3Query(config);
    private final TS3Api api = query.getApi();
    List<Integer> groups = new ArrayList<Integer>();

    public TeamSpeakManager() {
        config.setHost(CloudBot.getInstance().getFileManager().getString("teamSpeakServerIP"));
        config.setDebugLevel(Level.ALL);
        getQuery().connect();
        getApi().login(CloudBot.getInstance().getFileManager().getString("serverQueryUserName"), CloudBot.getInstance().getFileManager().getString("serverQueryPassword"));
        getApi().selectVirtualServerById(CloudBot.getInstance().getFileManager().getInt("virtualServerID"));
        getApi().setNickname(CloudBot.getInstance().getFileManager().getString("botNickName"));
        getApi().broadcast(CloudBot.getInstance().getFileManager().getString("startUpMessage"));
        addGroupIDs();
    }

    public TS3Query getQuery() {
        return query;
    }

    public TS3Api getApi() {
        return api;
    }

    public void addGroupIDs() {
        for (Client client : getApi().getClients()) {
            for (int i : client.getServerGroups())
                groups.add(i);
        }
    }

    public List<Integer> getStaffGroupID() {
        List<Integer> list = (List<Integer>) CloudBot.getInstance().getFileManager().getConfig().get("staffGroups");
        return list;
    }

    public List<Integer> getGroupIds() {
        return groups;
    }

}
