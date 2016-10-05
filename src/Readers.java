/**
 * Created by R00715649 on 04-Oct-16.
 * The read()
 */
public class Reader {
    String type;

    Reader(String s){
        if (s.equals("xlsx")){
            type = "xls";
        }else if(s.equals("xls")){
            type = "xls";
        }else{
            type = "none";
        }
    }

}

class XLSXReader{


}

class XLSReader{


}