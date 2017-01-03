package com.pfh.promiselist.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.jaeger.library.StatusBarUtil;
import com.pfh.promiselist.R;
import com.pfh.promiselist.adapter.CustomItemTouchHelpCallback;
import com.pfh.promiselist.adapter.TaskListAdapter;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.dao.SimulatedData;
import com.pfh.promiselist.model.MultiItemModel;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.others.Constant;
import com.pfh.promiselist.others.CustomItemAnimator;
import com.pfh.promiselist.others.DiffCallback;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.DensityUtil;
import com.pfh.promiselist.utils.SPUtil;
import com.pfh.promiselist.widget.CustomPopupWindow;
import com.pfh.promiselist.widget.ProjectChooseView;
import com.pfh.promiselist.widget.TagsChooseView;
import com.pfh.promiselist.widget.TaskListToolbar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



/**
 * 首页 展示当前筛选条件(时间、清单...)下的所有任务
 */
public class MainActivity extends BaseActivity implements
        ColorChooserDialog.ColorCallback,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final int REQUEST_NEW = 1; // 新建任务
    public static final int REQUEST_EDIT = 2; // 修改任务
    public static final String EDIT_TASK_ID = "edit_task_id"; // 修改的任务

    private TaskListToolbar toolbar;
    private RecyclerView recyclerview;
    private FloatingActionButton fb_add;
    private CoordinatorLayout coordinator;

    private int orderMode;// 读取用户上次的模式，默认1,暂时不考虑多用户
    private String selectedProjectId ;// 读取用户上次选择的在主界面展示的projectId，""表示选择所有项目
    private List<Project> selectedProjects = new ArrayList<>();//选择的项目，多个或1个
    private List<MultiItemModel> modelList = new ArrayList<>();// 根据排序模式处理过的数据
    private List<Task> searchResultTasks = new ArrayList<>();
    private TaskListAdapter mTaskListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Task> selectedTasks = new ArrayList<>();//所有选中的task
    private Calendar targetCalendar; // 选择的日期
    private Task deletedTask;
    private boolean isDragSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setStatusBarColor(ColorsUtil.TRANSPARENT);
//        initStatusBar();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.importance_normal),0);
        initSimulatedData();// todo
        initSymbol();
        loadData();
        initViews();

    }

    private void initSymbol() {
        orderMode = (int) SPUtil.get(mContext, Constant.LAST_ORDER_MODE,Constant.ORDER_BY_DATE);
        selectedProjectId = (String) SPUtil.get(mContext,Constant.LAST_SELECTED_PROJECT,"");
        Log.e("TAG","selectedProjectId: " +selectedProjectId);
    }

    /**
     * 根据当前选择的模式从数据库中加载数据并处理好，用来传给adapter解析
     * 可以选择所有项目，也可以选择特定项目
     * 排序方式分：按日期排序1、按项目排序2、按优先级排序3
     */
    private void loadData() {
        loadProjects();
        modelList = orderByMode();
    }

    private List<MultiItemModel> orderByMode() {
        List<MultiItemModel> tempModeList = new ArrayList<>();
        //按日期排序，分今天，明天，以后
        if (orderMode == Constant.ORDER_BY_DATE){
            List<Task> allTasks = new ArrayList<>();
            //所有task
            for (int i = 0; i < selectedProjects.size(); i++) {
                allTasks.addAll(RealmDB.getAllUncompletedTasksByProjectId(mRealm,selectedProjects.get(i).getProjectId()));
            }
            //筛选出固定的
            List<MultiItemModel> fixedList = new ArrayList<>();
            List<Task> temp = new ArrayList<>();
            for (int i = 0; i < allTasks.size(); i++) {
                if (allTasks.get(i).isFixed()) {
                    fixedList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),getString(R.string.label_fixed)));
                }else {
                    temp.add(allTasks.get(i));
                }
            }
            if (fixedList.size() != 0){ // 如果没有则不显示
                tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_FIXED,getString(R.string.label_fixed)));
                tempModeList.addAll(fixedList);
            }

            if (temp.size() != 0){ // 如果没有则不显示
                //对剩下的按时间排序
                Collections.sort(temp, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return (int) (o1.getDueTime() - o2.getDueTime());
                    }
                });
                tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_OTHER,getString(R.string.label_others)));
                for (int i = 0; i < temp.size(); i++) {
                    tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,temp.get(i),getString(R.string.label_others)));
                }
            }

