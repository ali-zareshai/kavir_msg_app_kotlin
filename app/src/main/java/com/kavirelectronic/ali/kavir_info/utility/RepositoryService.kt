package com.kavirelectronic.ali.kavir_info.utility

import android.content.Context
import android.util.Log
import android.view.View
import com.kavirelectronic.ali.kavir_info.db.models.Category
import com.kavirelectronic.ali.kavir_info.models.CategoryModel
import com.kavirelectronic.ali.kavir_info.models.SubCategoryModel
import com.kavirelectronic.ali.kavir_info.server.GetDataCategory
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryService(val context: Context) {
    private lateinit var myCategoryCallback:CategoryCallback

    public fun getCategory(catId:String,categoryCallback: CategoryCallback){
        myCategoryCallback =categoryCallback
        val output:List<CategoryModel?>? =getCategoryFromDatabase(catId)
        if (output!=null && output.size>0){
            myCategoryCallback.getCategoryList(output)
        }else{
            if (catId.equals("0")){
                getCategoryFromNetwork()
            }else{
                getSubCategoryFromNetwork(catId)
            }
        }

    }

    public fun clearDatabase(){
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val rows =realm.where(Category::class.java).findAll()
        rows.deleteAllFromRealm()
        realm.commitTransaction()
    }

    interface CategoryCallback {
        fun getCategoryList(categoryList:List<CategoryModel?>?)

    }

    private fun existInDataBase(id:String):Boolean{
        val realm = Realm.getDefaultInstance()
        val count:Long? =realm?.where(Category::class.java)?.equalTo("parent",id.toInt())?.count()
        if (count!=null)
            return count>0
        return false
    }

    private fun getCategoryFromDatabase(id:String):List<CategoryModel?>?{
        var categoryModelList: MutableList<CategoryModel?> =ArrayList()
        val realm = Realm.getDefaultInstance()
        val categories =realm?.where(Category::class.java)?.equalTo("parent",id.toInt())?.findAll()

        if (categories?.size!! >0){
            categoryModelList =ArrayList()
            for (row in categories){
                val cat =CategoryModel(
                        id = row?.id!!,
                        slug = row.slug,
                        title = row.title,
                        description = row.description,
                        post_count = row.post_count!!,
                        parent = row.parent!!,
                        sub = row.subCount!!
                )
                categoryModelList.add(cat)
            }
            return categoryModelList
        }
        return ArrayList()
    }

    private fun getCategoryFromNetwork(){
        val retrofit = RetrofitClientInstance.retrofitInstance
        val getDataService = retrofit!!.create(GetDataCategory::class.java)
        getDataService.getAllCategorys(SaveItem.getItem(context, SaveItem.USER_COOKIE, ""))?.enqueue(object : Callback<List<CategoryModel?>?> {
            override fun onResponse(call: Call<List<CategoryModel?>?>, response: Response<List<CategoryModel?>?>) {
                if (response.isSuccessful) {
                    saveDataInDatabase(response.body())
                    myCategoryCallback.getCategoryList(response.body())
                }
            }

            override fun onFailure(call: Call<List<CategoryModel?>?>, t: Throwable) {
                Log.e("onFailure:", t.message)

            }
        })
    }

    private fun getSubCategoryFromNetwork(id:String){
        val retrofit = RetrofitClientInstance.retrofitInstance
        val getDataService = retrofit!!.create(GetDataCategory::class.java)
        getDataService.getAllSubCategorys(SaveItem.getItem(context, SaveItem.USER_COOKIE, ""),id)?.enqueue(object : Callback<List<SubCategoryModel?>?> {
            override fun onResponse(call: Call<List<SubCategoryModel?>?>, response: Response<List<SubCategoryModel?>?>) {
                if (response.isSuccessful) {
                    val categoryModel =convertToCategoryModel(response.body())
                    saveDataInDatabase(categoryModel)
                    myCategoryCallback.getCategoryList(categoryModel)
                }
            }

            override fun onFailure(call: Call<List<SubCategoryModel?>?>, t: Throwable) {
                Log.e("onFailure:", t.message)

            }
        })
    }

    private fun convertToCategoryModel(cat:List<SubCategoryModel?>?):List<CategoryModel>{
        val list:MutableList<CategoryModel> =ArrayList()
        if (cat!=null){
            for(sub in cat){
                val category =CategoryModel(
                        id = sub?.id!!,
                        slug = sub.slug,
                        title = sub.title,
                        description = sub.description,
                        post_count = sub.post_count!!,
                        parent = sub.parent!!,
                        sub = 0
                )
                list.add(category)
            }
        }

        return list
    }

    private fun saveDataInDatabase(body:List<CategoryModel?>?){
        if (body!=null && body.size>0){
            if (!existInDataBase(body[0]?.parent.toString())){
                val realm = Realm.getDefaultInstance()
                for (cat in body){
                    realm.beginTransaction()
                    val catDb = realm.createObject(Category::class.java)
                    catDb.id =cat?.id
                    catDb.description =cat?.description
                    catDb?.parent =cat?.parent
                    catDb?.post_count =cat?.post_count
                    catDb?.slug =cat?.slug
                    catDb?.subCount =cat?.subCount
                    catDb?.title =cat?.title
                    realm.commitTransaction()
                }
            }

        }
    }

}