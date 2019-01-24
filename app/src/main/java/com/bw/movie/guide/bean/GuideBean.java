package com.bw.movie.guide.bean;

public class GuideBean {
    Integer image;
    String text="一场电影";
    String descirbe;

    public GuideBean(Integer image, String descirbe) {
        this.image = image;
        this.descirbe = descirbe;
    }

    public GuideBean(Integer image, String text, String descirbe) {
        this.image = image;
        this.text = text;
        this.descirbe = descirbe;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescirbe() {
        return descirbe;
    }

    public void setDescirbe(String descirbe) {
        this.descirbe = descirbe;
    }
}
