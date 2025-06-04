package Principal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.*;

public class Grafico extends Application {
	private static Map<String, List<Long>> tempos;
    private static Map<String, List<Long>> ocorrencias;

    public static void iniciarGrafico(Map<String, List<Long>> ocorrenciasPalavra, Map<String, List<Long>> temposExecucao) {
        ocorrencias = ocorrenciasPalavra;
        tempos = temposExecucao;
        System.setProperty("prism.order", "sw"); 
        Application.launch(Grafico.class);
    }
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("Desempenho x Ocorrência de Palavra");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis(); // 950, 1000, 10
        xAxis.setLabel("Tempo de Execução (ms)");
        yAxis.setLabel("Ocorrências da Palavra");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        for (String categoria : tempos.keySet()) {
            XYChart.Series<Number, Number> serie = new XYChart.Series<>();
            serie.setName(categoria);

            List<Long> ocorrenciasPalavra = ocorrencias.get(categoria);
            List<Long> temposExecucao = tempos.get(categoria);

            for (int i = 0; i < temposExecucao.size(); i++) {
                serie.getData().add(new XYChart.Data<>(temposExecucao.get(i), ocorrenciasPalavra.get(i)));
            }

            lineChart.getData().add(serie);
        }
        
        Scene scene = new Scene(lineChart, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
