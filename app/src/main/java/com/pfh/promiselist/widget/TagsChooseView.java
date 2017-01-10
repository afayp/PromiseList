package com.pfh.promiselist.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pfh.promiselist.R;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.UuidUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Administrator on 2016/12/31 0031.
 */

public class TagsChooseView extends LinearLayout {
    private LayoutInflater inflater;
    private List<CustomCheckBox> checkBoxList = new ArrayList<>();
    private List<Task> selectedTasks;
    private Context mContext;
    private String newTagName;
    private Realm realm;

    public TagsChooseView(Context context) {
        this(context,null);
    }

    public TagsChooseView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TagsChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Tag> allTags, List<Task> selectedTasks, Realm realm){
        this.removeAllViews();
        setOrientation(VERTICAL);
        this.realm = realm;
        this.selectedTasks = selectedTasks;
        checkBoxList.clear();
        HashMap<String,Integer> tagsCount = new HashMap<>();//key为tag的tagId,value为该tag在选中的任务中有的个数
        for (int i = 0; i < selectedTasks.size(); i++) {
            RealmList<Tag> tags = selectedTasks.get(i).getTags();
            if (tags != null){
                for (int j = 0; j < tags.size(); j++) {
                    if (tagsCount.get(tags.get(j).getTagId()) == null){
                        tagsCount.put(tags.get(j).getTagId(),1);
                    }else {
                        Integer preCount = tagsCount.get(tags.get(j).getTagId());
                        tagsCount.put(tags.get(j).getTagId(),preCount+1);
                    }
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
        View add_tag = inflater.inflate(R.layout.item_add_tag, null);
        add_tag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTagDialog();
            }
        });
        addView(add_tag);
    }

    private void showAddTagDialog() {
        newTagName = "";
        new MaterialDialog.Builder(mContext)
                .title(R.string.new_tag)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .alwaysCallInputCallback()
                .content("")
                .contentColorRes(R.color.red)
                .input(R.string.new_tag_name, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.e("TAG","input: "+input);
                        newTagName = input.toString();
                        dialog.setContent("");
                        if (TextUtils.isEmpty(newTagName)) {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            return;
                        }
                        if ( RealmDB.isTagNameExist(realm,newTagName)){
                            dialog.setContent("标签名重复!");
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            return;
                        }
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (!TextUtils.isEmpty(newTagName)) {
                            Tag tag = new Tag();
                            tag.setName(newTagName);
                            tag.setTagId(UuidUtils.getShortUuid());
                            RealmDB.saveTag(realm,tag);
                            setData(RealmDB.getAllTagsByUserId(realm,RealmDB.getCurrentUserId()),selectedTasks,realm);
                        }
                    }
                })
                .show();
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
