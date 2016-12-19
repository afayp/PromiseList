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

import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<MultiItemModel> mData;
    private int mOrderMode;

    private onItemClickListener onItemClickListener;
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
            View taskItem = LayoutInflater.from(mContext).inflate(R.layout.item_task_list_task_type, parent, false);
            final TaskViewHolder taskViewHolder = new TaskViewHolder(taskItem);
            // 长按开启拖动
            taskItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskViewHolder.task_item.toggleView();
                }
            });
            taskItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemTouchHelper != null){
                        itemTouchHelper.startDrag(taskViewHolder);
                        Vibrator vibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
                        vibrator.vibrate(70);
                    }
                    return true;
                }
            });
            return taskViewHolder;
        }else {
            final View titleItem = LayoutInflater.from(mContext).inflate(R.layout.item_task_list_title_type, parent, false);
            final TitleViewHolder titleViewHolder = new TitleViewHolder(titleItem);
            // 点击title收起task
            titleItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleViewHolder.task_title.toggle();//隐藏或显示数字
                    //todo 收起或展开子任务
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(v,mData.get(titleViewHolder.getLayoutPosition()), titleViewHolder.getLayoutPosition());
                    }
                }
            });
            return titleViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == Constant.ITEM_TYPE_TASK){
            Task task = (Task) mData.get(position).getData();
            ((TaskViewHolder)holder).task_item.setData(task,mOrderMode);
        }else {
            ((TitleViewHolder)holder).task_title.setTitle((String) mData.get(position).getData());
            ((TitleViewHolder)holder).task_title.setNum("8");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
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

    public interface onItemClickListener{
        void onItemClick(View view ,MultiItemModel model,int position);
//        void onItemLongClick(View view ,MultiItemModel model,int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public void setItemTouchHelper(ItemTouchHelper helper){
        this.itemTouchHelper = helper;

    }

}