//            List<MultiItemModel> today = new ArrayList<>();
//            List<MultiItemModel> tomorrow = new ArrayList<>();
//            List<MultiItemModel> future = new ArrayList<>();
//            for (int i = 0; i < allTasks.size(); i++) {
//                if (DateUtil.isToday(allTasks.get(i).getDueTime())){
//                    today.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),"今天"));
//                }else if (DateUtil.isTomorrow(allTasks.get(i).getDueTime())){
//                    tomorrow.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),"明天"));
//                }else if (DateUtil.isMoreThanToday(allTasks.get(i).getDueTime())){
//                    future.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),"未来"));
//                }
//            }
//            tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TIME,"今天"));
//            tempModeList.addAll(today);
//            tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TIME,"明天"));
//            tempModeList.addAll(tomorrow);
//            tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TIME,"未来"));
//            tempModeList.addAll(future);

        }else if (orderMode == Constant.ORDER_BY_PROJECT){
            //按清单排序
            List<MultiItemModel> fixedList = new ArrayList<>();
            List<Task> fixedTaskList = new ArrayList<>();
            for (int i = 0; i < selectedProjects.size(); i++) {
                List<Task> tasks = RealmDB.getFixedUncompletedTasksByProjectId(mRealm, selectedProjects.get(i).getProjectId(), true);
                fixedTaskList.addAll(tasks);
//                for (int j = 0; j < tasks.size(); j++) {
//                    tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,tasks.get(j),getString(R.string.label_fixed)));
//                }
            }
            if (fixedTaskList.size() != 0) {
                tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_FIXED,getString(R.string.label_fixed)));//固定
                for (int i = 0; i < fixedTaskList.size(); i++) {
                    tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,fixedTaskList.get(i),getString(R.string.label_fixed)));
                }
            }


            for (int i = 0; i < selectedProjects.size(); i++) {
                List<Task> tasks = RealmDB.getFixedUncompletedTasksByProjectId(mRealm, selectedProjects.get(i).getProjectId(), false);
                if (tasks.size() != 0) {
                    MultiItemModel projectModel = new MultiItemModel(Constant.ITEM_TYPE_PROJECT, selectedProjects.get(i).getName());
                    projectModel.setData(selectedProjects.get(i));
                    tempModeList.add(projectModel);
                    for (int j = 0; j < tasks.size(); j++) {
                        tempModeList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,tasks.get(j),selectedProjects.get(i).getName()));
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
        }else if (orderMode == Constant.ORDER_BY_IMPORTANCE){
            //取消的优先级 改成按颜色排序 TODO
//            List<Task> allTasks = new ArrayList<>();
//            for (int i = 0; i < selectedProjects.size(); i++) {
//                allTasks.addAll(RealmDB.getAllUncompletedTasksByProjectId(mRealm, selectedProjects.get(i).getProjectId()));
//            }
//
//            List<MultiItemModel> high = new ArrayList<>();
//            List<MultiItemModel> normal = new ArrayList<>();
//            List<MultiItemModel> low = new ArrayList<>();
//            for (int i = 0; i < allTasks.size(); i++) {
//                if (allTasks.get(i).getImportance() == 3){
//                    high.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),"高"));
//                }else if (allTasks.get(i).getImportance() == 2){
//                    normal.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),"正常"));
//                }else if (allTasks.get(i).getImportance() == 1){
//                    low.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),"低"));
//                }
//            }
//
//            tempModeList.add(new MultiItemModel(Constant.IITEM_TYPE_IMPORTANCE,"高"));
//            tempModeList.addAll(high);
//            tempModeList.add(new MultiItemModel(Constant.IITEM_TYPE_IMPORTANCE,"正常"));
//            tempModeList.addAll(normal);
//            tempModeList.add(new MultiItemModel(Constant.IITEM_TYPE_IMPORTANCE,"低"));
//            tempModeList.addAll(low);
        }

        for (int i = 0; i < tempModeList.size(); i++) {
            Log.e("TAG",tempModeList.get(i).toString());
        }
        return tempModeList;

    }

    private void loadProjects() {
        if (selectedProjectId.equals("")){
            selectedProjects = RealmDB.getAllActiveProjectsByUserId(mRealm, RealmDB.getCurrentUserId());
            Log.e("TAG","selectedProjects size: " + selectedProjects.size());
        }else {
            selectedProjects.add(RealmDB.getProjectByProjectId(mRealm,selectedProjectId));
        }
    }

    private void initViews() {
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        toolbar = (TaskListToolbar) findViewById(R.id.toolbar);
        fb_add = (FloatingActionButton) findViewById(R.id.fb_add);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        linearLayoutManager = new LinearLayoutManager(mContext){
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 400;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setItemAnimator(new CustomItemAnimator());
        mTaskListAdapter = new TaskListAdapter(mContext, modelList, orderMode);
        mTaskListAdapter.setOnItemClickListener(new TaskListAdapter.onItemClickListener() {
            @Override
            public void onClickTask(View view, int position) {
                //goto task activity
                Intent intent = new Intent(MainActivity.this,NewTaskActivity.class);
                Task task = (Task) modelList.get(position).getContent();
                intent.putExtra(EDIT_TASK_ID,task.getTaskId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent,REQUEST_EDIT,ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,fb_add,"add").toBundle());
                }else {
                    startActivityForResult(intent,REQUEST_EDIT);
                }

            }

            @Override
            public void onSelectChanged(boolean select,int position, int count) {
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
                    if (!selectedTasks.get(i).isFixed()){
                        allFixed = false;
                    }
                }
                toolbar.setFixedIconActive(allFixed);
            }
        });
        CustomItemTouchHelpCallback callback = new CustomItemTouchHelpCallback(new CustomItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                Log.e("TAG","adapterPosition: "+adapterPosition);
                deletedTask = (Task) modelList.get(adapterPosition).getContent();
                deletedTask.setState(2);
                Log.e("TAG","deletedTask: "+deletedTask.toString());
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
                Log.e("TAG","src: "+srcPosition);
                Log.e("TAG","tar: "+targetPosition);
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
                if (toolbar.isSelect()){
                    return false;
                }else {
                    return true;
                }
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                Log.e("TAG","onSelectedChanged !" + actionState);

                // 进入选择模式 TODO
                 if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                     Log.e("TAG","onSelectedChanged " +viewHolder.getLayoutPosition());
                 }

                switch (actionState){
                    case ItemTouchHelper.ACTION_STATE_IDLE:
                        Log.e("TAG","ACTION_STATE_IDLE");
                        break;
                    case ItemTouchHelper.DOWN:
                        Log.e("TAG","DOWN");
                        break;
                    case ItemTouchHelper.ACTION_STATE_SWIPE:
                        Log.e("TAG","ACTION_STATE_SWIPE");
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

                if (!toolbar.isSelect() && isDragSuccess){
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

        toolbar.getIvProjects().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProjectListActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });

        toolbar.getIvMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPopupWindow more = new CustomPopupWindow(mContext, R.layout.popup_more_menu);
                TextView tv_setting = (TextView) more.getContentView().findViewById(R.id.tv_setting);
                tv_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                    }
                });
                more.showAsDropDown(v);
            }
        });

        toolbar.getIvSort().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final CustomPopupWindow sort = new CustomPopupWindow(mContext, R.layout.popup_sort_menu);
                sort.showAsDropDown(v, -DensityUtil.dp2px(mContext,30),0);
                LinearLayout ll_date_sort = (LinearLayout) sort.getContentView().findViewById(R.id.ll_date_sort);
                LinearLayout ll_project_sort = (LinearLayout) sort.getContentView().findViewById(R.id.ll_project_sort);
                LinearLayout ll_importance_sort = (LinearLayout) sort.getContentView().findViewById(R.id.ll_importance_sort);
                ll_date_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sort.dismiss();
                        orderMode = Constant.ORDER_BY_DATE;
                        refreshByMode();
                    }
                });
                ll_project_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sort.dismiss();
                        orderMode = Constant.ORDER_BY_PROJECT;
                        refreshByMode();
                    }
                });
                ll_importance_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sort.dismiss();
