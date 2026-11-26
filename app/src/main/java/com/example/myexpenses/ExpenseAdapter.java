package com.example.myexpenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenses=new ArrayList<>();




    public interface OnExpenseLongClickListener{
        void onLongClick(Expense expense);

    }
    private final OnExpenseLongClickListener listener;
    public ExpenseAdapter(OnExpenseLongClickListener listener) {
        this.listener = listener;
    }
    public void setExpenses(List<Expense> expenses){
        if (expenses!=null){
            this.expenses=expenses;
        }else {
            this.expenses=new ArrayList<>();
        }
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ExpenseAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense,parent,false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseViewHolder holder, int position) {
        Expense expense=expenses.get(position);
        holder.tvExpenseName.setText(expense.getTitle());
        holder.tvExpenseAmount.setText(expense.getAmount()+ " ₪");
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(expense);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
    static class ExpenseViewHolder extends RecyclerView.ViewHolder{
        TextView tvExpenseName, tvExpenseAmount;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExpenseName=itemView.findViewById(R.id.tvExpenseName);
            tvExpenseAmount=itemView.findViewById(R.id.tvExpenseAmount);
        }
    }
}
