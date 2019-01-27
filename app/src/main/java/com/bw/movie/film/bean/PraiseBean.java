package com.bw.movie.film.bean;

public class PraiseBean {


    private String message;
    private String status;
    private static final String SUCCESS_STATUS="点赞成功";
    public boolean isSuccess(){
        return message.equals(SUCCESS_STATUS);
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
