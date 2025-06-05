package Principal;

import java.io.FileWriter;
import java.io.IOException;
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

            // Serial CPU
            for (int i = 0; i < 3; i++) {
            	long serialCPUTempoInicial = System.currentTimeMillis();
                int resultadoSerialCPU = serialCPU.buscarPalavra(livro, palavras[i]);
                long serialCPUTempoFinal = System.currentTimeMillis();
                long tempoSerial = calculaTempo(serialCPUTempoInicial, serialCPUTempoFinal);
                System.out.println("SerialCPU: " + resultadoSerialCPU + " ocorrências em " + tempoSerial + " ms - Palavra: " + palavras[i]);
                arquivo.append(String.format("SerialCPU,%d,%d\n", resultadoSerialCPU, tempoSerial));

            }
            
            // Paralelo CPU
            for (int i = 0; i < 3; i++) {
                long paraleloCPUTempoInicial = System.currentTimeMillis();
                int resultadoParaleloCPU = paraleloCPU.buscarPalavra(livro, palavras[i]);
                long paraleloCPUTempoFinal = System.currentTimeMillis();
                long tempoParalelo = calculaTempo(paraleloCPUTempoInicial, paraleloCPUTempoFinal);
                System.out.println("ParaleloCPU: " + resultadoParaleloCPU + " ocorrências em " + tempoParalelo + " ms - Palavra: " + palavras[i]);
                arquivo.append(String.format("ParaleloCPU,%d,%d\n", resultadoParaleloCPU, tempoParalelo));
            }

            // Paralelo GPU (OpenCL)
            for (int i = 0; i < 3; i++) {
            	long paraleloGPUTempoInicial = System.currentTimeMillis();
                int resultadoParaleloGPU = paraleloGPU.buscarPalavra(livro, palavras[i]);
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
 
}
