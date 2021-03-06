
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

                String [] valuesInCell = cellValue.split(" ");

                for (String value:valuesInCell){
                    //create IMEI object
                    Imei imei = new Imei(value);
                    if (imei.check()){
                        imeiDict.put(imei.imeiNumber, imei.imeiNumber);
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

                String [] valuesInCell = cellValue.split(" ");

                for (String value:valuesInCell){
                    //create IMEI object
                    Imei imei = new Imei(value);
                    if (imei.check()){
                        imeiDict.put(imei.imeiNumber, imei.imeiNumber);
                    }
                }

            }
        }
        return imeiDict;
    }

}

class Imei{

    String imeiNumber;

    public Imei(String imei){
        imeiNumber = imei ;
    }

    public Boolean check() {
        Pattern imeiPattern = Pattern.compile("\\b\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\b");
        Matcher matcher = imeiPattern.matcher(imeiNumber);
        if (matcher.find()){
            String imeiToCheck = matcher.group();
            return luhn(imeiToCheck);
        }else{
            return false;
        }
    }

    List<String> extract(){
        List<String>imeiList = new ArrayList<String>();
        Pattern imeiPattern = Pattern.compile("\\b\\d{15}\\b");
        Matcher matcher = imeiPattern.matcher(imeiNumber);
        while (matcher.find()){
            imeiList.add(matcher.group());
        }
        return imeiList;
    }
    boolean luhn(String imeiNumber){
        imeiNumber = this.imeiNumber;
        int[]digitsList = new int[14];
        int[]sumDigits = new int[14];

        for (int i = 0; i< 14; i++){
            digitsList[i] = Integer.parseInt(String.valueOf(imeiNumber.charAt(i)));
        }

        for (int i = 0; i < 14; i++){
            if (i ==0 ){
                sumDigits[i] = digitsList[i];
            }else if (i % 2 == 0){
                sumDigits[i] = digitsList [i];
            }else {
                int digit = digitsList[i]*2;
                if (digit <=9){
                    sumDigits[i] = digit;
                }else{
                    sumDigits[i] = digit-9;
                }
            }
        }

        //sum the list
        int digitsSum=0;
        for (int d:sumDigits){
            digitsSum+=d;
        }

        //multyply the sum
        int multyply;

        multyply = digitsSum * 9;

        //get the las number
        String multiplyResult = Integer.toString(multyply);

        char checkSumDigit = multiplyResult.charAt(multiplyResult.length()-1);
        char imeiCheckDigit = imeiNumber.charAt(imeiNumber.length()-1);

        return checkSumDigit == imeiCheckDigit;
    }
}

class test{
    public static void main(String[] args) {
        String s = "860483030201203  860483030202219 86048303020221985 PNTBBBB630900014";
        //String s = "PNTBBBB630900014 860483030201203";

        Imei imei = new Imei("021611070000312");
        System.out.println(imei.check());
    }
}