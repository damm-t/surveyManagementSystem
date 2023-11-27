import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class OE_controller extends QuestionPage {

    @FXML
    private TextField answerTF;

    @FXML
    private AnchorPane paneForQuestion;

    @FXML
    private ChoiceBox<String> reselection;

    @FXML
    private FlowPane questBox;

    public void setQuestBox(FlowPane questBox){
        this.questBox = questBox;
    }

    private int childIdController;

    public void setChildIdController(int childId){
        this.childIdController = childId;
    }

    public int getChildIdController(){
        return this.childIdController;
    }

    @FXML
    public void delete(ActionEvent event) throws IOException {
        deleteNodeByType(childIdController);
        System.out.println(SharedNodeList.getNodeSize());
        System.out.println("This is child Id: " + this.childIdController);
        ID.totalQ -=1;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
    // @FXML
    // public void delete(ActionEvent event) throws IOException {
    //     if (questBox != null) {
    //         int indexToRemove = questBox.getChildren().indexOf(paneForQuestion); // Adjust accordingly
    //         deleteNodeByType("Open-Ended", indexToRemove); // Call the modified method
    //     } else {
    //         System.out.println(questBox);
    //     }
    // }
    // @FXML
    // public void delete(ActionEvent event) throws IOException {
    //     if (questBox != null) {
    //         int indexToRemove = questBox.getChildren().indexOf(paneForQuestion);
    //         deleteNodeByType("Open-Ended", indexToRemove);
    //     } else {
    //         System.out.println("questBox is null");
    //     }
    // }

    // @FXML
    // private void select(ActionEvent e) throws IOException {
    // reselections();
    // }

