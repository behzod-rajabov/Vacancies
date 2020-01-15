package tuit.vacancies.uz.service;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tuit.vacancies.uz.model.Phone;

@Dao
public interface MyDao {

    @Insert
    public void addPhone(Phone phone);

    @Query("select * from phone")
    public List<Phone> getPhone();

    @Query("DELETE from phone where phone = :phone")
    public void deleteByIdPhone(String phone);

    @Delete
    public void deletePhone(Phone phone);

    @Update
    public void updatePhone(Phone phone);

    @Query("DELETE from phone")
    public void clearPhone();
}
