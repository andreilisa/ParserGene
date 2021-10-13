package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface JsonRepository  {
    @Select("insert into json values('${result}')")
    void save(Object result);


}
