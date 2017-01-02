package com.pfh.promiselist.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pfh.promiselist.R;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.UuidUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Administrator on 2017/1/2 0002.
 */

public class ProjectChooseView extends LinearLayout {

    private Context mContext;
    private LayoutInflater inflater;
    private String newProjectName;
    private Realm realm;
    private List<Task> selectedTasks;
    private List<RadioButton> radioButtonList = new ArrayList<>();


    public ProjectChooseView(Context context) {
        this(context,null);
    }

    public ProjectChooseView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProjectChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Project> allProjects , final List<Task> selectedTasks, Realm realm){
        this.removeAllViews();
        setOrientation(VERTICAL);
        this.realm = realm;
        this.selectedTasks = selectedTasks;
        radioButtonList.clear();

        for (int i = 0; i < allProjects.size(); i++) {
            View item_project = inflater.inflate(R.layout.item_project_selection, null);
            TextView tv_project_name = (TextView) item_project.findViewById(R.id.tv_project_name);
            tv_project_name.setText(allProjects.get(i).getName());
            final RadioButton rb = (RadioButton) item_project.findViewById(R.id.rb);
            item_project.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onProjectSelectedListener != null) {
                        Project project = (Project) rb.getTag();
                        for (int j = 0; j < selectedTasks.size(); j++) {
                            selectedTasks.get(j).setProject(project);
                        }
                        onProjectSelectedListener.projectSelected();
                    }
                }
            });
            rb.setTag(allProjects.get(i));
            radioButtonList.add(rb);
            addView(item_project);
        }

        if (isAllTasksSameProject(selectedTasks)) {
            for (int i = 0; i < radioButtonList.size(); i++) {
                Project project = (Project) radioButtonList.get(i).getTag();
                if (project.getProjectId().equals(selectedTasks.get(0).getProject().getProjectId())){
                    radioButtonList.get(i).setChecked(true);
                }
            }
        }

        View item_add = inflater.inflate(R.layout.item_add_project, null);
        item_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProjectDialog();
            }
        });

        addView(item_add);
    }

    private void showAddProjectDialog() {
        newProjectName = "";
        new MaterialDialog.Builder(mContext)
                .title(R.string.new_project)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .alwaysCallInputCallback()
                .content("")
                .input(R.string.new_tag_name, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.e("TAG","input: "+input);
                        newProjectName = input.toString();
                        dialog.setContent("");
                        if (TextUtils.isEmpty(newProjectName)){
//                            dialog.setContent("清单名不能为空!");
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            return;
                        }
                        if ( RealmDB.isProjectNameExist(realm,newProjectName)){
                            dialog.setContent("清单名重复!");
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            return;
                        }
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (!TextUtils.isEmpty(newProjectName)) {
                            Project project = new Project();
                            project.setName(newProjectName);
                            project.setProjectId(UuidUtils.getShortUuid());
                            project.setCreatedTime(new Date().getTime());
                            project.setState(1);
                            RealmDB.saveProject(realm,project);
                            setData(RealmDB.getAllActiveProjectsByUserId(realm,RealmDB.getCurrentUserId()),selectedTasks,realm);
                        }
                    }
                })
                .show();
    }

    private boolean isAllTasksSameProject(List<Task> selectedTasks){
        HashSet<String> projectSet = new HashSet<>();
        for (int i = 0; i < selectedTasks.size(); i++) {
            if (selectedTasks.get(i).getProject() != null) {
                projectSet.add(selectedTasks.get(i).getProject().getProjectId());
            }
        }
        return projectSet.size() == 1;// 0 1 多
    }

    private onProjectSelectedListener onProjectSelectedListener;

    public void setOnProjectSelectedListener(onProjectSelectedListener listener){
        this.onProjectSelectedListener = listener;

    }
    public interface onProjectSelectedListener{
        void projectSelected();
    }


}
