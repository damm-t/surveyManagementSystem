import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuestionPage implements Initializable {

    @FXML
    private VBox BigBox;

    @FXML
    private Button LS;

    @FXML
    private Button MCQ;

    @FXML
    private ChoiceBox<String> NewQuestionChoices;

    @FXML
    private Button OE;

    @FXML
    private Button close;

    @FXML
    private VBox defaultBox;

    @FXML
    private Label defaultW;

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
    private Button profile;

    @FXML
    private Pane profileCard;

    @FXML
    private AnchorPane profilePic;

    @FXML
    protected FlowPane questAddBox;

    // @FXML
    // public FlowPane questionBox;

    @FXML
    public FlowPane questBox;

    @FXML
    private Button saveBtn;

    @FXML
    private Button survey;

    @FXML
    private Label surveyLabel;

    @FXML
    private TextField surveyTitle;

    @FXML
    private Button uploadBtn;

    @FXML
    private Label user;

    protected Node node;

    protected ComboBox<String> comboBox = new ComboBox<String>();

    private static final int MCQ_TYPE = 1;
    private static final int OE_TYPE = 2;
    private static final int LS_TYPE = 3;

    List<Integer> IDList = new ArrayList<>();
    private static int questionCount = 1;

    JsonObject SC_ori_data = SignInController.retrive_data.SCdata;

    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    void logOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> options = FXCollections.observableArrayList("MCQ", "Open-Ended", "Likert-Scale");
        comboBox.setItems(options);
        NewQuestionChoices.setItems(options);
        // comboBox.setOnAction(this::reselection);
    }

    @FXML
    private void proceedToQuestion(MouseEvent e) throws IOException {
        questBox.getChildren().remove(defaultBox);
        NewQuestionChoices.setVisible(true);
    }

    private int generateID(int questionNumber, int questionType) {
        questionCount++;
        // Combine the question number and question type to form the ID
        return Integer.parseInt(String.valueOf(questionNumber) + String.valueOf(questionType));
    }

    private void renumberSubsequentQuestions(int deletedID) {
        IDList = ID.getlist();
        for (int i = 0; i < IDList.size(); i++) {
            int currentID = IDList.get(i);
            if (currentID > deletedID) {
                int questionNumber = i + 1;
                int questionType = currentID % 10;
                int newID = generateID(questionNumber, questionType);
                IDList.set(i, newID);
            }
        }
        ID.setList(IDList);
    }

    @FXML
    private void handleButtonClick(ActionEvent event) throws IOException {

        System.out.println(SharedNodeList.getNodeSize());
        Button sourceButton = (Button) event.getSource();
        String buttonType = sourceButton.getText();

        int childId = SharedNodeList.generateUniqueChildId();

        Node children = createNode(buttonType, childId);

        children.setUserData(childId);
        System.out.println("Child id " + childId + "created.");

        if (children != null) {
            // Add the node to the observable list
            SharedNodeList.addNode(children);

            // Add all nodes from nodeList to questBox
            for (Node n : SharedNodeList.getNodeList()) {
                questBox.getChildren().add(n);
            }

            SharedNodeList.setQuestBox(questBox);

            System.out.println(SharedNodeList.getNodeSize());
        }
        ID.setList(IDList);
    }

    @FXML
    private void AddNew(ActionEvent e) {

        String addNewQ = (String) NewQuestionChoices.getValue();

        try {

            int childId = SharedNodeList.generateUniqueChildId();
            Node node = createNode(addNewQ, childId);
            System.out.println(SharedNodeList.getNodeSize());

            node.setUserData(childId);
            System.out.println("Child id " + childId + "created.");

            SharedNodeList.addNode(node);

            // Clear existing nodes in questBox
            SharedNodeList.getQuestBox().getChildren().clear();

            // Add all nodes from nodeList to questBox
            for (Node n : SharedNodeList.getNodeList()) {
                SharedNodeList.getQuestBox().getChildren().add(n);
            }

            System.out.println(SharedNodeList.getNodeSize());

        } catch (IOException exception) {
            System.out.println("Error creating or adding node.");
            exception.printStackTrace();
        }
        ID.setList(IDList);
    }

    final private String filepath = "DB/survey.json";

    @FXML
    private void save(ActionEvent e) throws IOException {

        String title = surveyTitle.getText();
        String desc = description.getText();
        ID.setTitle(title);
        ID.setDesc(desc);
        save();
    }

    private JsonObject save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(filepath);
        JsonObject result = new JsonObject();

        try {
            if (file.exists()) {
                // File exists, read existing data
                try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
                    JsonElement root = gson.fromJson(br, JsonElement.class);

                    if (root != null && root.isJsonArray()) {
                        JsonArray rootObject = root.getAsJsonArray();
                        JsonObject survey = new JsonObject();

                        // Update surveyInfo if needed
                        for (JsonElement element : rootObject) {
                            JsonObject monoSurvey = element.getAsJsonObject();

                            JsonObject surveyInfo = monoSurvey.getAsJsonObject("surveyInfo");

                            if (surveyInfo == null) {
                                // If "surveyInfo" is not present, create a new JsonObject for it
                                surveyInfo = new JsonObject();
                                monoSurvey.add("surveyInfo", surveyInfo);
                            }
                            surveyInfo.addProperty("title", ID.getTitle());
                            surveyInfo.addProperty("description", ID.getDesc());
                            surveyInfo.addProperty("email", ID.getEmail());
                            surveyInfo.addProperty("datePosted", setCreatedDate());

                            // Add or update questions
                            JsonArray questionsArray = monoSurvey.getAsJsonArray("questions");
                            for (int i = 0; i < ID.getlist().size(); i++) {
                                int n = ID.getlist().get(i);
                                JsonObject questionDetails = determine(n);

                                // Add or update question based on questionId
                                boolean found = false;
                                for (JsonElement element2 : questionsArray) {
                                    JsonObject existingQuestion = element2.getAsJsonObject();
                                    if (existingQuestion.getAsJsonPrimitive("questionId").getAsInt() == n) {
                                        // Question found, update it
                                        existingQuestion.add("questionDetails", questionDetails);
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    // Question not found, add a new question
                                    JsonObject newQuestion = new JsonObject();
                                    newQuestion.addProperty("questionId", n);
                                    newQuestion.add("questionDetails", questionDetails);
                                    questionsArray.add(newQuestion);
                                }
                            }

                            // Write the updated JSON data back to the file
                            try (FileWriter writer = new FileWriter(filepath)) {
                                gson.toJson(rootObject, writer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            result = monoSurvey;
                            survey.add("surveys", monoSurvey);
                            rootObject.add(survey);
                        } // Assign the found JsonObject to result

                    } else {
                        // Handle the case where the file is empty or not in a valid JSON format
                        // You may want to create a new JSON structure or take appropriate action
                        // based on your application's requirements.

                        // Here, we'll create a new JSON structure with the new data
                        JsonObject newJson = createNewJson();
                        try (FileWriter writer = new FileWriter(filepath)) {
                            gson.toJson(newJson, writer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        result = newJson;
                    }
                }
            } else {
                // File doesn't exist, create a new JSON array with the new data
                JsonObject newJson = createNewJson();
                try (FileWriter writer = new FileWriter(filepath)) {
                    gson.toJson(newJson, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result = newJson;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private JsonObject createNewJson() {
        JsonObject newJson = new JsonObject();
        JsonObject monoArray = new JsonObject();
        JsonArray polySurvey = new JsonArray();

        String id = Integer.toString(ID.IdList.size());

        // Create surveyInfod
        JsonObject surveyInfo = new JsonObject();
        surveyInfo.addProperty("title", ID.getTitle());
        surveyInfo.addProperty("description", ID.getDesc());
        surveyInfo.addProperty("created_date", setCreatedDate());
        surveyInfo.addProperty("email", ID.getEmail());

        // Create questions array
        JsonArray questionsArray = new JsonArray();

        // Add or update questions
        for (int i = 0; i < ID.getlist().size(); i++) {
            int n = ID.getlist().get(i);
            JsonObject questionDetails = determine(n);

            // Add a new question
            JsonObject newQuestion = new JsonObject();
            newQuestion.addProperty("questionId", n);
            newQuestion.add("questionDetails", questionDetails);
            
            questionsArray.add(newQuestion);
        }

        // Add surveyInfo and questions to the new JSON structure
        monoArray.addProperty("SurveyID", id);
        monoArray.add("surveyInfo",surveyInfo);
        monoArray.add("questions",questionsArray);
        polySurvey.add(monoArray);
        newJson.add("surveys",polySurvey);
        return newJson;
    }

    private String setCreatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String datenow = sdf.format(new Date());
        return datenow;
    }

    private JsonObject determine(int q) {
        int n = q % 10;

        JsonObject data = new JsonObject();

        switch (n) {
            case 1:
                // MCQ Question
                data.addProperty("type", "MCQ");
                data.addProperty("questionId", q);

                Map<Integer, String> optionsMap = MCQ_controller.getMCQData.getOptions();
                for (Map.Entry<Integer, String> entry : optionsMap.entrySet()) {
                    int smcqId = entry.getKey();
                    String optionTitle = entry.getValue();
                    data.addProperty("optionId", smcqId);
                    data.addProperty("optionTitle", optionTitle);
                }
                return data;

            case 2:
                // Open-ended Question
                data.addProperty("type", "OE");
                data.addProperty("questionId", q);
                // creating a new question title by static method
                String questionTitle = OE_controller.getQ();
                data.addProperty("Question Title", questionTitle);

                return data;

            case 3:
                // Likert-Scale Question
                data.addProperty("type", "LS");
                data.addProperty("questionId", q);
                data.addProperty("Question Title", LS_controller.getQ());

                return data;

            default:
                // Handle other question types or throw an exception if needed
                return null;
        }
    }

    @FXML
    public void deleteNodeByType(int childId) {
        if (SharedNodeList.getQuestBox() != null) {

            if (!SharedNodeList.getNodeList().isEmpty()) {

                int indexToRemove = -1;

                for (int i = 0; i < SharedNodeList.getNodeSize(); i++) {
                    Node node = SharedNodeList.getNodeList().get(i);
                    Object userData = node.getUserData();

                    if (userData != null && ((int) userData) == childId) {
                        indexToRemove = i;
                        break;
                    }
                }

                if (indexToRemove != -1) {

                    // Remove a single node
                    SharedNodeList.removeNode(SharedNodeList.getNodeList().remove(indexToRemove));

                    // Clear existing nodes in questBox
                    SharedNodeList.getQuestBox().getChildren().clear();

                    // Add all nodes from nodeList to SharedNodeList.getQuestBox()
                    for (Node n : SharedNodeList.getNodeList()) {
                        SharedNodeList.getQuestBox().getChildren().add(n);
                    }

                    System.out.println("Removed node with ID: " + childId);
                    // System.out.println(SharedNodeList.getNodeSize());
                } else {
                    System.out.println("Node with ID " + childId + " not found.");
                }

            }

            // for (Node children : new ArrayList<>(questBox.getChildren())) {
            // tracking_children = children;
            // if (children != null) {
            // questBox.getChildren().remove(tracking_children);
            // break; // Assuming IDs are unique, exit loop after finding the node
            // }
            // }
        } else {
            System.out.println("the flowpane is null");
        }
    }

    public Node createNode(String nodeType, int childId) throws IOException {
        Node node;
        switch (nodeType) {
            case "Multiple-Choice":
            case "MCQ":
                node = connect("MCQ", childId);
                int id = generateID(questionCount, MCQ_TYPE);
                IDList.add(childId, id);
                ;
                System.out.println(IDList.size());
                ID.setID(id);
                return node;

            case "Likert-Scale":
                node = connect("LS", childId);
                int id2 = generateID(questionCount, LS_TYPE);
                IDList.add(childId, id2);
                ID.setID(id2);
                return node;

            case "Open-Ended":
                node = connect("OE", childId);
                int id3 = generateID(questionCount, OE_TYPE);
                IDList.add(childId, id3);
                ID.setID(id3);
                return node;

            default:
                System.out.println("Unsupported node type: " + nodeType);
                return null; // Return null for unsupported node types
        }
    }

    public void delete(int index) {
        List<Integer> IDList = ID.getlist();

        if (IDList.contains(index) || (!IDList.isEmpty() && index >= 0 && index < IDList.size())) {
            // Remove the ID from the list
            IDList.remove(Integer.valueOf(index));
            renumberSubsequentQuestions(index);

            // Read the existing JSON data from the file
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File file = new File(filepath);

            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
                    JsonElement root = gson.fromJson(br, JsonElement.class);

                    if (root != null && root.isJsonObject()) {
                        JsonObject rootObject = root.getAsJsonObject();

                        // Remove the question from the JSON structure
                        JsonArray questionsArray = rootObject.getAsJsonArray("questions");
                        JsonArray newQuestionsArray = new JsonArray();

                        // Find the index of the question to remove
                        for (int i = 0; i < questionsArray.size(); i++) {
                            JsonObject questionObject = questionsArray.get(i).getAsJsonObject();
                            int questionId = questionObject.getAsJsonPrimitive("questionId").getAsInt();

                            if (questionId != index) {
                                // Add questions to the new array, excluding the one to remove
                                newQuestionsArray.add(questionObject);
                            }
                        }

                        // Replace the original array with the new one
                        rootObject.add("questions", newQuestionsArray);

                        // Write the updated JSON data back to the file
                        try (FileWriter writer = new FileWriter(filepath)) {
                            gson.toJson(rootObject, writer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Question with ID " + index + " deleted from JSON.");
                    } else {
                        System.out.println("JSON file is empty or not in a valid format.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("JSON file does not exist.");
            }

            ID.setList(IDList);
        } else {
            System.out.println("Question with ID " + index + " not found in the list.");
        }
    }

    // private void reselection(ActionEvent e) {
    // String choice = (String) comboBox.getValue();
    // try {
    // createNode(choice);
    // questAddBox.getChildren().add(node);
    // } catch (IOException e1) {
    // e1.printStackTrace();
    // }
    // }

    private Parent connect(String fxml, int childId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));

        // Set the controller factory to pass the nodeList
        if (fxml == "MCQ") {
            final int childIdHolder = childId;
            loader.setControllerFactory(param -> {
                MCQ_controller controller = new MCQ_controller();
                controller.setChildIdController(childIdHolder);
                controller.setQuestBox(SharedNodeList.getQuestBox());
                return controller;
            });
        } else if (fxml == "OE") {
            final int childIdHolder = childId;
            loader.setControllerFactory(param -> {
                OE_controller controller = new OE_controller();
                controller.setChildIdController(childIdHolder);
                controller.setQuestBox(SharedNodeList.getQuestBox());
                return controller;
            });
        } else if (fxml == "LS") {
            final int childIdHolder = childId;
            loader.setControllerFactory(param -> {
                LS_controller controller = new LS_controller();
                controller.setChildIdController(childIdHolder);
                controller.setQuestBox(SharedNodeList.getQuestBox());
                return controller;
            });
        }
        return loader.load();
    }

    private QuestionPage getController(Parent parent) {
        Object controller = parent.getProperties().get("controller");
        if (controller instanceof QuestionPage) {
            return (QuestionPage) controller;
        }
        return null;
    }

    @FXML
    private void close(ActionEvent e) {
        System.exit(0);
    }

    public class ID {

        private static int id;
        private static String surveyID;
        public static int totalQ;
        public static List<Integer> IdList = new ArrayList<>();
        public static String title;
        public static String desc;
        public static String email = SignInController.retrive_data.getEmail();

        ID(String iD, String title, String desc) {
            QuestionPage.ID.surveyID = iD;
            QuestionPage.ID.title = title;
            QuestionPage.ID.desc = desc;
        }

        public static int getID() {
            return id;
        }

        public static void setID(int newID) {
            id = newID;
        }

        public static String getSurveyID() {
            return surveyID;
        }

        public static void setSurveyID(String id) {
            surveyID = id;
        }

        public static int getTotal() {
            return totalQ;
        }

        public static void setTotal(int id) {
            totalQ = id;
        }

        public static List<Integer> getlist() {
            return IdList;
        }

        public static void setList(List<Integer> newList) {
            QuestionPage.ID.IdList = new ArrayList<>(newList);
        }

        public static String getTitle() {
            return title;
        }

        public static void setTitle(String titile) {
            title = titile;
        }

        public static String getDesc() {
            return desc;
        }

        public static void setDesc(String dsc) {
            desc = dsc;
        }

        public static String getEmail() {
            return email;
        }
    }

}
//
// @FXML
// private void proceedToQuestion(MouseEvent e) throws IOException {
// questionBox.getChildren().remove(defaultBox);
// NewQuestionChoices.setVisible(true);
// }

// @FXML
// private void handleButtonClick(ActionEvent event) throws IOException {
// Button sourceButton = (Button) event.getSource();
// String buttonType = sourceButton.getText();

// Node children = createNode(buttonType);
// if (children != null) {
// questionList.add(children); // Add the node to the list
// questionBox.getChildren().add(children);
// tracking_children = children;

// if (children instanceof Parent) {
// Parent parent = (Parent) children;
// QuestionPage controller = getController(parent);
// // Access and interact with the controller after it's initialized
// } else {
// System.out.println("Node is not an instance of Parent");
// }
// }
// }

// @FXML
// public void deleteNodeByType(String nodeType) {
// if (questionBox != null) {
// for (Node children : new ArrayList<>(questionBox.getChildren())) {
// if (children instanceof FlowPane) {
// FlowPane questBox = (FlowPane) children;
// if (questBox.getUserData().equals(nodeType)) {
// int indexToRemove = questionBox.getChildren().indexOf(questBox); // Get the
// index
// questionBox.getChildren().remove(questBox);
// questionList.remove(indexToRemove);
// // Move other questions dynamically upward
// for (int i = indexToRemove; i < questionBox.getChildren().size(); i++) {
// Node node = questionBox.getChildren().get(i);
// if (node instanceof FlowPane) {
// FlowPane fp = (FlowPane) node;
// fp.setUserData(i); // Update the user data
// }
// }
// break; // Assuming IDs are unique, exit loop after finding the node
// }
// }
// }
// } else {
// System.out.println("the flowpane is null");
// }
// }

// public void deleteNodeByType(String nodeType, int indexToRemove) {
// if (questBox != null) {
// // Remove the node
// questBox.getChildren().remove(indexToRemove);

// // Move the subsequent nodes up
// for (int i = indexToRemove; i < questBox.getChildren().size(); i++) {
// Node node = questBox.getChildren().get(i);

// if (node instanceof Parent) {
// Parent parent = (Parent) node;
// QuestionPage controller = getController(parent);

// if (controller != null) {
// // Adjust the position of the node
// questBox.getChildren().remove(node);
// questBox.getChildren().add(i - 1, node);
// }
// }
// }

// // Update the tacking_children variable
// int newIndex = Math.max(0, indexToRemove - 1);
// tracking_children = questBox.getChildren().size() > 0 ?
// questBox.getChildren().get(newIndex) : null;
// } else {
// System.out.println("The FlowPane is null");
// }
// }

// Pass the reference to the ComboBox and the selection list to the child

// MCQ_controller mcq = new MCQ_controller();
// mcq.setComboBox(reselection);
// mcq.setSelectionList(selection);

// private void reformingBox(Node node) throws IOException {
// //getting the box from the demo.fxml for precise calling out
// Parent root = connect("demo");
// BorderPane borderPane = (BorderPane) root.lookup("#Box");

// //we letting a new vbox
// VBox vboxDefault = new VBox();

// //setting the margin
// vboxDefault.setPrefHeight(285);
// vboxDefault.setPrefWidth(103);

// //adding the node
// vboxDefault.getChildren().add(node);
// borderPane.setLeft(vboxDefault);

// //adding the children into it
// questAddBox.getChildren().add(borderPane);

// //getting the node to knowing the node
// addedNode = node;

// }

// }
// // a function for determine wherease the id have been used;
// private static boolean isSurveyIdExists(JsonArray jsonArray, String surveyId)
// {
// for (JsonElement element : jsonArray) {
// JsonObject surveyObject = element.getAsJsonObject();
// String existingSurveyId = surveyObject.get("surveyID").getAsString();
// if (existingSurveyId.equals(surveyId)) {
// return true;
// }
// }
// return false;
// }

// // a function on bringing all the details to the survey
// private void putItIn(JsonArray jsonArray, JsonObject data) {
// for (JsonElement element : jsonArray) {
// // adding all the info on the basic sides of the survey
// JsonObject surveyInfo = new JsonObject();
// surveyInfo.addProperty("title", ID.getTitle());
// surveyInfo.addProperty("description", ID.getDesc());
// surveyInfo.addProperty("created_date", setCreatedDate());
// surveyInfo.addProperty("email", ID.getEmail());
// data.add("surveyInfo", surveyInfo);
// }
// }

// // generate a new id
// private static String generateNewSurveyId(JsonArray jsonArray, String
// existingSurveyId) {
// Set<String> existingIds = new HashSet<>();
// for (JsonElement element : jsonArray) {
// JsonObject surveyObject = element.getAsJsonObject();
// existingIds.add(surveyObject.get("surveyID").getAsString());
// }

// // Generate a new survey ID by adding one until it's unique
// String newSurveyId = existingSurveyId;
// int counter = 1;
// while (existingIds.contains(newSurveyId)) {
// newSurveyId = existingSurveyId + counter;
// counter++;
// }

// return newSurveyId;
// }

// private static void saveJsonArrayToFile(String filePath, JsonArray jsonArray)
// throws IOException {
// try (FileWriter fileWriter = new FileWriter(filePath)) {
// fileWriter.write(jsonArray.toString());
// }
// }
