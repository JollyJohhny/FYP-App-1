package com.example.blockcertify_manufacturerapp;

public class ListType {
    private String Name,Id,TimeStamp;
    public ListType(){

    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public ListType(String name, String id, String timestamp) {
        Name = name;
        Id = id;
        TimeStamp = timestamp;
    }
}
