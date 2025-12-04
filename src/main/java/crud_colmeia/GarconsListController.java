package crud_colmeia;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import dao.DAO;
import model.Garcon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GarconsListController {

    @FXML
    private TableView<Garcon> tableGarcons;
    @FXML
    private TableColumn<Garcon, Long> colId;
    @FXML
    private TableColumn<Garcon, String> colIdentificacao;
    
    // Área de detalhes
    @FXML
    private VBox boxDetalhes;
    @FXML
    private Label lblDetalheEndereco;
    @FXML
    private Label lblDetalheRG;
    @FXML
    private Label lblDetalheSituacao;
    @FXML
    private Label lblDetalheDataCadastro;
    @FXML
    private Label lblDetalheCTPS;
    @FXML
    private Label lblDetalheObservacoes;
    
    // Campo de pesquisa
    @FXML
    private TextField txtPesquisa;

    private ObservableList<Garcon> todosGarcons;
    private ObservableList<Garcon> garconsFiltrados;

    @FXML
    public void initialize() {
        // CONFIGURAÇÃO DAS COLUNAS DA TABELA (APENAS ID E NOME)
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdentificacao.setCellValueFactory(new PropertyValueFactory<>("identificacao"));
        
        // CONFIGURAR EVENTO DE SELEÇÃO PARA MOSTRAR DETALHES
        configurarEventoSelecao();
        
        // CONFIGURAR EVENTO DE PESQUISA EM TEMPO REAL
        configurarPesquisa();
        
        // CARREGAR DADOS DO BANCO
        carregarDadosTabela();
        
        // INICIAR COM DETALHES OCULTOS
        boxDetalhes.setVisible(false);
    }

    private void configurarEventoSelecao() {
        // Evento quando seleciona uma linha (clique)
        tableGarcons.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalhesGarcon(newSelection);
            } else {
                // Se desselecionar, oculta os detalhes
                boxDetalhes.setVisible(false);
            }
        });
    }

    private void configurarPesquisa() {
        // Pesquisa em tempo real enquanto digita
        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            pesquisarGarcon();
        });
    }

    @FXML
    private void pesquisarGarcon() {
        String termoPesquisa = txtPesquisa.getText().toLowerCase().trim();
        
        if (termoPesquisa.isEmpty()) {
            // Se campo vazio, mostra todos os garçons
            tableGarcons.setItems(todosGarcons);
        } else {
            // Filtra os garçons
            List<Garcon> filtrados = todosGarcons.stream()
                .filter(garcon -> 
                    String.valueOf(garcon.getId()).toLowerCase().contains(termoPesquisa) ||
                    (garcon.getIdentificacao() != null && 
                     garcon.getIdentificacao().toLowerCase().contains(termoPesquisa))
                )
                .collect(Collectors.toList());
            
            garconsFiltrados = FXCollections.observableArrayList(filtrados);
            tableGarcons.setItems(garconsFiltrados);
        }
        
        // Limpa seleção após pesquisa
        tableGarcons.getSelectionModel().clearSelection();
        boxDetalhes.setVisible(false);
    }

    private void mostrarDetalhesGarcon(Garcon garcon) {
        if (garcon != null) {
            lblDetalheEndereco.setText(garcon.getEndereco() != null ? garcon.getEndereco() : "Não informado");
            lblDetalheRG.setText(garcon.getRg() != null ? garcon.getRg() : "Não informado");
            lblDetalheSituacao.setText(garcon.getStatus() != null ? garcon.getStatus() : "Não informado");
            lblDetalheDataCadastro.setText(garcon.getDateCadastro() != null ? garcon.getDateCadastro().toString() : "Não informado");
            lblDetalheCTPS.setText(String.valueOf(garcon.getNumeroCTPS()));
            lblDetalheObservacoes.setText(garcon.getObservacoes() != null ? garcon.getObservacoes() : "Nenhuma observação");
            
            boxDetalhes.setVisible(true);
        }
    }

    // MÉTODO PARA CARREGAR OS DADOS DO BANCO NA TABELA
    private void carregarDadosTabela() {
        try {
            // Busca todos os garçons do banco
            List<Garcon> garcons = new DAO<>(Garcon.class).obterTodos(100, 0);
            
            // Converte a lista para ObservableList e define na tabela
            todosGarcons = FXCollections.observableArrayList(garcons);
            tableGarcons.setItems(todosGarcons);
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar dados");
            alert.setContentText("Não foi possível carregar os garçons do banco: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // MÉTODO para abrir tela de edição
    private void abrirTelaEdicao(Garcon garcon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/view/TelaEditGarcons.fxml"));
            Node telaEdicao = loader.load();
            
            GarconEditController controller = loader.getController();
            controller.carregarDadosGarcon(garcon);
            
            // Obtém o StackPane principal do MainLayout
            StackPane painelConteudo = (StackPane) tableGarcons.getScene().lookup("#painelConteudo");
            painelConteudo.getChildren().setAll(telaEdicao);
            
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de edição");
            alert.setContentText("Erro: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // MÉTODO para abrir tela de cadastro
    @FXML
    private void abrirCadastro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/view/TelaCadastroGarcons.fxml"));
            Node telaCadastro = loader.load();
            
            // Obtém o StackPane principal do MainLayout
            StackPane painelConteudo = (StackPane) tableGarcons.getScene().lookup("#painelConteudo");
            painelConteudo.getChildren().setAll(telaCadastro);
            
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de cadastro");
            alert.setContentText("Erro: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void editarCadastro() {
        Garcon garconSelecionado = tableGarcons.getSelectionModel().getSelectedItem();
        
        if (garconSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum garçon selecionado");
            alert.setContentText("Por favor, selecione um garçon para editar.");
            alert.showAndWait();
            return;
        }
        
        // Chama o método para abrir a edição
        abrirTelaEdicao(garconSelecionado);
    }

    @FXML
    private void excluirCadastro() {
        Garcon garconSelecionado = tableGarcons.getSelectionModel().getSelectedItem();
        
        if (garconSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum garçon selecionado");
            alert.setContentText("Por favor, selecione um garçon para excluir.");
            alert.showAndWait();
            return;
        }
        
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Excluir garçon: " + garconSelecionado.getIdentificacao());
        confirmacao.setContentText("Tem certeza que deseja excluir este garçon?");
        
        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                new DAO<>(Garcon.class).removerPorIdTransacional(garconSelecionado.getId());
                carregarDadosTabela(); // Atualiza a tabela
                boxDetalhes.setVisible(false); // Oculta detalhes após exclusão
                
                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Sucesso");
                sucesso.setHeaderText("Garçon excluído com sucesso!");
                sucesso.showAndWait();
                
            } catch (Exception e) {
                e.printStackTrace();
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro");
                erro.setHeaderText("Não foi possível excluir o garçon");
                erro.setContentText("Erro: " + e.getMessage());
                erro.showAndWait();
            }
        }
    }
}