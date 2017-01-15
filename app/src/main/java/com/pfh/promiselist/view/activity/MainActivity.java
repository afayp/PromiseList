package com.pfh.promiselist.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bumptech.glide.Glide;
import com.pfh.promiselist.R;
import com.pfh.promiselist.adapter.CustomItemTouchHelpCallback;
import com.pfh.promiselist.adapter.NavItemAdapter;
import com.pfh.promiselist.adapter.TaskListAdapter;
import com.pfh.promiselist.dao.PresetData;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.dao.SimulatedData;
import com.pfh.promiselist.model.MultiItemModel;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.others.Constant;
import com.pfh.promiselist.others.CustomItemAnimator;
import com.pfh.promiselist.others.DiffCallback;
import com.pfh.promiselist.others.SpacesItemDecoration;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.DensityUtil;
import com.pfh.promiselist.utils.SPUtil;
import com.pfh.promiselist.widget.CircleImageView;
import com.pfh.promiselist.widget.CustomPopupWindow;
import com.pfh.promiselist.widget.NavigationItem;
import com.pfh.promiselist.widget.ProjectChooseView;
import com.pfh.promiselist.widget.TagsChooseView;
import com.pfh.promiselist.widget.TaskListToolbar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pfh.promiselist.utils.SPUtil.get;


/**
 * 首页 展示当前筛选条件(时间、清单...)下的所有任务
 */
public class MainActivity extends BaseActivity implements
        ColorChooserDialog.ColorCallback,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final int REQUEST_CODE_NEW_TASK = 1; // 新建任务
    public static final int REQUEST_CODE_EDIT_TASK = 2; // 修改任务
    private static final int REQUEST_CODE_PICK_IMAGE_FOR_AVATAR = 3;
    private static final int REQUEST_CODE_PICK_IMAGE_FOR_USER_BG = 4;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA_FOR_AVATAR = 5;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA_FOR_USER_BG = 6;

    public static final int REQUEST_CODE_MANAGE = 7; // 管理清单或者标签
    public static final String EDIT_TASK_ID = "edit_task_id"; // 修改的任务

    public static final int space = 2;// dp


    //--------------data-------------------//
    private int display; // 用户上次选择的模块，默认今日任务
    private int orderMode;// 读取用户上次的模式，默认1,暂时不考虑多用户
    private String selectedProjectId;// if(display ==DISPLAY_PROJECT) 读取用户上次选择的在主界面展示的projectId

    private List<Project> selectedProjects = new ArrayList<>();//选择的项目，多个或1个
    private List<MultiItemModel> modelList = new ArrayList<>();// 根据排序模式处理过的数据
    private List<Task> searchResultTasks = new ArrayList<>();
    private TaskListAdapter mTaskListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<Task> selectedTasks = new ArrayList<>();//所有选中的task
    private Calendar targetCalendar; // 选择的日期
    private Task deletedTask;
    private boolean isDragSuccess = false;
    private NavItemAdapter navProjectAdapter;
    private NavItemAdapter navTagAdapter;
    private String selectedTagId;
    private List<Project> navActiveProjects;
    private List<Tag> navTags;
    private boolean displayChange; // 标识主动点击了nav上选项

    //---------------view-------------------//

    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @BindView(R.id.toolbar)
    TaskListToolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.fb_add)
    FloatingActionButton fb_add;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    //--------nav---------//
    @BindView(R.id.nav_avatar)
    CircleImageView nav_avatar;
    @BindView(R.id.nav_today)
    NavigationItem nav_today;
    @BindView(R.id.nav_all_task)
    NavigationItem nav_all_task;
    @BindView(R.id.rv_projects)
    RecyclerView rv_projects;
    @BindView(R.id.nav_all_projects)
    NavigationItem nav_all_projects;
    @BindView(R.id.nav_all_tags)
    NavigationItem nav_all_tags;
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.nav_manage_project)
    NavigationItem nav_manage_project;
    @BindView(R.id.nav_manage_tag)
    NavigationItem nav_manage_tag;
    @BindView(R.id.nav_sv)
    ScrollView nav_sv;
    @BindView(R.id.rl_user_bg)
    RelativeLayout rl_user_bg;
    @BindView(R.id.ll_empty_view)
    LinearLayout ll_empty_view;
    private File mCurrentPhotoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        initSimulatedData();// todo
        PresetData.createSimulatedData(mContext,mRealm);
        initSymbol();
        modelList = loadData();
        initViews();
    }

    private void initSymbol() {
        display = (int) get(mContext, Constant.DISPLAY_INDEX_KEY,Constant.DISPLAY_TODAY);
        orderMode = (int) get(mContext, Constant.lAST_ORDER_MODE_KEY, Constant.ORDER_BY_DATE);
        selectedProjectId = (String) get(mContext, Constant.LAST_SELECTED_PROJECT_KEY, "");
        selectedTagId = (String) get(mContext, Constant.LAST_SELECTED_TAG_KEY,"");

        Log.e("TAG","display: " +display);
        Log.e("TAG","orderMode: " +orderMode);
        Log.e("TAG","selectedProjectId: " +selectedProjectId);
        Log.e("TAG","selectedTagId: " +selectedTagId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SPUtil.put(mContext,Constant.DISPLAY_INDEX_KEY,display);
        SPUtil.put(mContext,Constant.lAST_ORDER_MODE_KEY,orderMode);
        SPUtil.put(mContext,Constant.LAST_SELECTED_PROJECT_KEY,selectedProjectId);
        SPUtil.put(mContext,Constant.LAST_SELECTED_TAG_KEY,selectedTagId);
    }

    /**
     * 根据当前选择的模式从数据库中加载数据并处理好，用来传给adapter解析
     * 可以选择所有项目，也可以选择特定项目
     * 排序方式分：按日期排序1、按项目排序2、
     */
    private List<MultiItemModel> loadData() {
        List<MultiItemModel> modelList = null;
        if (display == Constant.DISPLAY_TODAY) {
            modelList = sortTaskByMode(RealmDB.getAllTodayUncompletedTasksByUserId(mRealm,RealmDB.getCurrentUserId()),orderMode);
        }else if (display == Constant.DISPLAY_ALL_TASKS) {
            modelList = sortTaskByMode(RealmDB.getAllUncompletedTasksByUserId(mRealm,RealmDB.getCurrentUserId()),orderMode);
        }else if (display == Constant.DISPLAY_PROJECT) {
            modelList = sortTaskByMode(RealmDB.getAllUncompletedTasksByProjectId(mRealm,selectedProjectId),orderMode);
        }else if (display == Constant.DISPLAY_TAG) {
            modelList = sortTaskByMode(RealmDB.getAllUncompletedTasksByTagId(mRealm,selectedTagId),orderMode);
        }
        for (int i = 0; i < modelList.size(); i++) {
            Log.e("TAG","modeList: "+modelList.get(i));
        }
        ll_empty_view.setVisibility(modelList.size() == 0 ? View.VISIBLE : View.GONE);
        initTitle();
        return modelList;
    }

    /**
     * @param orderMode
     * @param projectId 为"" 则表示加载所有项目
     * @return
     */
    private List<MultiItemModel> getDataByProject(int orderMode, String projectId) {
        List<Project> selectedProjects = loadProjects(projectId);
        List<MultiItemModel> tempModeList = new ArrayList<>();
        //按日期排序，分今天，明天，以后
        if (orderMode == Constant.ORDER_BY_DATE) {
            List<Task> allTasks = new ArrayList<>();
            //所有task
            for (int i = 0; i < selectedProjects.size(); i++) {
                allTasks.addAll(RealmDB.getAllUncompletedTasksByProjectId(mRealm, selectedProjects.get(i).getProjectId()));
            }
            //筛选出固定的
            List<MultiItemModel> fixedList = new ArrayList<>();
            List<Task> temp = new ArrayList<>();
            for (int i = 0; i < allTasks.size(); i++) {
                if (allTasks.get(i).isFixed()) {
                    fixedList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK, allTasks.get(i), getString(R.string.label_fixed)));
                } else {
                    temp.add(allTasks.get(i));
                }
            }
            if (fixedList.size() != 0) { // 如果没有则不显示
                tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_FIXED, getString(R.string.label_fixed)));
                tempModeList.addAll(fixedList);
                tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_OTHER, getString(R.string.label_others)));
            }

            if (temp.size() != 0) { // 如果没有则不显示
                //对剩下的按时间排序
                Collections.sort(temp, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return (int) (o1.getDueTime() - o2.getDueTime());
                    }
                });
                for (int i = 0; i < temp.size(); i++) {
                    tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK, temp.get(i), getString(R.string.label_others)));
                }
            }
        } else if (orderMode == Constant.ORDER_BY_PROJECT) {
            //按清单排序
//            List<MultiItemModel> fixedList = new ArrayList<>();
            List<Task> fixedTaskList = new ArrayList<>();
            for (int i = 0; i < selectedProjects.size(); i++) {
                List<Task> tasks = RealmDB.getFixedUncompletedTasksByProjectId(mRealm, selectedProjects.get(i).getProjectId(), true);
                fixedTaskList.addAll(tasks);
            }
            if (fixedTaskList.size() != 0) {
                tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_FIXED, getString(R.string.label_fixed)));//固定
                for (int i = 0; i < fixedTaskList.size(); i++) {
                    tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK, fixedTaskList.get(i), getString(R.string.label_fixed)));
                }
            }

            for (int i = 0; i < selectedProjects.size(); i++) {
                List<Task> tasks = RealmDB.getFixedUncompletedTasksByProjectId(mRealm, selectedProjects.get(i).getProjectId(), false);
                if (tasks.size() != 0) {
                    MultiItemModel projectModel = new MultiItemModel(Constant.ITEM_TYPE_PROJECT, selectedProjects.get(i).getName());
                    projectModel.setData(selectedProjects.get(i));
                    tempModeList.add(projectModel);
                    for (int j = 0; j < tasks.size(); j++) {
                        tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK, tasks.get(j), selectedProjects.get(i).getName()));
                    }
                }
            }

