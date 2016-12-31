package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by Administrator on 2016/12/31 0031.
 */

public class TagsChooseView extends LinearLayout {
    private LayoutInflater inflater;
    private List<CustomCheckBox> checkBoxList = new ArrayList<>();
    private List<Task> selectedTasks;

    public TagsChooseView(Context context) {
        this(context,null);
    }

    public TagsChooseView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TagsChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Tag> allTags, List<Task> selectedTasks){
        this.removeAllViews();
        setOrientation(VERTICAL);

        this.selectedTasks = selectedTasks;
        HashMap<String,Integer> tagsCount = new HashMap<>();//key为tag的tagId,value为该tag在选中的任务中有的个数
        for (int i = 0; i < selectedTasks.size(); i++) {
            RealmList<Tag> tags = selectedTasks.get(i).getTags();
            for (int j = 0; j < tags.size(); j++) {
                if (tagsCount.get(tags.get(j).getTagId()) == null){
                    tagsCount.put(tags.get(j).getTagId(),1);
                }else {
                    Integer preCount = tagsCount.get(tags.get(j).getTagId());
                    tagsCount.put(tags.get(j).getTagId(),preCount+1);
                }
            }
        }
        for (int i = 0; i < allTags.size(); i++) {
            View item_tag = inflater.inflate(R.layout.item_tag_selection, null);
            TextView tv_tag_name = (TextView) item_tag.findViewById(R.id.tv_tag_name);
            tv_tag_name.setText(allTags.get(i).getName());
            CustomCheckBox cb = (CustomCheckBox) item_tag.findViewById(R.id.cb);
            Integer count = tagsCount.get(allTags.get(i).getTagId());
            if (count == null){
                cb.setChecked(false);
            }else if (count == selectedTasks.size()){
                cb.setChecked(true);
            }else {
                cb.setHalfChecked(true);
            }
            cb.setTag(allTags.get(i));
            checkBoxList.add(cb);
            addView(item_tag);
        }
    }

    public void onPositive(){
        for (int i = 0; i < checkBoxList.size(); i++) {
            Tag tag = (Tag) checkBoxList.get(i).getTag();
            for (int j = 0; j < selectedTasks.size(); j++) {
                if (checkBoxList.get(i).isChecked()){
                    if (!selectedTasks.get(j).getTags().contains(tag)){
                        selectedTasks.get(j).getTags().add(tag);
                    }
                }else {
                    if (selectedTasks.get(j).getTags().contains(tag)){
                        selectedTasks.get(j).getTags().remove(tag);
                    }
                }

            }
        }
    }
}
