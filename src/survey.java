import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

public class survey extends QuestionPage {

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
    private VBox bigBox;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    survey_data sd = new survey_data();
    private Scene scene;
    private Stage stage;
    private Parent root;

    final private String filepath = "DB/survey.json";

    @Override
    public void initialize (URL url, ResourceBundle rl){
        try {
            readFromJson();
            construct();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
    survey_data get_data = new survey_data();
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
                    String date = surveyInfo.get("created_date").getAsString();
                    sd.setSurveyTitle(title);
                    sd.setSurveyDescription(desc);
                    sd.setSurveyDate(date);
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

    private void construct() throws IOException {
        createdMCQ();
        createdLS();
        createdOE();
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

    public void createdMCQ() throws IOException {
        List<Node> mcqNodes = new ArrayList<>();
        for (int i = 0; i < mcqCount; i++) {
            Node mcqNode = createNode("MCQ", i);
            mcqNodes.add(mcqNode);
        }

        BigBox.getChildren().addAll(mcqNodes);
    }

    public void createdOE() throws IOException {
        List<Node> oeNodes = new ArrayList<>();
        for (int i = 0; i < mcqCount; i++) {
            Node oeNode = createNode("OE", i);
            oeNodes.add(oeNode);
        }

        BigBox.getChildren().addAll(oeNodes);
    }

    public void createdLS() throws IOException {
        List<Node> lsNodes = new ArrayList<>();
        for (int i = 0; i < mcqCount; i++) {
            Node lsNode = createNode("LS", i);
            lsNodes.add(lsNode);
        }

        BigBox.getChildren().addAll(lsNodes);
    }
}