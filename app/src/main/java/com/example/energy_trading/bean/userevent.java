package com.example.energy_trading.bean;

public class userevent {
    private String useremail;
    private String userpassword;

    public userevent() {
    }
    public userevent(String useremail,String userpassword) {
        this.useremail = useremail;
        this.userpassword = userpassword;
    }
    public String getUseremail() {
        return useremail;
    }
    public String getUserpassword() {
        return userpassword;
    }

    public void setUseremail(String useremail) {
        this.useremail= useremail;
    }
    public void setUserpassword(String userpassword) {
        this.userpassword= userpassword;
    }
}
