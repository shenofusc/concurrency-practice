package example.pattern.sort.oddevensort;

/**
 * Created by shy on 7/12/20.
 */
public class OddEvenSort {
    private static int[] arr = {2,0,2,1,1,0};

    public static void oddEvenSort(int[] arr) {
        int start = 0;
        boolean changed = true;
        while (changed || start == 1) {
            changed = false;
            for (int i = start; i < arr.length - 1; i+=2) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i+1];
                    arr[i+1]=temp;
                    changed = true;
                }
            }
            if (start == 0) {
                start = 1;
            } else {
                start = 0;
            }
        }
    }

    public static void main(String[] args) {
        long start;
        for (int n : arr) {
            System.out.print(n + " ");
        }
        System.out.println();
        start = System.currentTimeMillis();
        oddEvenSort(arr);
        System.out.println("sort cost " + (System.currentTimeMillis() - start) + "ms");
        for (int n : arr) {
            System.out.print(n + " ");
        }
    }
}
