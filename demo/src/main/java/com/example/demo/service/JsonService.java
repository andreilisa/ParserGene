package com.example.demo.service;

import com.example.demo.mapper.JsonRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class JsonService {
    @Value("${reader.keys}")
    private String keys;

    @Autowired
    private JsonRepository jsonMapper;

    public void write() throws IOException {
        InputStream inputStream = new FileInputStream("C:\\Users\\andrei.lisa\\Folder\\doc1.json");
        Reader inputStreamReader = new InputStreamReader(inputStream);
        JsonReader jsonReader = new JsonReader(inputStreamReader);

        jsonReader.beginObject();
        jsonReader.nextName();
        jsonReader.beginObject();
        jsonReader.nextName();
        jsonReader.beginArray();
        Gson gson = new Gson();
        while (jsonReader.hasNext()) {
            Object obj = gson.fromJson(jsonReader, Object.class);
            StringBuilder builder = new StringBuilder();

            builder.append("insert into json2 (").append(keys).append(")").append("values(");
            for (String key : keys.split(",")) {
                String value = JsonPath.read(obj, key);
                builder.append("'").append(value).append("',");

            }
            builder.deleteCharAt(builder.length()-1);
            builder.append(")");
            jsonMapper.save(builder.toString());

            System.out.println(builder);


            }
        }

    }

