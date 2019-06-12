package Extractor;

import NYTimes.Contract;
import NYTimes.Docs;
import NYTimes.JsonOperations;
import NYTimes.Response;

import java.util.ArrayList;
import java.util.List;

public class URLExtractor {

    public List<String> getUrls(String  jsonFilePath){
        List<String> urls = new ArrayList<String>();
        JsonOperations jsonOperations = new JsonOperations();
        List<Contract> data = jsonOperations.readFromFile(jsonFilePath);
        if(data != null && data.size() > 0){
            for (Contract item : data) {
                Response response = item.getResponse();
                if(response != null && response.docs != null && response.docs.size() > 0){
                    for (Docs doc :response.docs ) {
                        if(doc.getWeb_url().contains("2019")){
                            urls.add(doc.getWeb_url() + "\n");
                        }
                    }
                }
            }
        }
        return urls;
    }
}
