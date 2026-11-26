package com.example.myexpenses;

import static android.app.ProgressDialog.show;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myexpenses.databinding.ActivityExpenseBinding;

import java.util.List;

public class ExpenseActivity extends AppCompatActivity {
    private ActivityExpenseBinding binding;
    private ExpenseDatabase database;
    private ExpenseAdapter adapter;
    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categoryId=getIntent().getIntExtra("CategoryId",-1);
        categoryName=getIntent().getStringExtra("CategoryName");

        if (categoryId==-1 ||categoryName==null){
            finish();
            return;
        }
        binding.tvExpenseTitle.setText("Expense - "+ categoryName);
        database=ExpenseDatabase.getDatabase(this);

        adapter=new ExpenseAdapter(new ExpenseAdapter.OnExpenseLongClickListener() {
            @Override
            public void onLongClick(Expense expense) {
                showDeleteDialog(expense);
            }
        });
        binding.recyclerExpenses.setLayoutManager(new LinearLayoutManager(ExpenseActivity.this));
        binding.recyclerExpenses.setAdapter(adapter);
        database.expenseDao().getByCategory(categoryId).observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                adapter.setExpenses(expenses);
            }
        });



        binding.btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense();
            }
        });

    }
    private void showDeleteDialog(Expense expense){
        new AlertDialog.Builder(this)
                .setTitle("Delete "+expense.getTitle())
                .setMessage("Are you sure you want to delete? "+ expense.getTitle())
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ExpenseDatabase.databaseWriteExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                database.expenseDao().deleteExpense(expense);
                            }
                        });
                    }
                })
        .show();
    }

    private void addExpense(){
        String title=binding.etExpenseTitle.getText().toString().trim();
        String amountText=binding.etAmount.getText().toString().trim();
        if (title.isEmpty()){
            binding.etExpenseTitle.setError("Enter expense title");
            return;
        }
        if (amountText.isEmpty()){
            binding.etAmount.setError("Enter amount");
            return;
        }
        double amount;
        try {
            amount=Double.parseDouble(amountText);

        }catch (NumberFormatException e){
            binding.etAmount.setError("Invalid amount");
            return;
        }
        if (amount<=0){
            binding.etAmount.setError("Enter a valid amount");
            return;
        }
        Expense expense=new Expense(categoryId, title, amount);
        ExpenseDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                database.expenseDao().insertExpense(expense);
            }
        });
        binding.etExpenseTitle.setText("");
        binding.etAmount.setText("");
    }

}