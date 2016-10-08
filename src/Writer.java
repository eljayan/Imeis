import java.io.FileWriter;
import java.util.Map;

/**
 * Created by R00715649 on 07-Oct-16.
 * the method write() creates the csv file in the parent directory
 */
public class Writer {
    static void write(Map<String, String> dict, String saveDirectory)throws Exception{
        //open file
        FileWriter writeFile = new FileWriter(saveDirectory+"/imeis.csv");

        //start writing
        for (String k : dict.keySet()){
            writeFile.append(k + ","+ dict.get(k) + "\n");
        }

        //save in same folder
        writeFile.close();
    }
}
