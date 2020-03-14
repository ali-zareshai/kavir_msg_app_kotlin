package com.shafa.ali.kavir_msg.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.models.AccessModel;
import com.shafa.ali.kavir_msg.utility.CircleTransform;
import com.shafa.ali.kavir_msg.utility.FormatHelper;
import com.shafa.ali.kavir_msg.utility.Setting;

import java.util.List;

public class AccessAdapter extends RecyclerView.Adapter<AccessAdapter.Holder> {
    private Context context;
    private List<AccessModel> accessModelList;

    public AccessAdapter(Context context, List<AccessModel> accessModelList) {
        this.context = context;
        this.accessModelList = accessModelList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        AccessModel accessModel = accessModelList.get(i);
        holder.groupNameTv.setText(accessModel.getGroupName());
        holder.groupDesTv.setText(accessModel.getGroupDes());
        holder.productDesTv.setText(accessModel.getProdectDes());
        holder.productIdTv.setText(FormatHelper.toPersianNumber(accessModel.getProductId()));

        Glide.with(context)
                .load(Setting.CATEGORY_IMAGES_URL+"p/"+accessModel.getProductId()+".png")
                .override(90, 90)
                .centerCrop()
                .crossFade()
                .transform(new CircleTransform(context))
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return accessModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView groupNameTv,groupDesTv,productIdTv,productDesTv;
        private ImageView imageView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            groupNameTv = itemView.findViewById(R.id.item_access_group_name);
            groupDesTv  = itemView.findViewById(R.id.item_access_group_des);
            productIdTv = itemView.findViewById(R.id.item_access_prodect_id);
            productDesTv= itemView.findViewById(R.id.item_access_prodect_des);
            imageView   = itemView.findViewById(R.id.item_access_image);
        }
    }
}
