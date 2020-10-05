package ca.bcit.clearcouncil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class Comment {

    private String name;
    private String time;
    private String body;


    // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    public Comment(){}

    public Comment(String name, String body, String time){
        this.name = name;
        this.time = time;
        this.body = body;
    }


    //Getters and setters required for returning Comment Object from database
    public String getName(){
        return name;
    }

    public String getTime(){
        return time;
    }

    public String getBody(){
        return body;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setBody(String body){
        this.body = body;
    }

    public void setTime(String time){
        this.time = time;
    }

}
