package com.dktechhub.applock.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {
    public static ArrayList<AppModel> all = new ArrayList<>();
    private static final String DATABASE_NAME = "lockedapps.db";
    private static final String TABLE_NAME = "lockedapplist";
    private static final String KEY_ID = "id";
    private static final String KEY_PACKAGE = "package";
    private static final String KEY_IS_LOCKED = "locked";
    private static AppDatabase appDatabase = null;
    private SQLiteDatabase writable,readable;
    public static HashMap<String, Pair<Integer, Integer>> allApps = new HashMap<>();
    public static void initialize(Context context2)
    {
        if(appDatabase==null)
        {
            appDatabase=new AppDatabase(context2);

        }
    }

    public static AppDatabase getInstance()
    {
        return appDatabase;
    }

    public AppDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @SuppressLint("Range")
    public HashMap<String, Pair<Integer, Integer>> loadAll()
    {
        if(readable==null)
            readable=getReadableDatabase();
        HashMap<String, Pair<Integer, Integer>> all = new HashMap<>();
       Cursor res =readable.rawQuery(String.format("SELECT * FROM %s",TABLE_NAME),null);
       if(res.moveToFirst())
       {
           while(!res.isAfterLast())
           {

              String packages= res.getString(res.getColumnIndex(KEY_PACKAGE));
              int id = res.getInt(res.getColumnIndex(KEY_ID));
              int locked = res.getInt(res.getColumnIndex(KEY_IS_LOCKED));
              Log.d("AppLock",id+'\t'+packages+'\t'+locked);
              all.put(packages,new Pair<>(id,locked));
               res.moveToNext();
           }
       }
       res.close();
       allApps=all;
       return all;
    }
    public  void updateApp(AppModel appModel)
    {   if(writable==null)
        writable=getWritableDatabase();
        if(appModel.id==-1)
        {
            String q =String.format("INSERT INTO %s (%s,%s) VALUES ('%s',%d)",TABLE_NAME,KEY_PACKAGE,KEY_IS_LOCKED,appModel.packageName,appModel.isLocked);
            writable.execSQL(q);
        }else {
            //update
            String q = String.format("UPDATE %s SET %s = %d WHERE %s = %d",TABLE_NAME,KEY_IS_LOCKED,appModel.isLocked,KEY_ID,appModel.id);
            writable.execSQL(q);
        }
        allApps.put(appModel.packageName, new Pair<>(appModel.id,appModel.isLocked));
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         try{
             sqLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,%s TEXT,%s BOOL);",TABLE_NAME,KEY_ID,KEY_PACKAGE,KEY_IS_LOCKED));

             sqLiteDatabase.execSQL(String.format("INSERT INTO %s (%s,%s,%s)VALUES (%d,'%s',%d);",TABLE_NAME,KEY_ID,KEY_PACKAGE,KEY_IS_LOCKED,0,"ASSS",0));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s",TABLE_NAME));
        onCreate(sqLiteDatabase);
    }


    public void loadAppsFromSystem(Context context,LoaderInterface loaderInterface)
    {
        new Loader(context.getPackageManager(), loaderInterface).start();
    }

    public class Loader extends Thread {
        PackageManager packageManager;
        HashMap<String,ArrayList<AppModel>> list = new HashMap<>();
        LoaderInterface loaderInterface;
        public Loader(PackageManager packageManager,LoaderInterface loaderInterface)
        {
            this.packageManager=packageManager;
            this.loaderInterface=loaderInterface;
        }

        public void load()
        {  HashMap<String, Pair<Integer, Integer>> all = AppDatabase.this.loadAll();
            List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
            ArrayList<AppModel> list1 = new ArrayList<>();
            ArrayList<AppModel> list2 = new ArrayList<>();
            ArrayList<AppModel> list3 = new ArrayList<>();
            for(ApplicationInfo t:apps)
            {
                String appName = (String) packageManager.getApplicationLabel(t);
                String packageName = t.packageName;
                if((t.flags&ApplicationInfo.FLAG_SYSTEM )==0)
                {   if(all.containsKey(packageName))
                    list3.add(new AppModel(appName,packageName,t.loadIcon(packageManager),all.get(packageName).first,all.get(packageName).second));
                    else
                    list1.add(new AppModel(appName,packageName,t.loadIcon(packageManager)));
                }else if(packageManager.getLaunchIntentForPackage(packageName)!=null) {
                    if(all.containsKey(packageName))
                        list3.add(new AppModel(appName,packageName,t.loadIcon(packageManager),all.get(packageName).first,all.get(packageName).second));
                    else
                    list2.add(new AppModel(appName,packageName,t.loadIcon(packageManager)));
                }
            }
            Comparator<AppModel> comparator = (appModel, t1) -> appModel.appName.compareTo(t1.appName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                list1.sort(comparator);
                list2.sort(comparator);
                list3.sort(comparator);
            }

            list.put("external",list1);
            list.put("system",list2);
            list.put("recent",list3);
        }
        @Override
        public void run() {
            super.run();
            load();
            new Handler(Looper.getMainLooper()).post(() -> loaderInterface.onLoaded(list));
        }
    }


    public interface LoaderInterface{
        void onLoaded(HashMap<String,ArrayList<AppModel>> list);
    }

}
