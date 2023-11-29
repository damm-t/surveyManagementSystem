import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class survey_data {

    final private String filepath = "DB/survey.json";
    int amount_of_mcq = 0;
    int amount_of_oe = 0;
    int amount_of_ls = 0;

    public void determine(String type, JsonObject details) {
        JsonArray questionArray = details.getAsJsonArray("questionArray");
        int questionNumber = 0; // Variable to keep track of the question number

        switch (type) {
            case "MCQ":
                for (JsonElement element : questionArray) {
                    JsonObject optionObject = element.getAsJsonObject();
                    int option = optionObject.get("Option").getAsInt();
                    String optionTitle = optionObject.get("OptionTitle").getAsString();
                    MCQdata.addOption(option, optionTitle);
                    amount_of_mcq++;
                    questionNumber++;
                    System.out.println("Question " + questionNumber + ": " + optionTitle);
                }
                break;

            case "OE":
                for (JsonElement element : questionArray) {
                    JsonObject optionObject = element.getAsJsonObject();
                    String OETitle = optionObject.get("QuestionTitle").getAsString();
                    amount_of_oe++;
                    questionNumber++;
                    System.out.println("Question " + questionNumber + ": " + OETitle);
                }
                break;

            case "LS":
                for (JsonElement element : questionArray) {
                    JsonObject optionObject = element.getAsJsonObject();
                    String OETitle = optionObject.get("QuestionTitle").getAsString();
                    amount_of_ls++;
                    questionNumber++;
                    System.out.println("Question " + questionNumber + ": " + OETitle);
                }
                break;

            default:
                break;
        }
    }

    public class MCQdata {
    
        public static Map<Integer, String> smallMCQOptions = new HashMap<>();

        public static void addOption(int option, String optionTitle) {
            smallMCQOptions.put(option, optionTitle);
        }

        public static Map<Integer, String> getOptions() {
            return new HashMap<>(smallMCQOptions);
        }
    }
    private String surveyTitle;
    private String surveyDescription;
    private String surveyDate;

    // Getter methods
    public String getSurveyTitle() {
        return surveyTitle;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public String getSurveyDate() {
        return surveyDate;
    }

    // Setter methods
    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }

    public void setSurveyDate(String surveyDate) {
        this.surveyDate = surveyDate;
    }

}