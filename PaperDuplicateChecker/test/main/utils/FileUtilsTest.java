package main.utils;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    @TempDir
    Path tempDir;
    private File testFile;

    @BeforeEach
    void setUp() {
        testFile = new File(tempDir.toFile(), "test.txt");
    }

    @Test
    void testReadFileSuccess() throws FileNotFoundException {
        // 准备测试数据
        String expectedContent = "Hello\nWorld\n";
        FileUtil.writeFile(testFile.getAbsolutePath(), expectedContent);

        // 执行测试
        String actualContent = FileUtil.readFile(testFile.getAbsolutePath());

        // 验证结果
        assertEquals(expectedContent, actualContent, "文件内容应该与写入的内容一致");
    }

    @Test
    void testReadFileEmptyFile() throws FileNotFoundException {
        // 创建空文件
        FileUtil.writeFile(testFile.getAbsolutePath(), "");

        // 执行测试
        String content = FileUtil.readFile(testFile.getAbsolutePath());

        // 验证结果
        assertEquals("", content, "空文件应该返回空字符串");
    }

    public static String readFile(String filePath) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            throw e; // 重新抛出异常，而不是只打印
        } catch (IOException e) {
            System.out.println("IO错误： " + e.getMessage());
        }
        return content.toString();
    }

    @Test
    void testWriteFileEmptyContent() throws FileNotFoundException {
        // 测试写入空内容
        FileUtil.writeFile(testFile.getAbsolutePath(), "");
        String content = FileUtil.readFile(testFile.getAbsolutePath());

        // 验证结果
        assertEquals("", content, "写入空内容应该创建空文件");
        assertTrue(testFile.exists(), "文件应该已被创建");
    }

    @Test
    void testReadFileNotFound() {
        String nonExistentFile = "non_existent_file.txt";
        String content = FileUtil.readFile(nonExistentFile);
        assertEquals("", content, "文件不存在时应返回空字符串");
        // 注意：当前实现捕获 FileNotFoundException 并打印消息，未抛出
    }

    @Test
    void testWriteFileIOException() throws FileNotFoundException {
        File readOnlyFile = new File(tempDir.toFile(), "readonly.txt");
        FileUtil.writeFile(readOnlyFile.getAbsolutePath(), "Initial content");
        readOnlyFile.setReadOnly(); // 设置为只读

        // 再次尝试写入
        FileUtil.writeFile(readOnlyFile.getAbsolutePath(), "New content");
        String content = FileUtil.readFile(readOnlyFile.getAbsolutePath());
        assertEquals("Initial content", content.trim(), "只读文件写入应失败，内容不变");
        // 注意：当前实现捕获 IOException 并打印消息，未抛出
    }

    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}