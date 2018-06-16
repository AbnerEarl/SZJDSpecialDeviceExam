package com.henau.pictureselect.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.henau.pictureselect.R;

import java.util.List;

/**
 * Created by admin on 2016/4/2.
 */
public class ViewPagerIndicator extends LinearLayout {

    private static final String TAG = ViewPagerIndicator.class.getSimpleName();
    private static final int DEFAULT_SHOW_TABS = 3;
    private static final int TEXT_COLOR_NORMAL = Color.parseColor("#77000000");
    private static final int TEXT_COLOR_FOCUSED = Color.parseColor("#000000");

    private Paint mPaint;//绘制直线的画笔
    private int mInitTranslationX;//直线初始移动距离
    private int mTranslationX;//直线移动距离
    private int mTabWidth;//标签的宽度
    private int mLineHeight = 5;//直线的高度

    private int mShowTabs = DEFAULT_SHOW_TABS;//显示在屏幕上的标签个数
    private List<String> mTabTitles;//标签数据的集合
    private int mLastPosition;
    private int mCurrPosition;
    private ViewPager mViewPager;//绑定的ViewPager
    private OnPageChangeListener mPagerListener;//页面改变监听器

    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mShowTabs = ta.getInt(R.styleable.ViewPagerIndicator_show_tabs, DEFAULT_SHOW_TABS);
        ta.recycle();

        init(context);
    }

    private void init(Context context) {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.FILL);//实心
        mPaint.setColor(Color.RED);//画笔颜色为白色
        mPaint.setStrokeWidth(mLineHeight);//设置画笔的宽度
        mPaint.setPathEffect(new CornerPathEffect(3));//设置路径拐点为圆角
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate: ");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: " + w + "," + h + "," + oldw + "," + oldh);
        mTabWidth = w / mShowTabs;
        mInitTranslationX = 0;

        formatChildTabs();
    }

    /**
     * 设置子tab的宽度
     */
    private void formatChildTabs() {
        int mChildCount = getChildCount();
        if (mChildCount <= 0) return;

        for (int i = 0; i < mChildCount; i++) {
            View view = getChildAt(i);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.weight = 0;
            params.width = getWidth() / mShowTabs;
            view.setLayoutParams(params);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure: ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.i(TAG, "dispatchDraw: ");
        canvas.save();//将之前绘制的内容保存
        canvas.drawLine(mInitTranslationX + mTranslationX,getHeight()-mLineHeight
                ,mTabWidth + mTranslationX,getHeight()-mLineHeight,mPaint);
        canvas.restore();//将现在绘制的内容与之前绘制的内容进行合并
    }

    /**
     * 页面滑动时调用
     *
     * @param position
     * @param positionOffset
     */
    public void scroll(int position, float positionOffset) {
        mTranslationX = (int) (mTabWidth * (position + positionOffset));

        if ((position >= mShowTabs - 2)
                && (position < getChildCount() - 2)
                && positionOffset > 0 && getChildCount() > mShowTabs) {
            scrollTo((int) (mTabWidth * (position - (mShowTabs - 2) + positionOffset)), 0);
        }

        invalidate();
    }

    /**
     * 设置页面改变监听器
     *
     * @param listener
     */
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        this.mPagerListener = listener;
    }

    /**
     * 绑定ViewPager
     *
     * @param viewPager
     * @param position
     */
    public void setViewPager(ViewPager viewPager, int position) {
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i(TAG, "onPageScrolled: " + position + "," + positionOffset + "," + positionOffsetPixels);
                scroll(position, positionOffset);
                if (mPagerListener != null)
                    mPagerListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                changeTextColor(position);
                if (mPagerListener != null)
                    mPagerListener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPagerListener != null)
                    mPagerListener.onPageScrollStateChanged(state);
            }
        });
        mViewPager.setCurrentItem(position, true);
        changeTextColor(position);
    }

    /**
     * 设置屏幕上显示tab的个数
     *
     * @param showTabs
     */
    public void setShowTabs(int showTabs) {
        this.mShowTabs = showTabs;
    }

    /**
     * 设置数据
     *
     * @param titles
     */
    public void setTitles(List<String> titles) {
        if (titles == null || titles.size() == 0)
            throw new RuntimeException("设置标签的数量必须大于0个");
        this.mTabTitles = titles;
        this.removeAllViews();
        for (String title : titles) {
            addView(createView(title));
        }
        setTabClickListener();
    }

    /**
     * 创建tab标签
     *
     * @param title
     * @return
     */
    private TextView createView(String title) {
        TextView textView = new TextView(getContext());
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setText(title);
        textView.setPadding(0,50,0,50);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(TEXT_COLOR_NORMAL);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    /**
     * 设置标签条目点击事件
     */
    private void setTabClickListener() {
        int mChildCount = getChildCount();
        if (mChildCount <= 0) return;

        for (int i = 0; i < mChildCount; i++) {
            View view = getChildAt(i);
            final int j = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j, true);
                }
            });
        }
    }

    /**
     * 改变文字颜色
     *
     * @param position
     */
    private void changeTextColor(int position) {
        mCurrPosition = position;
        setTextColor(mLastPosition, TEXT_COLOR_NORMAL);
        setTextColor(position, TEXT_COLOR_FOCUSED);
        mLastPosition = position;
    }

    /**
     * 设置文本颜色
     *
     * @param postion
     * @param color
     */
    private void setTextColor(int postion, int color) {
        View view = getChildAt(postion);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
    }

    public int getCurrPosition(){
        return mCurrPosition;
    }

}
