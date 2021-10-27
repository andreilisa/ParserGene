package com.example.demo.service;

import com.example.demo.JavaConfigReader;
import com.example.demo.KeyConfig;
import com.example.demo.Type;
import com.example.demo.TypeReader;
import com.example.demo.mapper.JsonRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JsonService {

    @Value("${file.path}")
    private String path;

    @Autowired
    private KeyConfig keyConfig;

    @Autowired
    private JavaConfigReader javaConfigReader;

    @Autowired
    private JsonRepository jsonMapper;


    public JsonReader read() throws IOException {
        InputStream inputStream = new FileInputStream(path);
        Reader inputStreamReader = new InputStreamReader(inputStream);
        JsonReader jsonReader = new JsonReader(inputStreamReader);
        List<TypeReader> list = javaConfigReader.getElement();
        for (TypeReader value : list) {
            value.setJsonType(jsonReader);
        }
        return jsonReader;
    }

    public void write() throws IOException {
        JsonReader jsonReader = read();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        while (jsonReader.hasNext()) {
            Object obj = gson.fromJson(jsonReader, Object.class);
            StringBuilder builder = new StringBuilder();
            builder.append("insert into json2 (").append(String.join(", ", keyConfig.getKeys().keySet())).append(")").append("values(");

            Set<Map.Entry<String, Type>> map = keyConfig.getKeys().entrySet();
            for (Map.Entry<String, Type> keyVal : map) {
                keyVal.getValue().readValue(builder, obj, keyVal.getKey());
            }

            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
            jsonMapper.save(builder.toString());
            System.out.println(builder);
        }
    }
}
