import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
//import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CCMR {

    public static class WarcMapper
            extends Mapper<Object, Text, Text, Text> {

        private Text word = new Text();
        public static enum Counter{
            TOTAL,
            VALID
        }

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String rawText = value.toString();
            String[] urls = rawText.split("\n");
            for (String url:urls) {
                System.out.println("The url is " + url);
                try{
                    String[] subtopics = new String[]{"Amazon", "Google", "Tesla", "Apple", "Microsoft", "Facebook"};
                    //S3Service restS3Service = new RestS3Service( );

                    //S3Object file = restS3Service.getObject("commoncrawl", url, null, null, null, null, null, null);
                    AWSCredentials credentials = new BasicAWSCredentials( "AKIAIVINMTSHWI7TWGBQ", "bHk7EbYvgSGUhLt9yNbmI4bY8NvkxUvkYKr01QEX");

                    AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                            .withCredentials(new AWSStaticCredentialsProvider(credentials))
                            .withRegion(Regions.US_EAST_1)
                            .build();
                    com.amazonaws.services.s3.model.S3Object s3object = s3client.getObject("commoncrawl", url);
                    InputStream inputStream = s3object.getObjectContent();


                    int i=0;
                    //ArchiveReader archiveReader = WARCReaderFactory.get(url, file.getDataInputStream(), true);
                    ArchiveReader archiveReader = WARCReaderFactory.get(url, inputStream, true);
                    for(ArchiveRecord r : archiveReader) {
                        if (!r.getHeader().getMimetype().equals("application/json")) {
                            continue;
                        }
                        String headerUrl = r.getHeader().getUrl();
                        //context.getCounter(Counter.TOTAL).increment(1);
                        if(headerUrl != null){
                            try{
                                if(url.contains("www.amazon.com") || url.contains("www.facebook.com")){
                                    continue;
                                }
                                i++;
                                System.out.println("Processing the doc.." + i);
                                byte[] rawData = IOUtils.toByteArray(r, r.available());
                                String content = new String(rawData);
                                JSONObject json = new JSONObject(content);
                                JSONArray metas = json.getJSONObject("Envelope").getJSONObject("Payload-Metadata").getJSONObject("HTTP-Response-Metadata").getJSONObject("HTML-Metadata").getJSONObject("Head").getJSONArray("Metas");

                                System.out.println("Found metas");


                                for(int j= 0; j < metas.length(); j++){
                                    if(metas.getJSONObject(j).getString("name").equals("keywords")){
                                        String contents = metas.getJSONObject(j).getString("content");
                                        for (String subtopic : subtopics) {
                                            if (contents.contains(" " + subtopic + " ") &&
                                                    (!contents.contains("apple") && !contents.contains("amazon"))) {
                                                Document doc = Jsoup.connect(headerUrl).get();
                                                Elements paras = doc.select("p");
                                                System.out.println("Got p tags");
                                                StringBuilder stringBuilder = new StringBuilder();
                                                boolean added = false;
                                                for (Element para:paras) {
                                                    String text = para.text();
                                                    if(text.length() > 100){
                                                        text = text.trim();
                                                        text = text.replaceAll("\n"," ");
                                                        stringBuilder.append(text);
                                                        added = true;
                                                    }
                                                }
                                                if(added){
                                                    stringBuilder.append(contents);
                                                    //stringBuilder.append("\n");
                                                    System.out.println(stringBuilder.toString());
                                                    System.out.println("*** Key emitted is: " + subtopic + " ***");
                                                    word.set(subtopic);
                                                    context.write(word, new Text(stringBuilder.toString()));
                                                    //context.getCounter(Counter.VALID).increment(1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            catch (JSONException e){
                                System.out.println("Json error.."+e.getMessage());
                            }
                            catch (Exception e){
                                System.out.println("ex error.."+e.getMessage());
                            }
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static class WarcReducer
            extends Reducer<Text,Text,Text,Text> {
        private Text result = new Text();

        private static long putToS3Bucket(String bucketName, String accessKey, String secret, String s3File, String content) throws IOException {
            AWSCredentials credentials = new BasicAWSCredentials( accessKey, secret);
            AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_EAST_2)
                    .build();
            if(s3client.getBucketAcl(bucketName) == null) {
                System.out.println("Bucket name is not available."
                        + " Try again with a different Bucket name.");
                return 0;
            }
            if(!s3client.doesObjectExist(bucketName, s3File)){
                s3client.putObject(bucketName, s3File,"");
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
            System.out.println("The put operation is performed to " + s3File);
            byte[] byteArray = stringBuilder.toString().getBytes("utf-8");
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(byteArray.length);
            s3client.putObject( bucketName, s3File,new ByteArrayInputStream(byteArray), metadata);
            System.out.println("The put operation is successful on file " + s3File);
            return metadata.getContentLength();
        }

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {
            System.out.println("The key in reducer is: " + key.toString());
            String bucketName = "diclab2kprabhakshishirs";
            String accessKey = "AKIAIVINMTSHWI7TWGBQ";
            String secretKey = "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";
            StringBuilder stringBuilder = new StringBuilder();
            int sum = 0;
            for (Text val : values) {
                sum++;
                stringBuilder.append(val.toString());
                stringBuilder.append("\n");
            }
            long length = putToS3Bucket(bucketName, accessKey, secretKey, key.toString() + ".txt", stringBuilder.toString());
            //System.out.println("The key in reducer is: " + key.toString());
            result.set(Integer.toString(sum));
            context.write(key, result);
        }
    }
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf);
        job.setJarByClass(CCMR.class);
        job.setMapperClass(CCMR.WarcMapper.class);
        job.setCombinerClass(CCMR.WarcReducer.class);
        job.setReducerClass(CCMR.WarcReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
