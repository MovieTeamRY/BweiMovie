package com.bw.movie.film.bean;

import java.util.List;

public class ScreenFilmBean {

    private String message;
    private String status;
    private List<ResultBean> result;

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
        private String followMovie;
        private int id;
        private String imageUrl;
        private String name;
        private int rank;
        private String summary;
        private String releaseTimeShow;

        public ResultBean(String followMovie, int id, String imageUrl, String name, int rank, String summary, String releaseTimeShow) {
            this.followMovie = followMovie;
            this.id = id;
            this.imageUrl = imageUrl;
            this.name = name;
            this.rank = rank;
            this.summary = summary;
            this.releaseTimeShow = releaseTimeShow;
        }

        public String isFollowMovie() {
            return followMovie;
        }

        public void setFollowMovie(String followMovie) {
            this.followMovie = followMovie;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getReleaseTimeShow() {
            return releaseTimeShow;
        }

        public void setReleaseTimeShow(String releaseTimeShow) {
            this.releaseTimeShow = releaseTimeShow;
        }
    }
}
