package io.andref.rx.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static android.view.View.GONE;
import static io.andref.rx.widgets.ListViewCard.TAG;

public class ListViewCardAdapter extends RecyclerView.Adapter
{
    private List<ListViewCard.Item> mItems = new ArrayList<>();

    private PublishSubject<ListViewCard.Item> mItemClicks = PublishSubject.create();
    private PublishSubject<ListViewCard.Item> mIconClicks = PublishSubject.create();

    private float mCellHeight;
    private float mAvatarAlpha;
    private float mIconAlpha;
    private int mAvatarTint;

    private boolean mShowLastDivider = true;

    public ListViewCardAdapter(float cellHeight, float avatarAlpha, float iconAlpha, int avatarTint)
    {
        mCellHeight = cellHeight;
        mAvatarAlpha = avatarAlpha;
        mIconAlpha = iconAlpha;
        mAvatarTint = avatarTint;
    }

    @SuppressWarnings("CheckResult")
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rxw_avatar_with_two_lines_and_icon, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view, mCellHeight);

        RxView.clicks(view)
                .takeUntil(RxView.detaches(parent))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mItemClicks.onNext(mItems.get(viewHolder.getAdapterPosition()));
                    }
                });


        View iconView = view.findViewById(R.id.image_button);
        RxView.clicks(iconView)
                .takeUntil(RxView.detaches(parent))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mIconClicks.onNext(mItems.get(viewHolder.getAdapterPosition()));
                    }
                });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int adapterPosition)
    {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindItem(mItems.get(adapterPosition), mAvatarAlpha, mAvatarTint, mIconAlpha);
        if (adapterPosition == getItemCount() - 1)
        {
            viewHolder.setDividerVisibility(mShowLastDivider);
        }
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public Observable<ListViewCard.Item> itemClicks()
    {
        return mItemClicks;
    }

    public Observable<ListViewCard.Item> iconClicks()
    {
        return mIconClicks;
    }

    public void setItems(@NonNull List<ListViewCard.Item> items)
    {
        mItems = items;
        notifyDataSetChanged();
    }

    public List<ListViewCard.Item> getItems()
    {
        return mItems;
    }

    public void addItem(ListViewCard.Item item)
    {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void removeItem(ListViewCard.Item item)
    {
        int indexOfItem = mItems.indexOf(item);
        mItems.remove(indexOfItem);
        notifyItemRemoved(indexOfItem);
    }

    public void removeItem(int position)
    {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(int position, ListViewCard.Item item)
    {
        mItems.set(position, item);
        notifyItemChanged(position);
    }

    public void hideDivider()
    {
        mShowLastDivider = false;
        notifyItemChanged(getItemCount() - 1);
    }

    public void showDivider()
    {
        mShowLastDivider = true;
        notifyItemChanged(getItemCount() - 1);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mImageView;
        private TextView mTextView1;
        private TextView mTextView2;
        private ImageButton mImageButton;
        private View mListItemSeparator;
        private Context mContext;

        private ViewHolder(View itemView, float viewHeight)
        {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view_1);
            mTextView1 = (TextView) itemView.findViewById(R.id.text_view_1);
            mTextView2 = (TextView) itemView.findViewById(R.id.text_view_2);
            mImageButton = (ImageButton) itemView.findViewById(R.id.image_button);
            mListItemSeparator = itemView.findViewById(R.id.list_item_separator);
            mContext = itemView.getContext();
            itemView.getLayoutParams().height = (int) viewHeight;
        }

        private void bindItem(ListViewCard.Item item, float avatarAlpha, int avatarTint, float iconAlpha)
        {
            mTextView1.setText(item.getLine1());

            if (!TextUtils.isEmpty(item.getLine2()))
            {
                mTextView2.setVisibility(View.VISIBLE);
                mTextView2.setText(item.getLine2());
            }
            else
            {
                mTextView2.setVisibility(GONE);
            }

            try
            {
                if (item.getAvatarResourceId() != 0)
                {
                    mImageView.setAlpha(avatarAlpha);
                    Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), item.getAvatarResourceId(), null);
                    if (drawable != null)
                    {
                        drawable.mutate().setColorFilter(avatarTint, PorterDuff.Mode.SRC_ATOP);
                        mImageView.setImageDrawable(drawable);
                    }
                }
                else
                {
                    mImageView.setVisibility(GONE);
                }
            }
            catch (Resources.NotFoundException exception)
            {
                mImageView.setVisibility(GONE);
                Log.e(TAG, "Drawable resource not found: " + exception.getMessage());
            }

            try
            {
                if (item.getIconResourceId() != 0)
                {
                    mImageButton.setImageResource(item.getIconResourceId());
                    mImageButton.setAlpha(iconAlpha);
                    mImageButton.setClickable(true);
                }
                else
                {
                    mImageButton.setVisibility(View.INVISIBLE);
                    mImageButton.setClickable(false);
                }
            }
            catch (Resources.NotFoundException exception)
            {
                mImageButton.setVisibility(GONE);
                Log.e(TAG, "Drawable resource not found: " + exception.getMessage());
            }
        }

        private void setDividerVisibility(boolean visible)
        {
            if (visible)
            {
                mListItemSeparator.setVisibility(View.VISIBLE);
            }
            else
            {
                mListItemSeparator.setVisibility(View.INVISIBLE);
            }
        }
    }
}
