package hidari;

import hidari.dto.CatchStep;
import hidari.dto.Operate;
import hidari.dto.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 左手掐腰
 * @since 2019/10/11 11:11
 */
public class CatchStarter {
    public static final String SPLIT = ",";

    /**
     * 下载进度条
     */
    public static JProgressBar downloadProgress;

    /**
     * 爬虫主线程
     */
    private static ExecutorService thread;

    /**
     * 是否正在执行
     */
    public static AtomicBoolean isWorking = new AtomicBoolean();

    /**
     * 初始化爬虫线程，单线程
     */
    private static void init(int downloadThCount) {
        if (null == thread || thread.isTerminated()) {
            Log.info("初始化主线程");
            thread = Executors.newSingleThreadExecutor();
        }
        // 初始化下载线程
        Downloader.init(downloadThCount);
    }

    /**
     * 爬虫开始
     *
     * @param steps
     * @param source
     */
    public static synchronized void start(CatchStep steps, String source, int thCount) {
        // 判断任务是否在执行
        if (isWorking.compareAndSet(false, true)) {
            // 初始化爬虫
            init(thCount);
            thread.submit(() -> {
                // 步骤数量
                downloadProgress.setMaximum(0);
                List<Future> downloadResults = new ArrayList<>();
                List<String> sources = new ArrayList<>();
                sources.add(source);
                Log.info("=============开始=============");
                start(steps, sources, 0, new HashMap<>(), downloadResults);

                // 等待下载任务完成
                try {
                    for (Future future : downloadResults) {
                        while (!future.isDone()) {
                            Thread.sleep(200);
                        }
                    }
                } catch (InterruptedException e) {
                    Log.error(e);
                }
                isWorking.set(false);
                Log.info("下载任务完成，任务数:" + downloadResults.size());
                Log.info("=============完成=============");
            });
        } else {
            Log.error("任务正在执行...");
        }
    }

    // 爬虫进度
    private static void addProgress(JProgressBar progress) {
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            Log.error(e);
//        }
        progress.setValue(progress.getValue() + 1);
    }

    // 下载进度
    private static void addDownloadProgressMax(int max) {
        downloadProgress.setMaximum(downloadProgress.getMaximum() + max);
    }

    public static synchronized void addDownloadProgress() {
        addProgress(downloadProgress);
    }

    private static void start(CatchStep catchStep, String source, int index, Map<String, String> variables, List<Future> downloadResults) {
        List<String> sources = new ArrayList<>();
        sources.add(source);
        start(catchStep, sources, index, variables, downloadResults);
    }

