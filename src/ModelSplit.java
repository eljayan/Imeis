import java.util.HashMap;
import java.util.Map;

/**
 * Created by R00715649 on 14-Oct-16.
 */
public class ModelSplit {
    Map<String, Map<String, String>> modelsDict = new HashMap<String, Map<String, String>>();

    Map<String, Map<String, String>> split(Map<String, String> dict){

        for (String k: dict.keySet()){

            String modelKey = k.substring(0,10);

            //if the model is not in the dictionary
            if (!modelsDict.containsKey(modelKey) || modelsDict.isEmpty()){
                //create a temporal dictionary with the item
                Map<String, String > tempDict = new HashMap<String, String>();

                tempDict.put(k, dict.get(k));

                //ad the temporal dictionary to de models dictionary
                modelsDict.put(modelKey, tempDict);

            }else {
                //get the dict associated to the model key
                Map<String, String>tempDict = modelsDict.get(modelKey);

                //add key and value to the dict
                tempDict.put(k, dict.get(k));

                //put it back to the models dictionary
                modelsDict.put(modelKey, tempDict);
            }

        }

        return this.modelsDict;
    }

}

//class Test{
//    public static void main(String[] args) {
//        Map<String, String>dictionary = new HashMap<String, String>();
//        dictionary.put("861956030014998", "UGM7N16903000247");
//        dictionary.put("861956030017678", "UGM7N16903000515");
//        dictionary.put("861956030025874", "UGM7N16903000516");
//
//        ModelSplit modelSplit = new ModelSplit();
//
//        Map<String, Map<String, String>>splitResults = modelSplit.split(dictionary);
//
//        System.out.println(splitResults);
//    }
//}
