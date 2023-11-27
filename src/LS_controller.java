import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LS_controller extends QuestionPage {

    @FXML
    private AnchorPane paneForQuestion;

    @FXML
    private HBox radioButtonBox;

    @FXML
    private Button deleteQ;

    @FXML
    private ComboBox<String> rangeCB;
    ObservableList<String> range = FXCollections.observableArrayList("1-3", "1-4", "1-5");

    @FXML
    private VBox vbox1;

    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private FlowPane questBox;

    public void setQuestBox(FlowPane questBox) {
        this.questBox = questBox;
    }

    private int childIdController;

    public void setChildIdController(int childId) {
        this.childIdController = childId;
    }

    public int getChildIdController() {
        return this.childIdController;
    }

    @FXML
    private void selectRange(ActionEvent e) {

        String selectedRange = (String) rangeCB.getValue();

        if (selectedRange != null) {
            int start = Integer.parseInt(selectedRange.split("-")[0]);
            int end = Integer.parseInt(selectedRange.split("-")[1]);
            updateRadioButtons(start, end);
            rangeCB.setVisible(true);
            radioButtonBox.setVisible(true);
        }
    }

    private void updateRadioButtons(int start, int end) {
        // clear the boxes
        radioButtonBox.getChildren().clear();
        // algorithm to adding the part of different
        for (int i = start; i <= end; i++) {
            radioButtonBox.setSpacing(10);

            RadioButton radioButton = new RadioButton(String.valueOf(i));
            radioButton.setToggleGroup(toggleGroup);

            radioButtonBox.getChildren().add(radioButton);
        }

    }

    
    @FXML
    public void delete(ActionEvent event) throws IOException {
        deleteNodeByType(childIdController);
        System.out.println(SharedNodeList.getNodeSize());
        System.out.println("This is child Id: " + this.childIdController);
        int id = this.childIdController;
        delete(id);
    }
    // @FXML
    // public void delete(ActionEvent event) throws IOException {
    //     if (questBox != null) {
    //         int indexToRemove = questBox.getChildren().indexOf(paneForQuestion); // Adjust accordingly
    //         deleteNodeByType("Likert-Scale", indexToRemove); // Call the modified method
    //     } else {
    //         System.out.println(questBox);
    //     }
    // }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rangeCB.getItems().addAll(range);
    }

    public static String Question;

    public static void setQ(String Q) {
        Question = Q;
    }

    public static String getQ() {
        return Question;
    }

    // @FXML
    // private void select(ActionEvent e) throws IOException{
    // reselections();
    // }
}