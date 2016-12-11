package com.pfh.promiselist.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.adapter.TaskListAdapter;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.dao.SimulatedData;
import com.pfh.promiselist.model.MultiItemModel;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.ColorsUtil;
import com.pfh.promiselist.utils.Constant;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.L;
import com.pfh.promiselist.utils.SPUtil;
import com.pfh.promiselist.widget.TaskListToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 首页 展示当前筛选条件(时间、清单...)下的所有任务
 */
public class MainActivity extends BaseActivity {

//    @BindView(R2.id.toolbar)
    TaskListToolbar toolbar;
//    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
//    @BindView(R2.id.fb_add)
    FloatingActionButton fb_add;

    private int orderMode;// 读取用户上次的模式，默认1,暂时不考虑多用户
    private String selectedProjectId ;// 读取用户上次选择的在主界面展示的projectId，""表示选择所有项目
    private List<Project> selectedProjects = new ArrayList<>();//选择的项目，多个或1个
    private List<MultiItemModel> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setStatusBarColor(ColorsUtil.TRANSPARENT);
//        initStatusBar();
        initSimulatedData();

        initSymbol();
        loadData();
        initViews();

    }

    private void initSymbol() {
        orderMode = (int) SPUtil.get(mContext, Constant.LAST_ORDER_MODE,Constant.ORDER_BY_DATE);
        selectedProjectId = (String) SPUtil.get(mContext,Constant.LAST_SELECTED_PROJECT,"");
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
        List<Task> allTasks = new ArrayList<>();

        //按日期排序，分今天，明天，以后
        if (orderMode == Constant.ORDER_BY_DATE){
            //所有task
            for (int i = 0; i < selectedProjects.size(); i++) {
                allTasks.addAll(RealmDB.getAllUncompletedTasksByProjectId(mRealm,selectedProjects.get(i).getProjectId()));
            }
            List<MultiItemModel> today = new ArrayList<>();
            List<MultiItemModel> tomorrow = new ArrayList<>();
            List<MultiItemModel> future = new ArrayList<>();
            for (int i = 0; i < allTasks.size(); i++) {
                if (DateUtil.isToday(allTasks.get(i).getDueTime())){
                    today.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i)));
                }else if (DateUtil.isTomorrow(allTasks.get(i).getDueTime())){
                    tomorrow.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i)));
                }else {
                    future.add(new MultiItemModel(Constant.ITEM_TYPE_TASK,allTasks.get(i)));
                }
            }
            modelList.add(new MultiItemModel(Constant.ITEM_TYPE_TIME,"今天"));
            modelList.addAll(today);
            modelList.add(new MultiItemModel(Constant.ITEM_TYPE_TIME,"明天"));
            modelList.addAll(tomorrow);
            modelList.add(new MultiItemModel(Constant.ITEM_TYPE_TIME,"未来"));
            modelList.addAll(future);

            for (int i = 0; i < modelList.size(); i++) {
                L.e(modelList.get(i).toString());
            }

        }else if (orderMode == Constant.ORDER_BY_PROJECT){

        }else if (orderMode == Constant.ORDER_BY_IMPORTANCE){

        }

    }

    private void loadProjects() {
        if (selectedProjectId.equals("")){
            selectedProjects = RealmDB.getAllActiveProjectsByUserId(mRealm, RealmDB.getCurrentUserId());
        }else {
            selectedProjects.add(RealmDB.getProjectByProjectId(mRealm,selectedProjectId));
        }
    }

    private void initViews() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(new TaskListAdapter(mContext,modelList,orderMode));
    }


    private void initSimulatedData() {
        RealmDB.setCurrentUserId("user_111");
        RealmDB.saveUser(mRealm, SimulatedData.getCurrentUser());
        RealmDB.saveProject(mRealm,SimulatedData.getWorkProject());
        RealmDB.saveProject(mRealm,SimulatedData.getReadProject());
        RealmDB.saveProject(mRealm,SimulatedData.getTravelProject());
        RealmDB.saveTask(mRealm,"project_111",SimulatedData.getTask1());
        RealmDB.saveTask(mRealm,"project_111",SimulatedData.getTask2());
        RealmDB.saveTask(mRealm,"project_222",SimulatedData.getTask3());
        RealmDB.saveTask(mRealm,"project_333",SimulatedData.getTask4());
        RealmDB.saveTask(mRealm,"project_333",SimulatedData.getTask5());
        RealmDB.saveTask(mRealm,"project_333",SimulatedData.getTask6());
        RealmDB.saveTask(mRealm,"project_111",SimulatedData.getTask7());
        RealmDB.saveTask(mRealm,"project_222",SimulatedData.getTask8());
    }
}
