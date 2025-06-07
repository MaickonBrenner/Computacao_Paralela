package Principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

import javax.swing.JOptionPane;

public class Controlador {
	
	private String livro = "";
	private String[] palavras = {"", "", ""};
	
    public void controle(int escolha) {
    	
    	switch (escolha) {
	    	case 1: 
	    		livro = "Amostras/DonQuixote-388208.txt";
	    		palavras = new String[]{"Rocinante", "Quijote", "Sancho"};
	    		break;
	    	case 2:
	    		livro = "Amostras/MobyDick-217452.txt";
	    		palavras = new String[]{"whale", "Ahab", "Moby"};
	    		break;
	    	case 3:
	    		livro = "Amostras/Dracula-165307.txt";
	    		palavras = new String[]{"Castle", "Transylvania", "Dracula"};
	    		break;
    	}
       
        SerialCPU serialCPU = new SerialCPU();
        ParaleloCPU paraleloCPU = new ParaleloCPU();
        ParaleloGPU paraleloGPU = new ParaleloGPU();

        try (FileWriter arquivo = new FileWriter("Resultados/resultados.csv")) {
            arquivo.append("Categoria,Ocorrencias,Tempo(ms)\n");
            
            List<String> livroFormatado = formatarArquivo(livro, 10);
            
//            Teste:
//            List<String> teste = List.of("Quijote Sancho Quijote", "Rocinante Quijote");
//            int total = new ParaleloGPU().buscarPalavra(teste, "Quijote");
//            System.out.println("Total: " + total);

            // Serial CPU
            for (int i = 0; i < 3; i++) {
            	long serialCPUTempoInicial = System.currentTimeMillis();
                int resultadoSerialCPU = serialCPU.buscarPalavra(livroFormatado, palavras[i]);
                long serialCPUTempoFinal = System.currentTimeMillis();
                long tempoSerial = calculaTempo(serialCPUTempoInicial, serialCPUTempoFinal);
                System.out.println("SerialCPU: " + resultadoSerialCPU + " ocorrências em " + tempoSerial + " ms - Palavra: " + palavras[i]);
                arquivo.append(String.format("SerialCPU,%d,%d\n", resultadoSerialCPU, tempoSerial));

            }
            
            // Paralelo CPU
            for (int i = 0; i < 3; i++) {
                long paraleloCPUTempoInicial = System.currentTimeMillis();
                int resultadoParaleloCPU = paraleloCPU.buscarPalavra(livroFormatado, palavras[i]);
                long paraleloCPUTempoFinal = System.currentTimeMillis();
                long tempoParalelo = calculaTempo(paraleloCPUTempoInicial, paraleloCPUTempoFinal);
                System.out.println("ParaleloCPU: " + resultadoParaleloCPU + " ocorrências em " + tempoParalelo + " ms - Palavra: " + palavras[i]);
                arquivo.append(String.format("ParaleloCPU,%d,%d\n", resultadoParaleloCPU, tempoParalelo));
            }

            // Paralelo GPU (OpenCL)
            for (int i = 0; i < 3; i++) {
            	long paraleloGPUTempoInicial = System.currentTimeMillis();
                int resultadoParaleloGPU = paraleloGPU.buscarPalavra(livroFormatado, palavras[i]);
                long paraleloGPUTempoFinal = System.currentTimeMillis();
                long tempoParalelo = calculaTempo(paraleloGPUTempoInicial, paraleloGPUTempoFinal);
                System.out.println("ParaleloGPU: " + resultadoParaleloGPU + " ocorrências em " + tempoParalelo + " ms - Palavra: " + palavras[i]);
                arquivo.append(String.format("ParaleloGPU,%d,%d\n", resultadoParaleloGPU, tempoParalelo));
            }
            
            arquivo.close();
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
    	JOptionPane.showMessageDialog(null, "Operação Concluída! Imprimindo Gráfico.");
    	Grafico.iniciarGrafico(processador.getOcorrencias(), processador.getTempo());
    }
    
    public static List<String> formatarArquivo(String arquivo, int palavrasPorLinha) {
        List<String> linhasFormatadas = new ArrayList<>();
        StringBuilder linhaAtual = new StringBuilder();
        int contadorPalavras = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo, StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] palavras = linha.split("\\s+"); // Divide por espaços
                for (String palavra : palavras) {
                    linhaAtual.append(palavra).append(" ");
                    contadorPalavras++;

                    if (contadorPalavras == palavrasPorLinha) {
                        linhasFormatadas.add(linhaAtual.toString().trim());
                        linhaAtual.setLength(0); // Limpa para a próxima linha
                        contadorPalavras = 0;
                    }
                }
            }
            // Adiciona a última linha, caso tenha sobrado palavras
            if (linhaAtual.length() > 0) {
                linhasFormatadas.add(linhaAtual.toString().trim());
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return linhasFormatadas;
    }
    
    
//    public static List<String> formatarArquivo(String arquivo) {
//        List<String> linhas = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(arquivo, StandardCharsets.UTF_8))) {
//            String linha;
//            while ((linha = br.readLine()) != null) {
//                // Remove pontuação, converte para minúsculas e adiciona à lista
//                linha = linha.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
//                linhas.add(linha);
//            }
//        } catch (IOException e) {
//            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
//        }
//        return linhas;
//    }


}
    
 

