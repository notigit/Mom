package com.xiaoaitouch.mom.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SHARE_CARD_MODEL.
 */
public class ShareCardModel {

    private Long id;
    private String message;
    private String img;
    private String location;
    private String feeling;
    private Integer type;
    private Double lat;
    private Double lng;
    private Long userId;
    private Integer cardType;
    private String date;
    private Integer dueDays;
    private Long createTime;
    private Long descTime;
    private Long cardId;

    public ShareCardModel() {
    }

    public ShareCardModel(Long id) {
        this.id = id;
    }

    public ShareCardModel(Long id, String message, String img, String location, String feeling, Integer type, Double lat, Double lng, Long userId, Integer cardType, String date, Integer dueDays, Long createTime, Long descTime, Long cardId) {
        this.id = id;
        this.message = message;
        this.img = img;
        this.location = location;
        this.feeling = feeling;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.userId = userId;
        this.cardType = cardType;
        this.date = date;
        this.dueDays = dueDays;
        this.createTime = createTime;
        this.descTime = descTime;
        this.cardId = cardId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDueDays() {
        return dueDays;
    }

    public void setDueDays(Integer dueDays) {
        this.dueDays = dueDays;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getDescTime() {
        return descTime;
    }

    public void setDescTime(Long descTime) {
        this.descTime = descTime;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

}
