package com.auro.application.home.data.model.response;


import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SlabDetailResModel {

    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("quiz_name")
    @Expose
    private String quizName;
    @SerializedName("translatedSubject")
    @Expose
    private String translatedSubject;
    @SerializedName("slab_price")
    @Expose
    private Integer slabprice;

    public Integer getSlabprice() {
        return slabprice;
    }

    public void setSlabprice(Integer slabprice) {
        this.slabprice = slabprice;
    }

    public String getTranslatedSubject() {
        return translatedSubject;
    }

    public void setTranslatedSubject(String translatedSubject) {
        this.translatedSubject = translatedSubject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

}