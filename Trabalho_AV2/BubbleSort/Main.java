package Trabalho_AV2.BubbleSort;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int[] Threads = {1, 2, 4, 8, 16};
    private static final int[] Tamanho_Problema = {10000, 50000, 100000, 200000, 500000};

    public static void main(String[] args) {
        BubbleSortSerial bubbleSerial = new BubbleSortSerial();
        BubbleSortParalelo bubbleParalelo = new BubbleSortParalelo();

        try (FileWriter arquivo = new FileWriter("Trabalho_AV2/Resultados/BubbleSort_resultados.csv")) {
            arquivo.append("Tipo,Tamanho_Array,Threads,Tempo(ms),Tempo(ns)\n");

            // Serial
            for (int i = 0; i < 5; i++) {
                int tamanho = Tamanho_Problema[i];
                int[] arraySerial = gerarArray(tamanho);
                long tempoInicialSerial = System.nanoTime();
                bubbleSerial.sort(arraySerial);
                long tempoFinalSerial = System.nanoTime();
                long tempoSerial = calculaTempo(tempoInicialSerial, tempoFinalSerial);
                arquivo.append(String.format("Serial,%d,%d,%d,%d,\n", arraySerial.length, 1, tempoSerial / 1_000_000, tempoSerial));
            }
            
            // Paralelo
            for (int i = 0; i < 5; i++) {
                int tamanho = Tamanho_Problema[i];
                int numThreads = Threads[i];
                int[] arrayParalelo = gerarArray(tamanho);
                ExecutorService executor = Executors.newFixedThreadPool(numThreads);

                long tempoInicialParalelo = System.nanoTime();
                bubbleParalelo.parallelBubbleSort(arrayParalelo, numThreads);
                long tempoFinalParalelo = System.nanoTime();
                executor.shutdown();

                long tempoParalelo = calculaTempo(tempoInicialParalelo, tempoFinalParalelo);
                arquivo.append(String.format("Paralelo,%d,%d,%d,%d,\n", tamanho, numThreads, tempoParalelo / 1_000_000, tempoParalelo));
            }

            System.out.println("Arquivo BubbleSort_resultados.csv gerado na pasta Resultados!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] gerarArray(int tamanho) {
        Random random = new Random();
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = random.nextInt(1000);
        }
        return array;
    }

    private static long calculaTempo(long inicio, long fim) {
        return fim - inicio;
    }
}
