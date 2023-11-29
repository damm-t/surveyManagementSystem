import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class insidesurvey_controler extends LS_controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private VBox BigBox;

    @FXML
    private TextField answerTF;

    @FXML
    private Button close;

    @FXML
    private TextField description;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label gmail;

    @FXML
    private HBox hbox1;

    @FXML
    private HBox hbox11;

    @FXML
    private HBox hbox12;

    @FXML
    private BorderPane layoutContent;

    @FXML
    private Button logOut;

    @FXML
    private ImageView pica;

    @FXML
    private Pane profileCard;

    @FXML
    private AnchorPane profilePic;

    @FXML
    private TextField questionTF;

    @FXML
    private TextField questionTF1;

    @FXML
    private TextField questionTF2;

    @FXML
    private TextField questionTF3;

    @FXML
    private TextField questionTF4;

    @FXML
    private TextField questionTF5;

    @FXML
    private RadioButton radioBtn;

    @FXML
    private RadioButton radioBtn1;

    @FXML
    private RadioButton radioBtn2;

    @FXML
    private HBox radioButtonBox;

    @FXML
    private ComboBox<String> rangeCB;
    ObservableList<String> range = FXCollections.observableArrayList("1-3", "1-4", "1-5");

    @FXML
    private Button saveBtn;

    @FXML
    private Button scdBtn;

    @FXML
    private Label surveyLabel;

    @FXML
    private TextField surveyTitle;

    @FXML
    private Label user;

    @FXML
    private VBox vbox1;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void SignOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void switchToDefault(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("user_survey.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void save(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Congratulation");
        alert.setHeaderText("Save succesfully");
    }

    @FXML
    void selectRange(ActionEvent event) {

        String selectedRange = (String) rangeCB.getValue();

        if (selectedRange != null) {
            int start = Integer.parseInt(selectedRange.split("-")[0]);
            int end = Integer.parseInt(selectedRange.split("-")[1]);
            rangeCB.setVisible(true);
            radioButtonBox.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
