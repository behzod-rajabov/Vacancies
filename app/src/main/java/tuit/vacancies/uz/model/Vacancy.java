package tuit.vacancies.uz.model;

public class Vacancy {

    private String id = "";
    private String category_id = "";
    private String category_name = "";
    private String region_id = "";
    private String region_name = "";
    private String author_id = "";
    private String author_name = "";
    private String phone = "";
    private String image = "";
    private String title = "";
    private String description = "";
    private String salary_from = "";
    private String salary_to = "";
    private String address = "";
    private String graph = "";  // ish grafigi
    private String type = "";   // ish turi
    private String status = "1";
    private String views_count = "";
    private String created_at = "";
    private String updated_at = "";

    public Vacancy() {
    }

    public Vacancy(String id, String category_id, String category_name, String region_id, String region_name, String author_id, String author_name, String phone, String image, String title, String description, String salary_from, String salary_to, String address, String graph, String type, String status, String views_count, String created_at, String updated_at) {
        this.id = id;
        this.category_id = category_id;
        this.category_name = category_name;
        this.region_id = region_id;
        this.region_name = region_name;
        this.author_id = author_id;
        this.author_name = author_name;
        this.phone = phone;
        this.image = image;
        this.title = title;
        this.description = description;
        this.salary_from = salary_from;
        this.salary_to = salary_to;
        this.address = address;
        this.graph = graph;
        this.type = type;
        this.status = status;
        this.views_count = views_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalary_from() {
        return salary_from;
    }

    public void setSalary_from(String salary_from) {
        this.salary_from = salary_from;
    }

    public String getSalary_to() {
        return salary_to;
    }

    public void setSalary_to(String salary_to) {
        this.salary_to = salary_to;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getViews_count() {
        return views_count;
    }

    public void setViews_count(String views_count) {
        this.views_count = views_count;
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

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }
}
