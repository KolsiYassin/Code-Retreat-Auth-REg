package com.example.viadee_coderetreat_app.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String id;
    private String username;
    private String email;
    private String role;
    private int numOfSessions = 0;
    //private Set<BadgeAward> badges = new HashSet<BadgeAward>();
    private int points;

    public User() {} // Required for Firebase

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getNumOfSessions() {
        return numOfSessions;
    }

    public void addNumOfSessions() {
        this.numOfSessions += 1;
    }

    public int getPoints() {
        return points;
    }
    public void addPoints(int points) {
        this.points += points;
    }
//    public Set<BadgeAward> getBadges() {
//        return badges;
//    }
//    public void addBadges(Set<BadgeAward> badges) {
//        this.badges.addAll(badges);
//    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
}

