package com.example.myexpenses;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>  {
    private List<CategoryTotal> categories= new ArrayList<>();
    public interface OnCategoryClickListener{
        void onClick(CategoryTotal categoryTotal);
        void onLongClick(CategoryTotal categoryTotal);
    }
    private final OnCategoryClickListener listener;
    public CategoryAdapter(OnCategoryClickListener listener){
        this.listener=listener;
    }

    public void setCategories(List<CategoryTotal> categories) {
        this.categories = categories !=null?categories : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new CategoryViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        CategoryTotal category= categories.get(position);
        holder.tvCategoryName.setText(category.name);
        holder.tvCategoryTotal.setText(category.total +" / "+ category.budget +" ₪");
        if (category.total> category.budget){
            holder.tvCategoryTotal.setTextColor(Color.RED);
        }else {
            holder.tvCategoryTotal.setTextColor(Color.parseColor("#444444"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(category);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(category);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
    static class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView tvCategoryName, tvCategoryTotal;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName=itemView.findViewById(R.id.tvCategoryName);
            tvCategoryTotal=itemView.findViewById(R.id.tvCategoryTotal);
        }
    }
}
