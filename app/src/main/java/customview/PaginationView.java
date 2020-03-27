package customview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kavir.ali.kavir_msg.R;
import com.kavir.ali.kavir_msg.utility.FormatHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



public class PaginationView extends RelativeLayout {

    public static final int PAGE_SIZE_10 = 0;
    public static final int PAGE_SIZE_20 = 1;
    public static final int PAGE_SIZE_50 = 2;
    public static final int PAGE_SIZE_100 = 3;
    public static final int PAGE_SIZE_200 = 4;
    public static final int PAGE_SIZE_500 = 5;

    @IntDef({PAGE_SIZE_10, PAGE_SIZE_20, PAGE_SIZE_50, PAGE_SIZE_100, PAGE_SIZE_200, PAGE_SIZE_500})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PageSize {}

    @PageSize int mDefaultPageSize = PAGE_SIZE_10;

    private final static int DEFAULT_PAGE_SIZE = 10;
    private SeekBar mSeekBar;
    private TextView mPagerTV;
    private TextView mPagerPopupTV;
    private TextView mTotalPageTV;
    private ImageButton mLeftBtn;
    private ImageButton mRightBtn;
    private int mPageCount;
    private int mTotalCount;
    private int mPageSize = DEFAULT_PAGE_SIZE;
    private int mPagerSmoother = 1;
    private OnPagerUpdate mOnPagerUpdate;
    private Context mContext;
    private PopupWindow mPopupWindow;

    public PaginationView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PaginationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PaginationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.layout_pagination_view, this, true);
        mSeekBar = v.findViewById(R.id.seekbar);
        mPagerTV = v.findViewById(R.id.tv_current_page);
        mTotalPageTV = v.findViewById(R.id.total_pages);
        mPagerPopupTV = v.findViewById(R.id.tv_current_page_popup);
        mLeftBtn = v.findViewById(R.id.left_arrow);
        mRightBtn = v.findViewById(R.id.right_arrow);
        ((ViewGroup)mPagerTV.getParent().getParent()).setClipChildren(false);
//        ((ViewGroup)mPagerTV.getParent().getParent()).setClipToPadding(false);
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
//                TextViewCompat.setAutoSizeTextTypeWithDefaults(mPagerTV, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                updatePosition(mSeekBar.getProgress());
            }
        },500);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePosition(progress/mPagerSmoother);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mPagerPopupTV.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPagerPopupTV.setVisibility(View.INVISIBLE);
                if(mOnPagerUpdate != null){
                    mOnPagerUpdate.onUpdate(mSeekBar.getProgress()/mPagerSmoother, mPageSize);
                }
            }
        });

        mLeftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePage(false);
            }
        });

        mRightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePage(true);
            }
        });






    }

    private void setPageCount(int pageCount){
        mPageCount = pageCount;
        mSeekBar.setMax(pageCount*mPagerSmoother);
        mTotalPageTV.setText(FormatHelper.toPersianNumber(" کل صفحات: "+(mPageCount+1)));
    }



    public int getPageCount() {
        return mPageCount;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public int getPageSize() {
        return mPageSize;
    }

    private void setPager(int totalCount, int pageSize) {
        mTotalCount = totalCount;
        mPageSize = pageSize;
        int pageCount = mTotalCount/mPageSize;
        if(pageCount<10){
            mPagerSmoother = 50;
        }else if(pageCount<80){
            mPagerSmoother = 10;
        }else{
            mPagerSmoother = 1;
        }
        setPageCount((mTotalCount%mPageSize==0) ? pageCount-1 : pageCount);
    }

    public void setPager(int totalCount) {
        setPager(totalCount, mPageSize);
    }

    public void setOnPagerUpdate(OnPagerUpdate onPagerUpdate) {
        mOnPagerUpdate = onPagerUpdate;
    }

    public void updatePosition(int progress){
        mPagerTV.setText(FormatHelper.toPersianNumber(progress+1+""));
        mPagerPopupTV.setText(mContext.getString(R.string.page)+" "+FormatHelper.toPersianNumber(progress+1+""));
        Rect bounds = mSeekBar.getThumb().getBounds();
        mPagerTV.setTranslationX(mSeekBar.getLeft() + bounds.left);
        mPagerTV.setTranslationY(bounds.height()*130/100);
        mPagerPopupTV.setTranslationX(mSeekBar.getLeft() + bounds.left);
        mPagerPopupTV.setTranslationY(-bounds.height()*12/20);
        if(progress<=0){
            mLeftBtn.setEnabled(false);
        }else{
            mLeftBtn.setEnabled(true);
        }
        if(progress>=mSeekBar.getMax()){
            mRightBtn.setEnabled(false);
        }else{
            mRightBtn.setEnabled(true);
        }
    }

    private void updatePage(boolean increase){
        int currentPage = mSeekBar.getProgress()/mPagerSmoother;
        if(increase){
            if(currentPage<mSeekBar.getMax()/mPagerSmoother){
                currentPage++;
            }
        }else{
            if(currentPage>0){
                currentPage--;
            }
        }
        mSeekBar.setProgress(currentPage*mPagerSmoother);
        if(mOnPagerUpdate != null){
            mOnPagerUpdate.onUpdate(mSeekBar.getProgress()/mPagerSmoother, mPageSize);
        }
    }


    public interface OnPagerUpdate{
        void onUpdate(int pageNumber, int pageSize);
    }


}
