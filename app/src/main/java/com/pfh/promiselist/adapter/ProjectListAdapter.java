package com.pfh.promiselist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.widget.ProjectItemView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


/**
 * Created by Administrator on 2016/12/18 0018.
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private Context mContext;
    private List<Project> mData;
    private Realm mRealm;


    public ProjectListAdapter(Context mContext, List<Project> projects, Realm realm) {
        this.mContext = mContext;
        this.mRealm = realm;
        if (projects == null){
            this.mData = new ArrayList<Project>();
        }else {
            this.mData = projects;
        }
    }

    public void refreshData(List<Project> projects){
        mData = projects;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_project_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_project.setProject(mData.get(position),mRealm);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProjectItemView item_project;
        public ViewHolder(View itemView) {
            super(itemView);
            item_project = (ProjectItemView) itemView.findViewById(R.id.item_project);
        }
    }
}
