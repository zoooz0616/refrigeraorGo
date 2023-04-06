package com.example.refrigeratorgo;

public class User {

    private String title;
    private String content;
    private String btn;
    private int resId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBtn() {
        return btn;
    }

    public void setBtn(String btn) { this.btn= btn; }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}