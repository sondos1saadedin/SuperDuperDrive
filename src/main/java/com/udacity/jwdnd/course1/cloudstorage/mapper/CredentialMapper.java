package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid)" +
            " VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getUserCredentials(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential getCredentialById(Integer credentialId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    int deleteCredential(Integer credentialId);

    @Update("UPDATE CREDENTIALS SET url=#{url}, key=#{key}, password=#{password}, username=#{username} where credentialid=#{credentialId}")
    int updateCredential(Credential credential);


}
