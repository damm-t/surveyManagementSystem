import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class user_survey {

    @FXML
    private Button close;

    @FXML
    private Label gmail;

    @FXML
    private Button logOut;

    @FXML
    private ImageView pica;

    @FXML
    private Pane profileCard;

    @FXML
    private AnchorPane profilePic;

    @FXML
    private Button scdBtn;

    @FXML
    private TableColumn<?, ?> tbvDatePosted;

    @FXML
    private TableColumn<?, ?> tbvTitle;

    @FXML
    private Label user;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

}
