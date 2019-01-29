package com.bw.movie.purchase.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FilmSchedulingBean implements Parcelable {



    private String message;
    private String status;
    private List<ResultBean> result;
    private static final String SUCCESS_STATUS="0000";

    protected FilmSchedulingBean(Parcel in) {
        message = in.readString();
        status = in.readString();
        result = in.createTypedArrayList(ResultBean.CREATOR);
    }

    public static final Creator<FilmSchedulingBean> CREATOR = new Creator<FilmSchedulingBean>() {
        @Override
        public FilmSchedulingBean createFromParcel(Parcel in) {
            return new FilmSchedulingBean(in);
        }

        @Override
        public FilmSchedulingBean[] newArray(int size) {
            return new FilmSchedulingBean[size];
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
        /**
         * address : 北京市崇文区崇文门外大街18号国瑞城首层、地下二层
         * commentTotal : 0
         * distance : 0
         * followCinema : 0
         * id : 9
         * logo : http://172.17.8.100/images/movie/logo/blh.jpg
         * name : 北京百老汇影城国瑞购物中心店
         */

        private String address;
        private int commentTotal;
        private double distance;
        private int followCinema;
        private int id;
        private String logo;
        private String name;

        protected ResultBean(Parcel in) {
            address = in.readString();
            commentTotal = in.readInt();
            distance = in.readDouble();
            followCinema = in.readInt();
            id = in.readInt();
            logo = in.readString();
            name = in.readString();
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCommentTotal() {
            return commentTotal;
        }

        public void setCommentTotal(int commentTotal) {
            this.commentTotal = commentTotal;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public int getFollowCinema() {
            return followCinema;
        }

        public void setFollowCinema(int followCinema) {
            this.followCinema = followCinema;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(address);
            dest.writeInt(commentTotal);
            dest.writeDouble(distance);
            dest.writeInt(followCinema);
            dest.writeInt(id);
            dest.writeString(logo);
            dest.writeString(name);
        }
    }
}
