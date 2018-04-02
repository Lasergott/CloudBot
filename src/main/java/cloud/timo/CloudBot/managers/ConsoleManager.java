package cloud.timo.CloudBot.managers;

import cloud.timo.CloudBot.CloudBot;
import cloud.timo.CloudBot.utils.ChatColorUtil;
import java.util.Arrays;
import java.util.function.Consumer;
import static cloud.timo.CloudBot.CloudBot.info;


public class ConsoleManager {

    public ConsoleManager() {
        sendStartScreen();
    }


    public static void sendStartScreen() {
        System.out.println("\n" +
                "   _____ _                 _ _           _   \n" +
                "  / ____| |               | | |         | |  \n" +
                " | |    | | ___  _   _  __| | |__   ___ | |_ \n" +
                " | |    | |/ _ \\| | | |/ _` | '_ \\ / _ \\| __|\n" +
                " | |____| | (_) | |_| | (_| | |_) | (_) | |_ \n" +
                "  \\_____|_|\\___/ \\__,_|\\__,_|_.__/ \\___/ \\__|\n" +
                "                                             \n" +
                "                                             \n");
        System.out.println("CloudBot (Version 1.0) by Sebastian - Lasergott");
        System.out.println("");
        info("Type 'help' for all available commands!");
    }

    private void sendHelp(Consumer<String> sendMessage) {
        info("All available commands:");
        info("  help             -    shows the help page");
        info("  info             -    information's about the bot");
        info("  reload           -    reload all configurations");
        info("  exit             -    terminate this process");
        info("  broadcast        -    broadcast a message");
    }

    private void send(String message) {
        info(message);
    }

    private void sendError(Consumer<String> sendMessage, String message) {
        sendMessage.accept("&c" + message);
    }

    private void sendError(Consumer<String> sendMessage, boolean local, String message) {
        if (local) sendError(sendMessage, message);
        else sendError(sendMessage, message);
    }

    public void onCommand(String command) {
        onCommand((str) -> send(ChatColorUtil.toLegacyText(str + "&r")), true, command);
    }

    public void onCommand(Consumer<String> sendMessage, boolean local, String command) {
        String[] split = command.split(" ");
        if (split.length < 1) return;
        String cmd = split[0];
        String[] args = split.length == 1 ? new String[0] : Arrays.copyOfRange(split, 1, split.length);
        onCommand(sendMessage, local, cmd, args);
    }

    public void onCommand(Consumer<String> sendMessage, boolean local, String command, String... args) {
        try {
            if (command.equalsIgnoreCase("reload")) {
                CloudBot.getInstance().getFileManager().load();
                sendMessage.accept("&2Successfully reloaded from configuration!");
                return;
            }
            if (command.equalsIgnoreCase("info")) {
                sendMessage.accept("&9&lAuthor: &2Lasergott | Sebastian");
                sendMessage.accept("&9Version: &2" + CloudBot.class.getPackage().getImplementationVersion());
                sendMessage.accept("&9Our Homepage: &2https://timo.cloud");
                return;
            }
            if (command.equalsIgnoreCase("help")) {
                sendHelp(sendMessage);
                return;
            }
            if (command.equalsIgnoreCase("broadcast")) {
                if (args.length == 0) {
                    info("Please use: broadcast <Message>");
                    return;
                }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String allArgs = sb.toString().trim();
                    MessageManager.broadcast(allArgs);
                    info("Successfully send message to the TeamSpeak server! (" + allArgs + ")");
                    return;
            }
            if (command.equalsIgnoreCase("exit")) {
                MessageManager.broadcast("CloudBot is now offline! :(");
                System.exit(0);
                return;
            }
        } catch (Exception e) {
            sendError(sendMessage, local, "Wrong usage.");
            sendHelp(sendMessage);
            e.printStackTrace();
            return;
        }
        sendError(sendMessage, local, "Unknown command: " + command);
        sendHelp(sendMessage);
    }
}
