package com.tags.selection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tags.popuplibrary.TagSelectCallbackSelectionFragment;
import com.tags.popuplibrary.models.Tag;
import com.tags.popuplibrary.models.Tags;
import com.tags.popuplibrary.models.tagSubmitCallback;

import java.util.ArrayList;
import java.util.List;

import static com.tags.popuplibrary.models.Constants.BundleKeys.MAX_SELECTABLE_TAGS;
import static com.tags.popuplibrary.models.Constants.BundleKeys.TAGS;

public class MainActivity extends AppCompatActivity implements tagSubmitCallback {
    private Tags tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("1", "Tag1"));
        tagList.add(new Tag("2", "Tag2"));
        tagList.add(new Tag("3", "Tag3"));
        tagList.add(new Tag("4", "Tag4"));
        tagList.add(new Tag("5", "Tag5"));
        tagList.add(new Tag("6", "Tag6"));
        tagList.add(new Tag("7", "Tag7"));
        tagList.add(new Tag("8", "Tag8"));
        tagList.add(new Tag("9", "Tag9"));
        tagList.add(new Tag("10", "Tag10"));
        tags = new Tags();
        tags.setAllTags(tagList);

       /* getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, TagSelectionFragment.newInstance(tags), TagSelectionFragment.class.getSimpleName())
                .commit();*/
    }

    public void openDialog(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAGS, tags);
        //bundle.putSerializable(MAX_SELECTABLE_TAGS, 3);
        TagSelectCallbackSelectionFragment tagSelectionFragment = TagSelectCallbackSelectionFragment.newInstance(bundle);
        tagSelectionFragment.show(getSupportFragmentManager().beginTransaction(), tagSelectionFragment.getClass().getSimpleName());
    }

    public void changeActivity(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    @Override
    public void onSubmit(Tags tags) {
        this.tags = tags;
    }
}