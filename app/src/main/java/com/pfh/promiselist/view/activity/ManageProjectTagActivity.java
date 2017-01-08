package com.pfh.promiselist.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pfh.promiselist.R;
import com.pfh.promiselist.adapter.ManageItemAdapter;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.others.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class ManageProjectTagActivity extends BaseActivity {

    public static final String MANAGE_TYPE = "manage_type";
    @BindView(R.id.rv_content)
    RecyclerView rv_content;
    //    public static final int  MANAGE_PROJECT_TYPE = 1;
//    public static final int  MANAGE_TAG_TYPE = 2;
    private int type;
    private boolean editing;
    private List<Project> allActiveProjects;
    private List<Tag> allTags;
    private ManageItemAdapter manageItemAdapter;
    private ImageView iv_plus;
    private String label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tag);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra(MANAGE_TYPE, 1);
        if (type == Constant.MANAGE_TYPE_PROJECT) {
            label = "清单";
        } else {
            label = "标签";
        }
        initViews();
    }

    private void initViews() {
//        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        final TextView tv_title = (TextView) findViewById(R.id.tv_title);
        iv_plus = (ImageView) findViewById(R.id.iv_plus);
//        rv_content = (RecyclerView) findViewById(R.id.rv_content);
        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(type == Constant.MANAGE_TYPE_PROJECT ? "创建新清单" : "创建新标签");
        tv_title.setText(type == Constant.MANAGE_TYPE_PROJECT ? "管理清单" : "管理标签");
        RelativeLayout rl_new = (RelativeLayout) findViewById(R.id.rl_new);
        rl_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mContext)
                        .title("新建" + label)
                        .positiveText("确认")
                        .negativeText("取消")
                        .alwaysCallInputCallback()
                        .input(label + "名", "", false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                if (TextUtils.isEmpty(input.toString())) {
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                    return;
                                }
                                if (checkExits(input.toString())) {
                                    dialog.setContent("该" + label + "已存在");
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                    return;
                                }
                                dialog.setContent("");
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Log.e("TAG", "input: " + dialog.getInputEditText().getText().toString());
                                String s = dialog.getInputEditText().getText().toString();
                                if (type == Constant.MANAGE_TYPE_PROJECT) {
                                    Project newProject = RealmDB.createAndSaveProject(mRealm, s);
                                    manageItemAdapter.addProject(newProject);
                                } else {
                                    Tag newTag = RealmDB.createAndSaveTag(mRealm, s);
                                    manageItemAdapter.addTag(newTag);
                                }
                            }
                        }).show();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_content.setLayoutManager(manager);

        allActiveProjects = RealmDB.getAllActiveProjectsByUserId(mRealm, RealmDB.getCurrentUserId());
        allTags = RealmDB.getAllTagsByUserId(mRealm, RealmDB.getCurrentUserId());

        manageItemAdapter = new ManageItemAdapter(mContext, allTags, allActiveProjects, type, mRealm);
        rv_content.setAdapter(manageItemAdapter);

    }

    @OnClick(R.id.iv_back)
    public void back(){
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private boolean checkExits(String name) {
        if (type == Constant.MANAGE_TYPE_PROJECT) {
            return RealmDB.isProjectNameExist(mRealm, name);
        } else if (type == Constant.MANAGE_TYPE_TAG) {
            return RealmDB.isTagNameExist(mRealm, name);
        }
        return false;
    }


}
