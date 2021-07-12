package domain;

public class Round extends Entity<Integer>{

    private String username;
    private String word;
    private Integer points;
    private Integer place;

    public Round(){}

    public Round(String username, String word, Integer points){
        this.username = username;
        this.word = word;
        this.points = points;
        this.place = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getWord() {
        return word;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getPlace() {
        return place;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "Round{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", word='" + word + '\'' +
                ", points='" + points + '\'' +
                '}';
    }

    public String getIdString() {
        return super.getId().toString();
    }

    public String getUsernameString() {
        return "" + username;
    }

    public String getWordString() {
        return "" + word;
    }

    public String getPointsString() {
        return "" + points;
    }

    public String getPlaceString() {
        return "" + place;
    }
}
