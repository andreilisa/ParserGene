package com.example.demo.service;

import com.example.demo.mapper.JsonRepository;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;


@Component
public class JsonService {

    @Value("${reader.keys}")
    private String keys;

    @Autowired
    private JsonRepository jsonMapper;


    public void convert() {

        try {
            File jsonFile = new File("C:\\Users\\andrei.lisa\\Folder\\doc1.json");
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(jsonFile));
            for (String key : keys.split(",")) {
                Object result = JsonPath.read(obj, key);

                jsonMapper.save(result.toString());
            }

        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }

    }

    public void write() throws IOException {
        InputStream inputStream = new FileInputStream("C:\\Users\\andrei.lisa\\Folder\\doc1.json");
        Reader inputStreamReader = new InputStreamReader(inputStream);
        JsonReader jsonReader = new JsonReader(inputStreamReader);

        jsonReader.beginObject();
        jsonReader.nextName();
        jsonReader.beginObject();
        jsonReader.nextName();
        jsonReader.beginArray();

        while (jsonReader.hasNext()) {
            Object obj = new Gson().fromJson(jsonReader, Object.class);
            for (String key : keys.split(",")) {
                List<String> result = JsonPath.read(obj, key);
                System.out.println(result);
            }

        }

    }

}