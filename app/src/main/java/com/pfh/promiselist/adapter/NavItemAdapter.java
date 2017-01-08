package com.pfh.promiselist.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.pfh.promiselist.R;
import com.pfh.promiselist.others.Constant;
import com.pfh.promiselist.utils.DensityUtil;
import com.pfh.promiselist.widget.NavigationItem;

import java.util.List;

/**
 *
 */

public class NavItemAdapter extends RecyclerView.Adapter<NavItemAdapter.MyViewHolder> {

    private List<String> names;
    private List<String> counts;
    private int type;
    private LayoutInflater inflater;
    private boolean expand = false;
    private int originHeight;

    public NavItemAdapter(Context Context, int type, List<String> counts, List<String> names) {
        this.type = type;
        this.counts = counts;
        this.names = names;
        inflater = LayoutInflater.from(Context);
        originHeight =  DensityUtil.dp2px(Context,40);
    }

    public int getTotalHeight(){
        return getItemCount() * originHeight;
    }

    public void refreshData(List<String> counts, List<String> names){
        this.counts = counts;
        this.names = names;
//        notifyDataSetChanged();
    }

    public void expand(boolean expand){
        this.expand = expand;
        if (expand) {
            for (int i = 0; i < names.size(); i++) {
                notifyItemChanged(i);
            }
        }else {
            for (int i = names.size() - 1; i >= 0  ; i--) {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_nav_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickNavItemListener != null) {
                    onClickNavItemListener.onClickItem(holder.getLayoutPosition(),type);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.item.setName(names.get(position));
        holder.item.setIcon(type == Constant.NAV_TYPE_TAG ? R.drawable.ic_tag_grey : R.drawable.ic_menu_grey);
        holder.item.setCount(counts.get(position));
//        holder.item.setVisibility(expand ? View.VISIBLE : View.GONE);
        expandAnim(holder.itemView,originHeight,expand);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    private OnClickNavItemListener onClickNavItemListener;

    public void setOnClickNavItemListener(OnClickNavItemListener listener){
        this.onClickNavItemListener = listener;
    }

    public interface OnClickNavItemListener{
        void onClickItem(int position,int type);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        NavigationItem item;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = (NavigationItem) itemView.findViewById(R.id.nav_item);
        }
    }

    private void expandAnim(final View view , int height, final boolean expand){
        ValueAnimator animator = null;
        if (expand) {
            animator = ValueAnimator.ofInt(0, height);
        }else {
            animator = ValueAnimator.ofInt(height, 0);
        }
        animator.setDuration(200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        animator.start();
    }
}
