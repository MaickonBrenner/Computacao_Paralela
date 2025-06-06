package Principal;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

public class ParaleloCPU {
    private final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public int buscarPalavra(List<String> linhas, String palavra) {
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
