package NYTimes;

import Extractor.URLExtractor;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class NYTimes {
    public static void main(String[] args) throws IOException,InterruptedException {
        //queryData();
        writeUrlsToFile("NYTimes" + "//" + "Tech Companies" + "//");

        //getAllStopwords();
    }

    private static void getAllStopwords() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("stopwords.txt")));
        StringBuilder builder = new StringBuilder();
        for(String line; (line = br.readLine()) != null; ) {
            if(line !=  ""){
                builder.append("\"" + line + "\", ");
            }
        }
        String test = builder.toString();
    }

    private static void writeUrlsToFile(String input) throws IOException{
        File file = new File(input);
        File[] files = file.listFiles();

        URLExtractor urlExtractor = new URLExtractor();
        if(files != null){
            for (File item: files) {
                String filePath ="";
                if(item.isDirectory() && item.listFiles() != null){
                    File f = item.listFiles()[0];
                    filePath = f.getPath();
                }
                else{
                    filePath = item.getPath();
                }
                if(filePath.contains(".json")){
                    List<String> urls = new ArrayList<String>();
                    urls.addAll(urlExtractor.getUrls(filePath));
                    FileWriter writer = new FileWriter(item.getPath()  +"//url.txt");
                    for(String url : urls) {
                        writer.write(url);
                    }
                    writer.close();
                }

            }

        }

    }




    private static void queryData() throws IOException, InterruptedException {
        //List<String> topics = Arrays.asList("microsoft", "amazon", "google", "facebook", "Apple");

        String[] topics = new String[]{"amazon", "google", "facebook", "Apple"};

        Query query = new Query();
        JsonOperations jsonOperations = new JsonOperations();
        for (String topic: topics) {

            List<Contract> data = new ArrayList<Contract>();
            int i=1;
            String response = query.getResults(topic,i);
            Contract res = jsonOperations.convertToObject(response);
            int hits = res.response.meta.getHits();
            System.out.println("The current Topic is: " + topic + "The hits is: " + hits);
            data.add(res);
            while (i*10 < hits){
                try{
                    i++;
                    response = query.getResults(topic,i);
                    res = jsonOperations.convertToObject(response);
                    data.add(res);
                    Thread.sleep(6200);
                    System.out.println("The page number is: " + i);
                }
                catch (Exception e){
                    e.printStackTrace();
                    break;
                }
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String file = dateFormat.format(date);
            String filePath = "NYTimes" + "//" + "Tech Companies" + "//" + topic + "//" + file + ".json";
            jsonOperations.writeToFile(data, filePath);
            List<Contract> storedData = jsonOperations.readFromFile(filePath);
            System.out.println(topic + "**" + storedData.size());
        }
    }

    private static void querySpecificTopics(Query query, List<Contract> responses, JsonOperations jsonOperations) throws IOException {
        String topic="Technology";
        String subTopic = "Cricket";
        String location = "California";
        for(int i= 0; i < 1; i ++){
            String response = query.getResults(topic, subTopic, location,i);

            Contract res = jsonOperations.convertToObject(response);
            responses.add(res);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String file = dateFormat.format(date);
        String filePath = topic + "//" + subTopic + "//" + file + ".json";
        jsonOperations.writeToFile(responses, filePath);
        //List<Contract> data = jsonOperations.readFromFile(filePath);
    }

    public String getFormattedList(List<String> list) {
        StringBuilder itemsSb = new StringBuilder();
        for (String s : list) {
            itemsSb.append("\"");
            itemsSb.append(s);
            itemsSb.append("\" ");
        }
        return itemsSb.toString();
    }
}
