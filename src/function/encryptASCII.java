package function;
// ASCII加密
public class encryptASCII {
    public static String EncryptASCIIfunction(String PlainTextASCII, String key) {
        int n=PlainTextASCII.length();
        // 存储每个字符的8位ASCII码
        int[][] PlainTextArray = new int[n][8];
        // 将每个字符转换为8位二进制数组
        for (int i = 0; i < n; i++) {
            char character = PlainTextASCII.charAt(i);
            for (int j = 7; j >= 0; j--) {
                PlainTextArray[i][j] = (character >> (7 - j)) & 1; // 从最高位到最低位
            }
        }

        // 存储每个字符的加密结果
        int[][] cipherTextArray = new int[n][8];

        // 对每个字符的8位二进制数组进行加密
        for (int i = 0; i < n; i++) {
            cipherTextArray[i] = SDES_Encrypt.encrypt(PlainTextArray[i], key);
        }

        // 加密后的字符串
        StringBuilder cipherTextASCII = new StringBuilder();

        // 将加密后的二进制数组转换回字符
        for (int[] value : cipherTextArray) {
            char character = 0;
            for (int j = 0; j < 8; j++) {
                character |= (value[j] << (7 - j)); // 从最低位到最高位
            }
            cipherTextASCII.append(character); // 将加密后的字符添加到构建器
        }

        return cipherTextASCII.toString(); // 返回加密后的字符串
    }

    public static void main(String[] args) {
        String key = "0000011111";
        //String类型的 ASCII 码
        String PlainTextASCII = "youarerigh";
        String CipherTextASCII = EncryptASCIIfunction(PlainTextASCII, key);
        System.out.println("ASCII 码加密输出结果: " + CipherTextASCII);
    }

}
