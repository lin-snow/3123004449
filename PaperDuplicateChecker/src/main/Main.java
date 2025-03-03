package main;

import main.core.SimHashUtils;
import main.utils.FileUtil;

import java.io.FileNotFoundException;
import java.math.BigInteger;


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
            System.out.println("对比文本1： " + originalContent);

            // 读取抄袭文件内容
            String plagiarizedContent = FileUtil.readFile(plagiarizedFilePath);
            System.out.println("对比文本2： " + plagiarizedContent);

            BigInteger hash1 = SimHashUtils.generateSimHash(originalContent);
            BigInteger hash2 = SimHashUtils.generateSimHash(plagiarizedContent);


//            System.out.println(hash1);
//            System.out.println(hash2);
            double similarity = SimHashUtils.getSimilarity(hash1, hash2);
            String result = String.format("%.2f", similarity * 0.4 + SimHashUtils.getJaccardSimilarity(originalContent, plagiarizedContent) * 0.6);
            System.out.printf("相似度：" + result);

            // 将结果写入文件
            FileUtil.writeFile(answerFilePath, result);


        } catch (FileNotFoundException e) {
            System.out.println("找不到文件: " + e.getMessage());
        }
    }
}
