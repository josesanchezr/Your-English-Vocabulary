package com.example.android.yourenglishvocabulary.translate;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by joseluis on 24/01/2018.
 */

public class TranslateData {

    @SerializedName("code")
    private String code;

    @SerializedName("lang")
    private String lang;

    @SerializedName("text")
    private List<String> text;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }
}
