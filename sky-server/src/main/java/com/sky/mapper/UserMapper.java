package com.sky.mapper;


import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getUserByOpenid(String openid);

    @Insert("Insert User(openid,create_time) values (#{openid},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id") //useGeneratedKeys = true：告诉 MyBatis 使用数据库自动生成的主键。keyProperty = "id"：指定将生成的主键值赋值给实体对象中的哪个字段。
    void insert(User user);
}
