
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MCQ_controller extends QuestionPage {

    @FXML
    private Button addBtn;

    @FXML
    private Button delBtn;

    @FXML
    private HBox hbox1;

    @FXML
    private Label limitLabel;

    @FXML
    private AnchorPane paneForQuestion;

    @FXML
    private TextField questionTF;

    @FXML
    private RadioButton radioBtn;

    @FXML
    private VBox vbox1;

    // @FXML
    // private ComboBox<String> reselection;
    // private ObservableList<String> selection;

    // // Set the ComboBox reference
    // public void setComboBox(ComboBox<String> comboBox) {
    // this.reselection = comboBox;
    // }

    // // Set the selection list
    // public void setSelectionList(ObservableList<String> selectionList) {
    // this.selection = selectionList;
    // }

    private int childIdController;

    @FXML
    private FlowPane questBox;

    public void setQuestBox(FlowPane questBox) {
        this.questBox = questBox;
    }

    public void setChildIdController(int childId) {
        this.childIdController = childId;
    }

    public int getChildIdController() {
        return this.childIdController;
    }

    @FXML
    private void AddMore(ActionEvent e) {

        vbox1.getChildren().add(children_orphan());
        // if (vbox1.getChildren().size() == 10) {
        // addBtn.setDisable(true);
        // limitLabel.setText("Reach Options Limit!!");
        // }
    }

    // calling out the hbox to elimate the err of duplicate
    private Node children_orphan() {
        // setting up the initialize of the children
        RadioButton opButton2 = new RadioButton();
        TextField opTxt2 = new TextField();
        Button del2 = new Button("Delete");
        opButton2.setDisable(true);

        HBox ophbox2 = new HBox();
        ophbox2.prefWidth(200);
        ophbox2.prefHeight(100);
        opTxt2.prefWidth(141);
        opTxt2.prefHeight(26);
        del2.setBackground(new Background(new BackgroundFill(Color.web("ffdba9"), new CornerRadii(5), Insets.EMPTY)));

        HBox.setMargin(opButton2, new Insets(17.5, 5, 5, 5));
        HBox.setMargin(opTxt2, new Insets(15, 0, 0, 10));
        HBox.setMargin(del2, new Insets(15, 0, 0, 20));

        // setting the activity for the mouse pressed
        del2.setOnAction(Event -> {

            vbox1.getChildren().remove(ophbox2);
        });

        ophbox2.getChildren().addAll(opButton2, opTxt2, del2);

        return ophbox2;
    }

    @FXML
    private void DeleteA(ActionEvent e) {
        vbox1.getChildren().remove(hbox1);
        if (vbox1.getChildren().size() == 1) {
            delBtn.setDisable(true);
        } else if (vbox1.getChildren().size() >= 2) {
            delBtn.setDisable(false);
        }
    }

    @FXML
    public void delete(ActionEvent event) throws IOException {

        deleteNodeByType(childIdController);
        System.out.println(SharedNodeList.getNodeSize());
        System.out.println("This is child Id: " + this.childIdController);

    }

    // @FXML
    // public void delete(ActionEvent event) throws IOException {
    // if (questBox != null) {
    // int indexToRemove = questBox.getChildren().indexOf(paneForQuestion); //
    // Adjust accordingly
    // deleteNodeByType("MCQ", indexToRemove); // Call the modified method
    // } else {
    // System.out.println(questBox);
    // }
    // }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public static class getMCQData {

        public static Map<Integer, String> smallMCQOptions = new HashMap<>();
        private static int idCounter = 1; // Counter for generating IDs

        public static void addOption(String optionTitle) {
            smallMCQOptions.put(idCounter, optionTitle);
            idCounter++;
        }

        public static Map<Integer, String> getOptions() {
            return new HashMap<>(smallMCQOptions);
        }

    }

}