import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
// import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

    public class AdminUserController implements Initializable{

        @FXML
        private Stage stage;
    
        @FXML
        private Scene scene;
    
        @FXML
        private Parent root;    

        @FXML
        private TableView<Profile> Table;

        @FXML
        private ObservableList<Profile> observablelist;

        @FXML
        private TableColumn<Profile, String> emailCol;

        @FXML
        private TableColumn<Profile, String> firstNameCol;

        @FXML
        private TableColumn<Profile, String> facultyCol;

        @FXML
        private TableColumn<Profile, String> lastNameCol;

        @FXML
        private TableColumn<Profile, String> phoneNoCol;

        @FXML
        private TableColumn<Profile, String> idCol;

        @FXML
        private TableColumn<Profile, String> passwordCol;
    
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
        void switchToSC(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("admin_scDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

        @FXML
        void switchToSurvey(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("admin_surveyDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

        @FXML
        void switchToUser(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("admin_userDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

        final static String filepath = "DB/user.json";

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            // Initialize column cell values
            idCol.setCellValueFactory(new PropertyValueFactory<>("username"));
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            phoneNoCol.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));

            // Call btnshown to populate the table
            btnshown(null);
        }

        @FXML
        private void btnshown(ActionEvent event) {
            ObservableList<Profile> data = FXCollections.observableArrayList();

            try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(br).getAsJsonArray();

                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();

                    String userId = jsonObject.get("username").getAsString();
                    String firstName = jsonObject.get("firstName").getAsString();
                    String lastName = jsonObject.get("lastName").getAsString();
                    String faculty = jsonObject.get("faculty").getAsString();
                    String phoneNo = jsonObject.get("phoneNumber").getAsString();
                    String email = jsonObject.get("email").getAsString();

                    // Add data to the ObservableList
                    data.add(new Profile(userId, firstName, lastName, faculty, email, phoneNo));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Set items to the table
            Table.setItems(data);
        }

        

        public static class Profile {

            private String username;
            private String firstName;
            private String lastName;
            private String faculty;
            private String phoneNo;
            private String email;
            private String password;

            public Profile(String username, String firstName, String lastName, String faculty, String email, String phoneNo) {
                this.username = username;
                this.firstName = firstName;
                this.lastName = lastName;
                this.phoneNo = phoneNo;
                this.faculty = faculty;
                this.email = email;
            }

            public String getUsername() {
                return username;
            }
    
            public String getFirstName() {
                return firstName;
            }
    
            public String getLastName() {
                return lastName;
            }
    
            public String getPhoneNo() {
                return phoneNo;
            }
    
            public String getEmail() {
                return email;
            }
    
            public String getFaculty() {
                return faculty;
            }
    
            public String getPassword() {
                return password;
            }
        }
    
}
