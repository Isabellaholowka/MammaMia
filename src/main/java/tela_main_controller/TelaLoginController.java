package tela_main_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 🚀 TelaLoginController
 * Controller para o modal de login do sistema MammaMia
 */
public class TelaLoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private CheckBox checkLembrar;
    @FXML private Button btnLogin;
    @FXML private Label lblMensagem;
    @FXML private AnchorPane rootPane;

    @FXML
    public void initialize() {
        // CORREÇÃO: Use event -> ao invés de _
        txtPassword.setOnAction(event -> fazerLogin());
        
        // CORREÇÃO: Corrija os parâmetros duplicados
        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> limparMensagem());
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> limparMensagem());
    }

    @FXML
    private void fazerLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarMensagem("Preencha todos os campos!", "erro");
            return;
        }

        if (validarCredenciais(username, password)) {
            mostrarMensagem("Login realizado com sucesso!", "sucesso");
            abrirMainLayout(); // CORREÇÃO: Agora abre o sistema principal
        } else {
            mostrarMensagem("Usuário ou senha incorretos!", "erro");
        }
    }

    /**
     * 🚀 MÉTODO CORRIGIDO - Abre o sistema principal
     */
    private void abrirMainLayout() {
        try {
            // 🔥 CORREÇÃO: Usa o stage atual de forma segura
            Stage loginStage = (Stage) btnLogin.getScene().getWindow();
            
            // Carrega o MainLayout
            Parent root = FXMLLoader.load(getClass().getResource("/telas/view/MainLayout.fxml"));
            
            // Cria nova cena com MainLayout
            Scene mainScene = new Scene(root);
            
            // Tenta carregar o CSS
            try {
                mainScene.getStylesheets().add(getClass().getResource("/globalStyle/style.css").toExternalForm());
                System.out.println("✅ CSS carregado com sucesso!");
            } catch (Exception e) {
                System.out.println("⚠️ CSS não carregado: " + e.getMessage());
            }
            
            // Configura o stage principal
            Stage mainStage = new Stage();
            mainStage.setTitle("MammaMia - Sistema de Gestão");
            mainStage.setScene(mainScene);
            mainStage.setMaximized(true); // Tela cheia
            
            // Fecha o login e abre o main
            loginStage.close();
            mainStage.show();
            
            System.out.println("✅ Sistema principal carregado com sucesso!");
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensagem("Erro ao carregar sistema: " + e.getMessage(), "erro");
        }
    }

    @FXML
    private void loginGoogle() {
        mostrarMensagem("Login com Google em desenvolvimento...", "sucesso");
    }

    @FXML
    private void loginRH() {
        mostrarMensagem("Login RH em desenvolvimento...", "sucesso");
    }

    private boolean validarCredenciais(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }

    @FXML
    private void fecharLogin() {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensagem(String mensagem, String tipo) {
        lblMensagem.setText(mensagem);
        if ("erro".equals(tipo)) {
            lblMensagem.setStyle("-fx-text-fill: #d63031; -fx-background-color: #ffe6e6; -fx-padding: 8px; -fx-background-radius: 4px;");
        } else {
            lblMensagem.setStyle("-fx-text-fill: #27ae60; -fx-background-color: #e6f7e6; -fx-padding: 8px; -fx-background-radius: 4px;");
        }
        lblMensagem.setVisible(true);
    }

    private void limparMensagem() {
        lblMensagem.setText("");
        lblMensagem.setVisible(false);
    }
}