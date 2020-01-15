package tuit.vacancies.uz.model;

public class Top {
    private String id = "";
    private String vacancy_id = "";
    private String days_count = "";
    private String created_at = "";
    private String updated_at = "";

    public Top() {
    }

    public Top(String id, String vacancy_id, String days_count, String created_at, String updated_at) {
        this.id = id;
        this.vacancy_id = vacancy_id;
        this.days_count = days_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVacancy_id() {
        return vacancy_id;
    }

    public void setVacancy_id(String vacancy_id) {
        this.vacancy_id = vacancy_id;
    }

    public String getDays_count() {
        return days_count;
    }

    public void setDays_count(String days_count) {
        this.days_count = days_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
