package com.bw.movie.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HotFilmDaoBean {
    String followMovie;
    @Id(autoincrement = false)
    long id;
    String imageUrl;
    String name;
    int rank;
    String summary;
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
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getFollowMovie() {
        return this.followMovie;
    }
    public void setFollowMovie(String followMovie) {
        this.followMovie = followMovie;
    }
    @Generated(hash = 1906009759)
    public HotFilmDaoBean(String followMovie, long id, String imageUrl,
            String name, int rank, String summary) {
        this.followMovie = followMovie;
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.rank = rank;
        this.summary = summary;
    }
    @Generated(hash = 1223918563)
    public HotFilmDaoBean() {
    }
}
