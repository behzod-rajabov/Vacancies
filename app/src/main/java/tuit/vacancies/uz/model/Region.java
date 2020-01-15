package tuit.vacancies.uz.model;

public class Region {
    private String id = "";
    private String parent_id = "";
    private String name = "";
    private String created_at = "";
    private String updated_at = "";

    public Region() {
    }

    public Region(String id, String parent_id, String name, String created_at, String updated_at) {
        this.id = id;
        this.parent_id = parent_id;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
