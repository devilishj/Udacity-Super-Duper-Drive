package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CredentialsMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid=#{userId} and url=#{url}")
    User getCredential(Integer userId,String url);

    @Insert("INSERT INTO CREDENTIALS (url, credUsername, key, credPassword, userid) " +
            "VALUES(#{url}, #{credUsername}, #{key}, #{credPassword}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    Integer createCredential(Credentials credential);

    @Select("SELECT * FROM CREDENTIALS")
    List<Credentials> getAllCredentials();

    @Select(("SELECT * FROM CREDENTIALS WHERE userid=#{userId}"))
    List<Credentials> getCredentialsForUser(Integer userId);
    @Update("UPDATE CREDENTIALS SET url=#{url},credUsername=#{credUsername},key=#{key},credPassword=#{credPassword} WHERE credentialId=#{credentialId}")
    Integer updateCredential(Credentials credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId=#{credentialId}")
    Integer deleteCredential(Integer credentialId);

    @Select("Select key FROM CREDENTIALS WHERE credentialId=#{credentialId}")
    String getKey(Integer credentialId);

    @Select("Select max(credentialId) from CREDENTIALS")
    Integer getLastCredentialId();
}
