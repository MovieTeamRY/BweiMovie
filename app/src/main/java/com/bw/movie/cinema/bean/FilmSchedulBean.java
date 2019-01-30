package com.bw.movie.cinema.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FilmSchedulBean implements Parcelable {

    private String message;
    private String status;
    private List<ResultBean> result;
    private static final String SUCCESS_STATUS="0000";

    protected FilmSchedulBean(Parcel in) {
        message = in.readString();
        status = in.readString();
        result = in.createTypedArrayList(ResultBean.CREATOR);
    }

    public static final Creator<FilmSchedulBean> CREATOR = new Creator<FilmSchedulBean>() {
        @Override
        public FilmSchedulBean createFromParcel(Parcel in) {
            return new FilmSchedulBean(in);
        }

        @Override
        public FilmSchedulBean[] newArray(int size) {
            return new FilmSchedulBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(status);
        dest.writeTypedList(result);
    }

    public static class ResultBean implements Parcelable{

        private String beginTime;
        private String duration;
        private String endTime;
        private int id;
        private double price;
        private String screeningHall;
        private int seatsTotal;
        private int seatsUseCount;
        private int status;

        protected ResultBean(Parcel in) {
            beginTime = in.readString();
            duration = in.readString();
            endTime = in.readString();
            id = in.readInt();
            price = in.readDouble();
            screeningHall = in.readString();
            seatsTotal = in.readInt();
            seatsUseCount = in.readInt();
            status = in.readInt();
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel in) {
                return new ResultBean(in);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(beginTime);
            dest.writeString(duration);
            dest.writeString(endTime);
            dest.writeInt(id);
            dest.writeDouble(price);
            dest.writeString(screeningHall);
            dest.writeInt(seatsTotal);
            dest.writeInt(seatsUseCount);
            dest.writeInt(status);
        }
    }
}
