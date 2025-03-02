package main;

import main.utils.FileUtil;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        // 检查命令行参数是否正确
        if (args.length != 3) {
            System.out.println("请确保输入必要的运行参数,用法：java -jar Main.jar <原版文件路径> <抄袭文件路径> <答案文件路径>");
            return;
        }

        // 获取三个文件路径
        String originalFilePath = args[0];
        String plagiarizedFilePath = args[1];
        String answerFilePath = args[2];

        try {
            // 读取原始文件内容
            String originalContent = FileUtil.readFile(originalFilePath);
//            System.out.println(originalContent);

            // 读取抄袭文件内容
            String plagiarizedContent = FileUtil.readFile(plagiarizedFilePath);
//            System.out.println(plagiarizedContent);




        } catch (FileNotFoundException e) {
            System.out.println("找不到文件: " + e.getMessage());
        }
    }
}
