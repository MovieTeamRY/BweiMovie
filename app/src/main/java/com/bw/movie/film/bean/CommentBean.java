package com.bw.movie.film.bean;

public class CommentBean {

    /**
     * message : 评论成功
     * status : 0000
     */

    private String message;
    private String status;
    private static final String SUCCESS_STATUS="0000";
    public boolean isSuccess(){
        return status.equals(SUCCESS_STATUS);
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
