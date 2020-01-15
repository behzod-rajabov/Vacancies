package tuit.vacancies.uz.model;

public class User {
    private String id = "";
    private String fname = "";
    private String lname = "";
    private String phone = "";
    private String password = "";
    private String type = "";
    private String status = "";
    private String created_at = "";
    private String updated_at = "";

    public User() {
    }

    public User(String id, String fname, String lname, String phone, String password, String type, String status, String created_at, String updated_at) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.password = password;
        this.type = type;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
