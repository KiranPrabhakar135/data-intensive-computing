package NYTimes;

import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Query {
    public String apiKey = "8gUXkqA86GL0nALy06XhjxRctYG52tUw";
    public String urlTemplate = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=%1$s&fq=news_desk:(%2$s)&glocations:(%3$s)&page=%4$s&api-key=%5$s";
    public String multiQueryUrlTemplate = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=%1$s&begin_date=20190101&end_date=20190405&&page=%2$s&api-key=%3$s";
    public String urlTemplateWithDate = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=%1$s&begin_date=20190101&end_date=20190405&&page=%2$s&api-key=%3$s&fq=section_name.contains:(\"Technology\" \"Science\" \"Education\" \"Business\" \"Job Market\" \"opinion\" \"NYRegion\" \" N.Y.Region\" \"Front Page\")";

    public String getResults(String q, int page)throws IOException{
        String urlString = String.format(urlTemplateWithDate, q, page, apiKey);
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        while((line = bufferedReader.readLine()) != null){
            stringBuffer.append(line);
        }
        bufferedReader.close();
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }
    public String getResults(String newsDesk, String subtopic, String location, int page) throws IOException {
        String urlString = String.format(urlTemplate, subtopic, newsDesk, location, page, apiKey);
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        while((line = bufferedReader.readLine()) != null){
            stringBuffer.append(line);
        }
        bufferedReader.close();
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }

    public String getResults(List<String> newsDesks, String subtopic, List<String> locations, int page) throws IOException {
        StringBuilder newsDeskSb = new StringBuilder();
        for (String s : newsDesks) {
            newsDeskSb.append("\"");
            newsDeskSb.append(s);
            newsDeskSb.append("\" ");
        }
        String newsDesk = newsDeskSb.toString();
        StringBuilder locationSb = new StringBuilder();
        for (String s : locations) {
            locationSb.append("\"");
            locationSb.append(s);
            locationSb.append("\" ");
        }
        String location = newsDeskSb.toString();

        String urlString = String.format(multiQueryUrlTemplate, subtopic, newsDesk, location, page, apiKey);
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        while((line = bufferedReader.readLine()) != null){
            stringBuffer.append(line);
        }
        bufferedReader.close();
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }


}
