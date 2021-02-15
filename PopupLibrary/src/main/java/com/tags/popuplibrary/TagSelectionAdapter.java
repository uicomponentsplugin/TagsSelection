package com.tags.popuplibrary;

import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tags.popuplibrary.databinding.ItemTagSelectionBinding;
import com.tags.popuplibrary.models.Tag;
import com.tags.popuplibrary.models.Tags;
import com.tags.popuplibrary.models.tagSelectCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TagSelectionAdapter extends RecyclerView.Adapter<TagSelectionAdapter.TagViewHolder> implements Filterable {

    private Tags mTags;
    private final Integer mMaxSelectableTags;
    private final Integer mCheckBoxColor;
    private final List<Tag> mFilteredTags = new ArrayList<>();
    private final com.tags.popuplibrary.models.tagSelectCallback tagSelectCallback;

    public TagSelectionAdapter(tagSelectCallback tagSelectCallback, Tags tags, Integer maxSelectableTags, Integer mCheckBoxColor) {
        this.tagSelectCallback = tagSelectCallback;
        this.mMaxSelectableTags = maxSelectableTags;
        this.mCheckBoxColor = mCheckBoxColor;
        this.mTags = tags;
        this.mFilteredTags.addAll(tags.getAllTags());
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagViewHolder(ItemTagSelectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final TagViewHolder holder, int position) {
        holder.cbTag.setText(mFilteredTags.get(position).getName());
        holder.cbTag.setPadding(Util.dpToPx((int) holder.itemView.getContext().getResources().getDimension(R.dimen.checkbox_padding))
                , Util.dpToPx((int) holder.itemView.getContext().getResources().getDimension(R.dimen.checkbox_padding))
                , Util.dpToPx((int) holder.itemView.getContext().getResources().getDimension(R.dimen.checkbox_padding))
                , Util.dpToPx((int) holder.itemView.getContext().getResources().getDimension(R.dimen.checkbox_padding))
        );
        holder.cbTag.setTag(position);
        holder.cbTag.setChecked(mTags.getSelectedTags().contains(mFilteredTags.get(position)));
        if (mCheckBoxColor != null){
            if (Build.VERSION.SDK_INT < 21) {
                CompoundButtonCompat.setButtonTintList(holder.cbTag, ColorStateList.valueOf(holder.itemView.getContext().getColor(mCheckBoxColor)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
            } else {
                holder.cbTag.setButtonTintList(ColorStateList.valueOf(holder.itemView.getContext().getColor(mCheckBoxColor)));//setButtonTintList is accessible directly on API>19
            }
        }
        //holder.cbTag.setOnCheckedChangeListener(this);
        holder.cbTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                int position = (int) checkBox.getTag();
                if (mMaxSelectableTags == null
                        || (mMaxSelectableTags != null && mTags.getSelectedTags().size() < mMaxSelectableTags)
                        || !checkBox.isChecked())
                    tagSelectCallback.onSelect(mFilteredTags.get(position), checkBox.isChecked());
                else {
                    Util.shortToast(checkBox.getContext(), String.format(Locale.getDefault(), "Max %d item(s) are allowed", mMaxSelectableTags));
                    checkBox.setOnClickListener(null);
                    checkBox.setChecked(false);
                    checkBox.setOnClickListener(this);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredTags.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence filter) {
                mFilteredTags.clear();
                if (filter == null || filter.toString().isEmpty())
                    mFilteredTags.addAll(mTags.getAllTags());
                else {
                    for (Tag tag : mTags.getAllTags()) {
                        if (tag.getName().toLowerCase().contains(filter.toString().toLowerCase()))
                            mFilteredTags.add(tag);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = mFilteredTags.size();
                filterResults.values = mFilteredTags;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence filter, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    /* @Override
     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
         if (mTags.getSelectedTags().size() < mMaxSelectableTags) {
             int position = (int) buttonView.getTag();
             tagSelectCallback.onSelect(mFilteredTags.get(position), isChecked);
         } else {
             Util.shortToast(buttonView.getContext(), String.format(Locale.getDefault(), "Max %d item(s) are allowed", mMaxSelectableTags));
             buttonView.setOnCheckedChangeListener(null);
             buttonView.setChecked(false);
             buttonView.setOnCheckedChangeListener(this);
         }
     }
 */
    public void updateTag(Tags tags, Tag selectedTag) {
        this.mTags = tags;
        notifyItemChanged(mFilteredTags.indexOf(selectedTag));
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox cbTag;

        public TagViewHolder(ItemTagSelectionBinding itemTagSelectionBinding) {
            super(itemTagSelectionBinding.getRoot());
            cbTag = itemTagSelectionBinding.cbTag;
        }
    }
}