    private static void start(CatchStep catchStep, List<String> source, int index, Map<String, String> variables, List<Future> downloadResults) {
        if (null == catchStep) {
            return;
        }
        JProgressBar progress = catchStep.getProgress();
        String logpre = String.join("", Collections.nCopies(index++, "  ")) + index;
        if (source.isEmpty()) {
            Log.info(logpre + "没有可处理的地址");
            progress.setMaximum(1);
            progress.setValue(1);
            return;
        }
        progress.setMaximum(source.size());
        progress.setValue(0);

        /*
         * 资源操作方式
         * 0. 访问资源，直接下载resource，无下一步
         * 1. 访问资源，获取结果 正则匹配
         * 2. 访问资源，获取结果 css选择器匹配
         * 3. 处理资源地址，不访问
         */
        switch (catchStep.getOperateType()) {
            case 0:
                addDownloadProgressMax(source.size());
                for (String url : source) {
                    String filePath = replaceFileVar(catchStep.getDownloadDir(), variables) + "\\" + getNameFromUrl(url, catchStep, variables);
                    downloadResults.add(Downloader.download(url, filePath));
                    addProgress(progress);
                }
                start(catchStep.getNext(), source, index, variables, downloadResults);
                break;
            case 1:
                for (String url : source) {
                    Document content = Downloader.getDoc(url);
                    if (null != content) {
                        if (Operate.REG_VAR_ADD.getOperateType() == catchStep.getRegReplace()) {
                            String[] vars = catchStep.getRegSource().split(SPLIT);
                            String[] regs = catchStep.getRegSelector().split(SPLIT);
                            if (regs.length != vars.length) {
                                Log.error(logpre + Operate.OPERATE_REG.getOperateName() + "设置变量失败,正则表达式格式和变量个数不相等");
                                return;
                            }
                            for (int i = 0; i < vars.length; i++) {
                                List<String> temp = RegUtil.find(content.toString(), regs[i]);
                                if (temp.size() == 0) {
                                    Log.error(logpre + Operate.OPERATE_REG.getOperateName() + "设置变量失败,正则未匹配到字符串：" + content.toString());
                                    return;
                                }
                                variables.put(vars[i], temp.get(0));
                                Log.info(logpre + Operate.OPERATE_REG.getOperateName() + "设置变量成功," + vars[i] + ":" + temp.get(0));
                            }
                            start(catchStep.getNext(), url, index, variables, downloadResults);
                        } else {
                            List<String> temp = RegUtil.find(content.toString(), catchStep.getRegSelector(),
                                    catchStep.getRegReplace() == Operate.REG_REPLACE.getOperateType(), catchStep.getRegSource());
//                            Log.info(logpre + Operate.OPERATE_REGRESULT.getOperateName() + url + "-->" + temp);
                            CatchStep next = stepSetVariables(catchStep.getNext(), variables, content.toString(), index);
                            start(next, temp, index, variables, downloadResults);
                        }
                    } else {
                        Log.error(logpre + Operate.OPERATE_REGRESULT.getOperateName() + "页面获取失败：" + url);
                    }
                    addProgress(progress);
                }
                break;
            case 2:
                for (String url : source) {
                    Document content = Downloader.getDoc(url);
                    List<String> temp = new ArrayList<>();
                    if (null == content) {
                        Log.error(logpre + Operate.OPERATE_CSSRESULT.getOperateName() + "页面获取失败：" + url);
                    } else {
                        Elements eles = content.select(catchStep.getCssSelector());
                        if (0 == catchStep.getAttrType()) {
                            for (Element ele : eles) {
                                temp.add(ele.attr(catchStep.getAttrName()));
                            }
                        } else if (1 == catchStep.getAttrType()) {
                            for (Element ele : eles) {
                                temp.add(ele.html());
                            }
                        } else if (2 == catchStep.getAttrType()) {
                            for (Element ele : eles) {
                                temp.add(ele.toString());
                            }
                        }
//                        Log.info(logpre + Operate.OPERATE_CSSRESULT.getOperateName() + url + "-->" + temp);
                        start(catchStep.getNext(), temp, index, variables, downloadResults);
                    }
                    addProgress(progress);
                }
                break;
            case 3:
                for (String url : source) {
                    if (Operate.REG_VAR_ADD.getOperateType() == catchStep.getRegReplace()) {
                        String[] vars = catchStep.getRegSource().split(SPLIT);
                        String[] regs = catchStep.getRegSelector().split(SPLIT);
                        if (regs.length != vars.length) {
                            Log.error(logpre + Operate.OPERATE_REG.getOperateName() + "设置变量失败,正则表达式格式和变量个数不相等");
                            return;
                        }
                        for (int i = 0; i < vars.length; i++) {
                            List<String> temp = RegUtil.find(url, regs[i]);
                            if (temp.size() == 0) {
                                Log.error(logpre + Operate.OPERATE_REG.getOperateName() + "设置变量失败,正则未匹配到字符串：" + url);
                                return;
                            }
                            variables.put(vars[i], temp.get(0));
                            Log.info(logpre + Operate.OPERATE_REG.getOperateName() + "设置变量成功," + vars[i] + ":" + temp.get(0));
                        }
                        start(catchStep.getNext(), url, index, variables, downloadResults);
                    } else {
                        List<String> temp = RegUtil.find(url, catchStep.getRegSelector(), catchStep.getRegReplace() == 1, replaceVar(catchStep.getRegSource(), variables));
//                        Log.info(logpre + Operate.OPERATE_REG.getOperateName() + url + "-->" + temp);
                        start(catchStep.getNext(), temp, index, variables, downloadResults);
                    }
                    addProgress(progress);
                }
                break;
            case 4:
                int min, max;
                boolean isChar;
                int fixedLen = 0;
                String fixedChar = null;
                try {
                    String minStr = replaceVar(catchStep.getPageMin(), variables);
                    if (minStr.contains("|")) {
                        String[] split = minStr.split("\\|");
                        if (split.length != 3) {
                            Log.error(logpre + Operate.OPERATE_PAGE.getOperateName() + "固定长度格式应为：{length}|{fixedChar}|{min}");
                            return;
                        }
                        fixedLen = Integer.valueOf(split[0]);
                        fixedChar = split[1];
                        minStr = split[2];
                    }
                    Pair<Boolean, Integer> minPair = parsePage(minStr);
                    Pair<Boolean, Integer> maxPair = parsePage(replaceVar(catchStep.getPageMax(), variables));
                    min = minPair.getRight();
                    max = maxPair.getRight();
                    Log.info(logpre + Operate.OPERATE_PAGE.getOperateName() + "页码:" + catchStep.getPageMin() + "~" + catchStep.getPageMax());
                    isChar = minPair.getLeft() && maxPair.getLeft();
                } catch (NumberFormatException e) {
                    Log.error(logpre + Operate.OPERATE_PAGE.getOperateName() + "替换页码失败:" + catchStep.getPageMin() + "~" + catchStep.getPageMax());
                    return;
                }
                if (min > max) {
                    Log.error(logpre + Operate.OPERATE_PAGE.getOperateName() + "页码错误:" + catchStep.getPageMin() + "~" + catchStep.getPageMax());
                    return;
                }
                for (String url : source) {
                    List<String> temp = new ArrayList<>();
                    for (int i = min; i <= max; i++) {
                        String nexStr = url.replaceFirst("\\{page\\}", getReplaceTarget(i, isChar, fixedLen, fixedChar));
                        temp.add(nexStr);
                    }
//                    Log.info(logpre + Operate.OPERATE_PAGE.getOperateName() + url + "-->" + temp);
                    start(catchStep.getNext(), temp, index, variables, downloadResults);
                    addProgress(progress);
                }
                break;
            case 5:
                if (Operate.VAR_ADD.getOperateType() == catchStep.getVarOperate()) {
                    String value = replaceVar(catchStep.getVarValue(), variables);
                    variables.put(catchStep.getVarName(), value);
                    Log.info(logpre + Operate.OPERATE_VAR.getOperateName() + "新增变量成功," + catchStep.getVarName() + ":" + value);
                }
                start(catchStep.getNext(), source, index, variables, downloadResults);
                progress.setValue(source.size());
                break;
            case 6:
                if (Operate.VAR_ADD.getOperateType() == catchStep.getVarOperate()) {
                    Downloader.headers.put(catchStep.getVarName(), catchStep.getVarValue());
                    Log.info(logpre + Operate.OPERATE_VAR.getOperateName() + "新增header成功," + catchStep.getVarName());
                }
                start(catchStep.getNext(), source, index, variables, downloadResults);
                progress.setValue(source.size());
                break;
            case 7:
                for (int i = 0; i < source.size(); i++) {
                    start(catchStep.getNext(), replaceVar(catchStep.getVarValue(),variables), index, variables, downloadResults);
                    addProgress(progress);
                }
                break;
        }
    }

