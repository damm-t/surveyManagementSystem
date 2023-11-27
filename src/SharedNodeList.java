import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public class SharedNodeList {
    private static List<Node> nodeList = new ArrayList<>();
    private static int childId = 0;
    private static FlowPane questBox;

    public static void setQuestBox(FlowPane questBox){
        SharedNodeList.questBox = questBox;
    }

    public static FlowPane getQuestBox(){
        return SharedNodeList.questBox;
    }

    public static List<Node> getNodeList() {
        return nodeList;
    }

    public static void addNode(Node node) {
        nodeList.add(node);
    }

    public static void removeNode(Node node) {
        nodeList.remove(node);
    }

    public static int getNodeSize(){
        return nodeList.size();
    }

    public static int generateUniqueChildId() {
        return childId++;
    }
    
}