package com.shafa.ali.kavir_msg.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.models.TiltlePostsModel;
import com.shafa.ali.kavir_msg.utility.FormatHelper;

import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.Holder> {
    private Context context;
    private List<TiltlePostsModel.PostsModel> tiltlePostsModels;

    public TitleAdapter(Context context, List<TiltlePostsModel.PostsModel> tiltlePostsModels) {
        this.context = context;
        this.tiltlePostsModels = tiltlePostsModels;
    }

    @Override
    public TitleAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_post,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        TiltlePostsModel.PostsModel  postsModel = tiltlePostsModels.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.titleTv.setText(Html.fromHtml(postsModel.getTitle(), Html.FROM_HTML_MODE_COMPACT));
            holder.contentTv.setText(Html.fromHtml(postsModel.getContent(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.titleTv.setText(Html.fromHtml(postsModel.getTitle()));
            holder.contentTv.setText(Html.fromHtml(postsModel.getContent()));
        }
        holder.autherTv.setText(postsModel.getAuthor());
        holder.dateTv.setText(FormatHelper.toPersianNumber(postsModel.getDate()));
        holder.commentCountTv.setText(FormatHelper.toPersianNumber(postsModel.getCommentCount()));
//        if (postsModel.getCommentCount().equalsIgnoreCase("0")){
//            holder.commentLinear.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return tiltlePostsModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView titleTv,contentTv,autherTv,dateTv,commentCountTv;
        private LinearLayout commentLinear;
        public Holder(View itemView) {
            super(itemView);
            titleTv = (TextView)itemView.findViewById(R.id.title_card);
            contentTv = (TextView)itemView.findViewById(R.id.content_card);
            autherTv =(TextView)itemView.findViewById(R.id.auther_card);
            dateTv =(TextView)itemView.findViewById(R.id.date_card);
            commentCountTv = (TextView)itemView.findViewById(R.id.comment_count_card);
            commentLinear =(LinearLayout)itemView.findViewById(R.id.comment_count_liner);
        }
    }
}
