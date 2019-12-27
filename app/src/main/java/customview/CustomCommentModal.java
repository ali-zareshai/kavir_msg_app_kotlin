package customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.server.Comments;
import com.shafa.ali.kavir_msg.server.GetPostsServer;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.valdesekamdem.library.mdtoast.MDToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomCommentModal {
    public static void showNewComment(final Context context,final String postId){
        final Dialog commentDialog = new Dialog(context);
        commentDialog.setContentView(R.layout.dialog_new_comment);
        commentDialog.setCancelable(true);
        //////

        //
        commentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText nameEdit = (EditText)commentDialog.findViewById(R.id.name_comment_dialog);
        final EditText emailEdit= (EditText)commentDialog.findViewById(R.id.email_comment_dialog);
        final EditText commentEdit= (EditText)commentDialog.findViewById(R.id.comment_comment_dialog);

        Button sendBtn =(Button)commentDialog.findViewById(R.id.submit_comment_dialog);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComment(context,postId,nameEdit.getText().toString().trim(),emailEdit.getText().toString().trim(),commentEdit.getText().toString().trim());
                commentDialog.dismiss();
            }
        });



        //////
//        commentDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        commentDialog.show();

    }

    private static void sendComment(final Context context, String postId , String name, String email, String comment) {
        if (name.isEmpty() || email.isEmpty() || comment.isEmpty()){
            MDToast.makeText(context,context.getString(R.string.full_all_fields),2500,MDToast.TYPE_WARNING).show();
            return;
        }

        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        Comments comments = retrofit.create(Comments.class);
        comments.postNewComment(postId,name,email,comment).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Log.e("onResponse",response.body());
                    MDToast.makeText(context,context.getString(R.string.success_send_comment),2500,MDToast.TYPE_SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("onFailure:",t.getMessage());
            }
        });
    }


}
