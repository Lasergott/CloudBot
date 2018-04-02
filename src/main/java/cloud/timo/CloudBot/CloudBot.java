package cloud.timo.CloudBot;

import cloud.timo.CloudBot.managers.*;
import cloud.timo.CloudBot.utils.ChatColorUtil;
import cloud.timo.CloudBot.utils.TimeUtil;
import org.jline.builtins.Completers;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.awt.*;
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
    private LineReader reader;
    private boolean waitingForCommand = false;
    private SimpleFormatter simpleFormatter = new SimpleFormatter();
    private boolean running = true;
    private Logger logger;
    private ConsoleManager consoleManager;
    private DataManager dataManager;
    private ConsoleManager commandManager;
    private TeamSpeakManager teamSpeakManager;
    private EventManager eventManager;
    private MessageManager messageManager;
    private FileManager fileManager;
    private ScheduledExecutorService scheduler;
    private SupportManager supportManager;

    public static void main(String[] args) {
        System.out.println("Loading libraries...");
        getInstance().makeInstances();
        getInstance().setRunning(true);
        getInstance().scheduleConnecting();
        try {
            waitForCommands();
        } catch (IOException e) {
            severe("Error while initializing terminal: ");
            e.printStackTrace();
        }
        info("TeamSpeakBot is now complete online!");
    }

    private void makeInstances() {
        fileManager = new FileManager();
        dataManager = new DataManager();
        consoleManager = new ConsoleManager();
        teamSpeakManager = new TeamSpeakManager();
        eventManager = new EventManager();
        messageManager = new MessageManager();
        scheduler = Executors.newScheduledThreadPool(1);
        supportManager = new SupportManager();
    }

    public static void info(String message) {
        if (getInstance().getReader() == null) {
            System.out.println(TimeUtil.formatTimeConsole() + CloudBot.getInstance().getDataManager().getPrefix() + message);
            return;
        }
        if (getInstance().isWaitingForCommand()) getInstance().getReader().callWidget(LineReader.CLEAR);
        getInstance().getReader().getTerminal().writer().print(TimeUtil.formatTimeConsole() + CloudBot.getInstance().getDataManager().getPrefix() + message + "\n");
        if (getInstance().isWaitingForCommand()) getInstance().getReader().callWidget(LineReader.REDRAW_LINE);
        if (getInstance().isWaitingForCommand()) getInstance().getReader().callWidget(LineReader.REDISPLAY);
        getInstance().getReader().getTerminal().writer().flush();
        if (getInstance().getLogger() != null) getInstance().getLogger().info(message);
        CloudBot.getInstance().getDataManager().addToLog(message);
    }

    private void scheduleConnecting() {
        scheduler.scheduleAtFixedRate(CloudBot.getInstance().getSupportManager()::openSupport, 0, 1, TimeUnit.SECONDS);
    }

    public static void severe(String message) {
        if (getInstance().getReader() == null) {
            System.err.println(CloudBot.getInstance().getDataManager().getPrefix() + message);
            return;
        }
        if (getInstance().isWaitingForCommand()) getInstance().getReader().callWidget(LineReader.CLEAR);
        getInstance().getReader().getTerminal().writer().print(getInstance().getSimpleFormatter().format(new LogRecord(Level.SEVERE, ChatColorUtil.ANSI_RED + message + ChatColorUtil.ANSI_RESET)));
        if (getInstance().isWaitingForCommand()) getInstance().getReader().callWidget(LineReader.REDRAW_LINE);
        if (getInstance().isWaitingForCommand()) getInstance().getReader().callWidget(LineReader.REDISPLAY);
        getInstance().getReader().getTerminal().writer().flush();

        if (getInstance().getLogger() != null) getInstance().getLogger().severe(message);
    }

    private static void waitForCommands() throws IOException {
        TerminalBuilder builder = TerminalBuilder.builder();
        builder.encoding(Charset.defaultCharset());
        builder.system(true);
        Terminal terminal = builder.build();
        Completer completer = new Completers.TreeCompleter(
                node("help"),
                node("info"),
                node("reload"),
                node("exit"),
                node("broadcast")
        );
        Parser parser = new DefaultParser();
        String prompt = "> ";
        String rightPrompt = null;
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(parser)
                .build();
        getInstance().reader = reader;
        while (getInstance().running) {
            getInstance().waitingForCommand = true;
            String line = null;
            try {
                line = reader.readLine(prompt, rightPrompt, (MaskingCallback) null, null);
            } catch (UserInterruptException e) {
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (line == null) continue;
            getInstance().waitingForCommand = false;
            line = line.trim();
            if (line.isEmpty()) continue;
            getInstance().consoleManager.onCommand(line);
        }
    }

    public TeamSpeakManager getTeamSpeakManager() {
        return teamSpeakManager;
    }

    public ConsoleManager getCommandManager() {
        return commandManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ConsoleManager getConsoleManager() {
        return consoleManager;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isRunning() {
        return running;
    }

    public SimpleFormatter getSimpleFormatter() {
        return simpleFormatter;
    }

    public boolean isWaitingForCommand() {
        return waitingForCommand;
    }

    public LineReader getReader() {
        return reader;
    }

    public static CloudBot getInstance() {
        return instance;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public void setCommandManager(ConsoleManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public SupportManager getSupportManager() {
        return supportManager;
    }

    public void setSupportManager(SupportManager supportManager) {
        this.supportManager = supportManager;
    }
}