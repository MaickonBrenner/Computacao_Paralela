package Trabalho_AV3.Principal;

import java.io.FileWriter;
import java.io.IOException;

public class Contralador {
    public void controle() {
        String livro = "Trabalho_AV3/Amostras/DonQuixote-388208.txt";
        String palavra = "Sancho";
        SerialCPU serialCPU = new SerialCPU();

        try (FileWriter arquivo = new FileWriter("Trabalho_AV3/Resultados/resultados.csv")) {
            arquivo.append("Tipo, Tamanho, Tempo(ms)");

            // Serial CPU
            long serialCPUTempoInicial = System.currentTimeMillis();
            int retorno = serialCPU.buscarPalavra(livro, palavra);
            long serialCPUTempoFinal = System.currentTimeMillis();
            long tempoSerial = calculaTempo(serialCPUTempoInicial, serialCPUTempoFinal);
            System.out.println("SerialCPU: " + retorno + " ocorrências em " + tempoSerial + " ms");
            arquivo.append(String.format("SerialCPU,%d,%d,\n", "Dom", tempoSerial));

            // Paralelo CPU
            for (int i = 0; i < 5; i++) {
                
            }

            // Paralelo GPU (OpenCL)
            for (int i = 0; i < 5; i++) {
                
            }
        
            System.out.println("Arquivo CSV gerado na pasta Resultados!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long calculaTempo(long TempoInicial, long TempoFinal) {
        long valor = TempoFinal - TempoInicial;
        return valor;
    }
}