//            for (int i = 0; i < selectedProjects.size(); i++) {
//                List<Task> tasksByProjectId = RealmDB.getAllUncompletedTasksByProjectId(mRealm, selectedProjects.get(i).getProjectId());
//                MultiItemModel projectModel = new MultiItemModel(Constant.ITEM_TYPE_PROJECT, selectedProjects.get(i).getName());
//                projectModel.setData(selectedProjects.get(i));
//                tempModeList.add(projectModel);
//                for (int j = 0; j < tasksByProjectId.size(); j++) {
//
//                    tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,tasksByProjectId.get(j),selectedProjects.get(i).getName()));
//                }
//            }
        }

        for (int i = 0; i < tempModeList.size(); i++) {
            Log.e("TAG", tempModeList.get(i).toString());
        }
        return tempModeList;
    }

    private void initTitle(){
        if (display == Constant.DISPLAY_TODAY) {
            toolbar.setTitle("今天");
        }else if (display == Constant.DISPLAY_ALL_TASKS) {
            toolbar.setTitle("所有任务");
        }else if (display == Constant.DISPLAY_PROJECT) {
            toolbar.setTitle(RealmDB.getProjectByProjectId(mRealm,selectedProjectId).getName());
        }else if (display == Constant.DISPLAY_TAG) {
            toolbar.setTitle(RealmDB.getTagByTagId(mRealm,selectedTagId).getName());
        }

    }

    private List<MultiItemModel> sortTaskByMode(List<Task> tasks , int orderMode){
        List<MultiItemModel> tempModeList = new ArrayList<>();
        List<Task> otherTasks = new ArrayList<>(); // 除了固定或者全部
        List<Task> fixedTasks = new ArrayList<>();
        if (display == Constant.DISPLAY_TODAY || display == Constant.DISPLAY_ALL_TASKS) { // 这两种模式下显示固定，否则不显示固定
            // 选出固定
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).isFixed()) {
                    fixedTasks.add(tasks.get(i));
                }else {
                    otherTasks.add(tasks.get(i));
                }
            }
            if (fixedTasks.size() != 0) {
                tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_FIXED, getString(R.string.label_fixed))); // 固定
                for (int i = 0; i < fixedTasks.size(); i++) {
                    tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK, fixedTasks.get(i), getString(R.string.label_fixed)));
                }
            }
        }else {
            otherTasks = tasks;
        }

        if (orderMode == Constant.ORDER_BY_DATE) {

            if (fixedTasks.size() != 0 && otherTasks.size() != 0 && (display == Constant.DISPLAY_TODAY || display == Constant.DISPLAY_ALL_TASKS)){
                tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_OTHER, getString(R.string.label_others)));// 其他
            }
            Collections.sort(otherTasks, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return (int) (o1.getDueTime() - o2.getDueTime());
                }
            });
            for (int i = 0; i < otherTasks.size(); i++) {
                tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK, otherTasks.get(i), getString(R.string.label_others)));
            }

        }else if (orderMode == Constant.ORDER_BY_PROJECT) {

            HashMap<String, List<Task>> taskByProject = new HashMap<>(); // key 为project name
            for (int i = 0; i < otherTasks.size(); i++) {
                List<Task> taskUnderProject = taskByProject.get(otherTasks.get(i).getProject().getName());
                if (taskUnderProject == null) {
                    taskUnderProject = new ArrayList<>();
                    taskByProject.put(otherTasks.get(i).getProject().getName(), taskUnderProject);
                }
                taskUnderProject.add(otherTasks.get(i));
            }
            for (String key : taskByProject.keySet()) {
                List<Task> allTasksUnderProject = taskByProject.get(key);
                if (allTasksUnderProject.size() != 0) {
                    MultiItemModel projectModel = new MultiItemModel(Constant.ITEM_TYPE_PROJECT, key);
                    projectModel.setData(RealmDB.getProjectByProjectName(mRealm,key));
                    tempModeList.add(projectModel);
                }
                for (int i = 0; i < allTasksUnderProject.size(); i++) {
                    tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK, allTasksUnderProject.get(i), key));
                }
            }
        }
        return tempModeList;
    }

    private List<Project> loadProjects(String selectedProjectId) {
        List<Project> selectedProjects = null;
        if (selectedProjectId.equals("")) {
            selectedProjects = RealmDB.getAllActiveProjectsByUserId(mRealm, RealmDB.getCurrentUserId());
        } else {
            selectedProjects.add(RealmDB.getProjectByProjectId(mRealm, selectedProjectId));
        }
        return selectedProjects;
    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 400;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setItemAnimator(new CustomItemAnimator());
        recyclerview.addItemDecoration(new SpacesItemDecoration(DensityUtil.dp2px(this,space)));
        mTaskListAdapter = new TaskListAdapter(mContext, modelList, orderMode);
        mTaskListAdapter.setOnItemClickListener(new TaskListAdapter.onItemClickListener() {
            @Override
            public void onClickTask(View view, int position) {
                //goto task activity
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                Task task = (Task) modelList.get(position).getContent();
                intent.putExtra(EDIT_TASK_ID, task.getTaskId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, REQUEST_CODE_EDIT_TASK, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, fb_add, "add").toBundle());
                } else {
                    startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
                }
            }

            @Override
            public void onSelectChanged(boolean select, int position, int count) {
                toolbar.setSelect(select);
                toolbar.setSelectCount(count);
                selectedTasks.clear();
                List<Integer> selectedPositions = mTaskListAdapter.getSelectedPositions();
                for (int i = 0; i < selectedPositions.size(); i++) {
                    Task task = (Task) modelList.get(selectedPositions.get(i)).getContent();
                    selectedTasks.add(task);
                }
                boolean allFixed = true;
                for (int i = 0; i < selectedTasks.size(); i++) {
                    if (!selectedTasks.get(i).isFixed()) {
                        allFixed = false;
                    }
                }
                toolbar.setFixedIconActive(allFixed);
            }
        });
        CustomItemTouchHelpCallback callback = new CustomItemTouchHelpCallback(new CustomItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                Log.e("TAG", "adapterPosition: " + adapterPosition);
                deletedTask = (Task) modelList.get(adapterPosition).getContent();
                deletedTask.setState(2);
                Log.e("TAG", "deletedTask: " + deletedTask.toString());
                RealmDB.refreshTask(mRealm, deletedTask);
                refreshTaskListUseDiffUtil();
                showConfrimSnackBar(coordinator, "已完成", "撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletedTask.setState(1);
                        RealmDB.refreshTask(mRealm, deletedTask);
                        refreshTaskListUseDiffUtil();
                    }
                });
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                Log.e("TAG", "src: " + srcPosition);
                Log.e("TAG", "tar: " + targetPosition);
