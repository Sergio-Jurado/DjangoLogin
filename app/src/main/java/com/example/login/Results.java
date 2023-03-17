package com.example.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("username")
    @Expose
    private String count;
    @SerializedName("id")
    @Expose
    private int id;

    public Results(String count, int id){
        this.count = count;
        this.id = id;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
