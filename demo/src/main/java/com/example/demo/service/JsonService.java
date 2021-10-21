package com.example.demo.service;

import com.example.demo.KeyConfig;
import com.example.demo.mapper.JsonRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JsonService {
    private final String val = "object";
    //    @Value("${reader.keys}")
//    private String keys;
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

        System.out.println(val);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        while (jsonReader.hasNext()) {
            Object obj = gson.fromJson(jsonReader, Object.class);
            String jsonInString = gson.toJson(obj);
            StringBuilder builder = new StringBuilder();
            builder.append("insert into json2 (").append(String.join(", ", keyConfig.getKeys().keySet())).append(")").append("values(");
            for (String key : keyConfig.getKeys().keySet()) {
                String value = keyConfig.getKeys().get(key);
                if (value.equals("object")) {
                    builder.append("'").append(getValue(obj, key)).append("',");
                } else {
                    builder.append("'").append(JsonPath.read(jsonInString, key).toString()).append("',");
                }
            }

            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
            jsonMapper.save(builder.toString());
            System.out.println(builder);
        }
    }


    private <T> String getValue(T object, String key) {
        try {
            return JsonPath.read(object, key);

        } catch (PathNotFoundException e) {
            return null;
        }
    }
}


