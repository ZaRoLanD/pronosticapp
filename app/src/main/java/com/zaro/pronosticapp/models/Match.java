package com.zaro.pronosticapp.models;

import android.util.Log;

public class Match {
    private static final String TAG = "Match";

    private String id;
    private String country;
    private String league;
    private String team1;
    private String team2;
    private long matchTime;
    private String pronostic;
    private String status;
    private boolean isVisible;
    private long visibleFrom;
    private long createdAt;
    private long updatedAt;

    // Constructeur vide requis pour Firebase
    public Match() {
    }

    // Constructeur complet
    public Match(String id, String country, String league, String team1, String team2,
                 long matchTime, String pronostic, String status, boolean isVisible,
                 long visibleFrom) {
        this.id = id;
        this.country = country;
        this.league = league;
        this.team1 = team1;
        this.team2 = team2;
        this.matchTime = matchTime;
        this.pronostic = pronostic;
        this.status = status;
        this.isVisible = isVisible;
        this.visibleFrom = visibleFrom;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getLeague() {
        return league;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public long getMatchTime() {
        return matchTime;
    }

    public String getPronostic() {
        return pronostic;
    }

    public String getStatus() {
        return status;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public long getVisibleFrom() {
        return visibleFrom;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setMatchTime(long matchTime) {
        this.matchTime = matchTime;
    }

    public void setPronostic(String pronostic) {
        this.pronostic = pronostic;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = System.currentTimeMillis();
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void setVisibleFrom(long visibleFrom) {
        this.visibleFrom = visibleFrom;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Méthodes utilitaires
    public String getTeamsDisplay() {
        return team1 + " vs " + team2;
    }

    public boolean isMatchStarted() {
        return System.currentTimeMillis() >= matchTime;
    }

    // VERSION SIMPLIFIÉE de shouldBeVisible
    public boolean shouldBeVisible() {
        long currentTime = System.currentTimeMillis();
        boolean result = isVisible && currentTime >= visibleFrom;

        Log.d(TAG, "shouldBeVisible() pour " + getTeamsDisplay());
        Log.d(TAG, "  isVisible: " + isVisible);
        Log.d(TAG, "  currentTime: " + currentTime);
        Log.d(TAG, "  visibleFrom: " + visibleFrom);
        Log.d(TAG, "  Résultat: " + result);

        return result;
    }

    public long getTimeUntilMatch() {
        return matchTime - System.currentTimeMillis();
    }
}