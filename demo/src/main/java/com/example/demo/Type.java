package com.example.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;

public enum Type {

    OBJECT {
        @Override
        public void  readValue(StringBuilder builder, Object obj, String key) {
         Type.append(builder,Type.getValue(obj, key).toString());
        }

    },
    ARRAY {
        @Override
        public void  readValue(StringBuilder builder, Object obj, String key) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String value = gson.toJson(Type.getValue(obj, key));
            Type.append(builder,value);
        }
    };

    private  static void append(StringBuilder builder,String value){
        builder.append("'").append(value).append("',");
    }

    private static  <T> Object getValue(Object object, String key) {
        return JsonPath.read(object, key);
    }

    public abstract void  readValue(StringBuilder builder, Object obj,String key);
}