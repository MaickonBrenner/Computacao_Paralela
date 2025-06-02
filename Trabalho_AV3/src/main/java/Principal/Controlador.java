package Principal;

import java.io.FileWriter;
import java.io.IOException;

public class Controlador {
    public void controle() {
        String livro = "Amostras/DonQuixote-388208.txt";
        String palavra = "Sancho";

        SerialCPU serialCPU = new SerialCPU();
        ParaleloCPU paraleloCPU = new ParaleloCPU();
        ParaleloGPU paraleloGPU = new ParaleloGPU();

        try (FileWriter arquivo = new FileWriter("Resultados/resultados.csv")) {
            arquivo.append("Categoria, Ocorrencias, Tempo(ms) \n");

            // Serial CPU
            long serialCPUTempoInicial = System.currentTimeMillis();
            int resultadoSerialCPU = serialCPU.buscarPalavra(livro, palavra);
            long serialCPUTempoFinal = System.currentTimeMillis();
            long tempoSerial = calculaTempo(serialCPUTempoInicial, serialCPUTempoFinal);
            System.out.println("SerialCPU: " + resultadoSerialCPU + " ocorrências em " + tempoSerial + " ms");
            arquivo.append(String.format("SerialCPU,%d,%d \n", resultadoSerialCPU, tempoSerial));

            // Paralelo CPU
            for (int i = 0; i < 3; i++) {
                long paraleloCPUTempoInicial = System.currentTimeMillis();
                int resultadoParaleloCPU = paraleloCPU.buscarPalavra(livro, palavra);
                long paraleloCPUTempoFinal = System.currentTimeMillis();
                long tempoParalelo = calculaTempo(paraleloCPUTempoInicial, paraleloCPUTempoFinal);
                System.out.println("ParaleloCPU: " + resultadoParaleloCPU + " ocorrências em " + tempoParalelo + " ms");
                arquivo.append(String.format("ParaleloCPU,%d,%d \n", resultadoParaleloCPU, tempoParalelo));
            }

            // Paralelo GPU (OpenCL)
            for (int i = 0; i < 3; i++) {
                
            }

            System.out.println("Arquivo CSV gerado na pasta Resultados!");
            
            processar();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long calculaTempo(long TempoInicial, long TempoFinal) {
        return TempoFinal - TempoInicial;
    }
    
    private void processar() {
    	ProcessadorDados processador = new ProcessadorDados();
    	
    	String arquivo = "Resultados/resultados.csv";
    	processador.executar(arquivo);
    	System.out.println("Dados processados! Chamando gráfico...");
    	Grafico.iniciarGrafico(processador.getDados(), processador.getOcorrencias());
    }
}
