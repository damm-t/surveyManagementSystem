import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SignUpController {

    @FXML
    private Button close;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField faculty;

    @FXML
    private TextField pn;

    @FXML
    private TextField email;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField retypePw;

    @FXML
    private AnchorPane root;

    @FXML
    private Button signIn;

    @FXML
    private Button signUp;

    // JSON file path
    private static final String filePath = "DB/user.json";

    @FXML
    void signUpUser(ActionEvent event) {
        // Get user input...
        String firstNameInput = firstName.getText();
        String lastNameInput = lastName.getText();
        String facultyInput = faculty.getText();
        String pnInput = pn.getText();
        String emailInput = email.getText();
        String usernameInput = username.getText();
        String passwordInput = password.getText();
        String retypePwInput = retypePw.getText();

        if (!passwordInput.equals(retypePwInput)) {
            // Passwords don't match, show alert and return
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Retype Passwords do not match.");
            alert.showAndWait();
            return;
        }

        User newUser = new User(firstNameInput, lastNameInput, facultyInput, pnInput, emailInput, usernameInput,
                passwordInput);

        if (createOrAppendJson(newUser)) {
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Success Message");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Sign-up complete.");
            successAlert.showAndWait();
            loadFXML("SignIn.fxml");
            return;
        } else {
            System.out.println("Cannot add.");
        }
    }

    private boolean createOrAppendJson(User newUser) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(filePath);

        if (file.exists()) {
            try {
                // File exists, read existing content
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                StringBuilder jsonString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                reader.close();

                // Parse existing content as an array
                User[] users = gson.fromJson(jsonString.toString(), User[].class);

                // Convert array to a list for easier manipulation
                List<User> userList = new ArrayList<>(Arrays.asList(users));

                // Add the new user
                userList.add(newUser);

                // Write the updated JSON array back to the file
                FileWriter writer = new FileWriter(filePath);
                gson.toJson(userList, writer);
                writer.close();

                System.out.println("User data has been appended to user.json");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // File doesn't exist, create a new JSON array with the new user
            try {
                FileWriter writer = new FileWriter(filePath);
                List<User> userList = new ArrayList<>();
                userList.add(newUser);
                gson.toJson(userList, writer);
                writer.close();

                System.out.println("User data has been created in user.json");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public void close() {
        System.exit(0);
    }

    public void signIn() {
        loadFXML("SignIn.fxml");
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

    public static class User {
        private String firstName;
        private String lastName;
        private String faculty;
        private String phoneNumber;
        private String email;
        private String username;
        private String password;

        public User(String firstName, String lastName, String faculty, String phoneNumber, String email,
                String username, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.faculty = faculty;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.username = username;
            this.password = password;
        }

        // Getters and setters for each field...
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFaculty() {
            return faculty;
        }

        public void setFaculty(String faculty) {
            this.faculty = faculty;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        // Custom toString() method for debugging or logging
        @Override
        public String toString() {
            return "User{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", faculty='" + faculty + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", email='" + email + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
