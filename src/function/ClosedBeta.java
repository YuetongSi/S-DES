package function;
//5.封闭测试
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClosedBeta {
    public static void main(String[] args) {
        // 存储找到的密钥
        List<String> foundKeys = new ArrayList<>();

        //key=1111111000
        int[] plaintext = {1,1,1,1,1,1,1,1};
        int[] ciphertext = {0,1,0,1,1, 1, 1, 1};

        for (int i = 0; i < 1024; i++) {
            // 将i转换为10位二进制形式作为密钥
            String binaryKey = Integer.toBinaryString(i);
            while (binaryKey.length() < 10) {
                // 确保密钥长度为10位
                binaryKey = "0" + binaryKey;
            }
            // 解密
            int[] decrypted = SDES_Decrypt.decrypt(ciphertext, binaryKey);
            // 比较是否一致
            if (Arrays.equals(plaintext, decrypted)) {
                foundKeys.add(binaryKey); // 将找到的密钥添加到列表中
            }
        }

        if (!foundKeys.isEmpty()) {
            System.out.println("Possible Keys are:");
            for (String key : foundKeys) {
                System.out.println("Key=" + key);
            }

        } else {
            System.out.println("未找到密钥！");
        }
    }


}
