package com.example.myexpenses;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    //{id, name, budget}
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private double budget;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }


    public Category(String name, double budget) {
        this.name = name;
        this.budget = budget;
    }
}
