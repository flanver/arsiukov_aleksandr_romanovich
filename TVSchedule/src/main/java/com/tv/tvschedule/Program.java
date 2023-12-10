package com.tv.tvschedule;

public class Program {
    private String channel;
    private String dayOfWeek;
    private String startTime;
    private String genre;

    public Program(String channel, String dayOfWeek, String startTime, String genre) {
        this.channel = channel;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.genre = genre;
    }

    public String getChannel() {
        return channel;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getGenre() {
        return genre;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
