package com.example.demo.service;

import com.example.demo.KeyConfig;
import com.example.demo.mapper.JsonRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JsonService {

    @Autowired
    private KeyConfig keyConfig;

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
            builder.append("insert into json2 (").append(String.join(", ", keyConfig.getKeys().keySet())).append(")").append("values(");

            Set<Map.Entry<String, String>> map = keyConfig.getKeys().entrySet();
            for (Map.Entry<String, String> keyVal : map) {
                if (keyVal.getValue().equals("object")) {
                    String value = JsonPath.read(obj, keyVal.getKey());
                    builder.append("'").append(value).append("',");
                } else {
                    List<String> values = JsonPath.read(jsonInString, keyVal.getKey());
                    builder.append("'").append(values).append("',");
                }
            }

            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
            jsonMapper.save(builder.toString());
            System.out.println(builder);
        }
    }
}
