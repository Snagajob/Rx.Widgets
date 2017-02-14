package io.andref.rx.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

import static android.view.View.GONE;
import static io.andref.rx.widgets.ListViewCard.TAG;

public class ListViewCardAdapter extends RecyclerView.Adapter
{
    private List<ListViewCard.Item> mItems = new ArrayList<>();

    private PublishSubject<Pair<ListViewCard.Item, Integer>> mItemClicks = PublishSubject.create();
    private PublishSubject<Pair<ListViewCard.Item, Integer>> mIconClicks = PublishSubject.create();

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rxw_avatar_with_two_lines_and_icon, parent, false);
                final ViewHolder viewHolder = new ViewHolder(view, mCellHeight);

                RxView.clicks(view)
                        .takeUntil(RxView.detaches(parent))
                        .subscribe(new Action1<Void>()
                        {
                            @Override
                            public void call(Void aVoid)
                            {
                                mItemClicks.onNext(new Pair<>(mItems.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition()));
                            }
                        });

                View iconView = view.findViewById(R.id.image_button);
                RxView.clicks(iconView)
                        .takeUntil(RxView.detaches(parent))
                        .subscribe(new Action1<Void>()
                        {
                            @Override
                            public void call(Void aVoid)
                            {
                                mIconClicks.onNext(new Pair<>(mItems.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition()));
                            }
                        });

                return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int adapterPosition)
    {
        switch (getItemViewType(adapterPosition))
        {
            default:
                final ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.bindItem(mItems.get(adapterPosition), mAvatarAlpha, mAvatarTint, mIconAlpha);
                if (adapterPosition == getItemCount() - 1)
                {
                    if (mShowLastDivider)
                    {
                        viewHolder.showDivider();
                    }
                    else
                    {
                        viewHolder.hideDivider();
                    }
                }
                break;
        }
    }

    @Override
    public int getItemCount()
    {
        return mItems != null ? mItems.size() : 0;
    }

    public Observable<Pair<ListViewCard.Item, Integer>> itemClicks()
    {
        return mItemClicks;
    }

    public Observable<Pair<ListViewCard.Item, Integer>> iconClicks()
    {
        return mIconClicks;
    }

    public void setItems(List<ListViewCard.Item> items)
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

    public void updateItem(int adapterPosition, ListViewCard.Item item)
    {
        mItems.set(adapterPosition, item);
        notifyItemChanged(adapterPosition);
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

            if (item.getLine2() != null && item.getLine2().length() > 0)
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

        private void showDivider()
        {
            mListItemSeparator.setVisibility(View.VISIBLE);
        }

        private void hideDivider()
        {
            mListItemSeparator.setVisibility(View.INVISIBLE);
        }
    }
}
