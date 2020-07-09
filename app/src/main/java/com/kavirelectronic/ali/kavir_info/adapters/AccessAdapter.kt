package com.kavirelectronic.ali.kavir_info.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.models.AccessModel
import com.kavirelectronic.ali.kavir_info.utility.CircleTransform
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper
import com.kavirelectronic.ali.kavir_info.utility.Setting

class AccessAdapter(private val context: Context, private val accessModelList: List<AccessModel?>?) : RecyclerView.Adapter<AccessAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_access, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {
        val accessModel = accessModelList?.get(i)
        holder.groupNameTv.text = accessModel!!.groupName
        holder.groupDesTv.text = accessModel!!.groupDes
        holder.productDesTv.text = accessModel!!.prodectDes
        holder.productIdTv.text = FormatHelper.toPersianNumber(accessModel!!.productId)
        Glide.with(context)
                .load(Setting.CATEGORY_IMAGES_URL + "p/" + accessModel!!.productId + ".png")
                .override(90, 90)
                .centerCrop()
                .crossFade()
                .transform(CircleTransform(context))
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return accessModelList!!.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupNameTv: TextView
        val groupDesTv: TextView
        val productIdTv: TextView
        val productDesTv: TextView
        val imageView: ImageView

        init {
            groupNameTv = itemView.findViewById(R.id.item_access_group_name)
            groupDesTv = itemView.findViewById(R.id.item_access_group_des)
            productIdTv = itemView.findViewById(R.id.item_access_prodect_id)
            productDesTv = itemView.findViewById(R.id.item_access_prodect_des)
            imageView = itemView.findViewById(R.id.item_access_image)
        }
    }

}