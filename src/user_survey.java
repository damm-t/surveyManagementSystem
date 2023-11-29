import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.exc.InvalidNullException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class user_survey implements Initializable {

    private Scene scene;
    private Parent root;
    private Stage stage;

    @FXML
    private Button close;

    @FXML
    private Label gmail;

    @FXML
    private Button logOut;

    @FXML
    private ImageView pica;

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
    private Label user;
    @FXML
    private TableView<SurveyDetails> table;

    @FXML
    private ObservableList<SurveyDetails> observableList;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
        void logOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle bl) {
        tbvTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tbvDatePosted.setCellValueFactory(new PropertyValueFactory<>("description"));

        try {
            shown();
        } catch (InvalidNullException e) {
            e.printStackTrace();
        }
    }

    private void shown() throws InvalidNullException {
        ObservableList<SurveyDetails> data = FXCollections.observableArrayList();
        data.add(new SurveyDetails("How is your day ?", "Have a nice day."));
        data.add(new SurveyDetails("How is your school life ?", "AOT weekly survey"));
        data.add(new SurveyDetails("Who is your favorite classes ?", "TES question..."));
        // Set the items to the TableView
        table.setItems(data);
    }

    @FXML
    void handleTableRowClick(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            SurveyDetails selectedDetails = table.getSelectionModel().getSelectedItem();
            // switch scene
            switchTosurvey();
        }
    };

    @FXML
    private void switchTosurvey() throws IOException {
        root = FXMLLoader.load(getClass().getResource("insidesurvey.fxml"));
        stage = (Stage) table.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public class SurveyDetails {
        private String title;
        private String description;

        public SurveyDetails(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}
