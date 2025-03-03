package main.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextPreprocessorTest {

    // 测试 genNGrams 方法
    @Test
    void testGenNGramsNormalText() {
        String text = "Hello世界";
        String[] result = TextPreprocessor.genNGrams(text);
        String[] expected = {"He", "el", "ll", "lo", "o世", "世界"};
        assertArrayEquals(expected, result, "应该正确生成 Bigram");
    }

    @Test
    void testGenNGramsShortText() {
        String text = "H";
        String[] result = TextPreprocessor.genNGrams(text);
        String[] expected = {"H"};
        assertArrayEquals(expected, result, "短文本应该返回单字符数组");
    }

    @Test
    void testGenNGramsEmptyText() {
        String text = "";
        String[] result = TextPreprocessor.genNGrams(text);
        String[] expected = {""};
        assertArrayEquals(expected, result, "空文本应该返回空字符串数组");
    }

    // 测试 preprocess 方法
    @Test
    void testPreprocessNormalText() {
        String text = "Hello，世界！ 全角空格　test";
        String result = TextPreprocessor.preprocess(text);
        String expected = "Hello世界全角空格test";
        assertEquals(expected, result, "应该正确预处理文本");
    }

    @Test
    void testPreprocessNullText() {
        String result = TextPreprocessor.preprocess(null);
        assertEquals("", result, "null 文本应该返回空字符串");
    }

    @Test
    void testPreprocessEmptyText() {
        String result = TextPreprocessor.preprocess("");
        assertEquals("", result, "空文本应该返回空字符串");
    }

    // 测试 ToDBC 方法
    @Test
    void testToDBCNormalText() {
        String text = "全角空格　全角标点，。";
        String result = TextPreprocessor.ToDBC(text);
        String expected = "全角空格 全角标点,.";
        assertEquals(expected, result, "应该将全角字符转换为半角");
    }

    @Test
    void testToDBCNoFullWidth() {
        String text = "Hello World";
        String result = TextPreprocessor.ToDBC(text);
        assertEquals(text, result, "没有全角字符时应该保持不变");
    }

    @Test
    void testToDBCFullWidthNumbers() {
        String text = "１２３";
        String result = TextPreprocessor.ToDBC(text);
        String expected = "123";
        assertEquals(expected, result, "全角数字应该转换为半角");
    }

    // 测试 removeTag 方法
    @Test
    void testRemoveTagNormalText() {
        String text = "Hello, 世界! 123 #special#";
        String result = TextPreprocessor.removeTag(text);
        String expected = "Hello世界123special";
        assertEquals(expected, result, "应该移除标签和特殊字符");
    }

    @Test
    void testRemoveTagOnlySpecialChars() {
        String text = "<!@#$%^&*()>";
        String result = TextPreprocessor.removeTag(text);
        String expected = "";
        assertEquals(expected, result, "只含特殊字符时应该返回空字符串");
    }

    @Test
    void testRemoveTagChineseAndEnglish() {
        String text = "中文English123!@#";
        String result = TextPreprocessor.removeTag(text);
        String expected = "中文English123";
        assertEquals(expected, result, "应该保留中文和英文字符");
    }

    @Test
    void testRemoveTagNullText() {
        String result = TextPreprocessor.removeTag(null);
        assertEquals("", result, "null 文本应该返回空字符串");
    }
}