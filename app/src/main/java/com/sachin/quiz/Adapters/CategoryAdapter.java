package com.sachin.quiz.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sachin.quiz.Models.CategoryModel;
import com.sachin.quiz.R;
import com.sachin.quiz.SubCategoryActivity;
import com.sachin.quiz.databinding.RvCategoryDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {

    Context context;
    ArrayList<CategoryModel>list;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_category_design,parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        CategoryModel categoryModel = list.get(position);

        holder.binding.categoryName.setText(categoryModel.getCategoryName());

        Picasso.get().load(categoryModel.getCategoryImage()).placeholder(R.drawable.logo).into(holder.binding.categoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra("catId", categoryModel.getKey());
                intent.putExtra("name",categoryModel.getCategoryName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        RvCategoryDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RvCategoryDesignBinding.bind(itemView);
        }
    }

}
