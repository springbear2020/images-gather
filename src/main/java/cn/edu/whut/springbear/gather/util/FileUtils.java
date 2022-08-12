package cn.edu.whut.springbear.gather.util;

import java.io.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 16:18 Thursday
 */
public class FileUtils {
    /**
     * Determine whether the directory of the specified path exists, create it if it does not exists
     */
    public static boolean createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }

    /**
     * Write string content to the specified file path in override mode
     *
     * @param content  String content
     * @param filePath Full path of the file
     * @return true or false
     */
    public static boolean writeStringContent(String content, String filePath) {
        File file = new File(filePath);
        // Create new file if not exists or not a file
        if (!file.exists() || !file.isFile()) {
            try {
                if (!file.createNewFile()) {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bufferedWriter.write(content);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(bufferedWriter).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Compress files in the specified directory into .zip format file
     */
    public static boolean compressDirectory(String srcDirectoryPath, String zipFilePath) {
        File srcDirectory = new File(srcDirectoryPath);
        // Directory not exists or it is not a directory
        if (!(srcDirectory.exists() && srcDirectory.isDirectory())) {
            return false;
        }

        ZipOutputStream zipOutputStream = null;
        try {
            File dstCompressFile = new File(zipFilePath);
            // The dest file not exists
            if (!dstCompressFile.exists()) {
                if (!dstCompressFile.createNewFile()) {
                    return false;
                }
            }

            zipOutputStream = new ZipOutputStream(new FileOutputStream(dstCompressFile));
            // Traverse all file items in the source directory
            File[] fileItems = srcDirectory.listFiles();
            assert fileItems != null;
            for (File fileItem : fileItems) {
                // The single file name
                FileInputStream fileInputStream = new FileInputStream(fileItem);
                zipOutputStream.putNextEntry(new ZipEntry(fileItem.getName()));
                // Write the current file to the dest compress file
                byte[] buffer = new byte[1024];
                int readLen;
                while ((readLen = fileInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, readLen);
                }
                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                Objects.requireNonNull(zipOutputStream).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
