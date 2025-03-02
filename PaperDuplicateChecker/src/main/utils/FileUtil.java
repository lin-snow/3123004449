package main.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    /**
     * 读取指定文件的内容
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static String readFile(String filePath) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("找不到文件： " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO错误： " + e.getMessage());
        }

        return content.toString();
    }

    public static void writeFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("IO错误: " + e.getMessage());
        }
    }
}
