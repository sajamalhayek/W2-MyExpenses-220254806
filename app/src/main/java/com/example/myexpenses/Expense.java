package com.example.myexpenses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity (tableName = "expenses",
        foreignKeys = {@ForeignKey(entity = Category.class,
        parentColumns = {"id"}, childColumns = {"category_id"},
        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)}, indices = {@Index("category_id")})
public class Expense {
@PrimaryKey(autoGenerate = true)
@NonNull
    private int id;
@ColumnInfo(name = "category_id")
    private int categoryId;
    private  String title;
    private double amount;
    private long createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt =  System.currentTimeMillis();
    }



    public Expense( int categoryId, String title, double amount, long createdAt) {
        this.categoryId = categoryId;
        this.title = title;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}
