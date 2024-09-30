package function;

import java.util.Arrays;
//加密
public class SDES_Encrypt {

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

    //1.初始IP置换
    public static int[] initialPermute(int IP[],int plainText[]){
        int n=IP.length;
        int permuteText[] =new int[n];
        for(int i=0;i<n;i++){
            permuteText[i]=plainText[IP[i]-1];
        }
        return permuteText;
    }
    //异或运算
    private static int[] xor(int[] a, int[] b) {
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] ^ b[i];
        }
        return result;
    }

    // 按置换表进行置换
    private static int[] permute(int[] a, int[] inputBox) {
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = inputBox[a[i] - 1];
        }
        return result;
    }

    //2.轮函数
    // EP扩展置换
    private static int[] ExpansionPermute(int[] rightInput) {
        return permute(EP, rightInput);
    }

    //直接置换SPBox：调用permute函数
    private static int[] directPermutation(int[] input) {
        return permute(SP, input);
    }

    // 二进制转十进制
    private static int binaryToInt(int b1, int b0) {
        return b1 * 2 + b0;
    }

    // 整数转二进制数组
    private static int[] intToBinary(int n) {
        int[] binary = new int[2];
        binary[0] = n / 2;
        binary[1] = n % 2;
        return binary;
    }
    // S盒替换
    private static int[] SBoxSubstitute(int[] temp) {
        // 左右分开
        int[] l = Arrays.copyOfRange(temp, 0, 4);
        int[] r = Arrays.copyOfRange(temp, 4, 8);

        // 将组元素组合成行和列的索引
        int row1 = binaryToInt(l[0], l[3]);
        int row2 = binaryToInt(r[0], r[3]);
        int col1 = binaryToInt(l[1], l[2]);
        int col2 = binaryToInt(r[1], r[2]);

        // 存储S盒置换后的结果
        int[] F_te = new int[4];

        // 根据row1和col1从S1盒中获取置换后的值
        int[] S1Output = intToBinary(S1[row1][col1]);

        // 根据row2和col2从S2盒中获取置换后的值
        int[] S2Output = intToBinary(S2[row2][col2]);

        // 将S1和S2的输出值赋值给F_te数组
        F_te[0] = S1Output[0];
        F_te[1] = S1Output[1];
        F_te[2] = S2Output[0];
        F_te[3] = S2Output[1];

        return F_te;
    }

    // 轮函数F
    private static int[] roundF(int[] leftInput, int[] rightInput, int[] roundKey) {
        // 对右半部分进行扩展置换，得到8位
        int[] rightInputExpansion = ExpansionPermute(rightInput);
        // 与子密钥进行异或
        int[] xorResult = xor(rightInputExpansion, roundKey);
        //S盒压缩 4位
        int[] SBoxSubstitution = SBoxSubstitute(xorResult);
        // 直接置换 SPBox
        int[] SP_result = directPermutation(SBoxSubstitution);
        // 与左半部分按位异或
        int[] resultxor = xor(SP_result, leftInput);
        return resultxor;
    }
    static int[] roundFunction(int[] roundKey, int[] inputBlock) {
        // 将输入拆分为左右两部分
        int[] leftBlock = Arrays.copyOfRange(inputBlock, 0, 4);
        int[] rightBlock = Arrays.copyOfRange(inputBlock, 4, 8);

        // 执行函数 F 得到 resultLeft
        int[] resultLeft = roundF(leftBlock, rightBlock, roundKey);

        // 合并 resultLeft 和 rightBlock 得到最终结果
        int[] resultBlock = new int[8];
        System.arraycopy(resultLeft, 0, resultBlock, 0, 4);
        System.arraycopy(rightBlock, 0, resultBlock, 4, 4);

        return resultBlock;
    }

    // 3.左右互换
    public static int[] swapLAndR(int[] input) {

        int[] left = Arrays.copyOfRange(input, 0, 4);
        int[] right = Arrays.copyOfRange(input, 4, 8);

        int[] swaplandr = new int[8];
        System.arraycopy(right, 0, swaplandr, 0, 4);
        System.arraycopy(left, 0, swaplandr, 4, 4);

        return swaplandr;
    }

    // IP逆置换
    public static int[] finalPermute(int[] result, int[] IP_inv) {
        // 存储最终结果
        int[] finalResult = new int[8];
        // 遍历逆置换表
        for (int i = 0; i < 8; i++) {
            finalResult[i] = result[IP_inv[i] - 1];
        }
        return finalResult;
    }

    //输入10位秘钥和8位的初始数组信息，输出加密后8位的数组
    public static int[] encrypt(int[] plaintext, String key) {
        // 生成秘钥 k1 和 k2
        int keys[][] = KeyGeneration.generateKey(P10, P8, key);
        int k1[] = keys[0];
        int k2[] = keys[1];
        // 初始置换
        int inputBlock[] = initialPermute(IP, plaintext);
        // 第一轮加密:k1 ，初始置换的结果
        int result1[] = roundFunction(k1, inputBlock);
        // 交换加密结果的左右
        int swap_result[] = swapLAndR(result1);
        // 第二轮加密: k2 ，左右互换的结果
        int result2[] = roundFunction(k2, swap_result);
        // 逆置换
        int final_result[] = finalPermute(result2,IP_inv);
        return final_result;
    }


    public static void main(String[] args) {
        String key = "0000011111"; // Input 10-bit key
        //处理8bit的明文数组类型
        //int[] plaintext = {1, 0, 0, 1, 1, 0, 1, 0}; // 8位初始加密信息 plaintext
        int[] plainText = {0,0,1,0,1,0,1,0}; // 8位初始加密信息 plaintext
        int[] cipherText = encrypt(plainText, key);
        System.out.println("数组类型8bits加密输出结果: " + Arrays.toString(cipherText));
    }
    }

