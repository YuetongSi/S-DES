package function;
//4.暴力破解
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteForceAttack {

    public static void main(String[] args) {
        long totalTime = 0; // 暴力破解用时
        int KeysNumber = 0; // 找到的秘钥对数量
        List<int[]> plaintextList = new ArrayList<>();
        List<int[]> ciphertextList = new ArrayList<>();

        // 明密文对
        //key=1111100000
        plaintextList.add(new int[]{0, 1, 0, 1,0, 1, 0, 1});
        ciphertextList.add(new int[]{0, 0, 0, 1, 0, 1, 1, 1});

        //key=1010101010
        plaintextList.add(new int[]{0,0, 0, 0, 0,0, 0, 0});
        ciphertextList.add(new int[]{1, 1, 1, 1, 1, 0, 1, 0});

        //key=1011010010
        plaintextList.add(new int[]{ 0, 0, 1, 0, 1,1,1,1});
        ciphertextList.add(new int[]{1, 1, 0,0, 0, 0, 0, 1});

        for (int num = 0; num < plaintextList.size(); num++) {
            int[] plaintext = plaintextList.get(num);
            int[] ciphertext = ciphertextList.get(num);

            long startTime = System.nanoTime(); // 记录开始时间（纳秒）

            for (int i = 0; i < 1024; i++) { // 2^10 = 1024
                // 将i转换为10位二进制形式作为密钥
                String binaryKey = Integer.toBinaryString(i);
                while (binaryKey.length() < 10) {
                    // 补充前导0，确保密钥长度为10位
                    binaryKey = "0" + binaryKey;
                }

                // 将测试秘钥与秘文进行输入解密函数中进行解密，得到测试明文
                int[] decrypted = SDES_Decrypt.decrypt(ciphertext, binaryKey);
                // 将测试明文与实际明文进行比较
                if (Arrays.equals(plaintext, decrypted)) {
                    long endTime = System.nanoTime();
                    long decryptionTime = endTime - startTime;
                    totalTime += decryptionTime; // 累计解密时间
                    KeysNumber++;
                    System.out.println("Possible key is:  " + binaryKey);
                    System.out.println("Crack Time: " + decryptionTime / 1_000 + " microsecond");
                    break;
                }
            }
        }

        if (KeysNumber > 0) {
            long averageDecryptionTime = totalTime / KeysNumber / 1_000_000; // 平均时间毫秒
            System.out.println("Average Decryption Time is " + averageDecryptionTime + " millisecond");
        } else {
            System.out.println("No keys found!");
        }
    }

}
