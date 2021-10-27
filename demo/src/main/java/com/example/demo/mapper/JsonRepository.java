package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JsonRepository {
    @Insert("${result}")
    void save(String builder);

}
