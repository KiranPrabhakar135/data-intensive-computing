package CommonCrawl;

import NYTimes.Contract;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

class IndexResponse{
    String filename;
}

public class URLReader {

    public void getWetUrls(String endpoint,String domain) throws IOException, InterruptedException{
        Gson gson = new Gson();
        String indexUrlTemplate = "https://index.commoncrawl.org/%1$s?url=*.%2$s.com&output=json&filter==status:200&fl=filename";
        String urlString = String.format(indexUrlTemplate, endpoint, domain);
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        FileWriter writer = new FileWriter("commonCrawlUrlMarch_"+ domain +".txt", true);

        while((line = bufferedReader.readLine()) != null){
            IndexResponse pojo = gson.fromJson(line, IndexResponse.class);
            String file = pojo.filename.replaceAll("warc/CC", "wat/CC");
            file = file.replaceAll("warc.", "warc.wat.");
            if(file.contains("wat/CC")){
                writer.write(file + "\n");
            }
        }
        writer.close();
    }

}
