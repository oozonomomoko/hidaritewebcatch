package hidari;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 左手掐腰
 * @since 2019/10/12 17:49
 */
public class RegUtil {

    /**
     * 正则表达式缓存
     */
    private static Map<String, Pattern> regTemp = new ConcurrentHashMap<>();

    /**
     * windows文件命名不可用字符
     */
    private static final Pattern illegal = Pattern.compile("[\\\\/:\\*\\?\"<>\\|(\\n)]");

    public static String getLegalName(String src) {
        return illegal.matcher(src).replaceAll("");
    }

    public static List<String> find(String content, String reg) {
        return find(content, reg, false, null);
    }
    /**
     * 正则匹配查找字符串
     * @param content 原文
     * @param reg 正则表达式
     * @param replace 是否将匹配到的内容替换到forReplace并返回
     * @param forReplace format string，如 I am %s, %s years old.
     * @return
     */
    public static List<String> find(String content, String reg, boolean replace, String forReplace) {
        Pattern p = regTemp.get(reg);
        if (null == p) {
            p = Pattern.compile(reg);
            regTemp.putIfAbsent(reg, p);
        }

        Matcher m = p.matcher(content);

        List<String> result = new ArrayList<>();
        while (m.find()) {
            List<String> groupList = new ArrayList<>();
            if (0 == m.groupCount()) {
                result.add(replace?String.format(forReplace, m.group()):m.group());
            } else if (0 < m.groupCount()) {
                for (int i = 1; i <= m.groupCount(); i++) {
                    groupList.add(m.group(i));
                }
                result.add(replace?String.format(forReplace, groupList.toArray()):groupList.get(0));
            }
        }
        return result;
    }
}
