package yanzhikai.shoppingcartdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yanzhikai.shoppingcartdemo.R;

/**
 * @description  表示各种状态的Layout：加载中，无网络，无数据等
 * @author  dangxy99
 * @date   2017/12/23
 * Modified by yany:    在原本的基础上增加了可以处理按键，这样就不会把触摸事件传入底下的View
 */
public class EmptyLayout extends FrameLayout {
    public static final int STATUS_HIDE = 1001;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_NO_NET = 2;
    public static final int STATUS_NO_DATA = 3;
    private Context mContext;
    private OnRetryListener mOnRetryListener;
    private int mEmptyStatus = STATUS_LOADING;
    private int mBgColor;
    //是否处理触摸事件
    private boolean mHandleTouch = false;

    @BindView(R.id.empty_loading)
    LoadingView emptyLoading;
    @BindView(R.id.tv_net_error)
    TextView mTvEmptyMessage;
    @BindView(R.id.rl_empty_container)
    FrameLayout mRlEmptyContainer;
    @BindView(R.id.empty_layout)
    FrameLayout mEmptyLayout;

    public EmptyLayout(Context context) {
        this(context, null);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context.getApplicationContext();
        init(attrs);
    }

    /**
     * 初始化
     */
    private void init(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.EmptyLayout);
        try {
            mBgColor = a.getColor(R.styleable.EmptyLayout_background_color, Color.WHITE);
        } finally {
            a.recycle();
        }
        View.inflate(mContext, R.layout.layout_empty_loading, this);
        ButterKnife.bind(this);

        _switchEmptyView();
    }

    /**
     * 隐藏视图
     */
    public void hide() {
        mEmptyStatus = STATUS_HIDE;
        _switchEmptyView();
    }

    /**
     * 设置状态
     *
     * @param emptyStatus
     */
    public void setEmptyStatus(@EmptyStatus int emptyStatus) {
        mEmptyStatus = emptyStatus;
        _switchEmptyView();
    }

    /**
     * 获取状态
     *
     * @return 状态
     */
    public int getEmptyStatus() {
        return mEmptyStatus;
    }

    /**
     * 设置异常消息
     *
     * @param msg 显示消息
     */
    public void setEmptyMessage(String msg) {
        mTvEmptyMessage.setText(msg);
    }

    public void hideErrorIcon() {
        mTvEmptyMessage.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 切换视图
     */
    private void _switchEmptyView() {
        switch (mEmptyStatus) {
            case STATUS_LOADING:
                mHandleTouch = true;
                setVisibility(VISIBLE);
                showBackground(false);
                mRlEmptyContainer.setVisibility(GONE);
                emptyLoading.setVisibility(VISIBLE);
                emptyLoading.start();
                break;
            case STATUS_NO_DATA:
            case STATUS_NO_NET:
                setVisibility(VISIBLE);
                showBackground(true);
                emptyLoading.setVisibility(GONE);
                mRlEmptyContainer.setVisibility(VISIBLE);
                emptyLoading.stop();
                break;
            case STATUS_HIDE:
                mHandleTouch = false;
                setVisibility(GONE);
                emptyLoading.stop();
                break;
            default:
                break;

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mHandleTouch || super.onTouchEvent(event);
    }

    /**
     * 设置重试监听器
     *
     * @param retryListener 监听器
     */
    public void setRetryListener(OnRetryListener retryListener) {
        this.mOnRetryListener = retryListener;
    }

    @OnClick(R.id.tv_net_error)
    public void onClick() {
        if (mOnRetryListener != null) {
            mOnRetryListener.onRetry();
        }
    }

    /**
     * 设置是否显示背景
     * @param showBackGround 是否能
     */
    private void showBackground(boolean showBackGround){
        if (showBackGround){
            mEmptyLayout.setBackgroundColor(mBgColor);
        }else {
            mEmptyLayout.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * 点击重试监听器
     */
    public interface OnRetryListener {
        void onRetry();
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_LOADING, STATUS_NO_NET, STATUS_NO_DATA})
    public @interface EmptyStatus {
    }
}
