package Principal;

import java.io.*;
import java.util.*;

public class ProcessadorDados {
	private Map<String, List<Long>> temposExecucao = new HashMap<>();
	private Map<String, List<Long>> ocorrencias = new HashMap<>();
	
	public void executar(String arquivo) {
		File file = new File(arquivo);
		System.out.println("Arquivo encontrado? " + file.exists());
		System.out.println("Tamanho do arquivo: " + file.length() + " bytes");

	    try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
	        String linha;
	        
	        while ((linha = br.readLine()) != null) {
	            if (linha.trim().startsWith("Categoria")) continue;

	            String[] valores = linha.split(",");
	            
	            String categoria = valores[0];
	            Long ocorrencia = Long.parseLong(valores[1]);
	            Long tempo = Long.parseLong(valores[2]);

	            List<Long> listaOcorrencias = ocorrencias.get(categoria);
	            if (listaOcorrencias == null) {
	                listaOcorrencias = new ArrayList<>();
	                ocorrencias.put(categoria, listaOcorrencias);
	            }
	            listaOcorrencias.add(ocorrencia);

	            List<Long> listaTempos = temposExecucao.get(categoria);
	            if (listaTempos == null) {
	                listaTempos = new ArrayList<>();
	                temposExecucao.put(categoria, listaTempos);
	            }
	            listaTempos.add(tempo);
	        }
	    } catch (IOException | NumberFormatException e) {
	        System.err.println("Erro ao processar arquivo: " + e.getMessage());
	    }

	}
	
	public Map<String, List<Long>> getTempo() {
		return temposExecucao;
	}
	
	public Map<String, List<Long>> getOcorrencias() {
		return ocorrencias;
	}
}
