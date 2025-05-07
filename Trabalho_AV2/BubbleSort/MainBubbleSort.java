package Trabalho_AV2.BubbleSort;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainBubbleSort {

    private static final int[] THREADS = {1, 2, 4, 8};
    private static final int[] SIZES = {1000, 5000, 10000, 20000, 50000};
    private static final int REPETICOES = 5;

    public static void main(String[] args) {
        try (FileWriter arquivo = new FileWriter("resultados_bubble.csv")) {
            arquivo.write("Tipo,Tamanho,Threads,Execucao,Tempo(ms),Tempo(ns)\n");

            for (int tamanho : SIZES) {
                for (int execucao = 1; execucao <= REPETICOES; execucao++) {
                    int[] original = gerarArray(tamanho);

                    // Serial
                    int[] serialArray = original.clone();
                    long inicio = System.nanoTime();
                    BubbleSortSerial.sort(serialArray);
                    long fim = System.nanoTime();
                    registrar(arquivo, "Serial", tamanho, 1, execucao, fim - inicio);

                    // Paralelo
                    for (int numThreads : THREADS) {
                        int[] paraleloArray = original.clone();
                        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
                        long inicioPar = System.nanoTime();
                        BubbleSortParalelo.parallelBubbleSort(paraleloArray, numThreads);
                        long fimPar = System.nanoTime();
                        executor.shutdown();
                        registrar(arquivo, "Paralelo", tamanho, numThreads, execucao, fimPar - inicioPar);
                    }
                }
            }

            System.out.println("Arquivo resultados_bubble.csv gerado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] gerarArray(int tamanho) {
        Random random = new Random();
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = random.nextInt(10000);
        }
        return array;
    }

    private static void registrar(FileWriter arquivo, String tipo, int tamanho, int threads, int execucao, long tempoNs) throws IOException {
        long tempoMs = tempoNs / 1_000_000;
        arquivo.write(String.format("%s,%d,%d,%d,%d,%d\n", tipo, tamanho, threads, execucao, tempoMs, tempoNs));
    }
}
