package com.shafa.ali.kavir_msg.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.models.CategoryModel;
import com.shafa.ali.kavir_msg.utility.CircleTransform;
import com.shafa.ali.kavir_msg.utility.FormatHelper;
import com.shafa.ali.kavir_msg.utility.Setting;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {
    private Context context;
    private List<CategoryModel> categoryModelList;

    public CategoryAdapter(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        CategoryModel categoryModel = categoryModelList.get(position);

        holder.id.setText(String.valueOf(categoryModel.getId()));
        holder.title.setText(categoryModel.getTitle());
        holder.description.setText(FormatHelper.toPersianNumber(categoryModel.getDescription()));
        holder.postCount.setText(String.valueOf(categoryModel.getPost_count()));
        Glide.with(context)
                .load(Setting.CATEGORY_IMAGES_URL+categoryModel.getSlug()+".png")
                .override(90, 90)
                .centerCrop()
                .crossFade()
                .transform(new CircleTransform(context))
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageCategory);

    }


    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
         TextView id,title,description,postCount;
         ImageView imageCategory;

        public Holder(View itemView) {
            super(itemView);
            ///
            id = (TextView)itemView.findViewById(R.id.id_category);
            title=(TextView)itemView.findViewById(R.id.name_category);
            description=(TextView)itemView.findViewById(R.id.desp_category);
            postCount=(TextView)itemView.findViewById(R.id.count_category);
            imageCategory=(ImageView)itemView.findViewById(R.id.image_category);

        }

    }
}
