package com.bw.movie.mine.bean;

public class VersionBean {

    /**
     * flag : 1
     * downloadUrl : http://172.17.8.100/media/movie.apk
     * message : 查询成功
     * status : 0000
     */

    private int flag;
    private String downloadUrl;
    private String message;
    private String status;
    private static final String SUCCESS_STATUS="0000";
    public boolean isSuccess(){
        return status.equals(SUCCESS_STATUS);
    }
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
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
