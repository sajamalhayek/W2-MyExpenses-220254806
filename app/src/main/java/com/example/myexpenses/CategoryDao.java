package com.example.myexpenses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(Category...categories);
    @Delete
    void deleteCategory(Category...categories);

    @Query("select categories.id, categories.name, categories.budget, " +
             " COALESCE(SUM(expenses.amount),0)AS total"+
            " FROM categories "+
            "LEFT JOIN expenses ON categories.id=expenses.category_id"+
    " GROUP BY categories.id, categories.name, categories.budget")
    LiveData<List<CategoryTotal>> getCategoriesWithTotal();
}
