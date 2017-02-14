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

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

import static android.view.View.GONE;
import static io.andref.rx.widgets.ListViewCard.TAG;

public class ListViewCardAdapter extends RecyclerView.Adapter implements ItemClickListener
{
    private List<ListViewCard.Item> mItems = new ArrayList<>();

    private PublishSubject<Pair<ListViewCard.Item, Integer>> mItemClicks = PublishSubject.create();
    private PublishSubject<Pair<ListViewCard.Item, Integer>> mIconClicks = PublishSubject.create();

    private float mCellHeight;
    private float mAvatarAlpha;
    private float mIconAlpha;
    private int mAvatarTint;

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
                return new ViewHolder(this, view, mCellHeight);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int adapterPosition)
    {
        switch (getItemViewType(adapterPosition))
        {
            default:
                ((ViewHolder) holder).bindItem(mItems.get(adapterPosition), mAvatarAlpha, mAvatarTint, mIconAlpha);
                break;
        }
    }

    @Override
    public int getItemCount()
    {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public void onItemClicked(int position)
    {
        mItemClicks.onNext(new Pair<>(mItems.get(position), position));
    }

    @Override
    public void onIconClicked(int position)
    {
        mIconClicks.onNext(new Pair<>(mItems.get(position), position));
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



    private static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mImageView;
        private TextView mTextView1;
        private TextView mTextView2;
        private ImageButton mImageButton;
        private Context mContext;

        private ViewHolder(final ItemClickListener itemClickListener, View itemView, float viewHeight)
        {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view_1);
            mTextView1 = (TextView) itemView.findViewById(R.id.text_view_1);
            mTextView2 = (TextView) itemView.findViewById(R.id.text_view_2);
            mImageButton = (ImageButton) itemView.findViewById(R.id.image_button);
            mContext = itemView.getContext();
            itemView.getLayoutParams().height = (int) viewHeight;

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    itemClickListener.onItemClicked(getAdapterPosition());
                }
            });

            mImageButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    itemClickListener.onIconClicked(getAdapterPosition());
                }
            });
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
    }
}
