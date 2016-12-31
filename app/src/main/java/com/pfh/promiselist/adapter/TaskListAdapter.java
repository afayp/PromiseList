package com.pfh.promiselist.adapter;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.MultiItemModel;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.others.Constant;
import com.pfh.promiselist.widget.TaskItemTitle;
import com.pfh.promiselist.widget.TaskItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<MultiItemModel> mData;
    private int mOrderMode;
    private boolean select;// 是否处于选择状态
    private List<Integer> selectedPosition = new ArrayList<>(); // 选中的位置

    private ItemTouchHelper itemTouchHelper;

    public TaskListAdapter(Context mContext, List<MultiItemModel> mData, int mOrderMode) {
        this.mContext = mContext;
        this.mData = mData;
        this.mOrderMode = mOrderMode;
    }

    public void refreshData(List<MultiItemModel> list,int orderMode){
        mData = list;
        this.mOrderMode = orderMode;
        notifyDataSetChanged();
    }

    public void setData(List<MultiItemModel> list){
        mData = list;
    }

    public List<MultiItemModel> getData(){
        return mData;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("TAG","onCreateViewHolder");
        if (viewType == Constant.ITEM_TYPE_TASK){
            View taskItem = LayoutInflater.from(mContext).inflate(R.layout.item_task_list_task_type, parent, false);
            TaskViewHolder taskViewHolder = new TaskViewHolder(taskItem);
            setupTaskItem(taskItem,taskViewHolder);
            return taskViewHolder;
        }else {
            final View titleItem = LayoutInflater.from(mContext).inflate(R.layout.item_task_list_title_type, parent, false);
            final TitleViewHolder titleViewHolder = new TitleViewHolder(titleItem);
            return titleViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("TAG","onBindViewHolder: "+position);
        int type = getItemViewType(position);
        if (type == Constant.ITEM_TYPE_TASK){
            Task task = (Task) mData.get(position).getContent();
            TaskItemView taskItem = ((TaskViewHolder)holder).taskItem;
            taskItem.setVisibility(mData.get(position).isExpand() ? View.VISIBLE : View.GONE);
            taskItem.setData(task,mOrderMode);
            taskItem.setSelectedBg(select && selectedPosition.contains((Integer)position));

        }else {
            ((TitleViewHolder)holder).task_title.setTitle((String) mData.get(position).getContent());
            ((TitleViewHolder)holder).task_title.setNum(getTypeCount(mData.get(position).getLabel())+"");
            ((TitleViewHolder)holder).task_title.toggle(mData.get(position).isExpand() ? true : false);
            ((TitleViewHolder)holder).task_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandOrFoldTitle(position);
                }
            });
        }
    }

    public void setSelectState(boolean select){
        this.select = select;
//        if (!select) {
//            selectedPosition.clear();
//        }
    }

    public void notifySelectedItem(){
        for (int i = 0; i < selectedPosition.size(); i++) {
            notifyItemChanged(selectedPosition.get(i));
        }
    }

    public void clearSelect(){
        selectedPosition.clear();
        notifyDataSetChanged();
    }

    private void setupTaskItem(View taskItem, final TaskViewHolder taskViewHolder){

//        taskItem.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (itemTouchHelper != null) {
//                    int startX = 0;
//                    int startY = 0;
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            startX = (int) event.getX();
//                            startY = (int) event.getY();
//                            Log.e("TAG","startX: "+startX +" | startY: "+startY);
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            if (Math.abs(event.getY() - startY) < 100 && Math.abs(event.getX() - startX) > 200) itemTouchHelper.startSwipe(taskViewHolder);
//                            break;
//                    }
//                }
//                return false;
//            }
//        });

        taskItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG","onclick "+taskViewHolder.getLayoutPosition());
                Log.e("TAG","select: "+select);
                if (select) {
                    handleClickIfSelect(taskViewHolder);
                }else {
                    onItemClickListener.onClickTask(v,taskViewHolder.getLayoutPosition());
                }

            }
        });

        taskItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("TAG","onLongClick "+taskViewHolder.getLayoutPosition());
                Log.e("TAG","select: "+select);

                if (itemTouchHelper != null){
                    if (!select) {
                        selectedPosition.clear();
                        itemTouchHelper.startDrag(taskViewHolder);
                        Vibrator vibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
                        vibrator.vibrate(70);
                        select = true;
                        taskViewHolder.taskItem.setSelectedBg(true);
                        selectedPosition.add((Integer) taskViewHolder.getLayoutPosition());
                        onItemClickListener.onSelectChanged(select,taskViewHolder.getLayoutPosition(),1);
                    }else {
                        handleClickIfSelect(taskViewHolder);
                    }
                }
                return true;
            }
        });
    }

    private void handleClickIfSelect(TaskViewHolder taskViewHolder){
        if (selectedPosition.contains(taskViewHolder.getLayoutPosition())) {//去掉
            taskViewHolder.taskItem.setSelectedBg(false);
            selectedPosition.remove((Integer) taskViewHolder.getLayoutPosition());
            Log.e("TAG","selectedPosition: "+selectedPosition);
            select = selectedPosition.size() != 0;
            onItemClickListener.onSelectChanged(select,taskViewHolder.getLayoutPosition(),selectedPosition.size());
        } else {//添加
            taskViewHolder.taskItem.setSelectedBg(true);
            selectedPosition.add((Integer) taskViewHolder.getLayoutPosition());
            onItemClickListener.onSelectChanged(select,taskViewHolder.getLayoutPosition(),selectedPosition.size());
            Log.e("TAG","selectedPosition: "+selectedPosition);
        }
    }

    public List<Integer> getSelectedPositions(){
        return selectedPosition;
    }

    public void expandOrFoldTitle(int position){
        String label = mData.get(position).getLabel();
        mData.get(position).setExpand(!mData.get(position).isExpand());
        int count = 0;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getItemType() == Constant.ITEM_TYPE_TASK && mData.get(i).getLabel().equals(label)){
                count++;
                if (mData.get(position).isExpand()){
                    //展开
                    mData.get(i).setExpand(true);
                }else {
                    //收起
                    mData.get(i).setExpand(false);
                }
            }
        }
        notifyItemRangeChanged(position,count+1);
    }

    //找到该task的title,展开该title,注意是向上找
    public void expandTaskOfTitle(int position){
        for (int i = position; i >= 0 ; i--) {
            if (mData.get(i).getItemType() != Constant.ITEM_TYPE_TASK && mData.get(i).getLabel().equals(mData.get(position).getLabel())){
                expandOrFoldTitle(i);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private int getTypeCount(String label){
        int count = 0;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getItemType() == Constant.ITEM_TYPE_TASK && mData.get(i).getLabel().equals(label)){
                count ++;
            }
        }
        return count;
    }

    public void notifyTitleChanged(String label){
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getItemType() != Constant.ITEM_TYPE_TASK && mData.get(i).getLabel().equals(label)){
                notifyItemChanged(i);
            }
        }

    }

    public void notifyAllTitleChanged(){
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getItemType() != Constant.ITEM_TYPE_TASK){
                notifyItemChanged(i);
            }
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TaskItemTitle task_title;
        public TitleViewHolder(View itemView) {
            super(itemView);
            task_title = (TaskItemTitle) itemView.findViewById(R.id.task_title);
        }
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TaskItemView taskItem;
        LinearLayout ll_root;

        public TaskViewHolder(View itemView) {
            super(itemView);
            ll_root = (LinearLayout) itemView.findViewById(R.id.ll_root);
            taskItem = (TaskItemView) itemView.findViewById(R.id.task_item);
        }
    }

    public void setItemTouchHelper(ItemTouchHelper helper){
        this.itemTouchHelper = helper;
    }

    private onItemClickListener onItemClickListener;

    public interface onItemClickListener {
        void onClickTask(View view , int position);
        void onSelectChanged(boolean select,int position , int count);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.onItemClickListener = listener;
    }


}
