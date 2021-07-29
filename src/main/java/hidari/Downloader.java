package hidari;

import hidari.dto.CatchConfig;
import hidari.dto.Operate;
import hidari.util.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 左手掐腰
 * @since 2019/10/11 15:39
 */
public final class Downloader {
    public static Map<String, String> headers = new HashMap<>();

    private static int MAX_BODY = 1024 * 1024 * 20;

    private static ExecutorService thread;

    public static void init(int downloadThCount, int maxSize) {
        if (null == thread || thread.isTerminated()) {
            Log.info("初始化下载线程：" + downloadThCount);
            thread = Executors.newFixedThreadPool(downloadThCount);
            MAX_BODY = maxSize * 1024 * 1024;
        }
    }

    public static Future<?> download(String url, String filePath) {
        String format = url.replace("\\", "");
        return thread.submit(() -> {
            try {
                File f = new File(filePath);
                if (f.exists()) {
                    Log.info(Operate.OPERATE_DOWNLOAD.getOperateName() + format + "--exist->" + filePath);
                    return;
                }
                Log.info(Operate.OPERATE_DOWNLOAD.getOperateName() + format + "--->" + filePath);
                Connection.Response rsp = getConnection(format).execute();
                try (BufferedInputStream bis = rsp.bodyStream()) {
                    if (!f.getParentFile().exists())
                        f.getParentFile().mkdirs();
                    // 替换已存在的文件
                    Files.copy(bis, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    Log.error(e);
                }
            } catch (Exception e) {
                Log.error(e);
            } finally {
                CatchStarter.addDownloadProgress();
            }
        });
    }

    public static Document getDoc(String url) {
        try {
            Connection.Response rsp = getConnection(url).execute();
            if (rsp.statusCode() == 302) {
                return getDoc(rsp.header("location"));
            }
            return rsp.parse();
        } catch (IOException e) {
            Log.error(e);
        }
        return null;
    }

    private static Connection getConnection(String url) {
        if (null == CatchConfig.proxy) {
            return Jsoup.connect(url).ignoreContentType(true).headers(headers).maxBodySize(MAX_BODY);
        } else {
            return Jsoup.connect(url).ignoreContentType(true).headers(headers).maxBodySize(MAX_BODY).proxy(CatchConfig.proxy);
        }
    }

    public static void stop() {
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
        headers.clear();
        Log.info("下载已停止");
    }
}
