package com.bw.movie.cinema.bean;

import java.util.List;

public class FilmSchedulBean {

    private String message;
    private String status;
    private List<ResultBean> result;
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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {

        private String beginTime;
        private String duration;
        private String endTime;
        private int id;
        private double price;
        private String screeningHall;
        private int seatsTotal;
        private int seatsUseCount;
        private int status;

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getScreeningHall() {
            return screeningHall;
        }

        public void setScreeningHall(String screeningHall) {
            this.screeningHall = screeningHall;
        }

        public int getSeatsTotal() {
            return seatsTotal;
        }

        public void setSeatsTotal(int seatsTotal) {
            this.seatsTotal = seatsTotal;
        }

        public int getSeatsUseCount() {
            return seatsUseCount;
        }

        public void setSeatsUseCount(int seatsUseCount) {
            this.seatsUseCount = seatsUseCount;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}