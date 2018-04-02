package cloud.timo.CloudBot.managers;

import cloud.timo.CloudBot.utils.ChatColorUtil;
import cloud.timo.CloudBot.utils.TimeUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class DataManager {

    public void addToLog(String message) {
        try {
            File logsDirectory;
            logsDirectory = new File("logs/");
            logsDirectory.mkdirs();
            File logFile = new File("logs/" + TimeUtil.formatTimeLog() + ".log");
            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
            if (!logFile.exists())
                logFile.createNewFile();
            bw.write(String.format("[%s]" + " [CloudBot] " + "%s\n",
                    TimeUtil.formatTimeLog(),
                    message));
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final String getPrefix() {
        return ChatColorUtil.ANSI_YELLOW + "[" + ChatColorUtil.ANSI_BLUE + "Cloud" + ChatColorUtil.ANSI_RESET + "Bot" + ChatColorUtil.ANSI_YELLOW + "]" + ChatColorUtil.ANSI_RESET + " ";
    }
}
