package com.zebia.model;

public class Song {

    //@SerializedName("from_user_name")
    private String artist_id;
    private String artist_latitude;
    private String artist_longitude;
    private String artist_location;
    private String artist_name;
    //private String[] artist_terms; //Echo Nest tags
    private String audio_md5;
    private float danceability;
	private float duration;
	private float energy;
	private float loudness;
	private String release;
    private String[] similar_artists;
	private float song_hotttnesss;
	private String song_id;
	private float tempo;
	private String title;
	private String track_id;
	private int year;

    public Song() {
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_latitude() {
        return artist_latitude;
    }

    public void setArtist_latitude(String artist_latitude) {
        this.artist_latitude = artist_latitude;
    }

    public String getArtist_location() {
        return artist_location;
    }

    public void setArtist_location(String artist_location) {
        this.artist_location = artist_location;
    }

    public String getArtist_longitude() {
        return artist_longitude;
    }

    public void setArtist_longitude(String artist_longitude) {
        this.artist_longitude = artist_longitude;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

/*
    public String[] getArtist_terms() {
        return artist_terms;
    }

    public void setArtist_terms(String[] artist_terms) {
        this.artist_terms = artist_terms;
    }
*/

    public String getAudio_md5() {
        return audio_md5;
    }

    public void setAudio_md5(String audio_md5) {
        this.audio_md5 = audio_md5;
    }

    public float getDanceability() {
        return danceability;
    }

    public void setDanceability(float danceability) {
        this.danceability = danceability;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getLoudness() {
        return loudness;
    }

    public void setLoudness(float loudness) {
        this.loudness = loudness;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String[] getSimilar_artists() {
        return similar_artists;
    }

    public void setSimilar_artists(String[] similar_artists) {
        this.similar_artists = similar_artists;
    }

    public float getSong_hotttnesss() {
        return song_hotttnesss;
    }

    public void setSong_hotttnesss(float song_hotttnesss) {
        this.song_hotttnesss = song_hotttnesss;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
