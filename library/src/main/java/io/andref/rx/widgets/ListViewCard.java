package io.andref.rx.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import rx.Observable;

public class ListViewCard extends FrameLayout
{
    public static final String TAG = "ListViewCard";

    private Observable<Void> mButtonClicks = Observable.empty();

    private FrameLayout mButton;
    private TextView mButtonText;
    private ListViewCardAdapter mListViewCardAdapter;

    public ListViewCard(@NonNull Context context)
    {
        super(context);
        initializeViews(context, null, 0, 0);
    }

    public ListViewCard(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initializeViews(context, attrs, 0, 0);
    }

    public ListViewCard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs, defStyleAttr, 0);

    }

    @TargetApi(21)
    public ListViewCard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initializeViews(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes)
    {
        String buttonText;
        float avatarAlpha, iconAlpha;
        int avatarTint;
        boolean denseListItem;

        final TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ListViewCard, defStyleAttr, 0);

        try
        {
            avatarAlpha = a.getFloat(R.styleable.ListViewCard_rxw_avatarAlpha, 1f);
            avatarTint = a.getColor(R.styleable.ListViewCard_rxw_avatarTint, Color.BLACK);
            denseListItem = a.getBoolean(R.styleable.ListViewCard_rxw_denseLayout, false);
            iconAlpha = a.getFloat(R.styleable.ListViewCard_rxw_iconAlpha, .54f);

            buttonText = a.getString(R.styleable.ListViewCard_rxw_buttonText);
        }
        finally
        {
            a.recycle();
        }

        View cardView = inflate(context, R.layout.rxw_list_view_card, this);

        float cellHeight = denseListItem ? getResources().getDimension(R.dimen.rxw_dense_avatar_with_two_lines_and_icon_tile_height)
                : getResources().getDimension(R.dimen.rxw_avatar_with_two_lines_and_icon_tile_height);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = (int) cellHeight;

        mButton = (FrameLayout) cardView.findViewById(R.id.button);
        mButton.setLayoutParams(layoutParams);
        mButtonClicks = RxView.clicks(mButton);
        mButtonText = (TextView) cardView.findViewById(R.id.button_text);
        mButtonText.setText(buttonText);

        mListViewCardAdapter = new ListViewCardAdapter(cellHeight, avatarAlpha, iconAlpha, avatarTint);
        RecyclerView recyclerView = (RecyclerView) cardView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mListViewCardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    // region Getters/Setters

    public CharSequence getButtonText()
    {
        return mButtonText.getText();
    }

    public void setButtonText(CharSequence text)
    {
        mButtonText.setText(text);
    }

    public List<Item> getItems()
    {
        return mListViewCardAdapter.getItems();
    }

    public void addItem(Item item)
    {
        mListViewCardAdapter.addItem(item);
    }

    public void removeItem(Item item)
    {
        mListViewCardAdapter.removeItem(item);
    }

    public void removeItem(int position)
    {
        mListViewCardAdapter.removeItem(position);
    }

    public void setItems(List<Item> items)
    {
        mListViewCardAdapter.setItems(items);
    }

    public void updateItem(int position, Item item)
    {
        mListViewCardAdapter.updateItem(position, item);
    }

    // endregion

    // region Observables

    public Observable<Void> buttonClicks()
    {
        return mButtonClicks;
    }

    public Observable<Pair<Item, Integer>> iconClicks()
    {
        return mListViewCardAdapter.iconClicks();
    }

    public Observable<Pair<Item, Integer>> itemClicks()
    {
        return mListViewCardAdapter.itemClicks();
    }

    // endregion

    // region Actions

    public void hideButton()
    {
        mButton.setVisibility(GONE);
    }

    public void showButton()
    {
        mButton.setVisibility(VISIBLE);
    }

    // endregion

    public static class Item<T>
    {
        private int mAvatarResourceId;
        private int mIconResourceId;
        private String mLine1;
        private String mLine2;
        private T mData;

        public Item(String line1, String line2, int avatarResourceId, int iconResourceId)
        {
            this(line1, line2, avatarResourceId, iconResourceId, null);
        }

        public Item(String line1, String line2, int avatarResourceId, int iconResourceId, T data)
        {
            mData = data;
            mAvatarResourceId = avatarResourceId;
            mIconResourceId = iconResourceId;
            mLine1 = line1;
            mLine2 = line2;
        }

        // region Getters/Setters

        public T getData()
        {
            return mData;
        }

        public void setData(T data)
        {
            mData = data;
        }

        public int getAvatarResourceId()
        {
            return mAvatarResourceId;
        }

        public void setAvatarResourceId(int avatarResourceId)
        {
            mAvatarResourceId = avatarResourceId;
        }

        public int getIconResourceId()
        {
            return mIconResourceId;
        }

        public void setIconResourceId(int iconResourceId)
        {
            mIconResourceId = iconResourceId;
        }

        public String getLine1()
        {
            return mLine1;
        }

        public void setLine1(String line1)
        {
            mLine1 = line1;
        }

        public String getLine2()
        {
            return mLine2;
        }

        public void setLine2(String line2)
        {
            mLine2 = line2;
        }

        // endregion

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Item<?> item = (Item<?>) o;

            if (mAvatarResourceId != item.mAvatarResourceId) return false;
            if (mIconResourceId != item.mIconResourceId) return false;
            if (mLine1 != null ? !mLine1.equals(item.mLine1) : item.mLine1 != null) return false;
            if (mLine2 != null ? !mLine2.equals(item.mLine2) : item.mLine2 != null) return false;
            return mData != null ? mData.equals(item.mData) : item.mData == null;

        }

        @Override
        public int hashCode()
        {
            int result = mAvatarResourceId;
            result = 31 * result + mIconResourceId;
            result = 31 * result + (mLine1 != null ? mLine1.hashCode() : 0);
            result = 31 * result + (mLine2 != null ? mLine2.hashCode() : 0);
            result = 31 * result + (mData != null ? mData.hashCode() : 0);
            return result;
        }
    }
}
