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
}
