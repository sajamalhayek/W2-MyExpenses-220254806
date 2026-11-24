package com.example.myexpenses;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import com.example.myexpenses.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ExpenseDatabase database;
    private CategoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database= ExpenseDatabase.getDatabase(this);
        adapter= new CategoryAdapter(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onClick(CategoryTotal category) {
                Intent intent=new Intent(MainActivity.this, ExpenseActivity.class);
                intent.putExtra("CategoryId", category.id);
                intent.putExtra("CategoryName",category.name);
                startActivity(intent);
            }

            @Override
            public void onLongClick(CategoryTotal category) {
                showDeleteDialog(category);

            }
        });
        binding.recyclerCategories.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.recyclerCategories.setAdapter(adapter);

        database.categoryDao()
                .getCategoriesWithTotal()
                .observe(this, new Observer<List<CategoryTotal>>() {

                    @Override
                    public void onChanged(List<CategoryTotal> categories) {
                        adapter.setCategories(categories);
                    }
                });
        binding.btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

    }

    private void addCategory(){
        String name=binding.etCategoryName.getText().toString().trim();
        String budgetText=binding.etBudget.getText().toString().trim();

        if (name.isEmpty()){
            binding.etCategoryName.setError("Enter category name");
            return;
        }
        if (budgetText.isEmpty()){
            binding.etBudget.setError("Enter budget");
            return;
        }
        double budget;
        try {
            budget=Double.parseDouble(budgetText);
        }catch (NumberFormatException e){
            binding.etBudget.setError("Invalid budget");
            return;
        }
        if (budget<0){
            binding.etBudget.setError("Enter a valid budget");
            return;
        }

        Category category=new Category(name, budget);
        ExpenseDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                database.categoryDao().insertCategory(category);
            }
        });
        binding.etCategoryName.setText("");
        binding.etBudget.setText("");
    }


    private void showDeleteDialog(CategoryTotal category){
        new AlertDialog.Builder(this).setTitle("Delete "+ category.name+"?")
                .setMessage( "All expenses in this category will also be deleted." )
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ExpenseDatabase.databaseWriteExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Category item =new Category(category.name, category.budget);
                        item.setId(category.id);
                        database.categoryDao().deleteCategory(item);
                    }
                });
            }
        })
            .show();

    }

}