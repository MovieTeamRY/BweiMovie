package com.bw.movie.mine.bean;

public class LoadHeadPicBean {

    private String headPath;
    private String message;
    private String status;
    private static final String SUCCESS_STATUS="0000";
    public boolean isSuccess(){
        return status.equals(SUCCESS_STATUS);
    }
    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
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
