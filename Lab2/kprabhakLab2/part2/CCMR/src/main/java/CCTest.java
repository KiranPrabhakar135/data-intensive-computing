import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.InMemoryRegionImpl;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CCTest {
    public static void main(String[] args) throws IOException, S3ServiceException, InterruptedException {
        //getCCUrls();
        String bucketName = "diclab2kprabhakshishirs";
        String accessKey = "AKIAIVINMTSHWI7TWGBQ";
        String secretKey = "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";
        String s3File = "nyta.txt";
        String content = "asdf1";

        putToS3Bucket(bucketName, accessKey, secretKey, s3File, content);


        //readCCDataFromS31("crawl-data/CC-MAIN-2019-13/segments/1552912202484.31/wat/CC-MAIN-20190321030925-20190321052925-00364.warc.wat.gz", 0);

        //processWetFiles();
    }

    private static void putToS3Bucket(String bucketName, String accessKey, String secret, String s3File, String content) throws IOException {
        AWSCredentials credentials = new BasicAWSCredentials( accessKey, secret);

        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();
        if(s3client.getBucketAcl(bucketName) == null) {
            System.out.println("Bucket name is not available."
                    + " Try again with a different Bucket name.");
            return;
        }
        if(!s3client.doesObjectExist(bucketName, s3File)){
            s3client.putObject(bucketName, s3File, "");
        }
        com.amazonaws.services.s3.model.S3Object s3object = s3client.getObject(bucketName, s3File);
        InputStream inputStream = s3object.getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while((line = reader.readLine()) != null){
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        stringBuilder.append(content);

        byte[] byteArray = stringBuilder.toString().getBytes("utf-8");
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(byteArray.length);
        s3client.putObject( bucketName, s3File,new ByteArrayInputStream(byteArray), metadata);
    }

    private static void processWetFiles() throws S3ServiceException, IOException {
        //crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153611-00020.warc.wet.gz
        //crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153613-00044.warc.wet.gz
        //crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153615-00002.warc.wet.gz
        //crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153617-00010.warc.wet.gz
        Document doc  = Jsoup.connect("http://americanrascals.us/files/how-to-download-microsoft-office-2018-all-software-with/").get();
        String[] urls= new String[]{
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153617-00012.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153618-00059.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153620-00027.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153620-00036.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153621-00023.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153622-00003.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153622-00009.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153622-00026.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153623-00057.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153624-00039.warc.wet.gz",
                "crawl-data/CC-MAIN-2019-13/segments/1552912201329.40/wet/CC-MAIN-20190318132220-20190318153625-00008.warc.wet.gz"
        };


        int i = 5;
        List<Integer> counts = Arrays.asList(0,0,0,0,0);
        for (String url:urls ) {
            i++;
            List<Integer> values = readCCDataFromS31(url, i);
            for (int j = 0; j < 5; j++) {
                counts.set(j, counts.get(j) + values.get(j));
            }
        }
        System.out.println("The final counts are: " + counts);
    }

//    private static void getCCUrls() throws IOException, InterruptedException {
//        String[] endpoints = new String[]{"CC-MAIN-2019-13-index", "CC-MAIN-2019-09-index", "CC-MAIN-2019-04-index"};
//        String[] subtopics = new String[]{"Tesla", "Nvidia", "Cisco", "adobe", "microsoft"};
//        URLReader urlReader = new URLReader();
//        for (String endpoint: endpoints) {
//            for (String subtopic: subtopics) {
//                urlReader.getWetUrls(endpoint, subtopic);
//            }
//        }
//    }
    private static void readCCDataFromS3(String ccUrl, String outputFile) throws S3ServiceException, IOException {
        S3Service restS3Service = new RestS3Service(null);
        S3Object file = restS3Service.getObject("commoncrawl", ccUrl, null, null, null, null, null, null);
        ArchiveReader archiveReader = WARCReaderFactory.get(ccUrl, file.getDataInputStream(), true);
        int i = 0;
        for(ArchiveRecord archiveRecord : archiveReader) {
            //System.out.println("URL: " + archiveRecord.getHeader().getUrl());
            byte[] rawData = new byte[archiveRecord.available()];
            archiveRecord.read(rawData);
            String text = new String(rawData);
            System.out.println(text);
            if (i++ > 50) break;
        }
    }



    private static List<Integer> readCCDataFromS31(String ccUrl, int urlCount) throws S3ServiceException, IOException {
        String[] subtopics = new String[]{"amazon", "google", "facebook", "Apple", "microsoft"};
        List<Integer> counts = new ArrayList<Integer>();
        List<String> contents = new ArrayList<String>();
        List<String> contentAttrs = new ArrayList<String>();

        //int i = 0;
        //FileWriter writer = new FileWriter(new File("March" + urlCount + "_"+ subtopic + ".txt"));
//        S3Service restS3Service = new RestS3Service(null);
//        S3Object file = restS3Service.getObject("commoncrawl", ccUrl, null, null, null, null, null, null);
//
//        ArchiveReader archiveReader = WARCReaderFactory.get(ccUrl, file.getDataInputStream(), true);
        AWSCredentials credentials = new BasicAWSCredentials( "AKIAIVINMTSHWI7TWGBQ", "bHk7EbYvgSGUhLt9yNbmI4bY8NvkxUvkYKr01QEX");

        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
        com.amazonaws.services.s3.model.S3Object s3object = s3client.getObject("commoncrawl", ccUrl);
        InputStream inputStream = s3object.getObjectContent();


        int i=0;
        ArchiveReader archiveReader = WARCReaderFactory.get(ccUrl, inputStream, true);
        for(ArchiveRecord r : archiveReader) {
            byte[] rawData = IOUtils.toByteArray(r, r.available());
            r.read(rawData);
            String url = r.getHeader().getUrl();
            //if((header != null && !header.contains(subtopic))) continue;
            if(url != null){
                try{
                    if(url.contains("www.amazon.com")){
                        continue;
                    }
                    //Document doc = Jsoup.connect(url).get();
                    String content = new String(rawData);
                    //Elements metas = doc.select("meta");
                    i++;
                    //System.out.println("Processing doc.." + i);
                    JSONObject json = new JSONObject(content);
                    JSONArray metas = json.getJSONObject("Envelope").getJSONObject("Payload-Metadata").getJSONObject("HTTP-Response-Metadata").getJSONObject("HTML-Metadata").getJSONObject("Head").getJSONArray("Metas");

                    for(int j= 0; j < metas.length(); j++){
                        if(metas.getJSONObject(j).getString("name").equals("keywords")){
                            String contentAttr = metas.getJSONObject(j).getString("content");
                            for (String subtopic : subtopics) {
                                if (contentAttr.contains(subtopic) && !contentAttr.contains("apple")) {

                                    Document doc = Jsoup.connect(url).get();
                                    Elements paras = doc.select("p");
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (Element para:paras) {
                                        String text = para.text();
                                        if(text.length() > 100){
                                            stringBuilder.append(text);
                                            stringBuilder.append("\n");
                                        }
                                    }
                                    System.out.println(stringBuilder.toString());
                                    System.out.println(contentAttr);
                                    contentAttrs.add(contentAttr);
                                    System.out.println(url);
                                    contents.add(url);
                                }
                            }
                        }
                    }
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
//                    e.printStackTrace();
//                    System.out.println("** Failed connecting to *** " + url);
                }
            }

            //System.out.println("----------------");
//                String content = new String(rawData);
//                System.out.println(content);
            //System.out.println("----------------");
            //System.out.println(r.getDigestStr());


//                if(content.contains(subtopic)){
//                    i++;
//                    content = content.replaceAll("[^a-zA-Z ]", " ").toLowerCase();
//                    content = content.replaceAll("  ", "");
//                    writer.write(content + "\n");
//                }

            counts.add(i);
            //writer.close();
        }
        //System.out.println("The counts are: " +  counts);
        System.out.println(contents);
        System.out.println(contentAttrs);
        return counts;
    }
}
