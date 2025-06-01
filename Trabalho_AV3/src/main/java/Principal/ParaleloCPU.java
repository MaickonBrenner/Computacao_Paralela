package Principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParaleloCPU {
    private final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public int buscarPalavra(String arquivo, String palavra) {
        List<String> linhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return 0;
        }

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<Integer>> resultados = new ArrayList<>();

        int chunkSize = linhas.size() / NUM_THREADS;
        for (int i = 0; i < NUM_THREADS; i++) {
            int start = i * chunkSize;
            int end = (i == NUM_THREADS - 1) ? linhas.size() : start + chunkSize;

            List<String> subLista = linhas.subList(start, end);
            Callable<Integer> tarefa = () -> {
                int contador = 0;
                for (String linha : subLista) {
                    String[] palavras = linha.split("\\s+");
                    for (String p : palavras) {
                        if (p.equalsIgnoreCase(palavra)) {
                            contador++;
                        }
                    }
                }
                return contador;
            };

            resultados.add(executor.submit(tarefa));
        }
        int total = 0;
        for (Future<Integer> futuro : resultados) {
            try {
                total += futuro.get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Erro ao executar tarefa paralela: " + e.getMessage());
            }
        }

        executor.shutdown();
        return total;
    }
}
