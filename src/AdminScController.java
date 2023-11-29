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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import javafx.scene.control.ButtonType;
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
    private TextField search;

    @FXML
    private Button searchBtn;

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

            String pic = "";
            for(int i = 0 ; i < jsonArray.length(); i ++ ) {
                JSONObject olddata = jsonArray.getJSONObject(i);
                pic = olddata.getString("picture");
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
            newEntry.put("picture", pic);
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
                    newEntry.getString("password"));
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
            inputFname = selectedSc.getFirstName();
            inputLname = selectedSc.getLastName();
            inputPhoneNo = selectedSc.getPhoneNo();
            inputEmail = selectedSc.getEmail();
            inputFaculty = selectedSc.getFaculty();
            inputUsername = selectedSc.getUsername();
            inputPassword = selectedSc.getPassword();
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
                            newEntry.getString("password"));
    
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
                            removedProfile.getUsername().equals(jsonObject.getString("username"))) {
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
            tfUsername.setText(String.valueOf(clickedSc.getUsername()));
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
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Call btnshown to populate the table
        btnshown(null);

        // Set up the search functionality
        observablelist = Table.getItems();
        searchBtn.setOnAction(this::searchText);
    }

    @FXML
    private void searchText(ActionEvent event) {
        String searchFirstName = search.getText().toLowerCase();

        // Filter the data based on the entered first name
        ObservableList<Profile> filteredData = observablelist.filtered(profile ->
                profile.getFirstName().toLowerCase().contains(searchFirstName));

        // Update the table with the filtered data
        Table.setItems(filteredData);
        Table.refresh();
    }

    @FXML
    private void btnshown(ActionEvent event) {
        ObservableList<Profile> data = FXCollections.observableArrayList();

        // Initialize columns
        idCol = new TableColumn<>("Id");
        firstNameCol = new TableColumn<>("First Name");
        lastNameCol = new TableColumn<>("Last Name");
        facultyCol = new TableColumn<>("Faculty");
        emailCol = new TableColumn<>("Email");
        phoneNoCol = new TableColumn<>("Contact Number");
        passwordCol = new TableColumn<>("Password");

        // Add columns to the table
        Table.getColumns().addAll(idCol, firstNameCol, lastNameCol, facultyCol, emailCol, phoneNoCol, passwordCol);

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
                data.add(new Profile(username, Fname, Lname, Faculty, Email, PhoneNo, Password));
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
            fileChooser.getExtensionFilters()
                    .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);

            // Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                profilePic.setImage(image);
                profilePic.setFitWidth(65);
                profilePic.setFitHeight(65);
                profilePic.setLayoutX(22.0);
                profilePic.setLayoutY(28.0);
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

    private void savingpic(String pic) throws IOException {
        Alert alert;
        String[] data = Files.readAllLines(Paths.get(filepath)).get(0).split(" , ");
        String pic_data = data[6];
        if (pic_data.equals(null))
            data[6] = pic;
        else {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("The picture will be replace");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                data[6] = pic;
            } else {
                return;
            }
        }
        Files.write(Paths.get(filepath), String.join(" , ", data).getBytes());
        // for decoding
        // String profilePic = data[5];
        // byte[] decodedBytes = Base64.getDecoder().decode(profilePic);
        // Files.write(Paths.get("txtfiles/SC.txt"), decodedBytes);
    }

    /*
     * //
     * //initialize the profile part
     */ //

    public static class Profile {

        private String username;
        private String firstName;
        private String lastName;
        private String faculty;
        private String phoneNo;
        private String email;
        private String password;

        public Profile(String username, String firstName, String lastName, String faculty, String email, String phoneNo, String password) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNo = phoneNo;
            this.faculty = faculty;
            this.email = email;
            this.phoneNo = phoneNo;
            this.password = password;
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