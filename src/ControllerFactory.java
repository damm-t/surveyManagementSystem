import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class ControllerFactory {

    public static <T> T getController(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ControllerFactory.class.getResource(fxmlPath));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader.getController();
    }

    public static <T> T getController(URL fxmlUrl) {
        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader.getController();
    }

    public static <T> T getController(String fxmlPath, Node root) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ControllerFactory.class.getResource(fxmlPath));
        loader.setRoot(root);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader.getController();
    }
}