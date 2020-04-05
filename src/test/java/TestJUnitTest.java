
import org.junit.Assert;
import org.junit.Test;



import java.util.HashMap;

public class TestJUnitTest extends TestJUnit {


    @Test
    public void testOneAndFourArray() {
        HashMap<int[], Boolean> map = new HashMap<>();
        map.put(new int[] {1, 1, 1, 4, 4, 1, 4, 4}, true);
        map.put(new int[] {1, 1, 1, 1, 1, 1}, false);
        map.put(new int[] {4, 4, 4, 4}, false);
        map.put(new int[] {4, 4, 4, 4}, false);
        map.put(new int[] {1, 4, 4, 1, 1, 4, 3}, false);
        for(HashMap.Entry<int[], Boolean> entry : map.entrySet()) {
            Assert.assertEquals(entry.getValue(),oneAndFourArray(entry.getKey()));

        }
    }

    @Test
    public void testTrimArray() {
        HashMap<int[], int[]> map = new HashMap<>();
        map.put(new int[] {1, 2, 4, 4, 2, 3, 4, 1, 7}, new int[] {1, 7});
        map.put(new int[] {1, 2, 4, 4, 4, 3, 1, 1, 7}, new int[] {3, 1, 1, 7});
        for(HashMap.Entry<int[], int[]> entry : map.entrySet()) {
           Assert.assertArrayEquals(entry.getValue(),trimArray(entry.getKey()));
        }
    }

    @Test(expected = RuntimeException.class)
    public void testTrimArrayException() {
        trimArray(new int[] {1, 2, 2, 2, 2, 3, 1, 1, 7});
    }
}