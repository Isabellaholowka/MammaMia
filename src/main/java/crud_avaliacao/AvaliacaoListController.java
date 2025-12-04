package crud_avaliacao;

import dao.DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import model.Avaliacoes;

import java.io.IOException;
import java.util.List;

public class AvaliacaoListController {

    @FXML private TableView<Avaliacoes> tableAvaliacoes;
    @FXML private TableColumn<Avaliacoes, Long> colId;
    @FXML private TableColumn<Avaliacoes, String> colCidade;
    @FXML private TableColumn<Avaliacoes, Integer> colValorVendas;
    @FXML private TableColumn<Avaliacoes, String> colPratosPedidos;
    @FXML private TableColumn<Avaliacoes, Integer> colVendasTotais;
    @FXML private TableColumn<Avaliacoes, Integer> colDesempenho;

    private final ObservableList<Avaliacoes> dados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));
        colCidade.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCidade()));
        colValorVendas.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getValorTotalDeVendas()));
        colPratosPedidos.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPratosMaisPedidos()));
        colVendasTotais.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getVendasTotais()));
        colDesempenho.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDesempenho()));

        carregarAvaliacoes();
    }

    private void carregarAvaliacoes() {
        List<Avaliacoes> lista = new DAO<>(Avaliacoes.class).obterTodos(100, 0);
        dados.setAll(lista);
        tableAvaliacoes.setItems(dados);
    }

    @FXML
    private void abrirCadastro() {
        try {
            Node tela = FXMLLoader.load(getClass().getResource("/telas/view/TelaAvaliacao/TelaCadastroAvaliacao.fxml"));
            StackPane painel = (StackPane) tableAvaliacoes.getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(tela);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void editarCadastro() {
        Avaliacoes selecionado = tableAvaliacoes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione uma avaliação para editar.").showAndWait();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "Função editar ainda não implementada.").showAndWait();
    }

    @FXML
    private void excluirCadastro() {
        Avaliacoes selecionado = tableAvaliacoes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione uma avaliação para excluir.").showAndWait();
            return;
        }
        new DAO<>(Avaliacoes.class).removerPorIdTransacional(selecionado.getId());
        carregarAvaliacoes();
    }
}