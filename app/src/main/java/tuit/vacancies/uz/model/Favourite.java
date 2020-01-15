package tuit.vacancies.uz.model;

public class Favourite {
    private String key = "";
    private String user = "";
    private String vacancy = "";

    public Favourite() {
    }

    public Favourite(String key, String user, String vacancy) {
        this.key = key;
        this.user = user;
        this.vacancy = vacancy;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }
}
