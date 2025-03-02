package main.core;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class SimHashUtils {
    private static final int HASH_SIZE = 128;

    /**
     * 生成SimHash
     *
     * @param text
     * @return
     */
    public static BigInteger generateSimHash(String text) {
        // 分词： 将文本分解为特征（词或短语）。
        String[] NGrams = TextPreprocessor.genNGrams(text);

        // 特征加权：为每个特征分配权重（例如词频）
        Map<String, Integer> freqMap = getFreqMap(NGrams);

        // 对每个特征计算哈希并加权合并
        double[] vector = getFeaturesHash(freqMap);
//        Arrays.stream(vector).forEach(System.out::println);

        // 生成 SimHash
        BigInteger simHash = BigInteger.ZERO;
//        double totalWeight = freqMap.values().stream().mapToDouble(Integer::doubleValue).sum();
        for (int i = 0; i < HASH_SIZE; i++) {
            if (vector[i] > 0) {
                simHash = simHash.setBit(i);
            }
        }
        return simHash;
    }

    /**
     * 统计词频，特征加权
     * @param nGrams
     * @return
     */
    public static Map<String, Integer> getFreqMap(String[] nGrams) {
        HashMap<String, Integer> freqMap = new HashMap<>();
        for (String feature : nGrams) {
            freqMap.put(feature, freqMap.getOrDefault(feature, 0) + 1);
        }
        return freqMap;
    }

    /**
     * 对每个特征计算哈希并加权合并
     *
     * @param freqMap
     * @return
     */
    public static double[] getFeaturesHash(Map<String, Integer> freqMap) {
        double[] vector = new double[HASH_SIZE];
//        int totalFeatures = freqMap.values().stream().mapToInt((Integer integer) -> integer).sum();
        for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            String feature = entry.getKey();
            double weight = entry.getValue();
            byte[] hash = md5Hash(feature);

            // 加权合并
            for (int i = 0; i < HASH_SIZE; i++) {
                int bit = (hash[i / 8] >> (i % 8)) & 1;
                vector[i] += (bit == 1) ? weight : -weight;
            }
        }
        return vector;
    }

    // 计算 MD5 哈希
    public static byte[] md5Hash(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("MD5 计算失败", e);
        }
    }

    /**
     * 计算汉明距离
     * 
     * @param hash1
     * @param hash2
     * @return
     */
    public static int hammingDistance(BigInteger hash1, BigInteger hash2) {
        return hash1.xor(hash2).bitCount();
    }

    /**
     * 计算相似度
     *
     * @param hash1
     * @param hash2
     * @return
     */
    public static double getSimilarity(BigInteger hash1, BigInteger hash2) {
        int distance = hammingDistance(hash1, hash2);
        return ((double)(HASH_SIZE - distance) / (double)HASH_SIZE);
    }
}
