import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdminScController implements Initializable {

    byte[] person_image = null;

    @FXML
    private Stage stage;

    @FXML
    private Scene scene;

    @FXML
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
    private ImageView profilePic;

    @FXML
    private ImageView profilePicUpload;

    @FXML
    private Button scdBtn;

    @FXML
    private Button thirdBtn;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpload;

    @FXML
    private Label user;

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
    private TextField tfEmail;

    @FXML
    private TextField tfFName;

    @FXML
    private TextField tfFaculty;

    @FXML
    private TextField tfLName;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfPhoneNo;

    @FXML
    private TextField tfUsername;

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

    @FXML
    void logOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // contraint the file pathway
    final static String filepath = "DB/surveycreator.json";

    @FXML
    private void addBtn(ActionEvent event) throws JSONException {
        try {
            File file = new File(filepath);

            // Check if the file exists
            if (!file.exists()) {
                file.createNewFile();
            }

            // Read the existing content of the file
            StringBuilder jsonString = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
            }

            // Parse the entire content of the file as a JSON array
            JSONArray jsonArray;
            if (jsonString.length() > 0) {
                jsonArray = new JSONArray(jsonString.toString());
            } else {
                jsonArray = new JSONArray();
            }

            // Create a new JSON object for the data entered by the user
            JSONObject newEntry = new JSONObject();
            newEntry.put("firstName", tfFName.getText());
            newEntry.put("lastName", tfLName.getText());
            newEntry.put("faculty", tfFaculty.getText());
            newEntry.put("email", tfEmail.getText());
            newEntry.put("phoneNumber", tfPhoneNo.getText());
            newEntry.put("username", tfUsername.getText());
            newEntry.put("password", tfPassword.getText());
            // clearTextFields();

            // Add the new entry to the existing array
            jsonArray.put(newEntry);

            // Write the updated JSON array back to the file using BufferedWriter
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(jsonArray.toString(4));
            }

            // Update the table with the new data
            Profile data = new Profile(
                    newEntry.getString("username"),
                    newEntry.getString("firstName"),
                    newEntry.getString("lastName"),
                    newEntry.getString("faculty"),
                    newEntry.getString("email"),
                    newEntry.getString("phoneNumber"),
                    newEntry.getString("password"),
                    newEntry.getString("picture"));
            clearTextFields();

            Table.getItems().add(data);
            Table.refresh();

            // Display success message
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Success Message");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Survey Creator Created.");
            successAlert.showAndWait();

        } catch (IOException e) {
            System.out.println("Error on reading/writing file");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Error creating JSON object");
            e.printStackTrace();
        }

    }

    @FXML
    private void updateBtn(ActionEvent event) {
        // Declare initial values
        String inputFname = "";
        String inputLname = "";
        String inputPhoneNo = "";
        String inputEmail = "";
        String inputFaculty = "";
        String inputUsername = "";
        String inputPassword = "";
    
        Profile selectedSc = Table.getSelectionModel().getSelectedItem();
    
        if (selectedSc != null) {
            inputFname = AdminScController.Profile.getFirstName();
            inputLname = AdminScController.Profile.getLastName();
            inputPhoneNo = AdminScController.Profile.getPhoneNo();
            inputEmail = AdminScController.Profile.getEmail();
            inputFaculty = AdminScController.Profile.getFaculty();
            inputUsername = AdminScController.Profile.getFirstName();
            inputPassword = AdminScController.Profile.getPassword();
        }
    
        try (BufferedReader br = new BufferedReader(new FileReader("DB/surveycreator.json"))) {
            StringBuilder jsonString = new StringBuilder();
            String line;
    
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
    
            // Parse the entire content of the file as a JSON array
            JSONArray jsonArray = new JSONArray(jsonString.toString());
    
            // Iterate through each object in the array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
    
                // Check if the current object matches the selected profile
                if (inputFname.equals(jsonObject.getString("firstName")) &&
                        inputLname.equals(jsonObject.getString("lastName")) &&
                        inputPhoneNo.equals(jsonObject.getString("phoneNumber")) &&
                        inputEmail.equals(jsonObject.getString("email")) &&
                        inputFaculty.equals(jsonObject.getString("faculty")) &&
                        inputUsername.equals(jsonObject.getString("username")) &&
                        inputPassword.equals(jsonObject.getString("password"))) {
    
                    // Remove the selected profile from the JSON array
                    jsonArray.remove(i);
    
                    // Update the JSON array with the modified object
                    JSONObject newEntry = new JSONObject();
                    newEntry.put("email", tfEmail.getText());
                    newEntry.put("faculty", tfFaculty.getText());
                    newEntry.put("firstName", tfFName.getText());
                    newEntry.put("lastName", tfLName.getText());
                    newEntry.put("password", tfPassword.getText());
                    newEntry.put("phoneNumber", tfPhoneNo.getText());
                    newEntry.put("username", tfUsername.getText());
    
                    // Add the new entry to the existing array
                    jsonArray.put(newEntry);
    
                    // Write the updated JSON array back to the file using BufferedWriter
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("DB/surveycreator.json"))) {
                        writer.write(jsonArray.toString(4) + "\n");
                    }
    
                    // Show a success message
                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Success Message");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Update Completed.");
                    successAlert.showAndWait();
    
                    // Update the table with the new data
                    Profile newData = new Profile(
                        newEntry.getString("username"),
                        newEntry.getString("firstName"),
                        newEntry.getString("lastName"),
                        newEntry.getString("faculty"),
                        newEntry.getString("email"),
                        newEntry.getString("phoneNumber"),
                        newEntry.getString("password"),
                        newEntry.getString("picture"));
    
                    Table.getItems().set(Table.getSelectionModel().getSelectedIndex(), newData);
    
                    // Optionally, perform other actions as needed
                    clearTextFields();
                    return; // Exit the loop once the object is found and updated
            }
        }

        // If the loop completes without finding a match, show an error message
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText("Selected profile not found in JSON file.");
        errorAlert.showAndWait();

    } catch (IOException | JSONException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void btnDelete(ActionEvent event) {
        ObservableList<Profile> allSc, selectedSc;
        allSc = Table.getItems();
        selectedSc = Table.getSelectionModel().getSelectedItems();

        // create a copy of selected items
        List<Profile> removeSc = new ArrayList<>(selectedSc);

        // remove selected sc from all sc list
        removeSc.forEach(allSc::remove);

        // Get the filepath
        String filepath = "DB/surveycreator.json";

        try {
            File file = new File(filepath);

            // Check if the file exists
            if (!file.exists()) {
                System.out.println("File does not exist.");
                return; // No need to proceed if the file doesn't exist
            }

            // Read the existing content of the file
            StringBuilder jsonString = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
            }

            // Parse the entire content of the file as a JSON array
            JSONArray jsonArray;
            if (jsonString.length() > 0) {
                jsonArray = new JSONArray(jsonString.toString());
            } else {
                jsonArray = new JSONArray();
            }

            // Iterate through each object in the array
            for (Profile removedProfile : removeSc) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Check if the current object matches the removed profile
                    if (removedProfile.getEmail().equals(jsonObject.getString("email")) &&
                            removedProfile.getFaculty().equals(jsonObject.getString("faculty")) &&
                            removedProfile.getFirstName().equals(jsonObject.getString("firstName")) &&
                            removedProfile.getLastName().equals(jsonObject.getString("lastName")) &&
                            removedProfile.getPassword().equals(jsonObject.getString("password")) &&
                            removedProfile.getPhoneNo().equals(jsonObject.getString("phoneNumber")) &&
                            removedProfile.getFirstName().equals(jsonObject.getString("username"))) {
                        // Remove the matched object from the JSON array
                        jsonArray.remove(i);
                        break; // No need to continue iterating once the object is found
                    }
                }
            }

            // Write the updated JSON array back to the file using BufferedWriter
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(jsonArray.toString(4));
            }

            // Optionally, perform other actions as needed

        } catch (IOException | JSONException e) {
            System.out.println("Error on reading/writing file");
            e.printStackTrace();
        }

        clearTextFields();
    }

    // @FXML
    // private void btnDelete(ActionEvent event) {
    // ObservableList<Profile> allSc, selectedSc;
    // allSc = Table.getItems();
    // selectedSc = Table.getSelectionModel().getSelectedItems();

    // // create a copy of selected items
    // List<Profile> RemoveSc = new ArrayList<>(selectedSc);

    // // remove selected sc from all sc list
    // RemoveSc.forEach(allSc::remove);

    // clearTextFields();
    // }

    private void clearTextFields() {
        tfFName.clear();
        tfLName.clear();
        tfFaculty.clear();
        tfEmail.clear();
        tfPhoneNo.clear();
        tfUsername.clear();
        tfPassword.clear();
    }

    @FXML
    private void rowClicked(MouseEvent event) {
        Profile clickedSc = Table.getSelectionModel().getSelectedItem();
        if (clickedSc != null) {
            tfFName.setText(String.valueOf(clickedSc.getFirstName()));
            tfLName.setText(String.valueOf(clickedSc.getLastName()));
            tfFaculty.setText(String.valueOf(clickedSc.getFaculty()));
            tfEmail.setText(String.valueOf(clickedSc.getEmail()));
            tfPhoneNo.setText(String.valueOf(clickedSc.getPhoneNo()));
            tfUsername.setText(String.valueOf(clickedSc.getFirstName()));
            tfPassword.setText(String.valueOf(clickedSc.getPassword()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize column cell values
        idCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNoCol.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        // passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Call btnshown to populate the table
        btnshown(null);
    }

    @FXML
    private void btnshown(ActionEvent event) {
        ObservableList<Profile> data = FXCollections.observableArrayList();

        // // Initialize columns
        // idCol = new TableColumn<>("Id");
        // firstNameCol = new TableColumn<>("First Name");
        // lastNameCol = new TableColumn<>("Last Name");
        // facultyCol = new TableColumn<>("Faculty");
        // emailCol = new TableColumn<>("Email");
        // phoneNoCol = new TableColumn<>("Contact Number");
        // passwordCol = new TableColumn<>("Password");

        // // Add columns to the table
        // Table.getColumns().addAll(idCol, firstNameCol, lastNameCol, facultyCol,
        // emailCol, phoneNoCol, passwordCol);

        try (BufferedReader br = new BufferedReader(new FileReader("DB/surveycreator.json"))) {
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(br).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();

                String username = jsonObject.get("username").getAsString();
                String Fname = jsonObject.get("firstName").getAsString();
                String Lname = jsonObject.get("lastName").getAsString();
                String Faculty = jsonObject.get("faculty").getAsString();
                String PhoneNo = jsonObject.get("phoneNumber").getAsString();
                String Email = jsonObject.get("email").getAsString();
                String Password = jsonObject.get("password").getAsString();
                String Picture = jsonObject.get("picture").getAsString();
                // String username = jsonObject.get("username").getAsString();
                // String pic = jsonObject.get("pic").getAsString();
                // String password = jsonObject.get("password").getAsString();

                // Decode and display the image
                // try {
                // String base64String = pic.replaceAll("\\s", ""); // Remove whitespace
                // byte[] decodedBytes = Base64.getDecoder().decode(base64String);
                // ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
                // Image image = new Image(bis);
                // profilePic.setImage(image);
                // } catch (IllegalArgumentException e) {
                // System.err.println("Input is not valid Base64: " + e.getMessage());
                // }

                // Add data to the ObservableList
                data.add(new Profile(username, Fname, Lname, Faculty, Email, PhoneNo, Password, Picture));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set items to the table
        Table.setItems(data);
    }

    // upload image part

    @FXML
    private void UploadImageActionPerformed(ActionEvent event) {
        // performing on handling the opening error of uploading the picture
        Platform.runLater(() -> {

            FileChooser fileChooser = new FileChooser();

            // Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);

            // Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                profilePic.setImage(image);
                // profilePic.setFitWidth(65);
                // profilePic.setFitHeight(65);
                // profilePic.setLayoutX(22.0);
                // profilePic.setLayoutY(28.0);
                profilePic.setCache(true);
                try (FileInputStream fin = new FileInputStream(file)) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[64];

                    for (int readNum; (readNum = fin.read(buf)) != -1;) {
                        bos.write(buf, 0, readNum);
                    }
                    person_image = bos.toByteArray();
                }
            } catch (IOException ex) {
                Logger.getLogger("");
            }
            String picture = Base64.getEncoder().encodeToString(person_image);

            try {
                savingpic(picture);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("filepath error");
            }
        });
    }

    // function for saving pic
    private void savingpic(String pic) throws IOException {
        Alert alert;

        String Fname = Profile.getFirstName();
        String Lname = Profile.getLastName();
        String PN = Profile.getPhoneNo();
        String Email = Profile.getEmail();
        String Faculty = Profile.getFaculty();
        String Username = Profile.getFirstName();
        String Password = Profile.getPassword();
        Profile.setPicture(pic);

        Profile input_data = new Profile(Fname, Lname, Faculty, PN, Email, Username, Password, pic);

        if (save(input_data)) {
            System.out.println("Data have been save");
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Imform");
            alert.setHeaderText(null);
            alert.setContentText("The data have been saved");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("The data is not save get contact with admin .249");
        }
    }

    private boolean save(Profile profile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(filepath);

        try {

            if (file.exists()) {
                // File exists, read existing data
                try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
                    JsonElement root = gson.fromJson(br, JsonElement.class);

                    if (root.isJsonArray()) {
                        JsonArray jsonArray = root.getAsJsonArray();

                        // Iterate over elements in the array
                        for (JsonElement element : jsonArray) {
                            JsonObject data = element.getAsJsonObject();
                            // Update the fields of the JsonObject with values from SCupdate
                            data.addProperty("firstName", profile.getFirstName());
                            data.addProperty("lastName", profile.getLastName());
                            data.addProperty("faculty", profile.getFaculty());
                            data.addProperty("phoneNumber", profile.getPhoneNo());
                            data.addProperty("email", profile.getEmail());
                            data.addProperty("username", profile.getUsername());
                            data.addProperty("password", profile.getPassword());
                            data.addProperty("picture", profile.getPicture());

                            // Break the loop since you found and updated the desired JsonObject
                            break;
                        }

                        // Write the updated JSON array back to the file
                        try (FileWriter writer = new FileWriter(filepath)) {
                            gson.toJson(jsonArray, writer);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            System.out.println("Data has been updated in " + filepath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class Profile {

        private static String firstName;
        private static String lastName;
        private static String faculty;
        private static String phoneNo;
        private static String email;
        private static String password;
        private static String username;
        private static String picture;

        public Profile(String pic, String firstName, String lastName, String faculty, String email, String phoneNo,
                String username, String password) {
            AdminScController.Profile.picture = pic;
            AdminScController.Profile.username = username;
            AdminScController.Profile.firstName = firstName;
            AdminScController.Profile.lastName = lastName;
            AdminScController.Profile.phoneNo = phoneNo;
            AdminScController.Profile.faculty = faculty;
            AdminScController.Profile.email = email;
            AdminScController.Profile.phoneNo = phoneNo;
            AdminScController.Profile.password = password;
        }

        public String getUsername() {
            return username;
        }

        public static String getFirstName() {
            return firstName;
        }

        public static String getLastName() {
            return lastName;
        }

        public static String getPhoneNo() {
            return phoneNo;
        }

        public static String getEmail() {
            return email;
        }

        public static String getFaculty() {
            return faculty;
        }

        public static String getPassword() {
            return password;
        }

        public void setUsername(String usrname) {
            username = usrname;
        }

        public void setFirstName(String fistName) {
            firstName = fistName;
        }

        public void setLastName(String latName) {
            lastName = latName;
        }

        public void setPhoneNo(String phnNo) {
            phoneNo = phnNo;
        }

        public void setEmail(String emil) {
            email = emil;
        }

        public void setFaculty(String faclty) {
            faculty = faclty;
        }

        public void setPassword(String pasword) {
            password = pasword;
        }

        public static void setPicture(String pic) {
            picture = pic;
        }

        public String getPicture() {
            return picture;
        }
    }
}