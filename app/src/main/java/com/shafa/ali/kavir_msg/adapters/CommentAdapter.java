package com.shafa.ali.kavir_msg.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.models.CommentModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {
    private List<CommentModel> commentModelList;
    private Context context;

    public CommentAdapter(List<CommentModel> commentModelList, Context context) {
        this.commentModelList = commentModelList;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        CommentModel commentModel = commentModelList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.titleTv.setText(Html.fromHtml(commentModel.getName(),Html.FROM_HTML_MODE_COMPACT));
            holder.contentTv.setText(Html.fromHtml(commentModel.getContent(),Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.titleTv.setText(Html.fromHtml(commentModel.getName()));
            holder.contentTv.setText(Html.fromHtml(commentModel.getContent()));
        }
        holder.dateTv.setText(commentModel.getDate());
    }


    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        private TextView titleTv,contentTv,dateTv;

        public Holder(View itemView) {
            super(itemView);
            titleTv = (TextView)itemView.findViewById(R.id.title_comment_card);
            contentTv =(TextView)itemView.findViewById(R.id.content_comment_card);
            dateTv =(TextView)itemView.findViewById(R.id.date_comment_card);
        }
    }
}
