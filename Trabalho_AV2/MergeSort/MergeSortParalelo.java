package Trabalho_AV2.MergeSort;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class MergeSortParalelo extends RecursiveAction {
	private final int[] a;

	public MergeSortParalelo(int[] a) {
		this.a = a;
	}

	@Override
	protected void compute() { // mergeSort
		if (a.length < 2) {
			return;
		}
		
		int mid = a.length / 2;
		int[] left = Arrays.copyOfRange(a, 0, mid);
		int[] right = Arrays.copyOfRange(a, mid, a.length);

		MergeSortParalelo leftTask = new MergeSortParalelo(left);
		MergeSortParalelo rightTask = new MergeSortParalelo(right);
		
		invokeAll(leftTask, rightTask);
		
		merge(left, right);
	}
	
	private void merge(int[] left, int[] right) {
		int i = 0, j = 0, k = 0;
		
		while (i < left.length && j < right.length) {
			a[k++] = (left[i] <= right[j]) ? left[i++] : right[j++]; 
		}
		
		while (i < left.length) a[k++] = left[i++];
        while (j < right.length) a[k++] = right[j++];
	}
}
