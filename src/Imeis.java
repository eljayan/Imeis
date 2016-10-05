import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by R00715649 on 27-Sep-16.
 * This program will categorize the imes in the competitors database
 */
public class Imeis {
    public static void main(String[] args) {
        //String fileName = args[0];
        String fileName = "D:\\myScripts\\Imeis\\test.xls";

        //find filetype
        FileClassifier fileClassifier = new FileClassifier();
        String fileType = fileClassifier.classify(fileName);


        //for each registry
            //read the description field and pass it to a calssifier function

            //update the category field with the result

        //end.
    }
}

class FileClassifier{
    String classify(String fname){
        Pattern xlsx = Pattern.compile("xls[xm]\\b", Pattern.CASE_INSENSITIVE);
        Pattern xls = Pattern.compile("xls\\b", Pattern.CASE_INSENSITIVE);

        Map<String, Pattern> patternDict = new HashMap<>();
        patternDict.put("xlsx", xlsx);
        patternDict.put("xls", xls);

        for (String k:patternDict.keySet()){
            Matcher matcher = patternDict.get(k).matcher(fname);
            if (matcher.find()){
                return k;
            }
        }
        return "none";
    }
}

