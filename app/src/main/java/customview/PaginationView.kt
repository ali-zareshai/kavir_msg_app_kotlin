package customview

import android.content.Context
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper.toPersianNumber
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class PaginationView : RelativeLayout {
    @IntDef(PAGE_SIZE_10, PAGE_SIZE_20, PAGE_SIZE_50, PAGE_SIZE_100, PAGE_SIZE_200, PAGE_SIZE_500)
    @Retention(RetentionPolicy.SOURCE)
    annotation class PageSize

    @PageSize
    var mDefaultPageSize = PAGE_SIZE_10
    private var mSeekBar: SeekBar? = null
    private var mPagerTV: TextView? = null
    private var mPagerPopupTV: TextView? = null
    private var mTotalPageTV: TextView? = null
    private var mLeftBtn: ImageButton? = null
    private var mRightBtn: ImageButton? = null
    private var mPageCount = 0
    var totalCount = 0
        private set
    var pageSize = DEFAULT_PAGE_SIZE
        private set
    private var mPagerSmoother = 1
    private var mOnPagerUpdate: OnPagerUpdate? = null
    private var mContext: Context? = null
    private val mPopupWindow: PopupWindow? = null

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        mContext = context
        val mInflater = LayoutInflater.from(context)
        val v = mInflater.inflate(R.layout.layout_pagination_view, this, true)
        mSeekBar = v.findViewById(R.id.seekbar)
        mPagerTV = v.findViewById(R.id.tv_current_page)
        mTotalPageTV = v.findViewById(R.id.total_pages)
        mPagerPopupTV = v.findViewById(R.id.tv_current_page_popup)
        mLeftBtn = v.findViewById(R.id.left_arrow)
        mRightBtn = v.findViewById(R.id.right_arrow)
        (mPagerTV!!.getParent().parent as ViewGroup).clipChildren = false
        //        ((ViewGroup)mPagerTV.getParent().getParent()).setClipToPadding(false);
        postDelayed({ //                TextViewCompat.setAutoSizeTextTypeWithDefaults(mPagerTV, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            updatePosition(mSeekBar!!.getProgress())
        }, 500)
        mSeekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                updatePosition(progress / mPagerSmoother)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mPagerPopupTV!!.setVisibility(View.VISIBLE)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mPagerPopupTV!!.setVisibility(View.INVISIBLE)
                if (mOnPagerUpdate != null) {
                    mOnPagerUpdate!!.onUpdate(mSeekBar!!.getProgress() / mPagerSmoother, pageSize)
                }
            }
        })
        mLeftBtn!!.setOnClickListener(OnClickListener { updatePage(false) })
        mRightBtn!!.setOnClickListener(OnClickListener { updatePage(true) })
    }

    var pageCount: Int
        get() = mPageCount
        private set(pageCount) {
            mPageCount = pageCount
            mSeekBar!!.max = pageCount * mPagerSmoother
            mTotalPageTV!!.text = toPersianNumber(" کل صفحات: " + (mPageCount + 1))
        }

    private fun setPager(totalCount: Int, pageSize: Int) {
        this.totalCount = totalCount
        this.pageSize = pageSize
        var pageCount = this.totalCount / this.pageSize
        mPagerSmoother = if (pageCount < 10) {
            50
        } else if (pageCount < 80) {
            10
        } else {
            1
        }
        pageCount = if (this.totalCount % this.pageSize == 0) pageCount - 1 else pageCount
    }

    fun setPager(totalCount: Int) {
        setPager(totalCount, pageSize)
    }

    fun setOnPagerUpdate(onPagerUpdate: (Nothing, Nothing) -> Unit) {
        mOnPagerUpdate = onPagerUpdate
    }

    fun updatePosition(progress: Int) {
        mPagerTV!!.text = toPersianNumber("${progress + 1}")
        mPagerPopupTV!!.text = mContext!!.getString(R.string.page) + " " + toPersianNumber("${progress + 1}")
        val bounds = mSeekBar!!.thumb.bounds
        mPagerTV!!.translationX = mSeekBar!!.left + bounds.left.toFloat()
        mPagerTV!!.translationY = bounds.height() * 130 / 100.toFloat()
        mPagerPopupTV!!.translationX = mSeekBar!!.left + bounds.left.toFloat()
        mPagerPopupTV!!.translationY = -bounds.height() * 12 / 20.toFloat()
        if (progress <= 0) {
            mLeftBtn!!.isEnabled = false
        } else {
            mLeftBtn!!.isEnabled = true
        }
        if (progress >= mSeekBar!!.max) {
            mRightBtn!!.isEnabled = false
        } else {
            mRightBtn!!.isEnabled = true
        }
    }

    private fun updatePage(increase: Boolean) {
        var currentPage = mSeekBar!!.progress / mPagerSmoother
        if (increase) {
            if (currentPage < mSeekBar!!.max / mPagerSmoother) {
                currentPage++
            }
        } else {
            if (currentPage > 0) {
                currentPage--
            }
        }
        mSeekBar!!.progress = currentPage * mPagerSmoother
        if (mOnPagerUpdate != null) {
            mOnPagerUpdate!!.onUpdate(mSeekBar!!.progress / mPagerSmoother, pageSize)
        }
    }

    interface OnPagerUpdate {
        fun onUpdate(pageNumber: Int, pageSize: Int)
    }

    companion object {
        const val PAGE_SIZE_10 = 0
        const val PAGE_SIZE_20 = 1
        const val PAGE_SIZE_50 = 2
        const val PAGE_SIZE_100 = 3
        const val PAGE_SIZE_200 = 4
        const val PAGE_SIZE_500 = 5
        private const val DEFAULT_PAGE_SIZE = 10
    }
}