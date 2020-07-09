package customview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import com.kavirelectronic.ali.kavir_info.R

class CustomButtonRectangle : Button {
    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        val type2 = Typeface.createFromAsset(context.assets, "fonts/sans.ttf")
        this.typeface = type2
        setBackgroundResource(R.drawable.clicked_button)
        this.setTextColor(Color.WHITE)
        this.gravity = Gravity.CENTER
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }
}