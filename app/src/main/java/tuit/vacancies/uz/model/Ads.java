package tuit.vacancies.uz.model;

public class Ads {

    private String id = "";
    private String title = "";
    private String image = "";
    private String views_count = "";
    private String status = "";
    private String created_at = "";
    private String updated_at = "";

    public Ads() {
    }

    public Ads(String id, String title, String image, String views_count, String status, String created_at, String updated_at) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.views_count = views_count;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getViews_count() {
        return views_count;
    }

    public void setViews_count(String views_count) {
        this.views_count = views_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
