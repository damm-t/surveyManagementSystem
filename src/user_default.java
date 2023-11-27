
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
public class user_default {

    private Stage stage;
    private Scene scene;
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
    private Label user;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void switchToSurvey(ActionEvent e) throws IOException{
        root = FXMLLoader.load(getClass().getResource("user_survey.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void logOut(ActionEvent e)throws IOException{
        root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
