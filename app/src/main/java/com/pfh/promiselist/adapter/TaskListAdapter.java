package com.pfh.promiselist.adapter;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.MultiItemModel;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.Constant;
import com.pfh.promiselist.widget.TaskItemTitle;
import com.pfh.promiselist.widget.TaskItemView;
import com.pfh.promiselist.widget.TaskItemView2;

import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<MultiItemModel> mData;
    private int mOrderMode;

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

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.ITEM_TYPE_TASK){
            View taskItem = LayoutInflater.from(mContext).inflate(R.layout.item_task_list_task_type_2, parent, false);
            final TaskView2Holder taskView2Holder = new TaskView2Holder(taskItem);

            taskItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskView2Holder.task_item_2.toggleView();
                }
            });

            taskItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemTouchHelper != null){
                        itemTouchHelper.startDrag(taskView2Holder);
                        Vibrator vibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
                        vibrator.vibrate(70);
                    }
                    return true;
                }
            });
            return taskView2Holder;
        }else {
            final View titleItem = LayoutInflater.from(mContext).inflate(R.layout.item_task_list_title_type, parent, false);
            final TitleViewHolder titleViewHolder = new TitleViewHolder(titleItem);
            return titleViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == Constant.ITEM_TYPE_TASK){
            Task task = (Task) mData.get(position).getContent();
//            ((TaskViewHolder)holder).task_item.setData(task,mOrderMode);
//            ((TaskViewHolder)holder).task_item.setVisibility(mData.get(position).isExpand() ? View.VISIBLE : View.GONE);
            ((TaskView2Holder)holder).task_item_2.setData(task,mOrderMode);
            ((TaskView2Holder)holder).task_item_2.setVisibility(mData.get(position).isExpand() ? View.VISIBLE : View.GONE);

        }else {
            ((TitleViewHolder)holder).task_title.setTitle((String) mData.get(position).getContent());
//            ((TitleViewHolder)holder).task_title.setNum(mData.get(position).getTaskCount()+"");
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

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TaskItemView task_item;

        public TaskViewHolder(View itemView) {
            super(itemView);
            task_item = (TaskItemView) itemView.findViewById(R.id.task_item);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TaskItemTitle task_title;
        public TitleViewHolder(View itemView) {
            super(itemView);
            task_title = (TaskItemTitle) itemView.findViewById(R.id.task_title);
        }
    }

    static class TaskView2Holder extends RecyclerView.ViewHolder {

        TaskItemView2 task_item_2;

        public TaskView2Holder(View itemView) {
            super(itemView);
            task_item_2 = (TaskItemView2) itemView.findViewById(R.id.task_item_2);
        }
    }


    public void setItemTouchHelper(ItemTouchHelper helper){
        this.itemTouchHelper = helper;
    }

}
