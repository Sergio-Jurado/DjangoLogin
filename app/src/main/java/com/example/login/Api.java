package com.example.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://192.168.1.173:8000/";

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("dj-rest-auth/login/")
    Call<JsonElement> getLogins(@Body JsonObject body);

    @POST("dj-rest-auth/logout/")
    Call<JsonElement> postLogout();

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PATCH("dj-rest-auth/user/")
    Call<JsonElement> patchUser(@Header("Authorization") String token, @Body JsonObject body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("snippets/")
    Call<JsonElement> getSnippets();

    @GET("snippets/")
    Call<JsonElement> getSnippets(@Query("page") int page);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("snippets/")
    Call<JsonElement> postSnippets(@Header("Authorization") String token, @Body JsonObject body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PATCH("snippets/{id}/")
    Call<JsonElement> patchSnippets(@Header("Authorization") String token, @Body JsonObject body,@Path("id")int id);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("snippets/{id}/")
    Call<JsonElement> deleteSnippets(@Header("Authorization") String token, @Path("id")int id);

    @GET("/options/")
    Call<JsonElement> getOptions();

}
