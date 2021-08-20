package com.example.energy_trading.DataBase;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


@Entity(nameInDb = "user_profile") //定义实体类，nameInDb：数据库名
public class UserProfile {

    @Id
    private long userId = 0;
    private String username = null;
    private String  email= null;
    private String  telephone= null;
    private String password = null;
    @Generated(hash = 334866319)
    public UserProfile(long userId, String username, String email, String telephone,
            String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
    }
    @Generated(hash = 968487393)
    public UserProfile() {
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelephone() {
        return this.telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
