package com.pfh.promiselist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

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
    private int startX;

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
//            setFullSpan(holder);
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

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.getItemViewType() != Constant.ITEM_TYPE_TASK) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
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

        taskItem.setOnTouchListener(new View.OnTouchListener() {

            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (itemTouchHelper != null) {

                    VelocityTracker velocityTracker = VelocityTracker.obtain();
                    velocityTracker.addMovement(event);
                    velocityTracker.computeCurrentVelocity(1000);
                    float yVelocity = velocityTracker.getYVelocity();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = (int) event.getX();
                            startY = (int) event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (startX > 30 && Math.abs(event.getX() - startX) / Math.abs(event.getY() - startY) > 1 && yVelocity < 50){
                                itemTouchHelper.startSwipe(taskViewHolder);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            //最后要记得回收
                            velocityTracker.clear();
                            velocityTracker.recycle();
                            break;
                    }
                }
                return false;
            }
        });

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
//                        itemTouchHelper.startDrag(taskViewHolder);
//                        Vibrator vibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
//                        vibrator.vibrate(70);
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

        public TaskViewHolder(View itemView) {
            super(itemView);
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
