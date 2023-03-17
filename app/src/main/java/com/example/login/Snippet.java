package com.example.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet {
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("highlight")
    @Expose
    public String highlight;
    @SerializedName("owner")
    @Expose
    public String owner;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("linenos")
    @Expose
    public boolean linenos;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("style")
    @Expose
    public String style;

    public Snippet(String url, String id, String highlight, String owner, String title, String code, boolean linenos, String language, String style){
        this.url=url;
        this.id=id;
        this.highlight=highlight;
        this.owner=owner;
        this.title=title;
        this.code=code;
        this.linenos=linenos;
        this.language=language;
        this.style=style;
    }

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHighlight() {
        return ""+highlight+"";
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getLinenos() {
        return linenos;
    }

    public String getLanguage() {
        return language;
    }
    public String getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                ", id='" + id + '\'' +
                ", highlight='" + highlight + '\'' +
                ", owner='" + owner + '\'' +
                ", title='" + title + '\'' +
                ", code='" + code + '\'' +
                ", linenos='" + linenos + '\'' +
                ", language='" + language + '\'' +
                ", style='" + style + '\'' +
                '}';
    }
}