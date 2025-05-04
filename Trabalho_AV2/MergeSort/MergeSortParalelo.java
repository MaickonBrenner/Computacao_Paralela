package Trabalho_AV2.MergeSort;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MergeSortParalelo {
    public void iniciarMergeSort(int[] a) {
		int numThreads = 10;
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        mergeSort(a, executor);
        executor.shutdown();
	}
	
	private void mergeSort(int[] a, ExecutorService executor) {
		if (a.length < 2) {
			return;
		}
		
		int mid = a.length / 2;
		int[] left = Arrays.copyOfRange(a, 0, mid);
		int[] right = Arrays.copyOfRange(a, mid, a.length);
		
		Future<?> leftFuture = executor.submit(() -> mergeSort(left, executor));
		Future<?> rightFuture = executor.submit(() -> mergeSort(right, executor));
		
		try {
			leftFuture.get();
			rightFuture.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		merge(a, left, right);
	}
	
	private void merge(int[] a, int[] left, int[] right) {
		int i = 0, j = 0, k = 0;
		
		while (i < left.length && j < right.length) {
			a[k++] = (left[i] <= right[j]) ? left[i++] : right[j++]; 
		}
		
		while (i < left.length) a[k++] = left[i++];
        while (j < right.length) a[k++] = right[j++];
	}
}
