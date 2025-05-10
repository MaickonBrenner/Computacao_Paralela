package Trabalho_AV2.MergeSort;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final int[] Threads = {2, 4, 8, 10, 16};
    private static final int[] Tamanho_Problema = {10000, 50000, 100000, 200000, 500000};
    public static void main(String[] args) {
        MergeSortSerial mergeSerial = new MergeSortSerial();

        try (FileWriter arquivo = new FileWriter("Trabalho_AV2/Resultados/MergeSort_resultados.csv")) {
            arquivo.append("Tipo,Tamanho_Array,Threads,Tempo(ms),Tempo(ns)\n");

            // Serial
            for (int i = 0; i < 5; i++) {
                int tamanho = Tamanho_Problema[i];
                int[] arraySerial = gerarArray(tamanho);
                long serialTempoInicial = System.nanoTime();
                mergeSerial.mergeSort(arraySerial, arraySerial.length);
                long serialTempoFinal = System.nanoTime();
                long tempoSerial = calculaTempo(serialTempoInicial, serialTempoFinal);
    
                arquivo.append(String.format("Serial,%d,%d,%d,%d,\n", arraySerial.length, 1, tempoSerial/1000000, tempoSerial));
    
            }
            
            // Paralelo 
            for (int i = 0; i < 5; i++) {
                int tamanho = Tamanho_Problema[i];
                int[] arrayParalelo = gerarArray(tamanho);
                int numThreads = Threads[i];
                ForkJoinPool pool = new ForkJoinPool(numThreads);
                long paraleloTempoInicial = System.nanoTime();
                pool.invoke(new MergeSortParalelo(arrayParalelo));
                long paraleloTempoFinal = System.nanoTime();
                long tempoParalelo = calculaTempo(paraleloTempoInicial, paraleloTempoFinal);

                // System.out.println("Tamanho: " + tamanho + " | Número de Threads " + numThreads + " para o problema.");

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
