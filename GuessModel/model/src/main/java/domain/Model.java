package domain;

public class Model extends Entity<Integer> {

    private String word;
    private String username;
    private String model;
    private Integer id;

    public Model(){}

    public Model(String word, String username) {
        this.word = word;
        this.username = username;
        this.model = word;
    }

    public String getWord() {

        return word;
    }

    public String getUsername() {

        return username;
    }

    public String getModel() {
        return model;
    }

    public void setWord(String word) {

        this.word = word;
    }

    public void setUsername(String password) {

        this.username = password;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getUsernameString(){
        return "" + username;
    }

    public String getWordString(){
        return "" + word;
    }
}
