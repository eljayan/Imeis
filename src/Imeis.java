import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by R00715649 on 27-Sep-16.
 * This program will categorize the imes in the competitors database
 */
public class Imeis {
    public static void main(String[] args){
        try {
            String fileName = args[0];
            //String fileName = "D:\\myScripts\\Imeis\\test5.XLSX";
            String directory = new File(fileName).getParent();

            //find filetype
            FileClassifier fileClassifier = new FileClassifier();
            String fileType = fileClassifier.classify(fileName);

            Reader reader = new Reader(fileType, fileName);

            Map<String, String> imeis = reader.read();

            ModelSplit modelSplit = new ModelSplit();
            Map<String, Map<String, String>>splitModels = modelSplit.split(imeis);

            for (String k: splitModels.keySet()){

                Map<String,String>modelDict = splitModels.get(k);

                Writer.write(modelDict, directory, k);
            }


            System.out.println("Process completed.");
            Scanner message = new Scanner(System.in);
            message.nextLine();

            //end.
        }catch (Exception err){
            System.out.println("An error ocurred.");
            err.printStackTrace();
            System.out.println(err.getMessage());
            System.out.println(err.toString());
            Scanner message = new Scanner(System.in);
            message.nextLine();
        };

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

