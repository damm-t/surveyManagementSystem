import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AdminmainPageController {

    @FXML
    private Stage stage;

    @FXML
    private Scene scene;

    @FXML
    private Parent root;
    
    @FXML
    private Button close;

    @FXML
    private Button fsBtn;

    @FXML
    private Label gmail;

    @FXML
    private Button logOut;

    @FXML
    private Pane profileCard;

    @FXML
    private AnchorPane profilePic;

    @FXML
    private Button scdBtn;

    @FXML
    private Button thirdBtn;

    @FXML
    private Label user;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void switchToSC(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("admin_scDashboard.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    }
    
    @FXML
    public void switchToSurvey(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("admin_surveyDashboard.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    }

    @FXML
    public void switchToUser(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("admin_userDashboard.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    }

    @FXML
    public void logOut(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    }
}

