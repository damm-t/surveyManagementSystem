import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class survey {

    @FXML
    private VBox BigBox;

    @FXML
    private Button close;

    @FXML
    private VBox defaultBox;

    @FXML
    private TextField description;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label gmail;

    @FXML
    private HBox hbox;

    @FXML
    private BorderPane layoutContent;

    @FXML
    private Button logOut;

    @FXML
    private Pane profileCard;

    @FXML
    private AnchorPane profilePic;

    @FXML
    private FlowPane questBox;

    @FXML
    private Button saveBtn;

    @FXML
    private Button survey;

    @FXML
    private Label surveyLabel;

    @FXML
    private TextField surveyTitle;

    @FXML
    private Label user;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    private Scene scene;
    private Stage stage;
    private Parent root;

    final private String filepath = "DB/survey.json";

    @FXML
    public void SignOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private int mcqCount = 0;
    private int lsCount = 0;
    private int oeCount = 0;
    int question_amount = 0;
    private void readFromJson() throws IOException {
        Gson gson = new Gson();
        

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            JsonElement root = gson.fromJson(br, JsonElement.class);

            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                JsonObject surveyInfo = jsonObject.getAsJsonObject("surveyInfo");

                // Check if the current JsonObject matches the one you want to get
                if (surveyInfo != null) {
                    String title = surveyInfo.get("title").getAsString();
                    String desc = surveyInfo.get("description").getAsString();
                    String date = surveyInfo.get("datePosted").getAsString();
                }

                JsonArray questionArray = jsonObject.getAsJsonArray("questions");
                if (questionArray != null) {
                    question_amount = questionArray.size();

                    for (JsonElement element : questionArray) {
                        JsonObject questionObject = element.getAsJsonObject();

                        JsonObject questionDetails = questionObject.getAsJsonObject("questionDetails");
                        String type = questionDetails.get("type").getAsString();
                        int questionId = questionDetails.get("questionId").getAsInt();
                        JsonObject question_array = questionDetails.getAsJsonObject("questionArray");

                        survey_data get_data = new survey_data();
                        get_data.determine(type, question_array);
                        
                        switch (type) {
                            case "MCQ":
                                mcqCount++;
                                break;
                            case "OE":
                                oeCount++;
                                break;
                            case "LS":
                                lsCount++;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void construct() throws IOException{
        readFromJson();
        
    }

    
    public int getMcqCount() {
        return mcqCount;
    }

    public int getLsCount() {
        return lsCount;
    }

    public int getOeCount() {
        return oeCount;
    }

}