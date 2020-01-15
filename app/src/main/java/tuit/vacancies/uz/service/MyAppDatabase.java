package tuit.vacancies.uz.service;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import tuit.vacancies.uz.model.Phone;

@Database(entities = {Phone.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase {
    public abstract MyDao myDao();
}
