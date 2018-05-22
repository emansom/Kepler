package org.alexdev.kepler.game.player;

public class PlayerDetails {
    private int id;
    private String username;
    private String password;
    private String figure;
    private String poolFigure;
    private int credits;
    private String motto;
    private String consoleMotto;
    private String sex;
    private int tickets;
    private int film;
    private int rank;
    private long lastOnline;
    private long clubSubscribed;
    private long clubExpiration;
    private String badge;
    private String badgeActive;

    public PlayerDetails() { }

    /**
     * Fill the player data for the entity.
     *
     * @param id the id to add
     * @param username the username
     * @param password the password
     * @param figure the figure
     * @param poolFigure the pool figure
     * @param credits the credits
     * @param motto the motto
     * @param consoleMotto the console motto
     * @param sex the sex
     * @param tickets the tickets
     * @param film the film
     * @param rank the rank
     * @param lastOnline the last time they were online in a unix timestamp
     * @param clubSubscribed the club subscribed date in a unix timestamp
     * @param clubExpiration the club expiration date in a unix timestamp
     * @param badge their badges
     * @param badgeActive the current badge
     */
    public void fill(int id, String username, String password, String figure, String poolFigure, int credits, String motto, String consoleMotto, String sex, int tickets, int film, int rank, long lastOnline, long clubSubscribed, long clubExpiration, String badge, String badgeActive) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.figure = figure;
        this.poolFigure = poolFigure;
        this.credits = credits;
        this.motto = motto;
        this.consoleMotto = consoleMotto;
        this.sex = sex;
        this.tickets = tickets;
        this.film = film;
        this.rank = rank;
        this.lastOnline = lastOnline;
        this.clubSubscribed = clubSubscribed;
        this.clubExpiration = clubExpiration;
        this.badge = badge;
        this.badgeActive = badgeActive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return username;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getPoolFigure() {
        return poolFigure;
    }

    public void setPoolFigure(String poolFigure) {
        this.poolFigure = poolFigure;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getConsoleMotto() {
        return consoleMotto;
    }

    public void setConsoleMotto(String consoleMotto) {
        this.consoleMotto = consoleMotto;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public int getFilm() {
        return film;
    }

    public void setFilm(int film) {
        this.film = film;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public long getClubSubscribed() {
        return clubSubscribed;
    }

    public void setClubSubscribed(long clubSubscribed) {
        this.clubSubscribed = clubSubscribed;
    }

    public long getClubExpiration() {
        return clubExpiration;
    }

    public void setClubExpiration(long clubExpiration) {
        this.clubExpiration = clubExpiration;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getBadgeActive() {
        return badgeActive;
    }

    public void setBadgeActive(String badgeActive) {
        this.badgeActive = badgeActive;
    }
}
