package com.example.tushar.destinationdiscussions;

import java.util.Date;

/**
 * Created by TUSHAR on 21-05-2017.
 */

public class listmodel {
   private String message;
   private String email;
   private Long time;
    public listmodel(String message, String email) {
        this.message = message;
        this.email = email;
        time=new Date().getTime();


    }

    public listmodel(){

    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }






}
