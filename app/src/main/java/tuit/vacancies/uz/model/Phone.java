package tuit.vacancies.uz.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "phone")
public class Phone {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "vacancy_key")
    private String vacancyKey;

    @ColumnInfo(name = "user_key")
    private String userKey;

    @ColumnInfo(name = "vacancy_name")
    private String vacancyName;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "date")
    private String date;

    public Phone(String vacancyKey, String userKey, String vacancyName, String userName, String phone, String date) {
        this.vacancyKey = vacancyKey;
        this.userKey = userKey;
        this.vacancyName = vacancyName;
        this.userName = userName;
        this.phone = phone;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVacancyKey() {
        return vacancyKey;
    }

    public void setVacancyKey(String vacancyKey) {
        this.vacancyKey = vacancyKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getVacancyName() {
        return vacancyName;
    }

    public void setVacancyName(String vacancyName) {
        this.vacancyName = vacancyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
