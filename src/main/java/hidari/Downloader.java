package hidari;

import hidari.dto.CatchConfig;
import hidari.dto.Operate;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 左手掐腰
 * @since 2019/10/11 15:39
 */
public final class Downloader {

    private static ExecutorService thread;

    public static void init(int downloadThCount){
        if (null == thread || thread.isTerminated()) {
            Log.info("初始化下载线程：" + downloadThCount);
            thread = Executors.newFixedThreadPool(downloadThCount);
        }
    }
    public static Future<?> download(String url, String filePath) {
        return thread.submit(()->{
            try {
                Thread.sleep(1);
                Log.info(Operate.OPERATE_DOWNLOAD.getOperateName() + url + "--->" + filePath);
                Connection.Response rsp = getConnection(url).execute();
                try (BufferedInputStream bis = rsp.bodyStream()){
                    File f = new File(filePath);
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
            return getConnection(url).get();
        } catch (IOException e) {
            Log.error(e);
        }
        return null;
    }

    private static Connection getConnection(String url) {
        if (null == CatchConfig.proxy) {
            return Jsoup.connect(url).ignoreContentType(true).maxBodySize(10485760);
        } else {
            return Jsoup.connect(url).ignoreContentType(true).maxBodySize(10485760).proxy(CatchConfig.proxy);
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
        Log.info("下载已停止");
    }
}
