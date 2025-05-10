package Trabalho_AV2.InsertionSort;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Main {
    private static final int[] Threads = {1, 2, 4, 8, 16};
    private static final int[] Tamanho_Problema = {10000, 50000, 100000, 200000, 500000};
    public static void main(String[] args) {
        InsertionSortSerial insertionSerial = new InsertionSortSerial();
        InsertionSortParalelo insertionParalelo = new InsertionSortParalelo();

        try (FileWriter arquivo = new FileWriter("Trabalho_AV2/Resultados/InsertionSort_resultados.csv")) {
            arquivo.append("Tipo, Tamanho_Array, Threads, Tempo(ms), Tempo(ns)\n");
            
            // Serial
            for (int i = 0; i < 5; i++) {
                int tamanho = Tamanho_Problema[i];
                int[] arraySerial = gerarArray(tamanho);
                long serialTempoInicial = System.nanoTime();
                insertionSerial.insertionSort(arraySerial);
                long serialTempoFinal = System.nanoTime();
                long tempoSerial = calculaTempo(serialTempoInicial, serialTempoFinal);

                arquivo.append(String.format("Serial,%d,%d,%d,%d,\n", arraySerial.length, 1, tempoSerial/1000000, tempoSerial));

            }
            
            // Paralelo
            for (int i = 0; i < 5; i++) {
                int tamanho = Tamanho_Problema[i];
                int[] arrayParalelo = gerarArray(tamanho);
                int numThreads = Threads[i];
                ExecutorService executor = Executors.newFixedThreadPool(numThreads);
                long paraleloTempoInicial = System.nanoTime();
                insertionParalelo.insertionSort(arrayParalelo, executor);
                long paraleloTempoFinal = System.nanoTime();
                long tempoParalelo = calculaTempo(paraleloTempoInicial, paraleloTempoFinal);

                arquivo.append(String.format("Paralelo,%d,%d,%d,%d,\n", tamanho, numThreads, tempoParalelo/1000000, tempoParalelo));
                
            }

            System.out.println("Arquivo CSV gerado na pasta Resultados!");

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

    private static long calculaTempo(long serialTempoInicial, long serialTempoFinal) {
        long valor = serialTempoFinal - serialTempoInicial;
        return valor;
    }
}
