package Trabalho_AV2.MergeSort;

public class MergeSortSerial {
    public void mergeSort(int[] a, int n) {
		if (n < 2) {
			return;
		}
		
		int mid = n / 2;
		int[] left = new int[mid];
		int[] right = new int[n - mid];
		
		for (int i = 0; i < mid; i++) {
			left[i] = a[i];
		}
		
		for (int i = mid; i < n; i++) {
			right[i - mid] = a[i];
		}
		
		mergeSort(left, mid);
		mergeSort(right, n - mid);
		merge(a, left, right, mid, n - mid);
	}
	
	public void merge(int[] a, int[] left, int[] right, int leftSize, int rightSize) {
		int i = 0, j = 0, k = 0;
		while(i < leftSize && j < rightSize) {
			if (left[i] <= right[j]) {
				a[k++] = left[i++];
			} else {
				a[k++] = right[j++];
			}
		}
		
		while(i < leftSize) {
			a[k++] = left[i++];
		}
		
		while(j < rightSize) {
			a[k++] = right[j++];
		}
	}
    
}