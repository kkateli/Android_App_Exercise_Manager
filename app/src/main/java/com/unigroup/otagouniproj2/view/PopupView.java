package com.unigroup.otagouniproj2.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.unigroup.otagouniproj2.R;
import com.unigroup.otagouniproj2.data.IPopup;

/*
* Popup View show event daily
* */
public class PopupView extends FrameLayout {

    protected IPopup mPopup;
    protected OnEventPopupClickListener mPopupClickListener;
    protected int mShowDuration = 3000;
    protected TextView mQuote;
    protected CardView mCardView;
    protected ImageView mImvEnd;
    protected TextView mTitle;
    protected TextView mDescription;
    protected AlphaAnimation mFadeOutAnim = new AlphaAnimation(1.0f, 0.0f);
    protected Animation.AnimationListener mFadeOutListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            setAlpha(1);
            setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Runnable mHidePopup = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    public PopupView(Context context) {
        super(context);
        init(null);
    }

    public PopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PopupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_event_popup, this, true);

        mDescription = findViewById(R.id.desc);
        mTitle = findViewById(R.id.title);
        mImvEnd = findViewById(R.id.imv_end);
        mQuote = findViewById(R.id.quote);
        mCardView = findViewById(R.id.cardview);

        mQuote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupClickListener != null) {
                    mPopupClickListener.onQuoteClick(PopupView.this, mPopup);
                }
            }
        });

        mCardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupClickListener != null) {
                    mPopupClickListener.onPopupClick(PopupView.this, mPopup);
                }
            }
        });
    }

    public void show() {
        setVisibility(View.VISIBLE);
        if (this.mPopup.isAutohide()) {
            removeCallbacks(mHidePopup);
            postDelayed(mHidePopup, mShowDuration);
        }
    }

    public void hide() {
        if (mFadeOutAnim.hasStarted() && !mFadeOutAnim.hasEnded()) {
            return;
        }
        mFadeOutAnim = new AlphaAnimation(getAlpha(), 0.0f);
        setVisibility(View.VISIBLE);
        mFadeOutAnim.setAnimationListener(mFadeOutListener);
        mFadeOutAnim.setDuration(200);
        startAnimation(mFadeOutAnim);
    }

    public void setPosition(Rect rect, int topMargin, int bottomMargin) {
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = rect.top + topMargin;
        params.leftMargin = rect.left;
        mCardView.getLayoutParams().height = rect.height() + bottomMargin;
        setLayoutParams(params);
    }

    public void setPopup(IPopup popup) {
        this.mPopup = popup;
        mDescription.setText(String.valueOf(popup.getDescription()));
        mTitle.setText(String.valueOf(popup.getTitle()));
        if(mPopupClickListener != null){
            mPopupClickListener.onLoadData(PopupView.this,mImvEnd, popup);
        }
        mQuote.setText(String.valueOf(popup.getQuote()));
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        throw new UnsupportedOperationException("you should use setOnEventClickListener()");
    }

    public void setOnPopupClickListener(OnEventPopupClickListener listener) {
        this.mPopupClickListener = listener;
    }

    public interface OnEventPopupClickListener {
        void onPopupClick(PopupView view, IPopup data);

        void onQuoteClick(PopupView view, IPopup data);

        void onLoadData(PopupView view, ImageView end, IPopup data);
    }
}
