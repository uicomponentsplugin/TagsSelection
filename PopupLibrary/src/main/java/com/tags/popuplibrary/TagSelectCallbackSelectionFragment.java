package com.tags.popuplibrary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tags.popuplibrary.databinding.FragmentTagSelectionListBinding;
import com.tags.popuplibrary.databinding.TagSelectedBinding;
import com.tags.popuplibrary.models.Tag;
import com.tags.popuplibrary.models.Tags;
import com.tags.popuplibrary.models.tagSelectCallback;
import com.tags.popuplibrary.models.tagSubmitCallback;

import static com.tags.popuplibrary.models.Constants.BundleKeys.MAX_SELECTABLE_TAGS;
import static com.tags.popuplibrary.models.Constants.BundleKeys.TAGS;

public class TagSelectCallbackSelectionFragment extends DialogFragment implements tagSelectCallback, View.OnClickListener {
    private int mMaxSelectableTags;
    private Tags mTags;
    private FragmentTagSelectionListBinding binding;
    private tagSubmitCallback tagSubmitCallback;

    public TagSelectCallbackSelectionFragment() {
    }

    public static TagSelectCallbackSelectionFragment newInstance(Tags tags, int maxSelectableTags) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAGS, tags);
        bundle.putSerializable(MAX_SELECTABLE_TAGS, maxSelectableTags);
        TagSelectCallbackSelectionFragment tagSelectionFragment = new TagSelectCallbackSelectionFragment();
        tagSelectionFragment.setArguments(bundle);
        return tagSelectionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null || !(getArguments().getSerializable(TAGS) instanceof Tags))
            return;
        mTags = (Tags) getArguments().getSerializable(TAGS);
        mMaxSelectableTags = getArguments().getInt(MAX_SELECTABLE_TAGS);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTagSelectionListBinding.inflate(inflater);
        binding.rvTags.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.rvTags.setAdapter(new TagSelectionAdapter(this, mTags, mMaxSelectableTags));
        binding.rvTags.post(this::displaySelectedTags);
        binding.etTagsSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TagSelectionAdapter) binding.rvTags.getAdapter()).getFilter().filter(s.toString());
            }
        });
        binding.btnSubmitTagSelection.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        tagSubmitCallback = (tagSubmitCallback) context;
    }

    @Override
    public void onSelect(Tag tag, boolean isChecked) {
        if (isChecked)
            mTags.getSelectedTags().add(tag);
        else
            mTags.getSelectedTags().remove(tag);
        displaySelectedTags();
    }

    private void displaySelectedTags() {
        binding.llSelectedTags.removeAllViews();
        for (Tag selectedTag : mTags.getSelectedTags()) {
            //TagSelectedBinding tagSelectedBinding = TagSelectedBinding.inflate(LayoutInflater.from(binding.getRoot().getContext()), binding.llSelectedTags, true);
            TagSelectedBinding tagSelectedBinding = TagSelectedBinding.inflate(LayoutInflater.from(binding.getRoot().getContext()));
            tagSelectedBinding.getRoot().setId(mTags.getSelectedTags().indexOf(selectedTag) + 5);
            tagSelectedBinding.imgRemoveTag.setId(tagSelectedBinding.getRoot().getId());
            tagSelectedBinding.txtTagName.setText(selectedTag.getName());
            tagSelectedBinding.imgRemoveTag.setOnClickListener(imgRemoveTag -> {
                int imgRemoveTagViewId = imgRemoveTag.getId();
                binding.llSelectedTags.removeView(binding.llSelectedTags.findViewById(imgRemoveTagViewId));
                mTags.getSelectedTags().remove(selectedTag);
                ((TagSelectionAdapter) binding.rvTags.getAdapter()).updateTag(mTags, selectedTag);
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Util.setLayoutParamsMargin(params, 16);
            tagSelectedBinding.getRoot().setLayoutParams(params);
            binding.llSelectedTags.addView(tagSelectedBinding.getRoot());
            /*int stackCount  = binding.llSelectedTags.getChildCount();
            binding.llSelectedTags.getChildCount();*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnSubmitTagSelection.getId()) {
            tagSubmitCallback.onSubmit(mTags);
            ((FragmentActivity) requireContext()).getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}