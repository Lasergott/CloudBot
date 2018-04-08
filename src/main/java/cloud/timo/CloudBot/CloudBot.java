package cloud.timo.CloudBot;

import cloud.timo.CloudBot.managers.*;
import cloud.timo.CloudBot.utils.ChatColorUtil;
import cloud.timo.CloudBot.utils.TimeUtil;
import org.jline.builtins.Completers;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.jline.builtins.Completers.TreeCompleter.node;

public class CloudBot {

    private static final CloudBot instance = new CloudBot();
    private ConsoleManager consoleManager;
    private DataManager dataManager;
    private TeamSpeakManager teamSpeakManager;
    private EventManager eventManager;
    private MessageManager messageManager;
    private FileManager fileManager;
    private ScheduledExecutorService scheduler;
    private SupportManager supportManager;
    private ClientManager clientManager;

    public static void main(String[] args) {
        System.out.println("Loading libraries...");
        getInstance().makeInstances();
        getInstance().scheduleConnecting();
        getInstance().getConsoleManager().info("TeamSpeakBot is now complete online!");
        try {
            CloudBot.getInstance().getConsoleManager().waitForCommands();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeInstances() {
        fileManager = new FileManager();
        dataManager = new DataManager();
        consoleManager = new ConsoleManager();
        teamSpeakManager = new TeamSpeakManager();
        eventManager = new EventManager();
        scheduler = Executors.newScheduledThreadPool(1);
        supportManager = new SupportManager();
        clientManager = new ClientManager();
        messageManager = new MessageManager();
    }

    private void scheduleConnecting() {
        scheduler.scheduleAtFixedRate(CloudBot.getInstance().getClientManager()::checkAFK, 0, 1, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(CloudBot.getInstance().getClientManager()::everySecond, 0, 1, TimeUnit.SECONDS);
    }

    public static CloudBot getInstance() {
        return instance;
    }

    public ConsoleManager getConsoleManager() {
        return consoleManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ConsoleManager getCommandManager() {
        return consoleManager;
    }

    public TeamSpeakManager getTeamSpeakManager() {
        return teamSpeakManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public SupportManager getSupportManager() {
        return supportManager;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }
}