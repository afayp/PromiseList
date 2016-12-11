package com.pfh.promiselist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.MultiItemModel;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.Constant;
import com.pfh.promiselist.utils.L;
import com.pfh.promiselist.widget.TaskItemView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<MultiItemModel> mData;
    private int mOrderMode;

    public TaskListAdapter(Context mContext, List<MultiItemModel> mData, int mOrderMode) {
        this.mContext = mContext;
        this.mData = mData;
        this.mOrderMode = mOrderMode;
        L.e("data size: "+mData.size());
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.ITEM_TYPE_TASK){
            View taskItem = LayoutInflater.from(mContext).inflate(R.layout.item_task_list_task_type, parent, false);
            return new TaskViewHolder(taskItem);
        }else {
            View textItem = LayoutInflater.from(mContext).inflate(R.layout.item_task_list_text_type, parent, false);
            return new TextViewHolder(textItem);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == Constant.ITEM_TYPE_TASK){
            Task task = (Task) mData.get(position).getData();
            ((TaskViewHolder)holder).task_item.setData(task,mOrderMode);
        }else {
            String text = (String) mData.get(position).getData();
            ((TextViewHolder)holder).tv_text.setText(text);
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

    static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;
        public TextViewHolder(View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}
