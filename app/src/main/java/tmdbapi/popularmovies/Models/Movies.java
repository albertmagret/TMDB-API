package tmdbapi.popularmovies.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by albert on 09/02/17.
 */

public class Movies extends Page{

    String poster_path, overview, original_title, original_language, title, backdrop_path;
    int page, id , popularity , vote_count;
    Boolean video;
    Double vote_average;
    Date release_date;

    public static List<Movies> moviesList;
    //public static List<Movies> moviesList_2;

    public Movies(){}

    public Movies(String poster_path, String overview, Date release_date, String original_title,
                    String original_language, String title, String backdrop_path, int id,
                    int popularity, int vote_count, Boolean video, Double vote_average) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.video = video;
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public static List<Movies> getMoviesList() {
        return moviesList;
    }

    public static void setMoviesList(List<Movies> moviesList) {
        Movies.moviesList = moviesList;
    }

    public static void addMovieToList(Movies movies){
        List<Movies> moviesList = Movies.getMoviesList();
        if (moviesList == null){
            moviesList = new ArrayList<>();
        }

        moviesList.add(movies);
        Movies.setMoviesList(moviesList);
    }

    public static void cleanList(){
        if (Movies.getMoviesList() != null)
            Movies.getMoviesList().clear();
    }


}


/************** JSON POPULAR MOVIES ********************/

/*

      "poster_path": "/5XFchtGifv8mz4qlyT8PZ7ZsjfG.jpg",
      "adult": false,
      "overview": "A koala named Buster recruits his best friend to help him drum up business for his theater by hosting a singing competition.",
      "release_date": "2016-12-08",
      "genre_ids": [
        16,
        35,
        10751,
        10402
      ],
      "id": 335797,
      "original_title": "Sing",
      "original_language": "en",
      "title": "Sing",
      "backdrop_path": "/fxDXp8un4qNY9b1dLd7SH6CKzC.jpg",
      "popularity": 11.293585,
      "vote_count": 453,
      "video": false,
      "vote_average": 6.6


*/