//                        orderMode = Constant.ORDER_BY_IMPORTANCE;
                        orderMode = Constant.ORDER_BY_COLOR;
                        refreshByMode();
                    }
                });
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
                RealmDB.refreshTasks(mRealm,selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
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
                        RealmDB.refreshTasks(mRealm,selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
                        refreshTaskListUseDiffUtil();
                        showConfrimSnackBar(coordinator, "已完成", "撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < selectedTasks.size(); i++) {
                                    selectedTasks.get(i).setState(1);
                                }
                                RealmDB.refreshTasks(mRealm,selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
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
                        RealmDB.refreshTasks(mRealm,selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
                        refreshTaskListUseDiffUtil();
                        showConfrimSnackBar(coordinator, "已删除", "撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < selectedTasks.size(); i++) {
                                    selectedTasks.get(i).setState(1);
                                }
                                RealmDB.refreshTasks(mRealm,selectedTasks);// 使用此方法要先刷新selectedTasks，再将更改保存到数据库
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
                selectMore.showAsDropDown(v,-DensityUtil.dp2px(mContext,30),0);
            }
        });

        fb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewTaskActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,fb_add,"add").toBundle());
                    startActivityForResult(intent,REQUEST_NEW,ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,fb_add,"add").toBundle());
                }else {
//                    startActivity(intent);
                    startActivityForResult(intent,REQUEST_NEW);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            refreshTaskListUseDiffUtil();
        }

    }

    private void refreshTaskListUseDiffUtil(){
        toolbar.setSelect(false);
        mTaskListAdapter.setSelectState(false);
        List<MultiItemModel> newModeList = orderByMode();//默认新数据来源重新遍历数据库
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(modelList, newModeList),true);//旧数据默认modelList
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
        dpd.show(getFragmentManager(),"tag");
    }

    private void showTimePickerDialog(){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setAccentColor(Color.parseColor("#BBD9BD"));
        tpd.setTitle("设置到期时间");
        tpd.show(getFragmentManager(),"tag");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Log.e("TAg","year: "+year + " monthOfYear: "+monthOfYear + " dayOfMonth: "+dayOfMonth);
        showTimePickerDialog();
        targetCalendar = Calendar.getInstance();
        targetCalendar.set(Calendar.YEAR,year);
        targetCalendar.set(Calendar.MONTH,monthOfYear);
        targetCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Log.e("TAg","hourOfDay: "+hourOfDay + " minute: "+minute + " second: "+second);
        targetCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        targetCalendar.set(Calendar.MINUTE,minute);
        for (int i = 0; i < selectedTasks.size(); i++) {
            selectedTasks.get(i).setDueTime(targetCalendar.getTimeInMillis());
        }
        RealmDB.refreshTasks(mRealm,selectedTasks);
        refreshTaskListUseDiffUtil();
//        mTaskListAdapter.setSelectState(false);
//        toolbar.setSelect(false);
//        mTaskListAdapter.notifySelectedItem();//不刷新位置吗？TODO
    }

    private void showProjectChooseDialog() {
        List<Project> allProjects = RealmDB.getAllActiveProjectsByUserId(mRealm, RealmDB.getCurrentUserId());
        ProjectChooseView projectChooseView = new ProjectChooseView(mContext);
        projectChooseView.setData(allProjects,selectedTasks,mRealm);

        final MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title(R.string.tag_choose_dialog_label)
                .customView(projectChooseView, true)
                .negativeText(R.string.cancel)
                .show();

        projectChooseView.setOnProjectSelectedListener(new ProjectChooseView.onProjectSelectedListener() {
            @Override
            public void projectSelected() {
                dialog.dismiss();
                RealmDB.refreshTasks(mRealm,selectedTasks);
                refreshTaskListUseDiffUtil();
            }
        });
    }

    private void showTagChooseDialog() {

        List<Tag> allTags = RealmDB.getAllTagsByUserId(mRealm, RealmDB.getCurrentUserId());
        final TagsChooseView tagsChooseView = new TagsChooseView(mContext);
        tagsChooseView.setData(allTags,selectedTasks,mRealm);
        new MaterialDialog.Builder(mContext)
                .title(R.string.tag_choose_dialog_label)
                .customView(tagsChooseView,true)
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        tagsChooseView.onPositive();
                        RealmDB.refreshTasks(mRealm,selectedTasks);
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
        new ColorChooserDialog.Builder(this,R.string.choose_color)
                .customColors(colors,null)
                .accentMode(false)
                .show(); // todo
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        String color_hex = String.format("#%06X", (0xFFFFFFFF & selectedColor));
        Log.e("TAG","selectedColor"+ color_hex);
        RealmDB.refreshTasksColor(mRealm,selectedTasks,color_hex);
//        mTaskListAdapter.notifyItemChanged();
        toolbar.setSelect(false);
        mTaskListAdapter.setSelectState(false);
        mTaskListAdapter.notifySelectedItem();


    }

    private void refreshByMode(){
        //todo 改成diffutil
        modelList = orderByMode();
        mTaskListAdapter.refreshData(modelList,orderMode);
        recyclerview.smoothScrollToPosition(0);
    }

    private boolean changeDataByMode2(int srcPosition, int targetPosition){
        boolean success = false;//默认交换不成功，下面只需对能成功的情况举例即可
        MultiItemModel srcModel = modelList.get(srcPosition);
        MultiItemModel targetModel = modelList.get(targetPosition);
        Task srctTask = (Task) srcModel.getContent();//src肯定是task
        if (orderMode == Constant.ORDER_BY_DATE){
            if (srcModel.getItemType() == Constant.ITEM_TYPE_TASK && targetModel.getItemType() == Constant.ITEM_TYPE_TASK
                    && srcModel.getLabel().equals(targetModel.getLabel())) {// 都是任务，并且都是固定或者都是其他
                success = true;
            }
        }else if (orderMode == Constant.ORDER_BY_PROJECT){

            if (srcModel.getItemType() == Constant.ITEM_TYPE_TASK && targetModel.getItemType() == Constant.ITEM_TYPE_TASK
                    && srcModel.getLabel().equals(targetModel.getLabel())) {
                success = true;
            }else if (srcModel.getItemType() == Constant.ITEM_TYPE_TASK && targetModel.getItemType() == Constant.ITEM_TYPE_PROJECT
                    && !targetModel.getLabel().equals(getString(R.string.label_fixed))){
                if (srcPosition < targetPosition) {// 下移 要变成的label即是targetd的label
                    if (!srctTask.isFixed()) {
                        srctTask.setProject((Project) targetModel.getData());
                        srcModel.setLabel(targetModel.getLabel());
                        success = true;
                    }
                }else {//上移，要变成的label是 target 上面的label
                    if (!modelList.get(targetPosition -1).getLabel().equals(getString(R.string.label_fixed))){//排除第一个project里的task向上移到固定里
                        srctTask.setProject(((Task) modelList.get(targetPosition-1).getContent()).getProject());
                        srcModel.setLabel(modelList.get(targetPosition -1).getLabel());
                        success = true;
                    }
                }
            }

        }else if (orderMode == Constant.ORDER_BY_COLOR){

        }

        if (success) {
            Collections.swap(modelList, srcPosition, targetPosition);
            mTaskListAdapter.notifyItemMoved(srcPosition,targetPosition);
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
                    && targetModel.getItemType() == Constant.ITEM_TYPE_TASK){// 同类别下的task交换位置
                success = true;
            }else {
                String targetLabel = (String) targetModel.getContent();//如果跨类别，第一个target一定是 date string 如果target在折叠状态下就不是了。。
                if (targetLabel.equals("今天")){ // 今天下面的task向上移 显然不能移成功
                    success = false;
                }else if (targetLabel.equals("明天")){
                    success = true;
                    if (srcPosition < targetPosition){// 今天的task 向下移到明天
                        srctTask.setDueTime(DateUtil.changeDateToTomorrow(srctTask.getDueTime()));
                        srcModel.setLabel("明天");
                    }else {
                        // 明天的task 向上移到今天
                        srctTask.setDueTime(DateUtil.changeDateToToday(srctTask.getDueTime()));
                        srcModel.setLabel("今天");
                    }
                }else if (targetLabel.equals("未来")){
                    success = true;
                    if (srcPosition < targetPosition){ //明天的task 向下移到以后
                        srctTask.setDueTime(DateUtil.changeDateToSomeday(srctTask.getDueTime(),3));
                        srcModel.setLabel("未来");
                    }else {
                        //以后的task 向上移到明天
                        srctTask.setDueTime(DateUtil.changeDateToTomorrow(srctTask.getDueTime()));
                        srcModel.setLabel("明天");
                    }
                }
            }
        }else if (orderMode == Constant.ORDER_BY_PROJECT) {
            //交换所属清单
            if (srcModel.getLabel().equals(targetModel.getLabel()) && srcModel.getItemType() == Constant.ITEM_TYPE_TASK
                    && targetModel.getItemType() == Constant.ITEM_TYPE_TASK){// 同类别下的task交换位置
                success = true;
            }else {
                String targetLabel = (String) targetModel.getContent();//如果跨类别，第一个target一定是project string
                if (targetPosition == 0){// 第一个清单不能移出去
                    success = false;
                }else {
                    success = true;
                    if (srcPosition < targetPosition){// 下移 要变成的label即是targetd的label
                        srctTask.setProject((Project) targetModel.getData());
                        srcModel.setLabel(targetModel.getLabel());
                    }else {//上移，要变成的label是 target 上面的label
                        if (modelList.get(targetPosition -1).getItemType() == Constant.ITEM_TYPE_TASK){
//                            Task task = (Task) modelList.get(targetPosition-1).getContent();
                            srctTask.setProject(((Task) modelList.get(targetPosition-1).getContent()).getProject());
                            srcModel.setLabel(modelList.get(targetPosition -1).getLabel());
                        }else {
                            srctTask.setProject((Project) modelList.get(targetPosition-1).getData());
                            srcModel.setLabel(modelList.get(targetPosition-1).getLabel());
                        }
                    }
                }
            }

        }else if (orderMode == Constant.ORDER_BY_IMPORTANCE) {
            //交换优先级
        }
        Log.e("TAG","success: "+success);

        if (success){
//            RealmDB.refreshTask(mRealm,srctTask); // release的时候统一刷新
            mTaskListAdapter.notifyItemChanged(srcPosition);
//            mTaskListAdapter.notifyItemChanged(targetPosition);
            Collections.swap(modelList, srcPosition, targetPosition);
            mTaskListAdapter.notifyItemMoved(srcPosition,targetPosition);
            mTaskListAdapter.notifyTitleChanged(srcModel.getLabel());
            mTaskListAdapter.notifyTitleChanged(targetModel.getLabel());
//            mTaskListAdapter.notifyAllTitleChanged();
        }

        return success;
    }

    @Override
    public void onBackPressed() {
        if (toolbar.isSelect()){
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
        RealmDB.saveProject(mRealm,SimulatedData.getDefaultProject());
        RealmDB.saveProject(mRealm,SimulatedData.getWorkProject());
        RealmDB.saveProject(mRealm,SimulatedData.getReadProject());
        RealmDB.saveProject(mRealm,SimulatedData.getTravelProject());
        RealmDB.saveTag(mRealm,SimulatedData.getTag1());
        RealmDB.saveTag(mRealm,SimulatedData.getTag2());
        RealmDB.saveTaskToProject(mRealm,"project_111",SimulatedData.getTask1());
        RealmDB.saveTaskToProject(mRealm,"project_111",SimulatedData.getTask2());
        RealmDB.saveTaskToProject(mRealm,"project_222",SimulatedData.getTask3());
        RealmDB.saveTaskToProject(mRealm,"project_333",SimulatedData.getTask4());
        RealmDB.saveTaskToProject(mRealm,"project_333",SimulatedData.getTask5());
        RealmDB.saveTaskToProject(mRealm,"project_333",SimulatedData.getTask6());
        RealmDB.saveTaskToProject(mRealm,"project_111",SimulatedData.getTask7());
        RealmDB.saveTaskToProject(mRealm,"project_222",SimulatedData.getTask8());
//        RealmDB.saveBgColor(mRealm,SimulatedData.getHighBgColor());
//        RealmDB.saveBgColor(mRealm,SimulatedData.getNormalBgColor());
//        RealmDB.saveBgColor(mRealm,SimulatedData.getLowBgColor());
    }


}
