import java.util.Map;

/**
 * Created by R00715649 on 04-Oct-16.
 * The read()
 */
public class Reader {
    String fileType;
    Reader(String s){
        if (s.equals("xlsx")){
            fileType = "xls";
        }else if(s.equals("xls")){
            fileType = "xls";
        }else{
            fileType = "none";
        }
    }

    Map<String, String> read (String fileName) throws Exception{
        //this is the function that returns the imei dicts
        if (fileType.equals("xlsx")){
            XLSXReader reader = new XLSXReader();

        }else if (fileType.equals("xls")){
            XLSReader reader = new XLSReader();
        }
    }

}

class XLSXReader{
    Map<String, String>read(String fname){
    
    }

}

class XLSReader{
    Map<String, String>read(String fname){

    }

}