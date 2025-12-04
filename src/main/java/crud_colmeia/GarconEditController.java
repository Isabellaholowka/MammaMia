package crud_colmeia;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Garcon;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * 🚀 GarconEditController -----------------------------------------
 * Controller para edição de garçons no sistema MammaMia.
 * Carrega os dados existentes, permite edição e atualiza no banco via DAO.
 */
public class GarconEditController {

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

    // Campo adicional para armazenar o ID do garçon sendo editado
    private Garcon garconEditando;
    @SuppressWarnings("unused")
    private Long garconId;

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

    /** 
     * Método para carregar os dados do garçon a ser editado
     * @param garcon O garçon a ser editado
     */
    public void carregarDadosGarcon(Garcon garcon) {
        this.garconEditando = garcon;
        this.garconId = garcon.getId(); // Supondo que a classe Garcon tenha um ID
        
        // Preenche os campos com os dados existentes
        txtNumero.setText(garcon.getNumero());
        dateCadastro.setValue(garcon.getDataCadastro());
        txtEndereco.setText(garcon.getEndereco());
        comboSituacao.setValue(garcon.getSituacao());
        comboRG.setValue(garcon.getRg());
        spinnerNumeroCTPS.getValueFactory().setValue(garcon.getNumeroCTPS());
        txtObservacoes.setText(garcon.getObservacoes());
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

    /** Atualiza o cadastro do Garçon */
    @FXML
    private void atualizarGarcon() {
        try {
            if (!validarCamposComVisual()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Campos Obrigatórios");
                alerta.setHeaderText("Preencha os campos obrigatórios destacados em vermelho.");
                alerta.setContentText("Os campos com borda vermelha são obrigatórios.");
                alerta.showAndWait();
                return;
            }

            // Coleta dados atualizados da tela
            String numero = txtNumero.getText();
            LocalDate data = dateCadastro.getValue();
            String endereco = txtEndereco.getText();
            String situacao = comboSituacao.getValue();
            String rg = comboRG.getValue();
            int numeroCTPS = spinnerNumeroCTPS.getValue();
            String obs = txtObservacoes.getText();

            // Atualiza o objeto Garçon existente
            garconEditando.setNumero(numero);
            garconEditando.setDataCadastro(data);
            garconEditando.setEndereco(endereco);
            garconEditando.setSituacao(situacao);
            garconEditando.setRg(rg);
            garconEditando.setNumeroCTPS(numeroCTPS);
            garconEditando.setObservacoes(obs);

            // CORREÇÃO: Usar atualizarTransacional em vez de atualizar
            new DAO<>(Garcon.class).atualizarTransacional(garconEditando);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Garçon");
            alerta.setHeaderText("Sucesso");
            alerta.setContentText("Garçon atualizado com sucesso!");
            alerta.showAndWait();

            // Fecha a tela de edição (opcional - depende da sua navegação)
            // ((Stage) txtNumero.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("Falha ao atualizar Garçon");
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

    /** Cancela a edição e fecha a tela */
    @FXML
    private void cancelarEdicao() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Cancelamento");
        alerta.setHeaderText("Deseja cancelar a edição?");
        alerta.setContentText("As alterações não salvas serão perdidas.");
        
        if (alerta.showAndWait().get() == ButtonType.OK) {
            // Fecha a tela de forma segura
            if (txtNumero != null && txtNumero.getScene() != null) {
                Stage stage = (Stage) txtNumero.getScene().getWindow();
                stage.close();
            }
        }
    }
}