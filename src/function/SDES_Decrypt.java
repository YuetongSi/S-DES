package function;

import java.util.Arrays;
//解密
public class SDES_Decrypt {
    //初始化一些密钥和转换盒
    // 密钥扩展
    private static int[]P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    // 密钥压缩
    private static int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
    private static int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
    private static int[] IP_inv ={4,1,3,5,7,2,8,6};
    private static int[] EP={4, 1, 2, 3, 2, 3, 4, 1};
    private static int[][] S1 = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 0, 2}};
    private static int[][] S2 = {{0, 1, 2, 3}, {2, 3, 1, 0}, {3, 0, 1, 2}, {2, 1, 0, 3}};
    private static int[] SP = {2, 4, 3, 1};

    //输入是加密String类型的ASCII码和秘钥key，输出是解密的String类型的ASCII码
    public static String decryptASCII(String ciphertextASCII, String key) {
        int[][] ciphertextArray = new int[ciphertextASCII.length()][8];
        for (int i = 0; i < ciphertextASCII.length(); i++) {
            char character = ciphertextASCII.charAt(i);
            for (int j = 7; j >= 0; j--) {
                ciphertextArray[i][j] = (character >> (7 - j)) & 1;
            }
        }

        int[][] plaintextArray = new int[ciphertextASCII.length()][8];
        for (int i = 0; i < ciphertextASCII.length(); i++) {
            plaintextArray[i] = decrypt(ciphertextArray[i], key);
        }

        StringBuilder plaintextASCII = new StringBuilder();
        for (int[] value : plaintextArray) {
            char character = 0;
            for (int j = 0; j < 8; j++) {
                character |= (value[j] << (7 - j));
            }
            plaintextASCII.append(character);
        }

        return plaintextASCII.toString();
    }

    //函数的输入是一个String类型的10位秘钥和8位的初始数组信息，输出也是8位的数组
    public static int[] decrypt(int[] ciphertext, String key) {
        // 生成秘钥 k1 和 k2，通过调用 KeyGeneration 类的 generateKey1 函数
        int[][] keys = KeyGeneration.generateKey(P10, P8, key);
        int[] k1 = keys[0];
        int[] k2 = keys[1];

        // 第一层：初始置换
        int[] inputBlock = SDES_Encrypt.initialPermute(IP, ciphertext);
        // 第二层：第一轮:f2，输入 k2 与初始置换的结果 IP(P)
        int[] result2_ni = SDES_Encrypt.roundFunction(k2, inputBlock);
        // 第三层：左右互换
        int[] swap_result = SDES_Encrypt.swapLAndR(result2_ni);
        // 第四层：第二轮:f1，输入 k1 与左右互换的结果 swap_result
        int[] result1_ni = SDES_Encrypt.roundFunction(k1, swap_result);
        // 第五层：最终置换
        int[] final_result = SDES_Encrypt.initialPermute(IP_inv, result1_ni);

        return final_result;
    }


    public static void main(String[] args) {
        String key = "0000011111"; // 输入的10位秘钥 key
        //处理8bit的密文数组类型
        //int[] ciphertext = {1, 1, 1, 0, 1, 1, 1, 1}; // 8位初始密文信息 ciphertext
        int[] ciphertext = {1, 1, 1, 0, 1, 0, 0, 0}; // 8位初始密文信息 ciphertext
        int[] plaintext = decrypt(ciphertext, key);
        System.out.println("数组类型8bits解密输出结果: " + Arrays.toString(plaintext));

        //处理String类型的秘文 ASCII 码输入
        String ciphertextASCII = "çl\u0017\u0002ËcyËÜé";
        String plaintextASCII = decryptASCII(ciphertextASCII, key);
        System.out.println("ASCII 码解密输出结果: " + plaintextASCII);
    }

}

