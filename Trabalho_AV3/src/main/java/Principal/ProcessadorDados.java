package Principal;

import java.io.*;
import java.util.*;
import javafx.scene.chart.XYChart;

public class ProcessadorDados {
	public static Map<String, List<Long>> lerArquivo(String arquivo) {
		Map<String, List<Long>> temposExecucao = new HashMap<>();
        Map<String, List<Long>> ocorrencias = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
			String linha;
			while((linha = br.readLine()) != null) {
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
		return temposExecucao;
	}
	
	public static XYChart.Series<Number, Number> criarSerie(String nomeSerie, List<Long> ocorrencias, List<Long> tempos) {
		XYChart.Series<Number, Number> serie = new XYChart.Series<>();
		serie.setName(nomeSerie);
		
		for (int i = 0; i < tempos.size(); i++) {
			serie.getData().add(new XYChart.Data<>(ocorrencias.get(i), tempos.get(i)));
		}
		return serie;
	}
}
