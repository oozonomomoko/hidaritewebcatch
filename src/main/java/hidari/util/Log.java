package hidari.util;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 左手掐腰
 * @since 2019/10/11 15:54
 */
public class Log {
    private static JTextArea jTextArea;

    private static final SimpleDateFormat format = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

    private static final String INFO = "[INFO]";
    private static final String ERROR = "[ERROR]";

    public static void info(String log) {
        if (null == log)
            return;
        if (null == jTextArea)
            System.out.println(INFO + log);
        else {
            jTextArea.append(format.format(new Date()) + INFO + log + "\r\n");
            jTextArea.setSelectionStart(jTextArea.getDocument().getLength());
        }
    }

    public static void error(String log) {
        if (null == log)
            return;
        if (null == jTextArea)
            System.err.println(ERROR + log);
        else {
            jTextArea.append(format.format(new Date()) + ERROR + log + "\r\n");
            jTextArea.setSelectionStart(jTextArea.getDocument().getLength());
        }
    }

    public static void error(Exception e) {
        if (null == jTextArea)
            System.out.println(ERROR + e.getMessage());
        else {
            jTextArea.append(format.format(new Date()) + ERROR + e.getMessage() + "\r\n");
            jTextArea.setSelectionStart(jTextArea.getDocument().getLength());
        }
    }

    public static void init(JTextArea jTextArea1) {
        Log.jTextArea = jTextArea1;
    }
}