//                if (srcPosition < targetPosition) {
//                    //向下
//                    if (modelList.get(targetPosition).getItemType() != Constant.ITEM_TYPE_TASK && !modelList.get(targetPosition).isExpand()){
////                    modelList.get(targetPosition).setExpand(true);
//                        mTaskListAdapter.expandOrFoldTitle(targetPosition);
//                    }
//                }else {
//                    //向上 targetPosition肯定是本title,所以要去判断再上面title是否展开，
//                    if (targetPosition - 1 < 0 ) return false;
//                    if ( modelList.get(targetPosition - 1).getItemType() != Constant.ITEM_TYPE_TASK
//                            && !modelList.get(targetPosition - 1).isExpand()){//如果是title,则该title下的task一个为0，没展开则展开(只为显示数字..)
//                        mTaskListAdapter.expandOrFoldTitle(targetPosition-1);
//                    }else if (!modelList.get(targetPosition - 1).isExpand()){//如果是task,并且没有展开，则找到task的title,展开
//                        mTaskListAdapter.expandTaskOfTitle(targetPosition - 1);
//                    }
//                }
//                // 如果targetPosition 处于屏幕上下边界，为了方便，屏幕要向上或向下滚动 TODO
//                if (targetPosition - 2 < linearLayoutManager.findFirstCompletelyVisibleItemPosition() && srcPosition > targetPosition ) {
//                    //向上
//                    recyclerview.scrollToPosition(targetPosition -2 == 0 ? 0 : targetPosition -2 );
//                    Log.e("TAG","smoothScrollToPosition up");
//
//                }else if (targetPosition + 2 > linearLayoutManager.findLastCompletelyVisibleItemPosition()){
//                    //向下
//                    recyclerview.scrollToPosition(linearLayoutManager.findFirstCompletelyVisibleItemPosition() + 2);
//                    Log.e("TAG","smoothScrollToPosition down");
//
//                }
                boolean success = changeDataByMode2(srcPosition, targetPosition);
                if (success) {
//                    mTaskListAdapter.setSelectState(false);
                    isDragSuccess = true;
                    toolbar.setSelect(false);
                }
                return success;
                // 先处理拖动 release的时候在处理数据和刷新view,否则太卡
