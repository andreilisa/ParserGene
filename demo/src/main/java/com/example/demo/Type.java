package com.example.demo;

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

            return read;
        }

    };


    public abstract <T> Object readValue(T read);

}