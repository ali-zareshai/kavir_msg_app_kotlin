package customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class CustomTextView3 : TextView {
    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        val type2 = Typeface.createFromAsset(context.assets, "fonts/Shabnam.ttf")
        this.setTypeface(type2, Typeface.NORMAL)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }
}