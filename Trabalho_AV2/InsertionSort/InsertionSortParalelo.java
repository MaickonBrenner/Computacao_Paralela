package Trabalho_AV2.InsertionSort;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InsertionSortParalelo {
    public void iniciarInsertionSort(int[] a) {
		int numThreads = 5;
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		insertionSort(a, executor);
        executor.shutdown();
	}
	
	public void insertionSort(int[] a, ExecutorService executor) {
		int n = a.length;
		
		Future<?>[] futures = new Future<?>[n];
		for (int i = 1; i < n; i++) {
			final int j = i;
			futures[i] = executor.submit(() -> {
				int key = a[j];
				int k = j - 1;
				
				while(k >= 0 && a[k] > key) {
					a[k + 1] = a[k];
					k--;
				}
				a[k + 1] = key;
			});
		}
		
		for (int i = 1; i < n; i++) {
			try {
				futures[i].get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