//                Collections.swap(modelList, srcPosition, targetPosition);
//                mTaskListAdapter.notifyItemMoved(srcPosition,targetPosition);
//                return true;
            }

            @Override
            public boolean canSwipe() {
                if (toolbar.isSelect()) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                Log.e("TAG", "onSelectedChanged !" + actionState);

                // 进入选择模式 TODO
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    Log.e("TAG", "onSelectedChanged " + viewHolder.getLayoutPosition());
                }

                switch (actionState) {
                    case ItemTouchHelper.ACTION_STATE_IDLE:
                        Log.e("TAG", "ACTION_STATE_IDLE");
                        break;
                    case ItemTouchHelper.DOWN:
                        Log.e("TAG", "DOWN");
                        break;
                    case ItemTouchHelper.ACTION_STATE_SWIPE:
                        Log.e("TAG", "ACTION_STATE_SWIPE");
                        break;
//                    case ItemTouchHelper.ACTION_STATE_DRAG:
//                        Log.e("TAG","ACTION_STATE_DRAG");
//                        break;
                }
            }

            @Override
            public void onRelease(RecyclerView.ViewHolder viewHolder) {
//                viewHolder.itemView.setBackgroundColor(0);
//                mTaskListAdapter.setSelectState(false);
//                if (modelList.get(viewHolder.getLayoutPosition()).getItemType() == Constant.ITEM_TYPE_TASK) {
//                    Task task = (Task) modelList.get(viewHolder.getLayoutPosition()).getContent();
//                    RealmDB.refreshTask(mRealm,task);
//                    Log.e("TAG","refresh task: "+(Task) modelList.get(viewHolder.getLayoutPosition()).getContent());
//                    Log.e("TAG","release " + viewHolder.getLayoutPosition());
//                }

                if (!toolbar.isSelect() && isDragSuccess) {
                    mTaskListAdapter.setSelectState(false);
                    mTaskListAdapter.notifyItemChanged(viewHolder.getLayoutPosition());
                    isDragSuccess = false;
//                    mTaskListAdapter.notifyDataSetChanged();
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerview);
        mTaskListAdapter.setItemTouchHelper(itemTouchHelper);
        recyclerview.setAdapter(mTaskListAdapter);

        toolbar.getIvMenu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayout.openDrawer(Gravity.LEFT,true);
            }
        });

        toolbar.getIvLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.toggleLayout();
                if (toolbar.isStreamLayout()) {
                    if (linearLayoutManager == null) {
                        linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    }
                    recyclerview.setLayoutManager(linearLayoutManager);
                }else {
                    if (staggeredGridLayoutManager == null) {
                        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    }
                    recyclerview.setLayoutManager(staggeredGridLayoutManager);
                }
            }
        });

//        toolbar.getIvMore().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomPopupWindow more = new CustomPopupWindow(mContext, R.layout.popup_more_menu);
//                TextView tv_setting = (TextView) more.getContentView().findViewById(R.id.tv_setting);
//                tv_setting.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//                        startActivity(intent);
//                    }
//                });
//                more.showAsDropDown(v);
//            }
//        });

        toolbar.getIvSort().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.toggleSort();
                if (toolbar.sortByTime()) {
                    orderMode = Constant.ORDER_BY_DATE;
                }else {
                    orderMode = Constant.ORDER_BY_PROJECT;
                }
                refreshTaskListUseDiffUtil();

//                final CustomPopupWindow sort = new CustomPopupWindow(mContext, R.layout.popup_sort_menu);
//                sort.showAsDropDown(v, -DensityUtil.dp2px(mContext, 30), 0);
//                LinearLayout ll_date_sort = (LinearLayout) sort.getContentView().findViewById(R.id.ll_date_sort);
//                LinearLayout ll_project_sort = (LinearLayout) sort.getContentView().findViewById(R.id.ll_project_sort);
//                LinearLayout ll_importance_sort = (LinearLayout) sort.getContentView().findViewById(R.id.ll_importance_sort);
//                ll_date_sort.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sort.dismiss();
//                        orderMode = Constant.ORDER_BY_DATE;
//                        refreshTaskListUseDiffUtil();
////                        refreshByMode();
//                    }
//                });
//                ll_project_sort.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sort.dismiss();
//                        orderMode = Constant.ORDER_BY_PROJECT;
//                        refreshTaskListUseDiffUtil();
////                        refreshByMode();
//                    }
//                });
            }
        });

        toolbar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setSelect(false);
                mTaskListAdapter.setSelectState(false);
