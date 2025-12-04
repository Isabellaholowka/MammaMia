package crud_avaliacao;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Avaliacoes;

public class AvaliacaoCreateController {

    @FXML private TextField txtCidade;
    @FXML private TextField txtValorVendas;
    @FXML private TextField txtPratosPedidos;
    @FXML private TextField txtVendasTotais;
    @FXML private TextField txtDesempenho;

    private void limparEstiloErro() {
        limparBordaVermelha(txtCidade);
        limparBordaVermelha(txtValorVendas);
        limparBordaVermelha(txtPratosPedidos);
        limparBordaVermelha(txtVendasTotais);
        limparBordaVermelha(txtDesempenho);
    }

    private void colocarBordaVermelha(Control campo) {
        campo.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }

    private void limparBordaVermelha(Control campo) {
        campo.setStyle("");
    }

    private boolean validarCamposComVisual() {
        limparEstiloErro();
        boolean valido = true;

        if (txtCidade.getText() == null || txtCidade.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtCidade);
            valido = false;
        }

        if (txtValorVendas.getText() == null || txtValorVendas.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtValorVendas);
            valido = false;
        } else {
            try {
                Integer.parseInt(txtValorVendas.getText());
            } catch (NumberFormatException e) {
                colocarBordaVermelha(txtValorVendas);
                valido = false;
            }
        }

        if (txtPratosPedidos.getText() == null || txtPratosPedidos.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtPratosPedidos);
            valido = false;
        }

        if (txtVendasTotais.getText() == null || txtVendasTotais.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtVendasTotais);
            valido = false;
        } else {
            try {
                Integer.parseInt(txtVendasTotais.getText());
            } catch (NumberFormatException e) {
                colocarBordaVermelha(txtVendasTotais);
                valido = false;
            }
        }

        if (txtDesempenho.getText() == null || txtDesempenho.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtDesempenho);
            valido = false;
        } else {
            try {
                Integer.parseInt(txtDesempenho.getText());
            } catch (NumberFormatException e) {
                colocarBordaVermelha(txtDesempenho);
                valido = false;
            }
        }

        return valido;
    }

    @FXML
    private void salvarAvaliacao() {
        try {
            if (!validarCamposComVisual()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Campos Obrigatórios");
                alerta.setHeaderText("Preencha os campos obrigatórios destacados em vermelho.");
                alerta.setContentText("Os campos com borda vermelha são obrigatórios.");
                alerta.showAndWait();
                return;
            }

            // Coleta dados da tela
            String cidade = txtCidade.getText();
            int valorVendas = Integer.parseInt(txtValorVendas.getText());
            String pratosPedidos = txtPratosPedidos.getText();
            int vendasTotais = Integer.parseInt(txtVendasTotais.getText());
            int desempenho = Integer.parseInt(txtDesempenho.getText());

            // Cria objeto Avaliacoes
            Avaliacoes novaAvaliacao = new Avaliacoes(cidade, valorVendas, pratosPedidos, vendasTotais, desempenho);

            // Salva no banco
            new DAO<>(Avaliacoes.class).incluirTransacional(novaAvaliacao);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Avaliação");
            alerta.setHeaderText("Sucesso");
            alerta.setContentText("Avaliação salva com sucesso!");
            alerta.showAndWait();

            limparCampos();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("Falha ao salvar avaliação");
            alerta.setContentText("Erro: " + e.getMessage());
            alerta.showAndWait();
        }
    }

    @FXML
    private void limparCampos() {
        txtCidade.clear();
        txtValorVendas.clear();
        txtPratosPedidos.clear();
        txtVendasTotais.clear();
        txtDesempenho.clear();
    }
}