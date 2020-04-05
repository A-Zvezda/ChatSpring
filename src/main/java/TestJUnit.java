import java.util.Arrays;

public class TestJUnit {
    public static void main (String[] args) {
        int[] a =   {1, 2, 4, 4, 2, 3, 4, 1, 7};
        String aa = Arrays.toString(trimArray(a));
        System.out.println(aa);
    }

    public static boolean oneAndFourArray (int[] inputArray) {
        boolean oneFound = false;
        boolean fourFound = false;
        for (int o: inputArray) {
            if (!oneFound & o == 1) {
                oneFound = true;
            }
            if (!fourFound & o == 4) {
                fourFound = true;
            }
            if (o != 1 & o != 4) {
                oneFound = false;
                fourFound = false;
                break;
            }
        }
        return oneFound & fourFound;
    }

    public static int[] trimArray (int [] inputArray) {
        int lastPos = -1;
        int inputArrayLn = inputArray.length;
        boolean done = false;
        for (int i = 0; i < inputArrayLn; i++) {
            if (inputArray[i] == 4) {
                lastPos = i + 1;
            }
        }
        if (lastPos == -1) {
            throw new RuntimeException();
        }
        int newArrayLen = inputArrayLn - lastPos;
        int[] newArray = new int[newArrayLen];
        System.arraycopy(inputArray, lastPos, newArray, 0, newArrayLen);
        return newArray;
    }
}