//                mTaskListAdapter.notifyDataSetChanged();//其实也可以只刷新selectedPosition里面的几个item
                mTaskListAdapter.notifySelectedItem();
            }
        });

        toolbar.getIvFixed().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selectedTasks.size(); i++) {
                    selectedTasks.get(i).setFixed(!toolbar.isFixedActive());
                }
                RealmDB.refreshTasks(mRealm, selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
                refreshTaskListUseDiffUtil();
            }
        });

        toolbar.getIvPalette().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorChooseDialog();
            }
        });

        toolbar.getIvTag().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagChooseDialog();
            }
        });

        toolbar.getIvSelectPorject().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProjectChooseDialog();
            }
        });

        toolbar.getIvDate().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        toolbar.getIVMoreSelect().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomPopupWindow selectMore = new CustomPopupWindow(mContext, R.layout.popu_more_select_menu);
                View view = selectMore.getContentView();
                TextView tv_complete = (TextView) view.findViewById(R.id.tv_complete);
                TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
                TextView tv_copy = (TextView) view.findViewById(R.id.tv_copy);
                TextView tv_send = (TextView) view.findViewById(R.id.tv_send);
                tv_complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < selectedTasks.size(); i++) {
                            selectedTasks.get(i).setState(2);
                        }
                        RealmDB.refreshTasks(mRealm, selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
                        refreshTaskListUseDiffUtil();
                        showConfrimSnackBar(coordinator, "已完成", "撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < selectedTasks.size(); i++) {
                                    selectedTasks.get(i).setState(1);
                                }
                                RealmDB.refreshTasks(mRealm, selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
                                refreshTaskListUseDiffUtil();
                            }
                        });
                    }
                });
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < selectedTasks.size(); i++) {
                            selectedTasks.get(i).setState(3);
                        }
                        RealmDB.refreshTasks(mRealm, selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
                        refreshTaskListUseDiffUtil();
                        showConfrimSnackBar(coordinator, "已删除", "撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < selectedTasks.size(); i++) {
                                    selectedTasks.get(i).setState(1);
                                }
                                RealmDB.refreshTasks(mRealm, selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
                                refreshTaskListUseDiffUtil();
                            }
                        });

                    }
                });
                tv_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                tv_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                selectMore.showAsDropDown(v, -DensityUtil.dp2px(mContext, 30), 0);
            }
        });

        drawerlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                refreshNavData();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                refreshTaskListUseDiffUtil();
            }
        });
        initNav();
    }

    private void initNav(){
        loadAvatar();
        loadUserBg();

        List<String> projectNames = new ArrayList<>();
        List<String> projectCounts = new ArrayList<>();
        List<String> tagNames = new ArrayList<>();
        List<String> tagCounts = new ArrayList<>();
        rv_projects.setItemAnimator(new CustomItemAnimator());
        rv_tags.setItemAnimator(new CustomItemAnimator());
        LinearLayoutManager projectManager = new LinearLayoutManager(mContext);
        projectManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_projects.setLayoutManager(projectManager);
        LinearLayoutManager tagstManager = new LinearLayoutManager(mContext);
        tagstManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_tags.setLayoutManager(tagstManager);
        navProjectAdapter = new NavItemAdapter(mContext, Constant.NAV_TYPE_PROJECT, projectCounts, projectNames);
        navTagAdapter = new NavItemAdapter(mContext, Constant.NAV_TYPE_TAG, tagCounts, tagNames);
        NavItemAdapter.OnClickNavItemListener onClickNavItemListener = new NavItemAdapter.OnClickNavItemListener() {
            @Override
            public void onClickItem(int position, int type) {
                if (type == Constant.NAV_TYPE_PROJECT) {
                    display = Constant.DISPLAY_PROJECT;
                    selectedProjectId = navActiveProjects.get(position).getProjectId();
                    displayChange = true;
                    drawerlayout.closeDrawers();
                } else if (type == Constant.NAV_TYPE_TAG) {
                    display = Constant.DISPLAY_TAG;
                    selectedTagId = navTags.get(position).getTagId();
                    displayChange = true;
                    drawerlayout.closeDrawers();
                }
            }
        };

        navProjectAdapter.setOnClickNavItemListener(onClickNavItemListener);
        navTagAdapter.setOnClickNavItemListener(onClickNavItemListener);
        rv_projects.setAdapter(navProjectAdapter);
        rv_tags.setAdapter(navTagAdapter);
        refreshNavData();
    }

    private void refreshNavData(){
        nav_today.setCount(RealmDB.getAllTodayUncompletedTasksByUserId(mRealm,RealmDB.getCurrentUserId()).size()+"");
        nav_all_task.setCount(RealmDB.getAllUncompletedTasksByUserId(mRealm,RealmDB.getCurrentUserId()).size()+"");
        navActiveProjects = RealmDB.getAllActiveProjectsByUserId(mRealm, RealmDB.getCurrentUserId());
        navTags = RealmDB.getAllTagsByUserId(mRealm, RealmDB.getCurrentUserId());
        List<String> projectNames = new ArrayList<>();
        List<String> projectCounts = new ArrayList<>();
        List<String> tagNames = new ArrayList<>();
        List<String> tagCounts = new ArrayList<>();
        for (int i = 0; i < navActiveProjects.size(); i++) {
            projectNames.add(navActiveProjects.get(i).getName());
            projectCounts.add(RealmDB.getUnCompletedTasksCountByProject(mRealm, navActiveProjects.get(i)) + "");
        }
        for (int i = 0; i < navTags.size(); i++) {
            tagNames.add(navTags.get(i).getName());
            tagCounts.add(RealmDB.getUnCompletedTaskCountByTag(mRealm, navTags.get(i).getTagId()) + "");
        }
        navProjectAdapter.refreshData(projectCounts,projectNames);
        navTagAdapter.refreshData(tagCounts,tagNames);

    }

    @OnClick(R.id.fb_add)
    public void newTask() {
        Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,fb_add,"add").toBundle());
            startActivityForResult(intent, REQUEST_CODE_NEW_TASK, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, fb_add, "add").toBundle());
        } else {
            //startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_NEW_TASK);
        }
    }

    @OnClick(R.id.nav_today)
    public void selectToday() {
        display = Constant.DISPLAY_TODAY;
        displayChange = true;
        drawerlayout.closeDrawer(Gravity.LEFT,true);
    }

    @OnClick(R.id.nav_all_task)
    public void selectAllTask(){
        display = Constant.DISPLAY_ALL_TASKS;
        displayChange = true;
        drawerlayout.closeDrawer(Gravity.LEFT,true);
    }

    @OnClick(R.id.rl_user_bg)
    public void changeUserBg(){
        new MaterialDialog.Builder(this)
                .items("选择本地图片","拍照")
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (position == 0) {
                            openAlbum(REQUEST_CODE_PICK_IMAGE_FOR_USER_BG);
                        }else {
                            openCamera(REQUEST_CODE_CAPTURE_CAMEIA_FOR_USER_BG);
                        }
                    }
                })
                .show();
    }

    @OnClick(R.id.iv_setting)
    public void setting(){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent,this);
    }

    @OnClick(R.id.iv_search)
    public void search(){

    }

    @OnClick(R.id.nav_avatar)
    public void clickAvatar() {
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        startActivity(intent,this);
    }

    @OnClick(R.id.nav_all_projects)
    public void toggleProjects(){
        nav_all_projects.setExpand(!nav_all_projects.isExpand());
        navProjectAdapter.expand(nav_all_projects.isExpand());
    }

    @OnClick(R.id.nav_all_tags)
    public void toggleTags(){
        nav_all_tags.setExpand(!nav_all_tags.isExpand());
        navTagAdapter.expand(nav_all_tags.isExpand());
    }

    @OnClick(R.id.nav_manage_project)
    public void manageProject(){
        Intent intent = new Intent(MainActivity.this, ManageProjectTagActivity.class);
        intent.putExtra(ManageProjectTagActivity.MANAGE_TYPE, Constant.MANAGE_TYPE_PROJECT);
        startActivityForResult(intent, REQUEST_CODE_MANAGE);
    }

    @OnClick(R.id.nav_manage_tag)
    public void manageTag(){
        Intent intent = new Intent(MainActivity.this, ManageProjectTagActivity.class);
        intent.putExtra(ManageProjectTagActivity.MANAGE_TYPE, Constant.MANAGE_TYPE_TAG);
//        startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE_MANAGE);
    }

    private void openAlbum(int requestCode){
        // 打开系统相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        startActivityForResult(intent, requestCode);
    }

    private void openCamera(int requestCode){
        try {
            File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
            PHOTO_DIR.mkdirs();
            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            startActivityForResult(intent, requestCode);
        }catch (Exception e) {
            Log.e("TAG","相机不可用");
        }
    }

    private void loadAvatar(){
        // 头像
        String avatarUrl = (String) SPUtil.get(mContext,Constant.USER_AVATAR_KEY,"");
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(mContext).load(new File(avatarUrl)).into(nav_avatar);
        }else {
            nav_avatar.setBackgroundResource(R.drawable.ic_default_avatar);
        }

    }

    private void loadUserBg(){
        // 背景
        String userBgUrl = (String) SPUtil.get(mContext,Constant.USER_BG_KEY,"");
        if (!TextUtils.isEmpty(userBgUrl)) {
            Glide.with(mContext).load(new File(userBgUrl)).into(nav_avatar);
        }else {
            rl_user_bg.setBackgroundResource(R.drawable.default_user_bg);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_NEW_TASK:
            case REQUEST_CODE_EDIT_TASK:
                refreshTaskListUseDiffUtil();
                break;
            case REQUEST_CODE_MANAGE:
                refreshNavData();
                displayChange = true;
                display = Constant.DISPLAY_TODAY; // 可能删除以打开的project，简单处理直接跳到today
                break;
            case REQUEST_CODE_PICK_IMAGE_FOR_AVATAR:
                String avatarUri= getRealFilePath(data.getData());
                SPUtil.put(mContext,Constant.USER_AVATAR_KEY,avatarUri);
                loadAvatar();
                break;
            case REQUEST_CODE_PICK_IMAGE_FOR_USER_BG:
                String userBgUri= getRealFilePath(data.getData());
                SPUtil.put(mContext,Constant.USER_BG_KEY,userBgUri);
                loadUserBg();
                break;
            case REQUEST_CODE_CAPTURE_CAMEIA_FOR_AVATAR:
                SPUtil.put(mContext,Constant.USER_AVATAR_KEY,mCurrentPhotoFile.getAbsolutePath());
                loadAvatar();
                break;
            case REQUEST_CODE_CAPTURE_CAMEIA_FOR_USER_BG:
                SPUtil.put(mContext,Constant.USER_BG_KEY,mCurrentPhotoFile.getAbsolutePath());
                loadUserBg();
                break;

        }
    }

    private void refreshTaskListUseDiffUtil() {
        toolbar.setSelect(false);
        mTaskListAdapter.setSelectState(false);
        List<MultiItemModel> newModeList = loadData();//默认新数据来源重新遍历数据库
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(modelList, newModeList), true);//旧数据默认modelList
        diffResult.dispatchUpdatesTo(mTaskListAdapter);
        modelList = newModeList;
        mTaskListAdapter.setData(newModeList);
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(Color.parseColor("#BBD9BD"));
        dpd.setTitle("设置到期时间");
        dpd.setMinDate(now);
        dpd.show(getFragmentManager(), "tag");
    }

    private void showTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setAccentColor(Color.parseColor("#BBD9BD"));
        tpd.setTitle("设置到期时间");
        tpd.show(getFragmentManager(), "tag");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Log.e("TAg", "year: " + year + " monthOfYear: " + monthOfYear + " dayOfMonth: " + dayOfMonth);
        showTimePickerDialog();
        targetCalendar = Calendar.getInstance();
        targetCalendar.set(Calendar.YEAR, year);
        targetCalendar.set(Calendar.MONTH, monthOfYear);
        targetCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Log.e("TAg", "hourOfDay: " + hourOfDay + " minute: " + minute + " second: " + second);
        targetCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        targetCalendar.set(Calendar.MINUTE, minute);
        for (int i = 0; i < selectedTasks.size(); i++) {
            selectedTasks.get(i).setDueTime(targetCalendar.getTimeInMillis());
        }
        RealmDB.refreshTasks(mRealm, selectedTasks);
        refreshTaskListUseDiffUtil();
    }

    private void showProjectChooseDialog() {
        List<Project> allProjects = RealmDB.getAllActiveProjectsByUserId(mRealm, RealmDB.getCurrentUserId());
        ProjectChooseView projectChooseView = new ProjectChooseView(mContext);
        projectChooseView.setData(allProjects, selectedTasks, mRealm);

        final MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title(R.string.tag_choose_dialog_label)
                .customView(projectChooseView, true)
                .negativeText(R.string.cancel)
                .show();

        projectChooseView.setOnProjectSelectedListener(new ProjectChooseView.onProjectSelectedListener() {
            @Override
            public void projectSelected() {
                dialog.dismiss();
                RealmDB.refreshTasks(mRealm, selectedTasks);
                refreshTaskListUseDiffUtil();
            }
        });
    }

    private void showTagChooseDialog() {

        List<Tag> allTags = RealmDB.getAllTagsByUserId(mRealm, RealmDB.getCurrentUserId());
        final TagsChooseView tagsChooseView = new TagsChooseView(mContext);
        tagsChooseView.setData(allTags, selectedTasks, mRealm);
        new MaterialDialog.Builder(mContext)
                .title(R.string.tag_choose_dialog_label)
                .customView(tagsChooseView, true)
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        tagsChooseView.onPositive();
                        RealmDB.refreshTasks(mRealm, selectedTasks);
                        mTaskListAdapter.setSelectState(false);
                        toolbar.setSelect(false);
                        mTaskListAdapter.notifySelectedItem();
                    }
                })
                .show();
    }

    private void showColorChooseDialog() {
        String[] colorArrays = getResources().getStringArray(R.array.colors);
        int[] colors = new int[colorArrays.length];
        for (int i = 0; i < colorArrays.length; i++) {
            colors[i] = Color.parseColor(colorArrays[i]);
        }
        new ColorChooserDialog.Builder(this, R.string.choose_color)
                .customColors(colors, null)
                .accentMode(false)
                .show(); // todo
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        String color_hex = String.format("#%06X", (0xFFFFFFFF & selectedColor));
        Log.e("TAG", "selectedColor" + color_hex);
        RealmDB.refreshTasksColor(mRealm, selectedTasks, color_hex);
        toolbar.setSelect(false);
        mTaskListAdapter.setSelectState(false);
        mTaskListAdapter.notifySelectedItem();
    }

    private void refreshByMode() {
        //todo 改成diffutil
//        modelList = getDataByProject(orderMode, selectedProjectId);
        modelList = loadData();
        mTaskListAdapter.refreshData(modelList, orderMode);
        recyclerview.smoothScrollToPosition(0);
    }

    private boolean changeDataByMode2(int srcPosition, int targetPosition) {
        boolean success = false;//默认交换不成功，下面只需对能成功的情况举例即可
        MultiItemModel srcModel = modelList.get(srcPosition);
        MultiItemModel targetModel = modelList.get(targetPosition);
        Task srctTask = (Task) srcModel.getContent();//src肯定是task
        if (orderMode == Constant.ORDER_BY_DATE) {
            if (srcModel.getItemType() == Constant.ITEM_TYPE_TASK && targetModel.getItemType() == Constant.ITEM_TYPE_TASK
                    && srcModel.getLabel().equals(targetModel.getLabel())) {// 都是任务，并且都是固定或者都是其他
                success = true;
            }
        } else if (orderMode == Constant.ORDER_BY_PROJECT) {

            if (srcModel.getItemType() == Constant.ITEM_TYPE_TASK && targetModel.getItemType() == Constant.ITEM_TYPE_TASK
                    && srcModel.getLabel().equals(targetModel.getLabel())) {
                success = true;
            } else if (srcModel.getItemType() == Constant.ITEM_TYPE_TASK && targetModel.getItemType() == Constant.ITEM_TYPE_PROJECT
                    && !targetModel.getLabel().equals(getString(R.string.label_fixed))) {
                if (srcPosition < targetPosition) {// 下移 要变成的label即是targetd的label
                    if (!srctTask.isFixed()) {
                        srctTask.setProject((Project) targetModel.getData());
                        srcModel.setLabel(targetModel.getLabel());
                        success = true;
                    }
                } else {//上移，要变成的label是 target 上面的label
                    if (!modelList.get(targetPosition - 1).getLabel().equals(getString(R.string.label_fixed))) {//排除第一个project里的task向上移到固定里
                        srctTask.setProject(((Task) modelList.get(targetPosition - 1).getContent()).getProject());
                        srcModel.setLabel(modelList.get(targetPosition - 1).getLabel());
                        success = true;
                    }
                }
            }
        }
        if (success) {
            Collections.swap(modelList, srcPosition, targetPosition);
            mTaskListAdapter.notifyItemMoved(srcPosition, targetPosition);
//            mTaskListAdapter.setSelectState(false);
        }

        return success;
    }

    private boolean changeDataByMode(int srcPosition, int targetPosition) {
        boolean success = false;
        MultiItemModel srcModel = modelList.get(srcPosition);
        MultiItemModel targetModel = modelList.get(targetPosition);
        Task srctTask = (Task) srcModel.getContent();//src肯定是task
        if (orderMode == Constant.ORDER_BY_DATE) {
            //交换时间 如果同在一个时间类别下，不修改数据，直接return true 意味着下次进来还是按原来的顺序排
            // 如果不在同一时间类别下，该变src的月和日，小时分钟保持不变。拖到以后加3天
            if (srcModel.getLabel().equals(targetModel.getLabel())
                    && srcModel.getItemType() == Constant.ITEM_TYPE_TASK
                    && targetModel.getItemType() == Constant.ITEM_TYPE_TASK) {// 同类别下的task交换位置
                success = true;
            } else {
                String targetLabel = (String) targetModel.getContent();//如果跨类别，第一个target一定是 date string 如果target在折叠状态下就不是了。。
                if (targetLabel.equals("今天")) { // 今天下面的task向上移 显然不能移成功
                    success = false;
                } else if (targetLabel.equals("明天")) {
                    success = true;
                    if (srcPosition < targetPosition) {// 今天的task 向下移到明天
                        srctTask.setDueTime(DateUtil.changeDateToTomorrow(srctTask.getDueTime()));
                        srcModel.setLabel("明天");
                    } else {
                        // 明天的task 向上移到今天
                        srctTask.setDueTime(DateUtil.changeDateToToday(srctTask.getDueTime()));
                        srcModel.setLabel("今天");
                    }
                } else if (targetLabel.equals("未来")) {
                    success = true;
                    if (srcPosition < targetPosition) { //明天的task 向下移到以后
                        srctTask.setDueTime(DateUtil.changeDateToSomeday(srctTask.getDueTime(), 3));
                        srcModel.setLabel("未来");
                    } else {
                        //以后的task 向上移到明天
                        srctTask.setDueTime(DateUtil.changeDateToTomorrow(srctTask.getDueTime()));
                        srcModel.setLabel("明天");
                    }
                }
            }
        } else if (orderMode == Constant.ORDER_BY_PROJECT) {
            //交换所属清单
            if (srcModel.getLabel().equals(targetModel.getLabel()) && srcModel.getItemType() == Constant.ITEM_TYPE_TASK
                    && targetModel.getItemType() == Constant.ITEM_TYPE_TASK) {// 同类别下的task交换位置
                success = true;
            } else {
                String targetLabel = (String) targetModel.getContent();//如果跨类别，第一个target一定是project string
                if (targetPosition == 0) {// 第一个清单不能移出去
                    success = false;
                } else {
                    success = true;
                    if (srcPosition < targetPosition) {// 下移 要变成的label即是targetd的label
                        srctTask.setProject((Project) targetModel.getData());
                        srcModel.setLabel(targetModel.getLabel());
                    } else {//上移，要变成的label是 target 上面的label
                        if (modelList.get(targetPosition - 1).getItemType() == Constant.ITEM_TYPE_TASK) {
//                            Task task = (Task) modelList.get(targetPosition-1).getContent();
                            srctTask.setProject(((Task) modelList.get(targetPosition - 1).getContent()).getProject());
                            srcModel.setLabel(modelList.get(targetPosition - 1).getLabel());
                        } else {
                            srctTask.setProject((Project) modelList.get(targetPosition - 1).getData());
                            srcModel.setLabel(modelList.get(targetPosition - 1).getLabel());
                        }
                    }
                }
            }

        } else if (orderMode == Constant.ORDER_BY_IMPORTANCE) {
            //交换优先级
        }
        Log.e("TAG", "success: " + success);

        if (success) {
//            RealmDB.refreshTask(mRealm,srctTask); // release的时候统一刷新
            mTaskListAdapter.notifyItemChanged(srcPosition);
//            mTaskListAdapter.notifyItemChanged(targetPosition);
            Collections.swap(modelList, srcPosition, targetPosition);
            mTaskListAdapter.notifyItemMoved(srcPosition, targetPosition);
            mTaskListAdapter.notifyTitleChanged(srcModel.getLabel());
            mTaskListAdapter.notifyTitleChanged(targetModel.getLabel());
//            mTaskListAdapter.notifyAllTitleChanged();
        }

        return success;
    }

    @Override
    public void onBackPressed() {
        if (toolbar.isSelect()) {
            toolbar.setSelect(false);
            mTaskListAdapter.setSelectState(false);
            mTaskListAdapter.notifySelectedItem();
            return;
        }
        super.onBackPressed();
    }

    private void initSimulatedData() {
        RealmDB.setCurrentUserId("user_111");
        RealmDB.saveUser(mRealm, SimulatedData.getCurrentUser());
        RealmDB.saveProject(mRealm, SimulatedData.getDefaultProject());
        RealmDB.saveProject(mRealm, SimulatedData.getWorkProject());
        RealmDB.saveProject(mRealm, SimulatedData.getReadProject());
        RealmDB.saveProject(mRealm, SimulatedData.getTravelProject());
        RealmDB.saveTag(mRealm, SimulatedData.getTag1());
        RealmDB.saveTag(mRealm, SimulatedData.getTag2());
        RealmDB.saveTaskToProject(mRealm, "project_111", SimulatedData.getTask1());
        RealmDB.saveTaskToProject(mRealm, "project_111", SimulatedData.getTask2());
        RealmDB.saveTaskToProject(mRealm, "project_222", SimulatedData.getTask3());
        RealmDB.saveTaskToProject(mRealm, "project_333", SimulatedData.getTask4());
        RealmDB.saveTaskToProject(mRealm, "project_333", SimulatedData.getTask5());
        RealmDB.saveTaskToProject(mRealm, "project_333", SimulatedData.getTask6());
        RealmDB.saveTaskToProject(mRealm, "project_111", SimulatedData.getTask7());
        RealmDB.saveTaskToProject(mRealm, "project_222", SimulatedData.getTask8());
//        RealmDB.saveBgColor(mRealm,SimulatedData.getHighBgColor());
//        RealmDB.saveBgColor(mRealm,SimulatedData.getNormalBgColor());
//        RealmDB.saveBgColor(mRealm,SimulatedData.getLowBgColor());
    }



}
