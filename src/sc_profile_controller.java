import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class sc_profile_controller extends SignInController {

    @FXML
    private Button close;

    @FXML
    private Button fsBtn;

    @FXML
    private Button logOut;

    @FXML
    private Button scdBtn;

    @FXML
    private Button uploadbtn;

    @FXML
    private TextField email_txt;

    @FXML
    private TextField faculty_txt;

    @FXML
    private TextField first_name_txt;

    @FXML
    private TextField last_name_txt;

    @FXML
    private TextField phoneNo_txt;

    @FXML
    private Pane profileCard;

    @FXML
    private ImageView profilePic;

    @FXML
    private Label user;

    @FXML
    private Label gmail;

    //initialize declaring of the picture
    byte[] person_image;

    //initialize declaring of the switch scene
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String filepath = "DB/surveycreator.json";

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void switch_to_survey(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sc_survey.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // showing data while entering the page
    private void onCLickShowingDetailofSC(ActionEvent e) throws IOException {

        String pic = "";
        String Fname = "";
        String Lname = "";
        String PN = "";
        String Email = "";
        String Faculty = "";
        String Username = "";
        String Password = "";

        // getting the Sc info from the sign in page
        JsonObject get_ScData = SignInController.retrive_data.getSCdata();

        try (FileReader br = new FileReader(filepath)) {
            Gson gson = new Gson();
            JsonElement root = gson.fromJson(br, JsonElement.class);

            if (root.isJsonArray()) {
                JsonArray jsonArray = root.getAsJsonArray();

                for (JsonElement element : jsonArray) {
                    JsonObject data = element.getAsJsonObject();
                    if (data.equals(get_ScData)) {
                        Fname = data.get("firstName").getAsString();
                        Lname = data.get("lastName").getAsString();
                        PN = data.get("phoneNumber").getAsString();
                        Email = data.get("email").getAsString();
                        Faculty = data.get("faculty").getAsString();
                        Username = data.get("username").getAsString();
                        Password = data.get("password").getAsString();
                        pic = data.get("picture").getAsString();
                    } else
                        break;
                }
                SC.setFirstName(Fname);
                SC.setLastName(Lname);
                SC.setPhoneNumber(PN);
                SC.setEmail(Email);
                SC.setFaculty(Faculty);
                SC.setUsername(Username);
                SC.setPassword(Password);

                System.out.println(Fname);
                first_name_txt.setText(Fname);
                last_name_txt.setText(Lname);
                phoneNo_txt.setText(PN);
                email_txt.setText(Email);
                faculty_txt.setText(Faculty);
            }

        } catch (FileNotFoundException problem) {

            Alert alert;
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("File not found, pls contact admin");
            alert.showAndWait();
            problem.printStackTrace();
        }

        String base64String = pic; // Your Base64 string here
        base64String = base64String.replaceAll("\\s", ""); // Remove whitespace
        byte[] decodedBytes;

        try {
            decodedBytes = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
            Image image = new Image(bis);
            profilePic.setImage(image);
        } catch (IllegalArgumentException problems) {
            System.err.println("Input is not valid Base64: " + problems.getMessage());
        }
    }

    // button on upload picture
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

        String Fname = SC.getFirstName();
        String Lname = SC.getLastName();
        String PN = SC.getPhoneNumber();
        String Email = SC.getEmail();
        String Faculty = SC.getFaculty();
        String Username = SC.getUsername();
        String Password = SC.getPassword();
        SC.setPic(pic);
        SC input_data = new SC(Fname, Lname, Faculty, PN, Email, Username, Password, pic);

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
        // for decoding
        // String profilePic = data[5];
        // byte[] decodedBytes = Base64.getDecoder().decode(profilePic);
        // Files.write(Paths.get("txtfiles/SC.txt"), decodedBytes);
    }

    @FXML
    private void savingButton(ActionEvent e) throws IOException {

        String firstNameInput = first_name_txt.getText();
        String lastNameInput = last_name_txt.getText();
        String facultyInput = faculty_txt.getText();
        String pnInput = phoneNo_txt.getText();
        String emailInput = email_txt.getText();
        String Username = SC.getUsername();
        String Password = SC.getPassword();
        String Pic = SC.getPic();

        SC update = new SC(firstNameInput, lastNameInput, facultyInput, pnInput, emailInput, Username, Password, Pic);
        if (save(update)) {
            Alert succesfuAlert = new Alert(AlertType.INFORMATION);
            succesfuAlert.setTitle("Success Message");
            succesfuAlert.setHeaderText(null);
            succesfuAlert.setContentText("Successfully saved.");
            succesfuAlert.showAndWait();
        } else {
            System.out.println("PLs contact admin, error SBT.248");
            throw new IOException("Error on update");
        }
        saveButton(e);
    }

    // this is for the saving function
    private boolean save(SC SCupdate) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(filepath);

        try {
            JsonObject SC_ori_data = retrive_data.SCdata;

            if (file.exists()) {
                // File exists, read existing data
                try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
                    JsonElement root = gson.fromJson(br, JsonElement.class);

                    if (root.isJsonArray()) {
                        JsonArray jsonArray = root.getAsJsonArray();

                        // Iterate over elements in the array
                        for (JsonElement element : jsonArray) {
                            JsonObject data = element.getAsJsonObject();

                            // Check if the current JsonObject matches the one you want to update
                            if (data.equals(SC_ori_data)) {
                                // Update the fields of the JsonObject with values from SCupdate
                                data.addProperty("firstName", SCupdate.getFirstName());
                                data.addProperty("lastName", SCupdate.getLastName());
                                data.addProperty("faculty", SCupdate.getFaculty());
                                data.addProperty("phoneNumber", SCupdate.getPhoneNumber());
                                data.addProperty("email", SCupdate.getEmail());
                                data.addProperty("username", SCupdate.getUsername());
                                data.addProperty("password", SCupdate.getPassword());
                                data.addProperty("picture", SCupdate.getPic());

                                // Break the loop since you found and updated the desired JsonObject
                                break;
                            }
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

    @FXML
    void editButton(ActionEvent event) throws IOException {
        System.out.println("edit button");
        // Active everything
        email_txt.setDisable(false);
        faculty_txt.setDisable(false);
        first_name_txt.setDisable(false);
        last_name_txt.setDisable(false);
        phoneNo_txt.setDisable(false);

    }

    private void saveButton(ActionEvent event) {
        // Disable everything
        email_txt.setDisable(true);
        faculty_txt.setDisable(true);
        first_name_txt.setDisable(true);
        last_name_txt.setDisable(true);
        phoneNo_txt.setDisable(true);
    }

    private void uneditable() {
        email_txt.setDisable(true);
        faculty_txt.setDisable(true);
        first_name_txt.setDisable(true);
        last_name_txt.setDisable(true);
        phoneNo_txt.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle bl) {
        try {
            uneditable();
            onCLickShowingDetailofSC(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SC {
        private static String firstName;
        private static String lastName;
        private static String faculty;
        private static String phoneNumber;
        private static String email;
        private static String username;
        private static String password;
        private static String picture;

        public SC(String firstName, String lastName, String faculty, String phoneNumber, String email, String Username,
                String password, String pic) {
            sc_profile_controller.SC.firstName = firstName;
            sc_profile_controller.SC.lastName = lastName;
            sc_profile_controller.SC.faculty = faculty;
            sc_profile_controller.SC.phoneNumber = phoneNumber;
            sc_profile_controller.SC.email = email;
            sc_profile_controller.SC.username = Username;
            sc_profile_controller.SC.password = password;
            sc_profile_controller.SC.picture = pic;
        }

        // Getters and setters for each field...
        public static String getFirstName() {
            return firstName;
        }

        public static void setFirstName(String frstName) {
            firstName = frstName;
        }

        public static String getLastName() {
            return lastName;
        }

        public static void setLastName(String lstName) {
            lastName = lstName;
        }

        public static String getFaculty() {
            return faculty;
        }

        public static void setFaculty(String fculty) {
            faculty = fculty;
        }

        public static String getPhoneNumber() {
            return phoneNumber;
        }

        public static void setPhoneNumber(String phneNumber) {
            phoneNumber = phneNumber;
        }

        public static String getEmail() {
            return email;
        }

        public static void setEmail(String emil) {
            email = emil;
        }

        public static String getUsername() {
            return username;
        }

        public static void setUsername(String usrname) {
            username = usrname;
        }

        public static String getPassword() {
            return password;
        }

        public static void setPassword(String pssword) {
            password = pssword;
        }

        public static String getPic() {
            return picture;
        }

        public static void setPic(String pic) {
            picture = pic;
        }
    }
}