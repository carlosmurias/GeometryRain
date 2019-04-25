package games.ameba.geometryrain;

import java.util.Date;

public class RankedUser {
    private long score;
    private Date date;
    private String username;
    private String country;

    public RankedUser() {
    }

    public RankedUser(long score, Date date, String username, String country) {
        this.score = score;
        this.date = date;
        this.username = username;
        this.country = country;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "RankedUser{" +
                "score=" + score +
                ", date=" + date +
                ", username='" + username + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}

