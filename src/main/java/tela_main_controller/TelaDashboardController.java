package tela_main_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaDashboardController implements Initializable {

    @FXML
    private PieChart pieChartTipoPedidos;

    @FXML
    private BarChart<String, Number> barChartVendasSemana;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarGraficos();
    }

    private void inicializarGraficos() {
        // Gráfico de Pizza - Distribuição de pedidos
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Mesa (45%)", 45),
            new PieChart.Data("Delivery (35%)", 35),
            new PieChart.Data("Retirada (20%)", 20)
        );
        pieChartTipoPedidos.setData(pieChartData);
        pieChartTipoPedidos.setTitle("");

        // Gráfico de Barras - Vendas por dia
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Vendas Diárias");
        
        series.getData().add(new XYChart.Data<>("Seg", 1200));
        series.getData().add(new XYChart.Data<>("Ter", 1800));
        series.getData().add(new XYChart.Data<>("Qua", 1500));
        series.getData().add(new XYChart.Data<>("Qui", 2200));
        series.getData().add(new XYChart.Data<>("Sex", 2800));
        series.getData().add(new XYChart.Data<>("Sáb", 3200));
        series.getData().add(new XYChart.Data<>("Dom", 2900));
        
        barChartVendasSemana.getData().clear();
        barChartVendasSemana.getData().add(series);
        barChartVendasSemana.setLegendVisible(false);
    }
}