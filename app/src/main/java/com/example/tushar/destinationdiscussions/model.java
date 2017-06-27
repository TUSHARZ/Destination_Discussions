package com.example.tushar.destinationdiscussions;

/**
 * Created by TUSHAR on 20-05-2017.
 */

public class model {
   private  String Title,Image;

    public model(String title,String Image) {
        Title = title;
        this.Image=Image;
    }
    public model(){


    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


}
