package customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

import com.kavirelectronic.ali.kavir_info.R;

public class CustomButtonRectangle extends Button {
    public CustomButtonRectangle(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Typeface type2 = Typeface.createFromAsset(context.getAssets(),"fonts/sans.ttf");
        this.setTypeface(type2);
        this.setBackgroundResource(R.drawable.clicked_button);
        this.setTextColor(Color.WHITE);
        this.setGravity(Gravity.CENTER);
    }

    public CustomButtonRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);init(context);
    }

    public CustomButtonRectangle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init(context);
    }

}

