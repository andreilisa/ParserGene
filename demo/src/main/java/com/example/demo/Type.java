package com.example.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public enum Type {

    OBJECT {
        @Override
        public <T> Object readValue(T read) {
            return read;
        }

    },
    ARRAY {
        @Override
        public <T> Object readValue(T read) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(read);

        }
    };

    public abstract <T> Object readValue(T read);


}