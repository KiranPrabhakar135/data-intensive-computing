package NYTimes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonOperations {

    public Contract convertToObject(String data){
        //Type collectionType = new TypeToken<T>(){}.getType();
        Gson gson = new Gson();
        Contract pojo = gson.fromJson(data, Contract.class);
        return pojo;
    }

    public void writeToFile(List<Contract> responses, String filePath){
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            gson.toJson(responses, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Contract> readFromFile(String filePath){
        Gson gson = new Gson();
        List<Contract> data = new ArrayList<Contract>();
        try  {
            FileReader reader = new FileReader(filePath);
            data = gson.fromJson(reader, new TypeToken<List<Contract>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
