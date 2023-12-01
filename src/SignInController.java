// import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
// import com.google.gson.JsonParser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// import java.io.FileReader;

public class SignInController implements Initializable {

    @FXML
    private Button close;

    @FXML
    protected TextField email;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField password;

    @FXML
    private AnchorPane right_scene;

    @FXML
    private Button signUp;

    @FXML
    private ResultSet result;

    public void login() {
        String enteredEmail = email.getText();
        String enteredPassword = password.getText();
        // path to determine whether it is admin, survey creator, user
        String[] filenames = { "DB/user.json", "DB/admin.json", "DB/surveycreator.json" };
        boolean foundUser = false;
        Alert alert = null;

        for (String filename : filenames) {
            try (FileReader reader = new FileReader(filename)) {
                Gson gson = new Gson();
                JsonElement root = gson.fromJson(reader, JsonElement.class);

                // extract the email and pw from JSON
                /*if (root.isJsonObject()) {
                    JsonObject jsonObject = root.getAsJsonObject();
                    String email = jsonObject.get("email").getAsString();
                    String password = jsonObject.get("password").getAsString();

                    if (enteredEmail.equals(email) && enteredPassword.equals(password)) {
                        foundUser = true;

                        if (filename.equals("src/user.json")) {
                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Welcome, User");
                            alert.showAndWait();

                            loadFXML("default_user.fxml");

                        } else if (filename.equals("src/admin.json")) {
                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Welcome, Admin");
                            alert.showAndWait();

                            loadFXML("admin_mainPage.fxml");
                        } else if (filename.equals("src/surveycreator.json")) {
                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Welcome, Survey Creator");
                            alert.showAndWait();

                            loadFXML("sc_mainPage.fxml");
                        }

                        loginBtn.getScene().getWindow().hide();
                        break;
                    }
                } */ if (root.isJsonArray()) {
                    JsonArray jsonArray = root.getAsJsonArray();

                    for (JsonElement element : jsonArray) {
                        JsonObject data = element.getAsJsonObject();
                        String email = data.get("email").getAsString();
                        String password = data.get("password").getAsString();

                        if (enteredEmail.equals(email) && enteredPassword.equals(password)) {
                            foundUser = true;

                            if (filename.equals("DB/user.json")) {
                                alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Information Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Welcome, User");
                                alert.showAndWait();

                                loadFXML("user_default.fxml");

                            } else if (filename.equals("DB/admin.json")) {
                                alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Information Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Welcome, Admin");
                                alert.showAndWait();

                                loadFXML("admin_mainPage.fxml");

                            } else if (filename.equals("DB/surveycreator.json")) {
                                alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Information Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Welcome, Survey Creator");
                                alert.showAndWait();
                                retrive_data getting = new retrive_data();
                                getting.comfirmation(email);
                                
                                loadFXML("sc_mainPage.fxml");
                            }

                            loginBtn.getScene().getWindow().hide();
                            break;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("File not found. Please check the file path: " + filename);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error reading the file: " + filename);
            }
        }

        if (!foundUser) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Wrong Email/Password");
            alert.showAndWait();
        }
    }

    private void loadFXML(String fxmlFileName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFileName));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential IOException, such as a missing FXML file
        }
    }

    public void close() {
        System.exit(0);
    }

    public void signUp() {
        loadFXML("SignUp.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code
    }

    public static class retrive_data{

        private String filepath = "DB/surveycreator.json";
        public static JsonObject SCdata;
        public static String email;
        //method
        public void comfirmation(String email){

            File file = new File(filepath);
            try (FileReader br = new FileReader(file)) {
                Gson gson = new Gson();
                JsonElement root = gson.fromJson(br, JsonElement.class);
        
                if (root.isJsonArray()) {
                    JsonArray jsonArray = root.getAsJsonArray();
        
                    for (JsonElement element : jsonArray) {
                        JsonObject data = element.getAsJsonObject();
                        String key = data.get("email").getAsString();
                        if (key.equalsIgnoreCase(email)) {
                            System.out.println("Imformation get found");
                            String Email = key;
                            setEmail(Email);
                            setSCdata(data);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception appropriately, e.g., log or rethrow
            }
        }
        public static JsonObject getSCdata(){
            return SCdata;
        }

        public static void setSCdata(JsonObject Scdata) {
            SCdata = Scdata;
        }

        public static String getEmail(){
            return email;
        }

        public static void setEmail(String Email){
            email = Email;
        }
        // public String getFirstName() {
        //     return firstName;
        // }

        // public void setFirstName(String firstName) {
        //     this.firstName = firstName;
        // }

        // public String getLastName() {
        //     return lastName;
        // }

        // public void setLastName(String lastName) {
        //     this.lastName = lastName;
        // }

        // public String getFaculty() {
        //     return faculty;
        // }

        // public void setFaculty(String faculty) {
        //     this.faculty = faculty;
        // }

        // public String getPhoneNumber() {
        //     return phoneNumber;
        // }

        // public void setPhoneNumber(String phoneNumber) {
        //     this.phoneNumber = phoneNumber;
        // }

        // public String getEmail() {
        //     return email;
        // }

        // public void setEmail(String email) {
        //     this.email = email;
        // }
        
    }
}
    



