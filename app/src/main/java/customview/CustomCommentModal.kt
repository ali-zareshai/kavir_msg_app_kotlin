package customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.server.Comments
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance.retrofitInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.kavirelectronic.ali.kavir_info.utility.SaveItem.getItem
import com.valdesekamdem.library.mdtoast.MDToast
import dmax.dialog.SpotsDialog
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomCommentModal {
    private var commentDialog: Dialog? = null
    fun showNewComment(context: Context, postId: String?) {
        commentDialog = Dialog(context)
        commentDialog!!.setContentView(R.layout.dialog_new_comment)
        commentDialog!!.setCancelable(true)
        //////

        //
        commentDialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val nameEdit = commentDialog!!.findViewById<View>(R.id.name_comment_dialog) as EditText
        val emailEdit = commentDialog!!.findViewById<View>(R.id.email_comment_dialog) as EditText
        val commentEdit = commentDialog!!.findViewById<View>(R.id.comment_comment_dialog) as EditText
        val closeBtn = commentDialog!!.findViewById<View>(R.id.close_toolbar_btn) as ImageButton
        if (!getItem(context, SaveItem.USER_NAME, "").equals("", ignoreCase = true)) {
            nameEdit.setText(getItem(context, SaveItem.USER_NAME, ""))
            nameEdit.isEnabled = false
        }
        if (!getItem(context, SaveItem.USER_EMAIL, "").equals("", ignoreCase = true)) {
            emailEdit.setText(getItem(context, SaveItem.USER_EMAIL, ""))
            nameEdit.isEnabled = false
        }
        closeBtn.setOnClickListener { commentDialog!!.dismiss() }
        val sendBtn = commentDialog!!.findViewById<View>(R.id.submit_comment_dialog) as RelativeLayout
        sendBtn.setOnClickListener {
            sendComment(context, postId, nameEdit.text.toString().trim { it <= ' ' }, emailEdit.text.toString().trim { it <= ' ' }, commentEdit.text.toString().trim { it <= ' ' })
            //                commentDialog.dismiss();
        }


        //////
//        commentDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        commentDialog!!.show()
        val window = commentDialog!!.window
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun sendComment(context: Context, postId: String, name: String, email: String, comment: String) {
        if (name.isEmpty() || email.isEmpty() || comment.isEmpty()) {
            MDToast.makeText(context, context.getString(R.string.full_all_fields), 2500, MDToast.TYPE_WARNING).show()
            return
        }
        val dialog = SpotsDialog.Builder()
                .setContext(context)
                .setMessage(R.string.please_wait)
                .build()
        dialog.show()
        val retrofit = retrofitInstance
        val comments = retrofit!!.create(Comments::class.java)
        Log.e("postId:", postId)
        comments.postNewComment(getItem(context, SaveItem.S_CODE, ""), getItem(context, SaveItem.APK_ID, ""), postId, name, email, comment)!!.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                Log.e("onResponse", response.toString())
                if (response.isSuccessful) {
                    try {
                        val jsonObject = JSONObject(response.body())
                        if (jsonObject.getString("status") == "error") {
                            commentDialog!!.dismiss()
                            MDToast.makeText(context, jsonObject.getString("error"), 2500, MDToast.TYPE_ERROR).show()
                        } else {
                            commentDialog!!.dismiss()
                            MDToast.makeText(context, context.getString(R.string.success_send_comment), 2500, MDToast.TYPE_SUCCESS).show()
                        }
                        dialog.dismiss()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        dialog.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("onFailure:", t.message)
            }
        })
    }
}