package com.kavirelectronic.ali.kavir_info.adapters

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.models.CommentModel
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper

class CommentAdapter(private val commentModelList: List<CommentModel>, private val context: Context) : RecyclerView.Adapter<CommentAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val commentModel = commentModelList[position]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.titleTv.text = Html.fromHtml(commentModel.name, Html.FROM_HTML_MODE_COMPACT)
            holder.contentTv.text = Html.fromHtml(commentModel.content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            holder.titleTv.text = Html.fromHtml(commentModel.name)
            holder.contentTv.text = Html.fromHtml(commentModel.content)
        }
        holder.dateTv.text = FormatHelper.toPersianNumber(commentModel.date)
    }

    override fun getItemCount(): Int {
        return commentModelList.size
    }

     inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView
        val contentTv: TextView
        val dateTv: TextView

        init {
            titleTv = itemView.findViewById<View>(R.id.title_comment_card) as TextView
            contentTv = itemView.findViewById<View>(R.id.content_comment_card) as TextView
            dateTv = itemView.findViewById<View>(R.id.date_comment_card) as TextView
        }
    }

}