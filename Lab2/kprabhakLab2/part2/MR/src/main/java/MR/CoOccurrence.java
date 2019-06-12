package MR;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

class Pair implements WritableComparable<Pair> {
    private Text t1;
    private Text t2;
    public int compareTo(Pair o) {
        return 0;
    }
    public Pair(){
        set(new Text(), new Text());
    }
    public void set(Text t1, Text t2) {
        this.t1 = t1;
        this.t2 = t2;
    }
    public void write(DataOutput dataOutput) throws IOException {}

    public void readFields(DataInput dataInput) throws IOException {}
}

public class CoOccurrence extends Configured implements Tool {
    public final static IntWritable ONE = new IntWritable(1);
    private Path inputPath;
    private Path outputPath;
    private String topwords;
    public static List<String> stopwords = new ArrayList<String>(
            Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves", "a's", "able", "about", "above", "according", "", "", "accordingly", "across", "actually", "after", "afterwards", "", "", "again", "against", "ain't", "all", "allow", "", "", "allows", "almost", "alone", "along", "already", "", "", "also", "although", "always", "am", "among", "", "", "amongst", "an", "and", "another", "any", "", "", "anybody", "anyhow", "anyone", "anything", "anyway", "", "", "anyways", "anywhere", "apart", "appear", "appreciate", "", "", "appropriate", "are", "aren't", "around", "as", "", "", "aside", "ask", "asking", "associated", "at", "", "", "available", "away", "awfully", "be", "became", "", "", "because", "become", "becomes", "becoming", "been", "", "", "before", "beforehand", "behind", "being", "believe", "", "", "below", "beside", "besides", "best", "better", "", "", "between", "beyond", "both", "brief", "but", "", "", "by", "c'mon", "c's", "came", "can", "", "", "can't", "cannot", "cant", "cause", "causes", "", "", "certain", "certainly", "changes", "clearly", "co", "", "", "com", "come", "comes", "concerning", "consequently", "", "", "consider", "considering", "contain", "containing", "contains", "", "", "corresponding", "could", "couldn't", "course", "currently", "", "", "definitely", "described", "despite", "did", "didn't", "", "", "different", "do", "does", "doesn't", "doing", "", "", "don't", "done", "down", "downwards", "during", "", "", "each", "edu", "eg", "eight", "either", "", "", "else", "elsewhere", "enough", "entirely", "especially", "", "", "et", "etc", "even", "ever", "every", "", "", "everybody", "everyone", "everything", "everywhere", "ex", "", "", "exactly", "example", "except", "far", "few", "", "", "fifth", "first", "five", "followed", "following", "", "", "follows", "for", "former", "formerly", "forth", "", "", "four", "from", "further", "furthermore", "get", "", "", "gets", "getting", "given", "gives", "go", "", "", "goes", "going", "gone", "got", "gotten", "", "", "greetings", "had", "hadn't", "happens", "hardly", "", "", "has", "hasn't", "have", "haven't", "having", "", "", "he", "he's", "hello", "help", "hence", "", "", "her", "here", "here's", "hereafter", "hereby", "", "", "herein", "hereupon", "hers", "herself", "hi", "", "", "him", "himself", "his", "hither", "hopefully", "", "", "how", "howbeit", "however", "i'd", "i'll", "", "", "i'm", "i've", "ie", "if", "ignored", "", "", "immediate", "in", "inasmuch", "inc", "indeed", "", "", "indicate", "indicated", "indicates", "inner", "insofar", "", "", "instead", "into", "inward", "is", "isn't", "", "", "it", "it'd", "it'll", "it's", "its", "", "", "itself", "just", "keep", "keeps", "kept", "", "", "know", "known", "knows", "last", "lately", "", "", "later", "latter", "latterly", "least", "less", "", "", "lest", "let", "let's", "like", "liked", "", "", "likely", "little", "look", "looking", "looks", "", "", "ltd", "mainly", "many", "may", "maybe", "", "", "me", "mean", "meanwhile", "merely", "might", "", "", "more", "moreover", "most", "mostly", "much", "", "", "must", "my", "myself", "name", "namely", "", "", "nd", "near", "nearly", "necessary", "need", "", "", "needs", "neither", "never", "nevertheless", "new", "", "", "next", "nine", "no", "nobody", "non", "", "", "none", "noone", "nor", "normally", "not", "", "", "nothing", "novel", "now", "nowhere", "obviously", "", "", "of", "off", "often", "oh", "ok", "", "", "okay", "old", "on", "once", "one", "", "", "ones", "only", "onto", "or", "other", "", "", "others", "otherwise", "ought", "our", "ours", "", "", "ourselves", "out", "outside", "over", "overall", "", "", "own", "particular", "particularly", "per", "perhaps", "", "", "placed", "please", "plus", "possible", "presumably", "", "", "probably", "provides", "que", "quite", "qv", "", "", "rather", "rd", "re", "really", "reasonably", "", "", "regarding", "regardless", "regards", "relatively", "respectively", "", "", "right", "said", "same", "saw", "say", "", "", "saying", "says", "second", "secondly", "see", "", "", "seeing", "seem", "seemed", "seeming", "seems", "", "", "seen", "self", "selves", "sensible", "sent", "", "", "serious", "seriously", "seven", "several", "shall", "", "", "she", "should", "shouldn't", "since", "six", "", "", "so", "some", "somebody", "somehow", "someone", "", "", "something", "sometime", "sometimes", "somewhat", "somewhere", "", "", "soon", "sorry", "specified", "specify", "specifying", "", "", "still", "sub", "such", "sup", "sure", "", "", "t's", "take", "taken", "tell", "tends", "", "", "th", "than", "thank", "thanks", "thanx", "", "", "that", "that's", "thats", "the", "their", "", "", "theirs", "them", "themselves", "then", "thence", "", "", "there", "there's", "thereafter", "thereby", "therefore", "", "", "therein", "theres", "thereupon", "these", "they", "", "", "they'd", "they'll", "they're", "they've", "think", "", "", "third", "this", "thorough", "thoroughly", "those", "", "", "though", "three", "through", "throughout", "thru", "", "", "thus", "to", "together", "too", "took", "", "", "toward", "towards", "tried", "tries", "truly", "", "", "try", "trying", "twice", "two", "un", "", "", "under", "unfortunately", "unless", "unlikely", "until", "", "", "unto", "up", "upon", "us", "use", "", "", "used", "useful", "uses", "using", "usually", "", "", "value", "various", "very", "via", "viz", "", "", "vs", "want", "wants", "was", "wasn't", "", "", "way", "we", "we'd", "we'll", "we're", "", "", "we've", "welcome", "well", "went", "were", "", "", "weren't", "what", "what's", "whatever", "when", "", "", "whence", "whenever", "where", "where's", "whereafter", "", "", "whereas", "whereby", "wherein", "whereupon", "wherever", "", "", "whether", "which", "while", "whither", "who", "", "", "who's", "whoever", "whole", "whom", "whose", "", "", "why", "will", "willing", "wish", "with", "", "", "within", "without", "won't", "wonder", "would", "", "", "wouldn't", "yes", "yet", "you", "you'd", "", "", "you'll", "you're", "you've", "your", "yours", "", "", "yourself", "yourselves", "zero", "", "a", "able", "about", "above", "abst", "accordance", "according", "accordingly", "across", "act", "actually", "added", "adj", "affected", "affecting", "affects", "after", "afterwards", "again", "against", "ah", "all", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "announce", "another", "any", "anybody", "anyhow", "anymore", "anyone", "anything", "anyway", "anyways", "anywhere", "apparently", "approximately", "are", "aren", "arent", "arise", "around", "as", "aside", "ask", "asking", "at", "auth", "available", "away", "awfully", "b", "back", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "begin", "beginning", "beginnings", "begins", "behind", "being", "believe", "below", "beside", "besides", "between", "beyond", "biol", "both", "brief", "briefly", "but", "by", "c", "ca", "came", "can", "cannot", "can't", "cause", "causes", "certain", "certainly", "co", "com", "come", "comes", "contain", "containing", "contains", "could", "couldnt", "d", "date", "did", "didn't", "different", "do", "does", "doesn't", "doing", "done", "don't", "down", "downwards", "due", "during", "e", "each", "ed", "edu", "effect", "eg", "eight", "eighty", "either", "else", "elsewhere", "end", "ending", "enough", "especially", "et", "et-al", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "except", "f", "far", "few", "ff", "fifth", "first", "five", "fix", "followed", "following", "follows", "for", "former", "formerly", "forth", "found", "four", "from", "further", "furthermore", "g", "gave", "get", "gets", "getting", "give", "given", "gives", "giving", "go", "goes", "gone", "got", "gotten", "h", "had", "happens", "hardly", "has", "hasn't", "have", "haven't", "having", "he", "hed", "hence", "her", "here", "hereafter", "hereby", "herein", "heres", "hereupon", "hers", "herself", "hes", "hi", "hid", "him", "himself", "his", "hither", "home", "how", "howbeit", "however", "hundred", "i", "id", "ie", "if", "i'll", "im", "immediate", "immediately", "importance", "important", "in", "inc", "indeed", "index", "information", "instead", "into", "invention", "inward", "is", "isn't", "it", "itd", "it'll", "its", "itself", "i've", "j", "just", "k", "keep", "keeps", "kept", "kg", "km", "know", "known", "knows", "l", "largely", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "line", "little", "'ll", "look", "looking", "looks", "ltd", "m", "made", "mainly", "make", "makes", "many", "may", "maybe", "me", "mean", "means", "meantime", "meanwhile", "merely", "mg", "might", "million", "miss", "ml", "more", "moreover", "most", "mostly", "mr", "mrs", "much", "mug", "must", "my", "myself", "n", "na", "name", "namely", "nay", "nd", "near", "nearly", "necessarily", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "ninety", "no", "nobody", "non", "none", "nonetheless", "noone", "nor", "normally", "nos", "not", "noted", "nothing", "now", "nowhere", "o", "obtain", "obtained", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "omitted", "on", "once", "one", "ones", "only", "onto", "or", "ord", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "owing", "own", "p", "page", "pages", "part", "particular", "particularly", "past", "per", "perhaps", "placed", "please", "plus", "poorly", "possible", "possibly", "potentially", "pp", "predominantly", "present", "previously", "primarily", "probably", "promptly", "proud", "provides", "put", "q", "que", "quickly", "quite", "qv", "r", "ran", "rather", "rd", "re", "readily", "really", "recent", "recently", "ref", "refs", "regarding", "regardless", "regards", "related", "relatively", "research", "respectively", "resulted", "resulting", "results", "right", "run", "s", "said", "same", "saw", "say", "saying", "says", "sec", "section", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sent", "seven", "several", "shall", "she", "shed", "she'll", "shes", "should", "shouldn't", "show", "showed", "shown", "showns", "shows", "significant", "significantly", "similar", "similarly", "since", "six", "slightly", "so", "some", "somebody", "somehow", "someone", "somethan", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specifically", "specified", "specify", "specifying", "still", "stop", "strongly", "sub", "substantially", "successfully", "such", "sufficiently", "suggest", "sup", "sure", "t", "take", "taken", "taking", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "that'll", "thats", "that've", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "thered", "therefore", "therein", "there'll", "thereof", "therere", "theres", "thereto", "thereupon", "there've", "these", "they", "theyd", "they'll", "theyre", "they've", "think", "this", "those", "thou", "though", "thoughh", "thousand", "throug", "through", "throughout", "thru", "thus", "til", "tip", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "ts", "twice", "two", "u", "un", "under", "unfortunately", "unless", "unlike", "unlikely", "until", "unto", "up", "upon", "ups", "us", "use", "used", "useful", "usefully", "usefulness", "uses", "using", "usually", "v", "value", "various", "'ve", "very", "via", "viz", "vol", "vols", "vs", "w", "want", "wants", "was", "wasnt", "way", "we", "wed", "welcome", "we'll", "went", "were", "werent", "we've", "what", "whatever", "what'll", "whats", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "wheres", "whereupon", "wherever", "whether", "which", "while", "whim", "whither", "who", "whod", "whoever", "whole", "who'll", "whom", "whomever", "whos", "whose", "why", "widely", "willing", "wish", "with", "within", "without", "wont", "getWords", "world", "would", "wouldnt", "www", "x", "y", "yes", "yet", "you", "youd", "you'll", "your", "youre", "yours", "yourself", "yourselves", "you've", "z", "zero")
    );

