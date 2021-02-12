package com.tags.popuplibrary;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tags.popuplibrary.databinding.ItemTagSelectionBinding;
import com.tags.popuplibrary.models.Tag;
import com.tags.popuplibrary.models.Tags;
import com.tags.popuplibrary.models.tagSelectCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TagSelectionAdapter extends RecyclerView.Adapter<TagSelectionAdapter.TagViewHolder> implements Filterable, CompoundButton.OnCheckedChangeListener {

    private Tags mTags;
    private final int mMaxSelectableTags;
    private final List<Tag> mFilteredTags = new ArrayList<>();
    private final com.tags.popuplibrary.models.tagSelectCallback tagSelectCallback;

    public TagSelectionAdapter(tagSelectCallback tagSelectCallback, Tags tags, int maxSelectableTags) {
        this.tagSelectCallback = tagSelectCallback;
        this.mMaxSelectableTags = maxSelectableTags;
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
        holder.cbTag.setTag(position);
        holder.cbTag.setChecked(mTags.getSelectedTags().contains(mFilteredTags.get(position)));
        holder.cbTag.setOnCheckedChangeListener(this);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mTags.getSelectedTags().size() <= mMaxSelectableTags) {
            int position = (int) buttonView.getTag();
            tagSelectCallback.onSelect(mFilteredTags.get(position), isChecked);
        } else {
            Util.shortToast(buttonView.getContext(), String.format(Locale.getDefault(), "Max %d item(s) are allowed", mMaxSelectableTags));
        }
    }

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