
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class sc_mainPage_controller implements Initializable {

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
    private ImageView pica;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void switch_to_profile(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sc_profile.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switch_to_survey(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sc_survey.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void logOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public final String filepath = "DB/surveycreator.json";

    public void showingPic(ImageView shownPic) throws IOException {

        JsonObject get_ScData = SignInController.retrive_data.getSCdata();
        String pic = "";
        try (FileReader br = new FileReader(filepath)) {
            Gson gson = new Gson();
            JsonElement root = gson.fromJson(br, JsonElement.class);

            if (root.isJsonArray()) {
                JsonArray jsonArray = root.getAsJsonArray();

                for (JsonElement element : jsonArray) {
                    JsonObject data = element.getAsJsonObject();
                    if (data.equals(get_ScData)) {
                        pic = data.get("picture").getAsString();
                    } else
                        break;
                }
            }
        }
        String base64String = pic; // Your Base64 string here
        base64String = base64String.replaceAll("\\s", ""); // Remove whitespace
        byte[] decodedBytes;

        try {
            decodedBytes = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
            Image image = new Image(bis);
            shownPic.setImage(image);
            
            shownPic.setLayoutX(10);
            shownPic.setLayoutY(10);

        } catch (IllegalArgumentException problems) {
            System.err.println("Input is not valid Base64: " + problems.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle bl) {
        try {
            showingPic(pica);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}