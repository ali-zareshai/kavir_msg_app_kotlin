package com.kavirelectronic.ali.kavir_info.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.models.CategoryModel
import com.kavirelectronic.ali.kavir_info.utility.CircleTransform
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.kavirelectronic.ali.kavir_info.utility.Setting

class CategoryAdapter(private val context: Context, private val categoryModelList: List<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val categoryModel = categoryModelList[position]
        holder.id.text = categoryModel.id.toString()
        holder.title.text = categoryModel.title
        holder.description.text = FormatHelper.toPersianNumber(categoryModel.description)
        holder.postCount.text = FormatHelper.toPersianNumber(categoryModel.post_count.toString())
        holder.subCount.text = FormatHelper.toPersianNumber(categoryModel.subCount.toString() + "")
        var newPosts = ""
        try {
            newPosts = getNewPosts(categoryModel.id.toString(), categoryModel.post_count)
        } catch (e: Exception) {
            Log.e("CategoryAdapter new:", e.message)
        }
        if (newPosts == "0") {
            holder.newPost.visibility = View.GONE
        } else {
            holder.newPost.text = FormatHelper.toPersianNumber(newPosts)
        }
        Glide.with(context)
                .load(Setting.CATEGORY_IMAGES_URL + categoryModel.id + ".png")
                .override(90, 90)
                .centerCrop()
                .crossFade()
                .transform(CircleTransform(context))
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageCategory)
    }

    override fun getItemCount(): Int {
        return categoryModelList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: TextView
        var title: TextView
        var description: TextView
        var postCount: TextView
        var newPost: TextView
        var subCount: TextView
        var imageCategory: ImageView

        init {
            ///
            id = itemView.findViewById<View>(R.id.id_category) as TextView
            title = itemView.findViewById<View>(R.id.name_category) as TextView
            description = itemView.findViewById<View>(R.id.desp_category) as TextView
            postCount = itemView.findViewById<View>(R.id.count_post_category) as TextView
            imageCategory = itemView.findViewById<View>(R.id.image_category) as ImageView
            newPost = itemView.findViewById<View>(R.id.new_post_category) as TextView
            subCount = itemView.findViewById<View>(R.id.count_sub_category) as TextView
        }
    }

    private fun getNewPosts(postId: String, totalPost: Int): String {
        return (totalPost - SaveItem.getItem(context, "post:$postId", "0").toInt()).toString()
    }

}