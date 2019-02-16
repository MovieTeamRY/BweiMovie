package com.bw.movie.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class RelaeseFilmDaoBean {
    String followMovie;
    @Id(autoincrement = false)
    int id;
    String imageUrl;
    String name;
    int rank;
    String summary;
    String releaseTimeShow;
    public String getReleaseTimeShow() {
        return this.releaseTimeShow;
    }
    public void setReleaseTimeShow(String releaseTimeShow) {
        this.releaseTimeShow = releaseTimeShow;
    }
    public String getSummary() {
        return this.summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public int getRank() {
        return this.rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFollowMovie() {
        return this.followMovie;
    }
    public void setFollowMovie(String followMovie) {
        this.followMovie = followMovie;
    }
    @Generated(hash = 2110606109)
    public RelaeseFilmDaoBean(String followMovie, int id, String imageUrl,
            String name, int rank, String summary, String releaseTimeShow) {
        this.followMovie = followMovie;
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.rank = rank;
        this.summary = summary;
        this.releaseTimeShow = releaseTimeShow;
    }
    @Generated(hash = 778928808)
    public RelaeseFilmDaoBean() {
    }
}
