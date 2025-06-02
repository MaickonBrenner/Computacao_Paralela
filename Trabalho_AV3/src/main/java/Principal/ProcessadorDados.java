package Principal;

import java.io.*;
import java.util.*;

public class ProcessadorDados {
	private Map<String, List<Long>> temposExecucao = new HashMap<>();
	private Map<String, List<Long>> ocorrencias = new HashMap<>();
	
	public void executar(String arquivo) {
		try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
			String linha;
			while ((linha = br.readLine()) != null) {
				String[] valores = linha.split(",");
				
				String categoria = valores[0];
				Long ocorrencia = Long.parseLong(valores[1]);
				Long tempoExecucao = Long.parseLong(valores[2]);
				
				ocorrencias.computeIfAbsent(categoria, k -> new ArrayList<>()).add(ocorrencia);
				temposExecucao.computeIfAbsent(categoria, k -> new ArrayList<>()).add(tempoExecucao);
			}
		} catch (IOException e) {
			System.err.println("Erro na leitura do arquivo." + e.getMessage());
		}
	}
	
	public Map<String, List<Long>> getDados() {
		return temposExecucao;
	}
	
	public Map<String, List<Long>> getOcorrencias() {
		return ocorrencias;
	}
}
