package com.workdance.chatbot.core.util;

import static com.workdance.chatbot.config.Constant.STATIC_SERVICE_HOSTNAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {
    /**
     * 获取文件名
     *
     * @param filepath dir+filename
     */
    public static String getFileNameFromPath(String filepath) {
        if ((filepath != null) && (filepath.length() > 0)) {
            int sep = filepath.lastIndexOf('/');
            if ((sep > -1) && (sep < filepath.length() - 1)) {
                return filepath.substring(sep + 1);
            }
        }
        return filepath;
    }

    public static byte[] readFile(String filePath) {
        File file = new File(filePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];
            inputStream.read(content);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStaticFilePath(String filePath) {
        if (filePath.startsWith("http")) {
            return filePath;
        }
        return STATIC_SERVICE_HOSTNAME + filePath;
    }
}
