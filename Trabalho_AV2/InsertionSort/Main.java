package Trabalho_AV2.InsertionSort;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class InnerMain {
    private static final int[] Threads = {1, 2, 4, 8, 16};
    private static final int[] Tamanho_Problema = {1000, 5000, 10000, 20000, 50000};
    public static void main(String[] args) {
        InsertionSortSerial insertionSerial = new InsertionSortSerial();
        InsertionSortParalelo insertionParalelo = new InsertionSortParalelo();

        try (FileWriter arquivo = new FileWriter("insertion_resultados.csv")) {
            arquivo.append("Tipo, Tamanho_Array, Threads, Tempo(ms), Tempo(ns)\n");
            
            // Serial
            int[] arraySerial = gerarArray(1000);
            long serialTempoInicial = System.nanoTime();
            insertionSerial.insertionSort(arraySerial);
            long serialTempoFinal = System.nanoTime();
            long tempo = calculaTempo(serialTempoInicial, serialTempoFinal);

            arquivo.append(String.format("Serial,%d,%d,%d,%d,\n", arraySerial.length, 1, tempo/1000000, tempo));

            // Paralelo 

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
