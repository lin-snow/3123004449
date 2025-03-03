package main.core;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimHashUtilsTest {

    private static final int HASH_SIZE = 128;

    // 测试 generateSimHash 方法
    @Test
    void testGenerateSimHashNormalText() {
        String text = "Hello World";
        BigInteger simHash = SimHashUtils.generateSimHash(text);
        assertNotNull(simHash, "SimHash 不应为 null");
        assertTrue(simHash.bitLength() <= HASH_SIZE, "SimHash 长度应不超过 128 位");
    }

    @Test
    void testGenerateSimHashEmptyText() {
        String text = "";
        BigInteger simHash = SimHashUtils.generateSimHash(text);
        assertEquals(BigInteger.ZERO, simHash, "空文本的 SimHash 应为零");
    }

    // 测试 getFreqMap 方法
    @Test
    void testGetFreqMapNormal() {
        String[] nGrams = {"ab", "bc", "ab", "cd"};
        Map<String, Integer> freqMap = SimHashUtils.getFreqMap(nGrams);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("ab", 2);
        expected.put("bc", 1);
        expected.put("cd", 1);
        assertEquals(expected, freqMap, "词频统计应正确");
    }

    @Test
    void testGetFreqMapEmptyArray() {
        String[] nGrams = {};
        Map<String, Integer> freqMap = SimHashUtils.getFreqMap(nGrams);
        assertTrue(freqMap.isEmpty(), "空数组应返回空 Map");
    }

    @Test
    void testGetFeaturesHashEmptyMap() {
        Map<String, Integer> freqMap = new HashMap<>();
        double[] vector = SimHashUtils.getFeaturesHash(freqMap);
        assertEquals(HASH_SIZE, vector.length, "向量长度应为 128");
        for (double v : vector) {
            assertEquals(0.0, v, "空 Map 的向量值应全为 0");
        }
    }

    // 测试 md5Hash 方法
    @Test
    void testMd5HashNormal() {
        String input = "test";
        byte[] hash = SimHashUtils.md5Hash(input);
        assertEquals(16, hash.length, "MD5 哈希应为 16 字节");
        byte[] expected = null;
        try {
            expected = MessageDigest.getInstance("MD5")
                    .digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        assertArrayEquals(expected, hash, "MD5 哈希应与标准实现一致");
    }

    @Test
    void testMd5HashEmptyString() {
        String input = "";
        byte[] hash = SimHashUtils.md5Hash(input);
        assertEquals(16, hash.length, "MD5 哈希应为 16 字节");
    }

    // 测试 hammingDistance 方法
    @Test
    void testHammingDistanceSameHash() {
        BigInteger hash = new BigInteger("1010", 2);
        int distance = SimHashUtils.hammingDistance(hash, hash);
        assertEquals(0, distance, "相同哈希的汉明距离应为 0");
    }

    @Test
    void testHammingDistanceDifferentHash() {
        BigInteger hash1 = new BigInteger("1010", 2); // 二进制: 1010
        BigInteger hash2 = new BigInteger("0101", 2); // 二进制: 0101
        int distance = SimHashUtils.hammingDistance(hash1, hash2);
        assertEquals(4, distance, "不同哈希的汉明距离应正确计算");
    }

    // 测试 getSimilarity 方法
    @Test
    void testGetSimilaritySameHash() {
        BigInteger hash = new BigInteger("1010", 2);
        double similarity = SimHashUtils.getSimilarity(hash, hash);
        assertEquals(1.0, similarity, 0.0001, "相同哈希的相似度应为 1.0");
    }

    @Test
    void testGetSimilarityDifferentHash() {
        BigInteger hash1 = SimHashUtils.generateSimHash("Hello World");
        BigInteger hash2 = SimHashUtils.generateSimHash("Hello Java");
        double similarity = SimHashUtils.getSimilarity(hash1, hash2);
        assertTrue(similarity > 0 && similarity < 1, "不同文本的相似度应在 0 到 1 之间");
    }

    @Test
    void testGetSimilarityCompletelyDifferent() {
        BigInteger hash1 = BigInteger.ZERO;
        BigInteger hash2 = BigInteger.ONE.shiftLeft(HASH_SIZE - 1); // 最高位为 1
        double similarity = SimHashUtils.getSimilarity(hash1, hash2);
        assertEquals((double)(HASH_SIZE - 1) / HASH_SIZE, similarity, 0.0001,
                "完全不同的哈希相似度应正确计算");
    }
}