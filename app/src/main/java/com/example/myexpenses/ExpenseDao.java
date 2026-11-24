package com.example.myexpenses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insertExpense(Expense...expenses);
    @Delete
    void deleteExpense(Expense...expenses);
    @Query(" select * from expenses where category_id=:categoryId order by createdAt  desc")
    LiveData<List<Expense>> getByCategory(int categoryId);
}
