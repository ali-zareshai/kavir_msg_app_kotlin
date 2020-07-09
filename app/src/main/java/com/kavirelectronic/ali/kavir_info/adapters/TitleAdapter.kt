package com.kavirelectronic.ali.kavir_info.adapters

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.models.TiltlePostsModel.PostsModel
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper

class TitleAdapter(private val context: Context, private val tiltlePostsModels: List<PostsModel>) : RecyclerView.Adapter<TitleAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_title_post, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val postsModel = tiltlePostsModels[position]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.titleTv.text = Html.fromHtml(postsModel.title, Html.FROM_HTML_MODE_COMPACT)
            holder.contentTv.text = Html.fromHtml(postsModel.content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            holder.titleTv.text = Html.fromHtml(postsModel.title)
            holder.contentTv.text = Html.fromHtml(postsModel.content)
        }
        holder.autherTv.text = postsModel.author
        holder.dateTv.text = FormatHelper.toPersianNumber(postsModel.date)
        holder.commentCountTv.text = FormatHelper.toPersianNumber(postsModel.commentCount)
        //        if (postsModel.getCommentCount().equalsIgnoreCase("0")){
//            holder.commentLinear.setVisibility(View.GONE);
//        }
    }

    override fun getItemCount(): Int {
        return tiltlePostsModels.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView
        val contentTv: TextView
        val autherTv: TextView
        val dateTv: TextView
        val commentCountTv: TextView
        val commentLinear: LinearLayout

        init {
            titleTv = itemView.findViewById<View>(R.id.title_card) as TextView
            contentTv = itemView.findViewById<View>(R.id.content_card) as TextView
            autherTv = itemView.findViewById<View>(R.id.auther_card) as TextView
            dateTv = itemView.findViewById<View>(R.id.date_card) as TextView
            commentCountTv = itemView.findViewById<View>(R.id.comment_count_card) as TextView
            commentLinear = itemView.findViewById<View>(R.id.comment_count_liner) as LinearLayout
        }
    }

}