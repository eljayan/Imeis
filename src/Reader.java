
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by R00715649 on 04-Oct-16.
 * The read()
 */
public class Reader {

    String fileType;
    String fileName;

    Reader(String ftype, String fname){
        if (ftype.equals("xlsx")){
            fileType = "xlsx";
        }else if(ftype.equals("xls")){
            fileType = "xls";
        }else{
            fileType = "none";
        }
        fileName = fname;
    }

    Map<String, String> read () throws Exception{
        //this is the function that returns the imei dicts
        Map<String, String> imeiDictionary = new HashMap<>();

        if (fileType.equals("xlsx")){
            XLSXReader reader = new XLSXReader();
            imeiDictionary = reader.read(fileName);

        }else if (fileType.equals("xls")){
            XLSReader reader = new XLSReader();
            imeiDictionary =  reader.read(fileName);
        }
        return imeiDictionary;
    }
}
class XLSXReader{

    Map<String, String>read(String fname) throws Exception{

        Map<String, String>imeiDict = new HashMap<>();

        FileInputStream f = new FileInputStream(new File(fname));

        XSSFWorkbook workbook = new XSSFWorkbook(f);

        //the excel file componen that holds all the strings in the workbook
        SharedStringsTable sharedStringsTable = workbook.getSharedStringSource();

        Integer sheetIndex = workbook.getActiveSheetIndex();

        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

        Iterator<Row> rowIterator = sheet.rowIterator();


        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            String cellValue;
            CTRst stringTable;

            Iterator<Cell>cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                //convert the cell to an xssf cell in order to use the getRawValueMethod
                XSSFCell xcell = (XSSFCell) cell;

                //get the string value from a cell
                //0 is integer, 1 is string, 3 is blank cell
                if (xcell.getCellType() == 0) {
                    //from integer cells i can get the string value directly
                    cellValue = xcell.getRawValue();
                }else if (xcell.getCellType() == 3){
                    continue;
                }else {
                    //from string cells it has to lookup the value in teh stringTable
                    //or get the cel value directly

                    String index = xcell.getRawValue();

                    if (index != null){
                        int sharedStringTableIndex = Integer.parseInt(index);
                        stringTable = sharedStringsTable.getEntryAt(sharedStringTableIndex);
                        cellValue = stringTable.getT();
                    }else{
                        cellValue = xcell.getStringCellValue();
                    }

                }


                //if it is an IMEI number
                if (Imei.check(cellValue)) {
                    List<String> imeiValues = Imei.extract(cellValue);
                    //agrega al diccionario
                    for (String imei : imeiValues) {
                        imeiDict.put(imei, imei);
                    }


                }
            }
        }
    return imeiDict;
    }

}

class XLSReader{
    Map<String, String>read(String fname) throws Exception{
        Map<String, String>imeiDict = new HashMap<>();

        DataFormatter dataFormatter = new DataFormatter();

        FileInputStream f = new FileInputStream(new File(fname));

        HSSFWorkbook workbook = new HSSFWorkbook(f);
        Integer sheetIndex = workbook.getActiveSheetIndex();
        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            Iterator<Cell>cellIterator = row.cellIterator();
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();

                String cellValue = dataFormatter.formatCellValue(cell).trim();

                //if it is an IMEI number
                if (Imei.check(cellValue)){
                    List<String> imeiValues = Imei.extract(cellValue);
                    //agrega al diccionario
                    for (String imei:imeiValues){

                        imeiDict.put(imei, imei);
                    }

                }

            }
        }
        return imeiDict;
    }

}

class Imei{

    static Boolean check(String i) {
        Pattern imeiPattern = Pattern.compile("\\b\\d{15}\\b");
        Matcher matcher = imeiPattern.matcher(i);
        return matcher.find();
    }

    static List<String> extract(String i){
        List<String>imeiList = new ArrayList<String>();
        Pattern imeiPattern = Pattern.compile("\\b\\d{15}\\b");
        Matcher matcher = imeiPattern.matcher(i);
        while (matcher.find()){
            imeiList.add(matcher.group());
        }
        return imeiList;
    }
    boolean luhn(String i){
        return true;
    }
}

//class test{
//    public static void main(String[] args) {
//        String s = "860483030201203  860483030202219 86048303020221985 PNTBBBB630900014";
//        //String s = "PNTBBBB630900014 860483030201203";
//        System.out.println("String: " + s);
//        System.out.println("Imei: " + Imei.check(s));
//        System.out.println("Number: " + Imei.extract(s));
//    }
//}