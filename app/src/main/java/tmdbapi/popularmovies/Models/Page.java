package tmdbapi.popularmovies.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albert on 10/02/17.
 */

public class Page {

    int page, total_results, total_pages;
    List<Page> pageList = new ArrayList<>();
    List<Movies> moviesList = new ArrayList<>();

    public Page(){}

    public Page(int page, int total_pages, int total_results) {
        this.page = page;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    public Page(int page, int total_results, int total_pages, List<Movies> moviesList) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.moviesList = moviesList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movies> getMoviesListFromPage() {
        return moviesList;
    }

    public void setMoviesListToPage(List<Movies> moviesList) {
        this.moviesList = moviesList;
    }


}