    public CoOccurrence(String[] args) {
        inputPath = new Path(args[0]);
        outputPath = new Path(args[1]);
        //topwords = args[2];
    }

    public static String[] getWords(String text) {
        text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        StringTokenizer st = new StringTokenizer(text);
        ArrayList<String> result = new ArrayList<String>();
        while (st.hasMoreTokens()){
            String token = st.nextToken();
            if(!stopwords.contains(token)){
                result.add(token);
            }
        }
        return Arrays.copyOf(result.toArray(),result.size(),String[].class);
    }

    public static class PairsMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private Text pair;
        @Override
        protected void setup(Context context) throws IOException,
                InterruptedException {
            super.setup(context);
            pair = new Text();
        }
        private static List<String> getFromS3Bucket(String bucketName, String accessKey, String secret, String s3File) throws IOException {
            AWSCredentials credentials = new BasicAWSCredentials( accessKey, secret);

            AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_EAST_2)
                    .build();
            if(s3client.getBucketAcl(bucketName) == null) {
                System.out.println("Bucket name is not available."
                        + " Try again with a different Bucket name.");
                throw new FileNotFoundException(s3File + " not available.");
            }
            com.amazonaws.services.s3.model.S3Object s3object = s3client.getObject(bucketName, s3File);
            InputStream inputStream = s3object.getObjectContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            List<String> words = new ArrayList<String>();
            while((line = reader.readLine()) != null){
                words.add(line);
            }
            return words;
        }

        public static List<String> readTopWords(String topwordsFile) throws  IOException{
            String bucketName = "diclab2kprabhakshishirs";
            String accessKey = "AKIAIVINMTSHWI7TWGBQ";
            String secretKey = "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";
            String s3File = topwordsFile;
            return getFromS3Bucket(bucketName, accessKey,secretKey, s3File);
        }

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws java.io.IOException, InterruptedException {
            String[] tokens = getWords(value.toString());
            Configuration conf = context.getConfiguration();
            String topwordsFile = "topwords_ms.txt";//conf.get("topwords");
            System.out.println("The path of topwords is: "+ topwordsFile);
            List<String> topWords = PairsMapper.readTopWords(topwordsFile);
            for (String word: topWords) {
                if(value.toString().contains(word)){
                    for (int i = 0; i < tokens.length; i++) {
                        //pair.set(new Text(tokens[i]), new Text(tokens[i+1]));
                        if(!word.equals(tokens[i]) && tokens[i].length() >= word.length()){
                            pair.set(word + " " + tokens[i]);
                            context.write(pair, ONE);
                        }
                    }
                }
            }
        }
    }

    public static class PairsReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable res;
        @Override
        protected void setup(Context context) throws IOException,
                InterruptedException {
            super.setup(context);
            res = new IntWritable();
        }
        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            res.set(sum);
            context.write(key, res);
        }
    }

    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        Job job = new Job(conf, "CoOccurrence");

        job.setMapperClass(PairsMapper.class);
        job.setReducerClass(PairsReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        TextInputFormat.addInputPath(job, inputPath);
        job.setInputFormatClass(TextInputFormat.class);

        FileOutputFormat.setOutputPath(job, outputPath);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setJarByClass(CoOccurrence.class);

        //conf.setStrings("topwords", topwords);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new CoOccurrence(args), args);
        System.exit(res);
    }
}
