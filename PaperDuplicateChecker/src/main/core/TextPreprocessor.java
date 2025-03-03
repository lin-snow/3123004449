package main.core;

public class TextPreprocessor {
    /**
     * 分词
     *
     * @param text
     * @return
     */
    public static String[] genNGrams(String text) {
        // 预处理
        text = preprocess(text);

        // 如果文本长度小于2，直接返回单字符数组
        if (text.length() < 2) {
            return new String[]{text};
        }

        // 生成Bigram
        String[] ngrams = new String[text.length() - 1];
        for (int i = 0; i < text.length() - 1; i++) {
            ngrams[i] = text.substring(i, i + 2); // 这里使用了2-Gram
        }

        return ngrams;
    }

    /**
     * 预处理文本
     *
     * @param text
     * @return
     */
    public static String preprocess(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        // 全角转半角
        text = ToDBC(text);

        // 去除各类标签和特殊字符
        text = removeTag(text);

        return text;
    }

    /**
     * 全角转半角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        if (input == null) return ""; // 处理 null 输入，与其他方法一致
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32; // 全角空格转半角
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248); // 全角字符转半角
            }
            // 特殊处理中文全角标点
            if (c[i] == '，') c[i] = ',';
            if (c[i] == '。') c[i] = '.';
        }
        return new String(c);
    }

    /**
     * 去除标签 和特殊字符
     *
     * @param text
     * @return
     */
    public static String removeTag(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        // 保留中文（\u4E00-\u9FA5）和英文字符（a-zA-Z0-9），移除其他所有字符
        return text.replaceAll("[^\\u4E00-\\u9FA5a-zA-Z0-9]", "");
    }
}
