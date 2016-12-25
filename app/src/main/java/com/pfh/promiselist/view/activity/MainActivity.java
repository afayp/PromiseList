package com.pfh.promiselist.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.pfh.promiselist.R;
import com.pfh.promiselist.adapter.CustomItemTouchHelpCallback;
import com.pfh.promiselist.adapter.TaskListAdapter;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.dao.SimulatedData;
import com.pfh.promiselist.model.MultiItemModel;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.ColorsUtil;
import com.pfh.promiselist.utils.Constant;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.DensityUtil;
import com.pfh.promiselist.utils.L;
import com.pfh.promiselist.utils.SPUtil;
import com.pfh.promiselist.widget.CustomPopupWindow;
import com.pfh.promiselist.widget.TaskListToolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 首页 展示当前筛选条件(时间、清单...)下的所有任务
 */
public class MainActivity extends BaseActivity {

    TaskListToolbar toolbar;
    RecyclerView recyclerview;
    FloatingActionButton fb_add;

    private int orderMode;// 读取用户上次的模式，默认1,暂时不考虑多用户
    private String selectedProjectId ;// 读取用户上次选择的在主界面展示的projectId，""表示选择所有项目
    private List<Project> selectedProjects = new ArrayList<>();//选择的项目，多个或1个
    private List<MultiItemModel> modelList = new ArrayList<>();// 更具排序模式处理过的数据
    private List<Task> searchResultTasks = new ArrayList<>();
    private TaskListAdapter mTaskListAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor(ColorsUtil.TRANSPARENT);
//        initStatusBar();
//        initSimulatedData();// todo
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
        orderByMode();
    }

    private void orderByMode() {
        modelList.clear();

        //按日期排序，分今天，明天，以后
        if (orderMode == Constant.ORDER_BY_DATE){
            List<Task> allTasks = new ArrayList<>();
            //所有task
            for (int i = 0; i < selectedProjects.size(); i++) {
                allTasks.addAll(RealmDB.getAllUncompletedTasksByProjectId(mRealm,selectedProjects.get(i).getProjectId()));
            }
            Log.e("TAG","allTasks size: "+allTasks.size());
            List<MultiItemModel> today = new ArrayList<>();
            List<MultiItemModel> tomorrow = new ArrayList<>();
            List<MultiItemModel> future = new ArrayList<>();
            for (int i = 0; i < allTasks.size(); i++) {
                if (DateUtil.isToday(allTasks.get(i).getDueTime())){
                    today.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),"今天"));
                }else if (DateUtil.isTomorrow(allTasks.get(i).getDueTime())){
                    tomorrow.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),"明天"));
                }else if (DateUtil.isMoreThanToday(allTasks.get(i).getDueTime())){
                    future.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i),"未来"));
                }
            }

            modelList.add(new MultiItemModel(Constant.ITEM_TYPE_TIME,"今天"));
            modelList.addAll(today);
            modelList.add(new MultiItemModel(Constant.ITEM_TYPE_TIME,"明天"));
            modelList.addAll(tomorrow);
            modelList.add(new MultiItemModel(Constant.ITEM_TYPE_TIME,"未来"));
            modelList.addAll(future);

        }else if (orderMode == Constant.ORDER_BY_PROJECT){
            //按清单排序
            for (int i = 0; i < selectedProjects.size(); i++) {
                List<Task> tasksByProjectId = RealmDB.getAllUncompletedTasksByProjectId(mRealm, selectedProjects.get(i).getProjectId());
                MultiItemModel projectModel = new MultiItemModel(Constant.ITEM_TYPE_PROJECT, selectedProjects.get(i).getName());
                projectModel.setData(selectedProjects.get(i));
                modelList.add(projectModel);
                for (int j = 0; j < tasksByProjectId.size(); j++) {
                    modelList.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,tasksByProjectId.get(j),selectedProjects.get(i).getName()));
                }
            }
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
//            modelList.add(new MultiItemModel(Constant.IITEM_TYPE_IMPORTANCE,"高"));
//            modelList.addAll(high);
//            modelList.add(new MultiItemModel(Constant.IITEM_TYPE_IMPORTANCE,"正常"));
//            modelList.addAll(normal);
//            modelList.add(new MultiItemModel(Constant.IITEM_TYPE_IMPORTANCE,"低"));
//            modelList.addAll(low);
        }

        for (int i = 0; i < modelList.size(); i++) {
            L.e(modelList.get(i).toString());
        }

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
        toolbar = (TaskListToolbar) findViewById(R.id.toolbar);
        fb_add = (FloatingActionButton) findViewById(R.id.fb_add);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        mTaskListAdapter = new TaskListAdapter(mContext, modelList, orderMode);
        CustomItemTouchHelpCallback callback = new CustomItemTouchHelpCallback(new CustomItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                modelList.remove(adapterPosition);
                mTaskListAdapter.notifyItemRemoved(adapterPosition);
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                Log.e("TAG","src: "+srcPosition);
                Log.e("TAG","tar: "+targetPosition);
                if (srcPosition < targetPosition) {
                    //向下
                    if (modelList.get(targetPosition).getItemType() != Constant.ITEM_TYPE_TASK && !modelList.get(targetPosition).isExpand()){
//                    modelList.get(targetPosition).setExpand(true);
                        mTaskListAdapter.expandOrFoldTitle(targetPosition);
                    }
                }else {
                    //向上 targetPosition肯定是本title,所以要去判断再上面title是否展开，
                    if (targetPosition - 1 < 0 ) return false;
                    if ( modelList.get(targetPosition - 1).getItemType() != Constant.ITEM_TYPE_TASK
                            && !modelList.get(targetPosition - 1).isExpand()){//如果是title,则该title下的task一个为0，没展开则展开(只为显示数字..)
                        mTaskListAdapter.expandOrFoldTitle(targetPosition-1);
                    }else if (!modelList.get(targetPosition - 1).isExpand()){//如果是task,并且没有展开，则找到task的title,展开
                        mTaskListAdapter.expandTaskOfTitle(targetPosition - 1);
                    }
                }
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
                return changeDataByMode(srcPosition,targetPosition);
                // 先处理拖动 release的时候在处理数据和刷新view,否则太卡
//                Collections.swap(modelList, srcPosition, targetPosition);
//                mTaskListAdapter.notifyItemMoved(srcPosition,targetPosition);
//                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                 if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                     Log.e("TAG","onSelectedChanged " +viewHolder.getAdapterPosition());
                     Log.e("TAG","onSelectedChanged " +viewHolder.getLayoutPosition());
                 }
            }

            @Override
            public void onRelease(RecyclerView.ViewHolder viewHolder) {
                viewHolder.itemView.setBackgroundColor(0);
                if (modelList.get(viewHolder.getLayoutPosition()).getItemType() == Constant.ITEM_TYPE_TASK) {
                    Task task = (Task) modelList.get(viewHolder.getLayoutPosition()).getContent();
                    RealmDB.refreshTask(mRealm,task);
                }
                Log.e("TAG","refresh task: "+(Task) modelList.get(viewHolder.getLayoutPosition()).getContent());
                Log.e("TAG","release " +viewHolder.getLayoutPosition());
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerview);
        mTaskListAdapter.setItemTouchHelper(itemTouchHelper);
        recyclerview.setAdapter(mTaskListAdapter);

        toolbar.setProjectsClickListener(new View.OnClickListener() {
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

        //更多
        toolbar.setMoreClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPopupWindow more = new CustomPopupWindow(mContext, R.layout.popup_more_menu);
                more.showAsDropDown(v);
            }
        });
        //排序
        toolbar.setSortClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPopupWindow sort = new CustomPopupWindow(mContext, R.layout.popup_sort_menu);
                sort.showAsDropDown(v, -DensityUtil.dp2px(mContext,20),0);
                LinearLayout ll_date_sort = (LinearLayout) sort.getContentView().findViewById(R.id.ll_date_sort);
                LinearLayout ll_project_sort = (LinearLayout) sort.getContentView().findViewById(R.id.ll_project_sort);
                LinearLayout ll_importance_sort = (LinearLayout) sort.getContentView().findViewById(R.id.ll_importance_sort);
                ll_date_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderMode = Constant.ORDER_BY_DATE;
                        orderByMode();
                        mTaskListAdapter.refreshData(modelList,orderMode);
                    }
                });
                ll_project_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderMode = Constant.ORDER_BY_PROJECT;
                        orderByMode();
                        mTaskListAdapter.refreshData(modelList,orderMode);
                    }
                });
                ll_importance_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderMode = Constant.ORDER_BY_IMPORTANCE;
                        orderByMode();
                        mTaskListAdapter.refreshData(modelList,orderMode);
                    }
                });
            }
        });

        //搜索
        toolbar.setOnSearchTaskListener(new TaskListToolbar.onSearchTaskListener() {
            @Override
            public void onSearch(String keyword) {
                if (keyword.equals("")){
                    mTaskListAdapter.refreshData(modelList,orderMode);
                }else {
                    searchResultTasks = RealmDB.searchTaskByKeyword(mRealm, keyword);
                    List<MultiItemModel> results = new ArrayList<MultiItemModel>();
                    for (int i = 0; i < searchResultTasks.size(); i++) {
                        results.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,searchResultTasks.get(i)));
                    }
                    mTaskListAdapter.refreshData(results,orderMode);
                }
            }
        });

        fb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewTaskActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,fb_add,"add").toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });
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


    private void initSimulatedData() {
        RealmDB.setCurrentUserId("user_111");
        RealmDB.saveUser(mRealm, SimulatedData.getCurrentUser());
        RealmDB.saveProject(mRealm,SimulatedData.getWorkProject());
        RealmDB.saveProject(mRealm,SimulatedData.getReadProject());
        RealmDB.saveProject(mRealm,SimulatedData.getTravelProject());
        RealmDB.saveTaskToProject(mRealm,"project_111",SimulatedData.getTask1());
        RealmDB.saveTaskToProject(mRealm,"project_111",SimulatedData.getTask2());
        RealmDB.saveTaskToProject(mRealm,"project_222",SimulatedData.getTask3());
        RealmDB.saveTaskToProject(mRealm,"project_333",SimulatedData.getTask4());
        RealmDB.saveTaskToProject(mRealm,"project_333",SimulatedData.getTask5());
        RealmDB.saveTaskToProject(mRealm,"project_333",SimulatedData.getTask6());
        RealmDB.saveTaskToProject(mRealm,"project_111",SimulatedData.getTask7());
        RealmDB.saveTaskToProject(mRealm,"project_222",SimulatedData.getTask8());
        RealmDB.saveBgColor(mRealm,SimulatedData.getHighBgColor());
        RealmDB.saveBgColor(mRealm,SimulatedData.getNormalBgColor());
        RealmDB.saveBgColor(mRealm,SimulatedData.getLowBgColor());
    }
}
