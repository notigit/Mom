package com.xiaoaitouch.mom.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MSYMPTOM_MODEL.
 */
public class MSymptomModel {

    private Long id;
    private Long userId;
    private String symptom;
    private String date;

    public MSymptomModel() {
    }

    public MSymptomModel(Long id) {
        this.id = id;
    }

    public MSymptomModel(Long id, Long userId, String symptom, String date) {
        this.id = id;
        this.userId = userId;
        this.symptom = symptom;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}