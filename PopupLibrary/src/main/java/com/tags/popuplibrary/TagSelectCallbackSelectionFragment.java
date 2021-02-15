package com.tags.popuplibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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

import static com.tags.popuplibrary.models.Constants.BundleKeys.BUTTON_COLOR;
import static com.tags.popuplibrary.models.Constants.BundleKeys.BUTTON_TEXT_COLOR;
import static com.tags.popuplibrary.models.Constants.BundleKeys.CHECK_BOX_COLOR;
import static com.tags.popuplibrary.models.Constants.BundleKeys.MAX_SELECTABLE_TAGS;
import static com.tags.popuplibrary.models.Constants.BundleKeys.TAGS;
import static com.tags.popuplibrary.models.Constants.BundleKeys.TAG_CANCEL_COLOR;

public class TagSelectCallbackSelectionFragment extends DialogFragment implements tagSelectCallback, View.OnClickListener, DialogInterface.OnKeyListener {
    private Integer mMaxSelectableTags;
    private Integer mSubmitColor;
    private Integer mSubmitTextColor;
    private Integer mCheckBoxColor;
    private Integer mTagCancelColor;
    private Tags mTags;
    private FragmentTagSelectionListBinding binding;
    private tagSubmitCallback tagSubmitCallback;

    public TagSelectCallbackSelectionFragment() {
    }

    public static TagSelectCallbackSelectionFragment newInstance(Bundle bundle) {
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

        if (getArguments().getInt(MAX_SELECTABLE_TAGS) != 0)
            mMaxSelectableTags = getArguments().getInt(MAX_SELECTABLE_TAGS);
        if (getArguments().getInt(BUTTON_COLOR) != 0)
            mSubmitColor = getArguments().getInt(BUTTON_COLOR);
        if (getArguments().getInt(BUTTON_TEXT_COLOR) != 0)
            mSubmitTextColor = getArguments().getInt(BUTTON_TEXT_COLOR);
        if (getArguments().getInt(CHECK_BOX_COLOR) != 0)
            mCheckBoxColor = getArguments().getInt(CHECK_BOX_COLOR);
        if (getArguments().getInt(TAG_CANCEL_COLOR) != 0)
            mTagCancelColor = getArguments().getInt(TAG_CANCEL_COLOR);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTagSelectionListBinding.inflate(inflater);
        binding.rvTags.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.rvTags.setAdapter(new TagSelectionAdapter(this, mTags, mMaxSelectableTags, mCheckBoxColor));
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
        if (mSubmitColor != null)
            binding.btnSubmitTagSelection.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(mSubmitColor)));
        if (mMaxSelectableTags != null)
            binding.btnSubmitTagSelection.setTextColor(ColorStateList.valueOf(getContext().getColor(mSubmitTextColor)));
        getDialog().setOnKeyListener(this);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        tagSubmitCallback = (tagSubmitCallback) context;
    }

    @Override
    public void onSelect(Tag tag, boolean isChecked) {
        if (isChecked
                && !mTags.getSelectedTags().contains(tag))
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
            if (mTagCancelColor != null)
                tagSelectedBinding.imgRemoveTag.getDrawable().setColorFilter(tagSelectedBinding.getRoot().getContext().getColor(mTagCancelColor), PorterDuff.Mode.MULTIPLY);
           /* tagSelectedBinding.getRoot().setPadding(Util.dpToPx((int) getContext().getResources().getDimension(R.dimen.selected_tag_padding))
                    , Util.dpToPx((int) getContext().getResources().getDimension(R.dimen.selected_tag_padding))
                    , Util.dpToPx((int) getContext().getResources().getDimension(R.dimen.selected_tag_padding))
                    , Util.dpToPx((int) getContext().getResources().getDimension(R.dimen.selected_tag_padding))
            );*/
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
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(binding.getRoot().getContext(), R.color.black)));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnSubmitTagSelection.getId()) {
            dismissDialog();
        }
    }

    private void dismissDialog() {
        tagSubmitCallback.onSubmit(mTags);
        ((FragmentActivity) requireContext()).getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismissDialog();
        }
        return true;
    }
}