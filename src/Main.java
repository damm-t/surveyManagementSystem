import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double x = 0;
    private double y = 0;
    static Scene scene;


    //setting on the stage texture
    @Override
    public void start(Stage stage) throws Exception{
        
        stage.setTitle("Survey Management");
        scene = new Scene(loadFXML("SignIn"));
        stage.setScene(scene);
        
        scene.getRoot().setOnMousePressed((MouseEvent event) ->{
            x = event.getSceneX();
            y = event.getSceneY();
        });
        
        scene.getRoot().setOnMouseDragged((MouseEvent event) ->{
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

            stage.setOpacity(.8);
        });
        
        scene.getRoot().setOnMouseReleased((MouseEvent event) ->{
            stage.setOpacity(1);
        });
        
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    //this is for the switching purpose
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    //running code
    public static void main(String[] args) {
        launch(args);
    }
}