    private static String getReplaceTarget(int i, boolean isChar, int fixedLen, String fixedChar) {
        String result = isChar ? Character.toString((char) i) : String.valueOf(i);
        if (fixedLen == 0) {
            return result;
        }
        String tmp = "";
        for (int n = 0; n < fixedLen - result.length(); n++) {
            tmp += fixedChar;
        }
        return tmp + result;
    }

    private static Pair<Boolean, Integer> parsePage(String str) {
        if (str.length() != 1) {
            return new Pair<>(false, Integer.valueOf(str));
        }
        try {
            return new Pair<>(false, Integer.valueOf(str));
        } catch (NumberFormatException e) {
            return new Pair<>(true, (int) str.charAt(0));
        }

    }


    private static CatchStep stepSetVariables(CatchStep next, Map<String, String> variables, String content, int index) {
        if (null == next)
            return null;
        String logpre = String.join("", Collections.nCopies(index, "  ")) + index++;
        if (next.getOperateType() == Operate.OPERATE_REGRESULT.getOperateType()
                && next.getRegReplace() == Operate.REG_VAR_ADD.getOperateType()) {
            List<String> temp = RegUtil.find(content, next.getRegSelector());
            next.getProgress().setMaximum(1);
            next.getProgress().setValue(1);
            if (temp.size() == 0) {
                Log.error(logpre + Operate.OPERATE_REGRESULT.getOperateName() + "设置变量失败,正则未匹配到字符串");
            } else {
                variables.put(next.getRegSource(), temp.get(0));
                Log.info(logpre + Operate.OPERATE_REGRESULT.getOperateName() + "设置变量成功," + next.getRegSource() + ":" + temp.get(0));
                return stepSetVariables(next.getNext(), variables, content, index);
            }
            return null;
        } else if (next.getOperateType() == Operate.OPERATE_VAR.getOperateType()
                && next.getVarOperate() == Operate.VAR_ADD.getOperateType()) {
            String value = replaceVar(next.getVarValue(), variables);
            variables.put(next.getVarName(), value);
            Log.info(logpre + Operate.OPERATE_VAR.getOperateName() + "新增变量成功," + next.getVarName() + ":" + value);
            next.getProgress().setMaximum(1);
            next.getProgress().setValue(1);
            return stepSetVariables(next.getNext(), variables, null, index);
        }
        return next;
    }

