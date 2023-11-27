import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    public static int Qid = 100;
    public static int deletedID;
    public static int[] IDArr = new int[ID.totalQ];

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

    
    public static void removeTheID() {
        for (int i = 0; i < IDArr.length; i++) {
            if (IDArr[i] > deletedID) {
                IDArr[i] -= 100;
            } else if (IDArr[i] == deletedID) {
                removeTheElement(IDArr, i);
            } else
                return;
        }
    }

    public static int[] removeTheElement(int[] arr, int index) {
        if (arr == null || index < 0 || index >= arr.length) {
            return arr;
        }

        int[] anotherArray = new int[arr.length - 1];

        for (int i = 0, k = 0; i < arr.length; i++) {

            if (i == index) {
                continue;
            }

            anotherArray[k++] = arr[i];
        }

        return anotherArray;
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
    }

    @FXML
    private void AddNew(ActionEvent e) {
        System.out.println(IDArr.length);
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
        int Q = ID.totalQ;
        int oldId = Qid;
        int d = Qid % 100;
        Qid = Qid + 100 - d;
        for (int i = Q; i > Q - 1; i--) {
            Q += 1;
            IDArr[i] = oldId;
            System.out.println(IDArr[i]);
        }

    }

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

                    if (root != null && root.isJsonObject()) {
                        JsonObject rootObject = root.getAsJsonObject();

                        // Update surveyInfo if needed
                        JsonObject surveyInfo = rootObject.getAsJsonObject("surveyInfo");
                        if (surveyInfo == null) {
                            // If "surveyInfo" is not present, create a new JsonObject for it
                            surveyInfo = new JsonObject();
                            rootObject.add("surveyInfo", surveyInfo);
                        }
                        surveyInfo.addProperty("title", ID.getTitle());
                        surveyInfo.addProperty("description", ID.getDesc());

                        // Add or update questions
                        JsonArray questionsArray = rootObject.getAsJsonArray("questions");
                        for (int i = 0; i < ID.getlist().size(); i++) {
                            int n = ID.getlist().get(i);
                            JsonObject questionDetails = determine(n);

                            // Add or update question based on questionId
                            boolean found = false;
                            for (JsonElement element : questionsArray) {
                                JsonObject existingQuestion = element.getAsJsonObject();
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
                        result = rootObject; // Assign the found JsonObject to result
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

        // Create surveyInfo
        JsonObject surveyInfo = new JsonObject();
        surveyInfo.addProperty("title", ID.getTitle());
        surveyInfo.addProperty("description", ID.getDesc());

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
        newJson.add("surveyInfo", surveyInfo);
        newJson.add("questions", questionsArray);

        return newJson;
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
                    data.addProperty("Option" + entry, smcqId);
                    data.addProperty("OptionTitle", optionTitle);
                }
                return data;

            case 2:
                // Open-ended Question
                data.addProperty("type", "OE");
                data.addProperty("questionId", q);
                // creating a new question title by static method
                String questionTitle = OE_controller.getOEData.getQ();
                data.addProperty("Question Title", questionTitle);
                return data;

            case 3:
                // Likert-Scale Question
                data.addProperty("type", "LS");
                data.addProperty("questionId", q);
                data.addProperty("Question Title", LS_controller.getLSData.getQ());
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

    private Node createNode(String nodeType, int childId) throws IOException {
        Node node;
        switch (nodeType) {
            case "Multiple-Choice":
            case "MCQ":
                node = connect("MCQ", childId);
                Qid += 1;
                return node;
            case "Likert-Scale":
                node = connect("LS", childId);
                Qid += 2;
                return node;
            case "Open-Ended":
                node = connect("OE", childId);
                Qid += 3;
                return node;
            default:
                System.out.println("Unsupported node type: " + nodeType);
                return null; // Return null for unsupported node types
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
        public static int totalQ;
        public static List<Integer> IdList = new ArrayList<>();
        public static String title;
        public static String desc;

        ID(String title, String desc) {
            QuestionPage.ID.title = title;
            QuestionPage.ID.desc = desc;
        }

        public static int getID() {
            return id;
        }

        public static void setID(int newID) {
            id = newID;
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