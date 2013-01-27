package com.zebia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SongsResponse {

    private Integer page;

    @SerializedName("results_per_page")
    private Integer resultsPerPage;

    @SerializedName("total_pages")
    private Integer totalPages;

    private List<Song> results;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Song> getResults() {
        return results;
    }

    public void setResults(List<Song> results) {
        this.results = results;
    }

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
