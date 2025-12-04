package tela_main_controller;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Locale;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * 🚀 MainLayoutController 
 * Controller corrigido para carregar todas as telas
 */
public class MainLayoutController implements Initializable {

    @FXML
    private Label labelRelogio;

    @FXML
    private Label labelEstacao;

    @FXML
    private StackPane painelConteudo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, HH:mm", Locale.forLanguageTag("pt-BR"));

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        iniciarRelogio();
        abrirDashboard(); // Abre o dashboard logo no início
    }

    private void iniciarRelogio() {
        // 🔥 CORREÇÃO AQUI: Troquei "_ ->" por "event ->"
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), event -> {
            LocalDateTime agora = LocalDateTime.now();
            String textoFormatado = formatter.format(agora);

            String textoComMesMaiusculo = capitalizarMes(textoFormatado);

            labelRelogio.setText(textoComMesMaiusculo);
            labelEstacao.setText(obterEstacao(agora.getMonthValue()));
        }), new KeyFrame(Duration.seconds(60)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private String capitalizarMes(String texto) {
        if (texto == null || texto.isEmpty())
            return texto;

        String[] partes = texto.split(" ");
        if (partes.length < 2)
            return texto;

        String mes = partes[1].replace(",", "");
        mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);

        int indexVirgula = texto.indexOf(",");
        String resto = indexVirgula != -1 ? texto.substring(indexVirgula) : "";

        return partes[0] + " " + mes + resto;
    }

    private String obterEstacao(int mes) {
        if (mes == 12 || mes <= 2) {
            return "Verão - Refresque-se com nossas massas leves e receba 20% de desconto nas saladas!";
        } else if (mes >= 3 && mes <= 5) {
            return "Outono - No aconchego do outono, peça um prato de risoto e ganhe a sobremesa italiana do dia!";
        } else if (mes >= 6 && mes <= 8) {
            return "Inverno - Rodízio de massas com fondue de queijo em promoção especial!.";
        } else if (mes >= 9 && mes <= 11) {
            return "Primavera - Na primavera do sabor, peça uma pizza marguerita e ganhe uma taça de vinho da casa!";
        }
        return "";
    }

    /**
     * 🚀 MÉTODOS CORRIGIDOS - Com verificação de arquivos
     */
    public void abrirDashboard() {
        carregarTela("/telas/view/TelaDashboard.fxml");
    }

    public void abrirListaColmeia() {
        carregarTela("/telas/view/TelaListaGarcons.fxml");
    }

    @FXML
    private void abrirLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/view/TelaLogin.fxml"));
            Parent root = loader.load();
            
            Stage loginStage = new Stage();
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.initOwner(painelConteudo.getScene().getWindow());
            loginStage.setTitle("Login - Mamma Mia");
            loginStage.setResizable(false);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/globalStyle/style.css").toExternalForm());
            loginStage.setScene(scene);
            
            loginStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao abrir login");
            alert.setContentText("Não foi possível abrir a tela de login.");
            alert.showAndWait();
        }
    }

    /**
     * 🚀 MÉTODO CORRIGIDO para carregar telas sem loop infinito
     */
    private void carregarTela(String caminho) {
        try {
            System.out.println("Tentando carregar: " + caminho);
            
            // Carrega o FXML normalmente - cada tela tem seu próprio controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Node tela = loader.load();

            tela.setOpacity(0);
            painelConteudo.getChildren().setAll(tela);

            FadeTransition fade = new FadeTransition(Duration.millis(900), tela);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
            
            System.out.println("✅ Tela carregada: " + caminho);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ ERRO ao carregar: " + caminho);
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Arquivo não encontrado");
            alert.setHeaderText("Tela em desenvolvimento");
            alert.setContentText("A tela '" + caminho + "' não foi encontrada ou está em desenvolvimento.");
            alert.showAndWait();
        }
    }

    /**
     * Método chamado ao clicar no botão "Sair". Encerra a aplicação com segurança.
     */
    @FXML
    private void sair() {
        Platform.exit();
    }
}