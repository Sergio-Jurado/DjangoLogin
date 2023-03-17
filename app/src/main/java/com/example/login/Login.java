package com.example.login;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class Login {
    @SerializedName("access_token")
    @Expose
    private String token;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("user")
    @Expose
    private JsonObject user;

    public Login(String token, String refreshToken, JsonObject user){

        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;

    }

    public JsonObject getUser() {
        return user;
    }
    public String getToken() {
        return "Bearer " + this.token;
    }

}