    private static String replaceVar(String src, Map<String, String> variables, boolean file) {
        Set<Map.Entry<String, String>> entrys = variables.entrySet();
        for (Map.Entry<String, String> entry : entrys) {
            src = src.replace("{" + entry.getKey() + "}", file ? RegUtil.getLegalName(entry.getValue()) : entry.getValue());
        }
        return src;
    }

    private static String replaceVar(String src, Map<String, String> variables) {
        return replaceVar(src, variables, false);
    }

    private static String replaceFileVar(String src, Map<String, String> variables) {
        return replaceVar(src, variables, true);
    }


    /**
     * 从url获取文件名，包含后缀
     *
     * @param url
     * @param catchStep
     * @param variables
     * @return
     */
    private static String getNameFromUrl(String url, CatchStep catchStep, Map<String, String> variables) {
        /*
         * 文件名获取方式,
         * 0:从url中获取，重名时加上随机字符命名
         * 1:随机生成文件名，
         * 2:自定义，
         */
        int fileNameFrom = catchStep.getFileNameFrom();
        /*
         * 文件后缀获取方式
         * 0：从url中获取
         * 1：自定义文件后缀
         */
        int fileTypeFrom = catchStep.getFileTypeFrom();

        // 1
        if (1 == fileNameFrom && 1 == fileTypeFrom)
            return UUID.randomUUID().toString() + catchStep.getFileType();

        // 从地址中取出文件名称和类型
        String fullNameFromUrl;
        int a = url.lastIndexOf('/');
        int b = url.indexOf('?');
        int c = url.indexOf('#');
        if (-1 == b && -1 == c)
            fullNameFromUrl = url.substring(a + 1);
        else
            fullNameFromUrl = url.substring(a + 1, -1 == b ? c : b);

        // 2
        if (0 == fileNameFrom && 0 == fileTypeFrom)
            return fullNameFromUrl;

        int dotIndex = fullNameFromUrl.lastIndexOf('.');
        String fileName;
        String fileType;
        if (-1 == dotIndex) {
            fileName = fullNameFromUrl;
            fileType = "";
        } else {
            fileName = fullNameFromUrl.substring(0, dotIndex);
            fileType = fullNameFromUrl.substring(dotIndex);
        }

        String finalName;
        if (0 == fileNameFrom)
            finalName = fileName;
        else if (1 == fileNameFrom)
            finalName = UUID.randomUUID().toString();
        else
            finalName = replaceFileVar(catchStep.getFileName(), variables);

        return finalName + (0 == fileTypeFrom ? fileType : catchStep.getFileType());
    }

    public static void stop() {
        Downloader.stop();
        if (null != thread) {
            thread.shutdownNow();
            while (true) {
                try {
                    if (thread.isTerminated()) {
                        break;
                    }
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.info("任务已停止");
        isWorking.set(false);
    }
}
