package com.pfh.promiselist.others;

import android.support.v7.util.DiffUtil;

import com.pfh.promiselist.model.MultiItemModel;
import com.pfh.promiselist.model.Task;

import java.util.List;

/**
 * Created by Administrator on 2016/12/31 0031.
 */

public class DiffCallback extends DiffUtil.Callback {
    private List<MultiItemModel> oldData;
    private List<MultiItemModel> newData;

    public DiffCallback(List<MultiItemModel> oldData, List<MultiItemModel> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData.size();
    }

    /**
     * 被DiffUtil调用，用来判断 两个对象是否是相同的Item。
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldData.get(oldItemPosition).getItemType() != Constant.ITEM_TYPE_TASK
                && newData.get(newItemPosition).getItemType() != Constant.ITEM_TYPE_TASK){
            return true;
        }

        if (oldData.get(oldItemPosition).getItemType() == Constant.ITEM_TYPE_TASK
                && newData.get(newItemPosition).getItemType() == Constant.ITEM_TYPE_TASK) {
            Task oldTask = (Task) oldData.get(oldItemPosition).getContent();
            Task newTask = (Task) newData.get(newItemPosition).getContent();
            return oldTask.getTaskId().equals(newTask.getTaskId());
        }
        return false;

//        if (oldData.get(oldItemPosition).getItemType() == newData.get(newItemPosition).getItemType()){
//
//            if (oldData.get(oldItemPosition).getItemType() != Constant.ITEM_TYPE_TASK){
//                return true;
////                return oldData.get(oldItemPosition).getLabel().equals(newData.get(newItemPosition).getLabel());
//            }else {
//                Task oldTask = (Task) oldData.get(oldItemPosition).getContent();
//                Task newTask = (Task) newData.get(newItemPosition).getContent();
//                return oldTask.getTaskId().equals(newTask.getTaskId());
//            }
//        }
//        return false;
    }

    /*
     * 被DiffUtil调用，用来检查 两个item是否含有相同的数据
     * 这个方法仅仅在areItemsTheSame()返回true时，才调用。
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));

//        if (oldData.get(oldItemPosition).getItemType() != Constant.ITEM_TYPE_TASK
//                && newData.get(newItemPosition).getItemType() != Constant.ITEM_TYPE_TASK) {
//            return oldData.get(oldItemPosition).getContent().equals(newData.get(newItemPosition).getContent());
//        }else {
//            Log.e("TAG","old: " +oldItemPosition +": "+ oldData.get(oldItemPosition).toString());
//            Log.e("TAG","new: " +newItemPosition +" : "+newData.get(newItemPosition).toString());
//
////            Task oldTask = (Task) oldData.get(oldItemPosition).getContent();
////            Task newTask = (Task) newData.get(newItemPosition).getContent();
////            return oldTask.equals(newTask);
//        }
    }

//    /**
//     *areItemsTheSame()返回true而areContentsTheSame()返回false，也就是说两个对象代表的数据是一条，但是内容更新了(只有task才会走到这里)
//     */
//    @Nullable
//    @Override
//    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
//        Task oldTask = (Task) oldData.get(oldItemPosition).getContent();
//        Task newTask = (Task) newData.get(oldItemPosition).getContent();
//        Bundle diffBundle = new Bundle();
//        if (oldTask.isFixed() != newTask.isFixed()){
//            diffBundle.putBoolean("FIXED",newTask.isFixed());
//        }
//        return diffBundle;
//    }
}
