package com.example.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;

public class Options {
    @SerializedName("languages")
    @Expose
    private String [][] languages = new String[][]{};
    @SerializedName("styles")
    @Expose
    public String [][] styles = new String[][]{};

    public Options(String [][] languages, String [][] styles){
        this.styles=styles;
        this.languages=languages;
    }

    public ArrayList<String> getLanguages(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0; i<languages.length; i++){
            arrayList.add(languages[i][0]);
        }
        return arrayList;
    }

    public ArrayList<String> getStyles(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0; i<styles.length; i++){
            arrayList.add(styles[i][0]);

        }
        return arrayList;
    }
}
