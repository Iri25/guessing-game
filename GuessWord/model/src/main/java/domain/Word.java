package domain;

public class Word extends Entity<Integer> {

    private String word;
    private String username;
    private Integer id;

    public Word(){}

    public Word(String word, String username) {
        this.word = word;
        this.username = username;
    }

    public String getWord() {

        return word;
    }

    public String getUsername() {

        return username;
    }

    public void setWord(String word) {

        this.word = word;
    }

    public void setUsername(String password) {

        this.username = password;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + getId() +
                ", word='" + word + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

}
