import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class sc_survey extends sc_mainPage_controller {

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
    private TableColumn<SurveyDetails, String> tbvDatePosted;

    @FXML
    private TableColumn<SurveyDetails, String> tbvTitle;

    @FXML
    private ImageView pica;

    @FXML
    private Label user;

    @FXML
    private Button CreateSurvey;

    @FXML
    private TableView<SurveyDetails> SurveyTable;

    @FXML
    private ObservableList<SurveyDetails> observableList;

    // initialize declaring of the switch scene
    private Stage stage;
    private Scene scene;
    private Parent root;

    final private String filepath = "DB/survey.json";

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void switchToProfile(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sc_profile.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void switchToCreatePage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("QuestionPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleTableRowClick(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            SurveyDetails selectedDetails = SurveyTable.getSelectionModel().getSelectedItem();

            if (selectedDetails != null) {
                String title = selectedDetails.getTitle();
                sc_survey.get_title.comfirmation(title);
            }

            // switch scene
            switchToSurvey();
        }
    };

    private void switchToSurvey() throws IOException {
        root = FXMLLoader.load(getClass().getResource("survey.fxml"));
        stage = (Stage) SurveyTable.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static JsonObject Surveydata;

    @Override
    public void initialize(URL url, ResourceBundle bl) {

        tbvTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tbvDatePosted.setCellValueFactory(new PropertyValueFactory<>("createdDate"));

        try {
            shown();
            showingPic(pica);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void shown() throws IOException {
        ObservableList<SurveyDetails> data = FXCollections.observableArrayList();
        Gson gson = new Gson();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            JsonElement root = gson.fromJson(reader, JsonElement.class);
    
            if (root.isJsonArray()) {
                JsonArray jsonArray = root.getAsJsonArray();
    
                for (JsonElement element : jsonArray) {
                    if (element.isJsonObject()) {
                        JsonObject surveyObject = element.getAsJsonObject();
    
                        if (surveyObject.has("surveyInfo")) {
                            JsonObject surveyInfo = surveyObject.getAsJsonObject("surveyInfo");
                            String title = surveyInfo.get("title").getAsString();
                            String desc = surveyInfo.get("description").getAsString();
                            data.add(new SurveyDetails(title, desc));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SurveyTable.setItems(data);
    }
    

    public static class get_title {

        // method
        public static void comfirmation(String title) {

            String filepath = "DB/survey.json";
            File file = new File(filepath);

            try (FileReader br = new FileReader(file)) {
                Gson gson = new Gson();
                JsonElement root = gson.fromJson(br, JsonElement.class);

                if (root.isJsonObject()) {
                    JsonObject jsonObject = root.getAsJsonObject();
                    JsonObject surveyInfo = jsonObject.getAsJsonObject("surveyInfo");

                    if (surveyInfo != null) {
                        String key = surveyInfo.get("title").getAsString();
                        if (key.equalsIgnoreCase(title)) {
                            System.out.println("Imformation get found");
                            setSurveydata(surveyInfo);
                            System.out.println(surveyInfo);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception appropriately, e.g., log or rethrow
            }
        }

        public static JsonObject Surveydata;

        public static JsonObject getSurveyData() {
            return Surveydata;
        }

        public static void setSurveydata(JsonObject surveyData) {
            Surveydata = surveyData;
        }
    }

    public static class SurveyDetails {
        private String title;
        private String createdDate;

        public SurveyDetails(String title, String createdDate) {
            this.title = title;
            this.createdDate = createdDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String creatdDate) {
            createdDate = creatdDate;
        }
    }

}