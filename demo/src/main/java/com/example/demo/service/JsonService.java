package com.example.demo.service;

import com.example.demo.mapper.JsonRepository;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class JsonService {
    @Value("${reader.keys}")
    private String keys;

    @Value("${reader.keyArr}")
    private String keysArray;

    @Value("${file.path}")
    private String path;


    @Autowired
    private JsonRepository jsonMapper;

    public void write() throws IOException {
        InputStream inputStream = new FileInputStream(path);
        Reader inputStreamReader = new InputStreamReader(inputStream);
        JsonReader jsonReader = new JsonReader(inputStreamReader);

        jsonReader.beginObject();
        jsonReader.nextName();
        jsonReader.beginObject();
        jsonReader.nextName();
        jsonReader.beginArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        while (jsonReader.hasNext()) {
            Object obj = gson.fromJson(jsonReader, Object.class);
            String jsonInString = gson.toJson(obj);
            StringBuilder builder = new StringBuilder();
            builder.append("insert into json2 (").append(keys).append(keysArray).append(")").append("values(");
            for (String key : keys.split(",")) {

                String value = JsonPath.read(jsonInString, key);
                builder.append("'").append(value).append("',");

            }
            for (String key : keysArray.split(",")) {
                List<String> value = JsonPath.read(jsonInString, key);
                builder.append("'").append(value).append("',");

            }

            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
            jsonMapper.save(builder.toString());

            System.out.println(builder);
        }
    }
}