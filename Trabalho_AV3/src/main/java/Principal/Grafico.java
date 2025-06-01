package Principal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;
import java.util.*;

public class Grafico extends Application {
	@Override
    public void start(Stage stage) {
        stage.setTitle("Desempenho x Ocorrência de Palavra");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Ocorrências da Palavra");
        yAxis.setLabel("Tempo de Execução (ms)");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        Map<String, List<Long>> tempos = ProcessadorDados.lerArquivo("Resultados/resultados.csv");
        Map<String, List<Long>> ocorrencias = ProcessadorDados.lerArquivo("Resultados/resultados.csv");

        for (String categoria : tempos.keySet()) {
            lineChart.getData().add(ProcessadorDados.criarSerie(
                categoria, ocorrencias.get(categoria), tempos.get(categoria)
            ));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
