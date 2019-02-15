package com.bw.movie.mine.bean;

public class MessageNumBean {

    /**
     * count : 1
     * message : 查询成功
     * status : 0000
     */

    private int count;
    private String message;
    private String status;
    private static final String SUCCESS_STATUS="0000";
    public boolean isSuccess(){
        return status.equals(SUCCESS_STATUS);
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
