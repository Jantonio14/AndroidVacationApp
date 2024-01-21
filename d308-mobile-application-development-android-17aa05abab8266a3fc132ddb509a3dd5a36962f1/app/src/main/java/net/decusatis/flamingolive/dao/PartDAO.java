package net.decusatis.flamingolive.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import net.decusatis.flamingolive.entities.Part;

import java.util.List;

@Dao
public interface PartDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Part part);

    @Update
    void update(Part part);

    @Delete
    void delete(Part part);

    @Query("SELECT * FROM PARTS ORDER BY partID ASC")
    List<Part> getAllParts();

    @Query("SELECT * FROM PARTS WHERE productID=:prod ORDER BY partID ASC ")
    List<Part> getAssociatedParts(int prod);
}
