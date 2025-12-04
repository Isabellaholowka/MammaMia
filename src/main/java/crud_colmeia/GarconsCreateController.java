package crud_colmeia;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Garcon;

import java.time.LocalDate;

/**
 * 🚀 GarconsCreateController -----------------------------------------
 * Controller para cadastro de garçons no sistema MammaMia.
 * Coleta os dados do usuário, valida e salva no banco via DAO.
 */
public class GarconsCreateController {

    @FXML
    private TextField txtNumero; // Nome do Garçon

    @FXML
    private DatePicker dateCadastro; // Data do cadastro

    @FXML
    private TextField txtEndereco; // Endereço

    @FXML
    private ComboBox<String> comboSituacao; // Situação (Ativo/Inativo)

    @FXML
    private ComboBox<String> comboRG; // RG do Garçon

    @FXML
    private Spinner<Integer> spinnerNumeroCTPS; // Número da Carteira de Trabalho

    @FXML
    private TextArea txtObservacoes; // Observações

    /**
     * Inicializa os campos da tela (ComboBox e Spinner)
     */
    @FXML
    public void initialize() {
        // Situação
        comboSituacao.getItems().addAll("Ativo", "Inativo", "Em férias", "Desligado");

        // RG (opções fictícias, podem ser alteradas)
        comboRG.getItems().addAll("123456789", "987654321", "456789123");

        // Spinner de CTPS: 0 a 20, valor inicial 10
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 10);
        spinnerNumeroCTPS.setValueFactory(valueFactory);
    }

    /** Remove estilo de erro de todos os campos */
    private void limparEstiloErro() {
        limparBordaVermelha(txtNumero);
        limparBordaVermelha(txtEndereco);
        limparBordaVermelha(comboRG);
        limparBordaVermelha(comboSituacao);
        limparBordaVermelha(dateCadastro);
    }

    private void colocarBordaVermelha(Control campo) {
        campo.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }

    private void limparBordaVermelha(Control campo) {
        campo.setStyle("");
    }

    /** Valida campos obrigatórios e aplica destaque visual se estiverem vazios */
    private boolean validarCamposComVisual() {
        limparEstiloErro();
        boolean valido = true;

        if (txtNumero.getText() == null || txtNumero.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtNumero);
            valido = false;
        }

        if (txtEndereco.getText() == null || txtEndereco.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtEndereco);
            valido = false;
        }

        if (comboRG.getValue() == null || comboRG.getValue().trim().isEmpty()) {
            colocarBordaVermelha(comboRG);
            valido = false;
        }

        if (comboSituacao.getValue() == null || comboSituacao.getValue().trim().isEmpty()) {
            colocarBordaVermelha(comboSituacao);
            valido = false;
        }

        if (dateCadastro.getValue() == null) {
            colocarBordaVermelha(dateCadastro);
            valido = false;
        }

        return valido;
    }

    /** Salva o cadastro do Garçon */
    @FXML
    private void salvarColmeia() {
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
            String numero = txtNumero.getText();
            LocalDate data = dateCadastro.getValue();
            String endereco = txtEndereco.getText();
            String situacao = comboSituacao.getValue();
            String rg = comboRG.getValue();
            int numeroCTPS = spinnerNumeroCTPS.getValue();
            String obs = txtObservacoes.getText();

            // Cria objeto Garçon
            Garcon novoGarcon = new Garcon(numero, endereco, rg, situacao, data, numeroCTPS, obs);

            // Salva no banco
            new DAO<>(Garcon.class).incluirTransacional(novoGarcon);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Garçon");
            alerta.setHeaderText("Sucesso");
            alerta.setContentText("Garçon salvo com sucesso!");
            alerta.showAndWait();

            limparCampos();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("Falha ao salvar Garçon");
            alerta.setContentText("Erro: " + e.getMessage());
            alerta.showAndWait();
        }
    }

    /** Limpa todos os campos da tela */
    @FXML
    private void limparCampos() {
        txtNumero.clear();
        dateCadastro.setValue(null);
        txtEndereco.clear();
        comboSituacao.setValue(null);
        comboRG.setValue(null);
        spinnerNumeroCTPS.getValueFactory().setValue(10);
        txtObservacoes.clear();
    }
}