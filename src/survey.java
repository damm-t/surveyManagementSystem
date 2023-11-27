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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class survey implements Initializable {

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

    final private String filepath = "DB/survey.json";

    private void getInfor() {
        Gson gson = new Gson();
        File file = new File(filepath);
        JsonObject _ori_data = sc_survey.get_title.Surveydata;
        String title;
        String desc;
        try {
            if (file.exists()) {
                // File exists, read existing data
                try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
                    JsonElement root = gson.fromJson(br, JsonElement.class);

                    if (root.isJsonArray()) {
                        JsonArray jsonArray = root.getAsJsonArray();
                        System.out.println(_ori_data);
                        // Iterate over elements in the array
                        for (JsonElement element : jsonArray) {
                            JsonObject data = element.getAsJsonObject();

                            // Check if the current JsonObject matches the one you want to get
                            if (data.equals(_ori_data)) {
                                title = data.get("title").getAsString();
                                desc = data.get("description").getAsString();
                                SurveyDetails sc = new SurveyDetails(title, desc);
                                System.out.println(title);
                                // Break the loop since you found the desired JsonObject
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        surveyTitle.setText(SurveyDetails.getTitle());
        description.setText(SurveyDetails.getDesc());
    }

    @Override
    public void initialize(URL url, ResourceBundle bl) {

        getInfor();
    }

    public static class SurveyDetails {
        private static String title;
        private static String desc;

        public SurveyDetails(String title, String desc) {
            this.title = title;
            this.desc = desc;
        }

        public static String getTitle() {
            return title;
        }

        public void setTitle(String ttle) {
            title = ttle;
        }

        public static String getDesc() {
            return desc;
        }

        public void setDesc(String dsc) {
            desc = dsc;
        }
    }

}
