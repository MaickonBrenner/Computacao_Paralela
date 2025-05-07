package Trabalho_AV2.QuickSort;

public class QuickSortSerial {
    public static void sort(int[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, high, low);
            sort(arr, low, pivot - 1);
            sort(arr, pivot + 1, high);
        }
    }

    private static int partition(int[] arr, int high, int low) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
            }
        }
        int finalTemp = arr[high];
        arr[high] = arr[i + 1];
        arr[i + 1] = finalTemp;
        return i + 1;
    }
}