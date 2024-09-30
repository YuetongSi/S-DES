package function;

import java.util.Arrays;
//生成子密钥
class KeyGeneration {
    // 初始置换函数
    private static int[] permutation(String input, int[] permutationTable) {
        int n=permutationTable.length;
        int[] output = new int[n];
        for (int i = 0; i < n; i++) {
            output[i] = input.charAt(permutationTable[i] - 1) - '0';
        }
        return output;
    }

    // 左循环移位函数
    private static int[] leftMove(int[] array) {
        int n=array.length;
        int[] result = new int[n];
        int temp = array[0];
        for (int i = 0; i < n - 1; i++) {
            result[i] = array[i + 1];
        }
        result[n - 1] = temp;
        return result;
    }
    //合并两个数组
    private static int[] merge(int[] a, int[] b) {
        int x=a.length , y=b.length;
        int[] result = new int[x + y];
        for (int i = 0; i < x; i++) {
            result[i] = a[i];
        }
        for (int i = 0; i < y; i++) {
            result[i + x] = b[i];
        }
        return result;
    }

    // 压缩置换函数
    private static int[] compressPermute(int[] input, int[] permutation) {
        int n= permutation.length;
        int[] output = new int[n];
        for (int i = 0; i < n; i++) {
            output[i] = input[permutation[i] - 1];
        }
        return output;
    }

    // 生成子秘钥的函数
    public static int[][] generateKey(int[] P10, int[] P8, String key) {
        int[][] keys = new int[2][]; // 创建一个包含两个秘钥的数组
        // 初始置换
        int[] initialPermuteOutput = permutation(key, P10);
        // 左右划分
        int[] initialPermuteOutputL = Arrays.copyOfRange(initialPermuteOutput, 0, 5);
        int[] initialPermuteOutputR = Arrays.copyOfRange(initialPermuteOutput, 5, 10);
        // 循环左移位，压缩置换，生成秘钥
        for (int round =0; round < 2; round++) {
            // 循环左移位
            initialPermuteOutputL = leftMove(initialPermuteOutputL);
            initialPermuteOutputR = leftMove(initialPermuteOutputR);
            // 合并左右两部分
            initialPermuteOutput = merge(initialPermuteOutputL, initialPermuteOutputR);
            // 压缩置换，生成秘钥k1或k2
            int[] k = compressPermute(initialPermuteOutput, P8);
            // 将k1和k2放入数组
            keys[round] = k;

        }
        return keys;
    }





}


