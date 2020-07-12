package com.kavirelectronic.ali.kavir_info.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.models.SubCategoryModel
import com.kavirelectronic.ali.kavir_info.utility.CircleTransform
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.kavirelectronic.ali.kavir_info.utility.Setting

class SubCategoryAdapter(private val context: Context, private val subCategoryModelList: List<SubCategoryModel?>?) : RecyclerView.Adapter<SubCategoryAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val subCategoryModel = subCategoryModelList!![position]
        holder.id.text = subCategoryModel!!.id.toString()
        holder.title.text = subCategoryModel!!.title
        holder.description.text = subCategoryModel!!.description
        holder.postCount.text = FormatHelper.toPersianNumber(subCategoryModel!!.post_count.toString())
        holder.subCatLine.visibility = View.GONE
        val newPosts = getNewPosts(subCategoryModel!!.id.toString(), subCategoryModel!!.post_count)
        if (newPosts == "0") {
            holder.newPost.visibility = View.GONE
        } else {
            holder.newPost.text = FormatHelper.toPersianNumber(newPosts)
        }
        Glide.with(context)
                .load(Setting.CATEGORY_IMAGES_URL + subCategoryModel!!.id + ".png")
                .override(90, 90)
                .centerCrop()
                .crossFade()
                .transform(CircleTransform(context))
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageCategory)
    }

    override fun getItemCount(): Int {
        return subCategoryModelList!!.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: TextView
        var title: TextView
        var description: TextView
        var postCount: TextView
        var newPost: TextView
        var imageCategory: ImageView
        var subCatLine: LinearLayout

        init {
            ///
            id = itemView.findViewById<View>(R.id.id_category) as TextView
            title = itemView.findViewById<View>(R.id.name_category) as TextView
            description = itemView.findViewById<View>(R.id.desp_category) as TextView
            postCount = itemView.findViewById<View>(R.id.count_post_category) as TextView
            imageCategory = itemView.findViewById<View>(R.id.image_category) as ImageView
            newPost = itemView.findViewById<View>(R.id.new_post_category) as TextView
            subCatLine = itemView.findViewById<View>(R.id.sub_line) as LinearLayout
        }
    }

    private fun getNewPosts(postId: String, totalPost: Int): String {
        return (totalPost - SaveItem.getItem(context, "post:$postId", "0").toInt()).toString()
    }

}