package com.pfh.promiselist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pfh.promiselist.R;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.others.Constant;
import com.pfh.promiselist.widget.ManageItemView;

import java.util.List;

import io.realm.Realm;


/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class ManageItemAdapter extends RecyclerView.Adapter<ManageItemAdapter.MyViewHolder> {

    private int type;
    private List<Project> allProjects;
    private List<Tag> allTags;
    private LayoutInflater inflater;
    private Realm realm;
    private Context context;
    private String label;

    public ManageItemAdapter(Context context, List<Tag> allTags, List<Project> allProjects, int type,Realm realm) {
        this.context = context;
        this.allTags = allTags;
        this.allProjects = allProjects;
        this.type = type;
        inflater = LayoutInflater.from(context);
        this.realm = realm;
        if (type == Constant.MANAGE_TYPE_PROJECT) {
            label = "清单";
        }else {
            label = "标签";
        }
    }

    public void addProject(Project project){
        allProjects.add(0,project);
        notifyItemRangeChanged(0,allProjects.size());
    }

    public void addTag(Tag tag) {
        allTags.add(0,tag);
        notifyItemRangeChanged(0,allTags.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_manage_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        holder.manage_item.getIvEdit().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .title("编辑"+label)
                        .positiveText("确认")
                        .negativeText("取消")
                        .alwaysCallInputCallback()
                        .input(label+"名", getDefaultName(holder.getLayoutPosition()), false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                if (TextUtils.isEmpty(input.toString())) {
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                    return;
                                }
                                if (checkExits(input.toString(),getDefaultName(holder.getLayoutPosition()))) {
                                    dialog.setContent("该" +label +"已存在");
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                    return;
                                }
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);

                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Log.e("TAG","input: "+dialog.getInputEditText().getText().toString());
                                String s = dialog.getInputEditText().getText().toString();
                                if (type == Constant.MANAGE_TYPE_PROJECT) {
                                    allProjects.get(holder.getLayoutPosition()).setName(s);
                                }else {
                                    allTags.get(holder.getLayoutPosition()).setName(s);
                                }
                                notifyItemChanged(holder.getLayoutPosition());
                            }
                        }).show();
            }
        });

        holder.manage_item.getIvDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == Constant.MANAGE_TYPE_PROJECT) {
                    new MaterialDialog.Builder(context)
                            .title("确认删除该清单？")
                            .content("清单中的所有任务将被删除。")
                            .positiveText("确认")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    int position = holder.getLayoutPosition();
                                    RealmDB.deleteProjectByProjectId(realm,allProjects.get(position).getProjectId());
                                    allProjects.remove(position);
                                    notifyItemRemoved(position);
                                }
                            })
                            .show();


                }else if (type == Constant.MANAGE_TYPE_TAG){

                    new MaterialDialog.Builder(context)
                            .title("确认删除该标签？")
                            .content("删除后，清单中的任务将不再包含此标签。")
                            .positiveText("确认")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    int position = holder.getLayoutPosition();
                                    RealmDB.deleteTag(realm,allTags.get(position));
                                    allTags.remove(position);
                                    notifyItemRemoved(position);
                                }
                            })
                            .show();
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (type == Constant.MANAGE_TYPE_PROJECT) {
            holder.manage_item.setProject(allProjects.get(position),Constant.MANAGE_TYPE_PROJECT,realm);

        }else if (type == Constant.MANAGE_TYPE_TAG) {
            holder.manage_item.setTag(allTags.get(position),Constant.MANAGE_TYPE_TAG,realm);
        }
    }


    @Override
    public int getItemCount() {
        return type == Constant.MANAGE_TYPE_PROJECT ? allProjects.size() : allTags.size();
    }

    private String getDefaultName(int position){
        if (type == Constant.MANAGE_TYPE_PROJECT) {
            return allProjects.get(position).getName();
        }else {
            return allTags.get(position).getName();
        }

    }
    private boolean checkExits(String input,String defaultName) {
        if (type == Constant.MANAGE_TYPE_PROJECT ) {
            if (defaultName.equals(input)) { // 原来的名字
                return false;
            }
            return RealmDB.isProjectNameExist(realm,input);
        }else if (type == Constant.MANAGE_TYPE_TAG ) {
            if (defaultName.equals(input)) { // 原来的名字
                return false;
            }
            return RealmDB.isTagNameExist(realm,input);
        }
        return false;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ManageItemView manage_item;
        public MyViewHolder(View itemView) {
            super(itemView);
            manage_item = (ManageItemView) itemView.findViewById(R.id.manage_item);
        }
    }
}
