package com.dktechhub.applock.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dktechhub.applock.R;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder>{
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);
        View AppView = inflator.inflate(R.layout.item_app,parent,false);
        return new ViewHolder(AppView);

    }

    @Override
    public void onBindViewHolder(@NonNull AppAdapter.ViewHolder holder, int position) {
        AppModel app = mApps.get(position);
        TextView textView =holder.nameText;
        CheckBox checkBox = holder.checkBox;
        ImageView iconView = holder.iconText;
        textView.setText(app.appName);
        //Log.d("AppLock Adapter",app.appName+"Loaded");
        iconView.setImageDrawable(app.icon);
        checkBox.setChecked(app.isLocked==1);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAppClickListener.OnItemClicked(app, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView nameText;
        public CheckBox checkBox;
        public ImageView iconText;
        LinearLayout linearLayout;
        public ViewHolder(View itemView)
        {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.check);
            nameText = (TextView) itemView.findViewById(R.id.itemsharedname);
            iconText =(ImageView) itemView.findViewById(R.id.imageView);
            linearLayout=itemView.findViewById(R.id.main_item_app);
        }
    }
    private List<AppModel> mApps=new ArrayList<>();

    public void addApp(AppModel mApp) {
        this.mApps.add(mApp);
        notifyItemInserted(mApps.size()-1);
    }

    public void removeApp(AppModel appModel,int position)
    {
        mApps.remove(position);
        notifyItemRemoved(position);
    }

    public void addApps(ArrayList<AppModel> appModels)
    {
        mApps.addAll(appModels);
        notifyDataSetChanged();
    }

    public AppAdapter(OnAppClickListener onItemClicked)
    {
        this.onAppClickListener=onItemClicked;
    }

    private OnAppClickListener onAppClickListener;
    public interface OnAppClickListener
    {
        void OnItemClicked(AppModel app,int position);
    }
}
