package com.dktechhub.applock;

import android.app.Fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dktechhub.applock.utils.AppAdapter;
import com.dktechhub.applock.utils.AppDatabase;
import com.dktechhub.applock.utils.AppModel;

import java.util.ArrayList;
import java.util.HashMap;


public class AppsFragment extends Fragment {

    LinearLayout root,locked,reccomended,other;
    AppDatabase appDatabase;
    public AppsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root2 = inflater.inflate(R.layout.fragment_apps, container, false);
        root=root2.findViewById(R.id.root);
        appDatabase = AppDatabase.getInstance();
        return root2;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locked=root.findViewById(R.id.locked);
        reccomended = root.findViewById(R.id.recommended);
        other = root.findViewById(R.id.other);
        appDatabase.loadAppsFromSystem(getActivity(),this::parseLoaded);

    }

    AppAdapter lockedAdapter;



    AppAdapter systemAdaper,externalAdapter;



    public void parseLoaded(HashMap<String, ArrayList<AppModel>> list)
    {
        Log.d("AppLock",list.toString());
        if(list.get("system").size()==0)
        {
            other.findViewById(R.id.o_empty).setVisibility(View.VISIBLE);
            other.findViewById(R.id.o_recycle).setVisibility(View.GONE);
        }else {
            other.findViewById(R.id.o_empty).setVisibility(View.GONE);
            RecyclerView recyclerView = other.findViewById(R.id.o_recycle);
            recyclerView.setVisibility(View.VISIBLE);
            systemAdaper = new AppAdapter((app, position) -> {
                addNewApp(app);
                removeSystem(app,position);
            });
            systemAdaper.addApps(list.get("system"));
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
            recyclerView.setAdapter(systemAdaper);
        }
        other.findViewById(R.id.o_loading).setVisibility(View.GONE);


        if(list.get("external").size()==0)
        {
            reccomended.findViewById(R.id.r_empty).setVisibility(View.VISIBLE);
            reccomended.findViewById(R.id.r_recycle).setVisibility(View.GONE);
        }else {
            reccomended.findViewById(R.id.r_empty).setVisibility(View.GONE);
            RecyclerView recyclerView =reccomended.findViewById(R.id.r_recycle);
            recyclerView.setVisibility(View.VISIBLE);
            externalAdapter= new AppAdapter((app, position) -> {
                addNewApp(app);
                removeExternal(app,position);
            });
            externalAdapter.addApps(list.get("external"));
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
            recyclerView.setAdapter(externalAdapter);
        }
        reccomended.findViewById(R.id.r_loading).setVisibility(View.GONE);


        if(list.get("recent").size()==0)
        {
            locked.findViewById(R.id.l_empty).setVisibility(View.VISIBLE);
            locked.findViewById(R.id.l_recycle).setVisibility(View.GONE);
        }else {

            RecyclerView recyclerView = locked.findViewById(R.id.l_recycle);
            recyclerView.setVisibility(View.VISIBLE);
            locked.findViewById(R.id.l_empty).setVisibility(View.GONE);
            lockedAdapter = new AppAdapter(this::updateApp);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
            lockedAdapter.addApps(list.get("recent"));
            recyclerView.setAdapter(lockedAdapter);

        }

        locked.findViewById(R.id.l_loading).setVisibility(View.GONE);
    }


    public void removeSystem(AppModel appModel,int position)
    {
        systemAdaper.removeApp(appModel,position);
        if(systemAdaper.getItemCount()==0)
        {
            other.findViewById(R.id.o_empty).setVisibility(View.VISIBLE);
        }
    }

    public void removeExternal(AppModel appModel,int position)
    {
        externalAdapter.removeApp(appModel, position);
        if(externalAdapter.getItemCount()==0)
        {
            reccomended.findViewById(R.id.r_empty).setVisibility(View.VISIBLE);
        }
    }
    public void addNewApp(AppModel appModel)
    {   appModel.isLocked=1;
        lockedAdapter.addApp(appModel);
        locked.findViewById(R.id.l_empty).setVisibility(View.GONE);
        appDatabase.updateApp(appModel);
    }

    public void updateApp(AppModel appModel,int position)
    {
        appModel.isLocked= appModel.isLocked==0?1:0;
        lockedAdapter.notifyItemChanged(position);
        appDatabase.updateApp(appModel);
    }
}