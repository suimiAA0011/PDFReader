package com.example.pdfreader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private Adapter adapter;
   private List<String> data =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = findViewById(R.id.ListView);
        adapter = new Adapter();
        listView.setAdapter(adapter);

    }
    @SuppressLint("NewApi")
    @Override
    protected void onResume(){
        super.onResume();
        if(notPermissions()){
            requestPermissions(PERMISSIONS , REQUEST_PERMISSIONS);
        }
        else{
            loadData();
        }
    }
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE ,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSIONS_COUNT = 2;
    private static final int REQUEST_PERMISSIONS = 1234;

    private boolean notPermissions(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionPtr =0;
            while(permissionPtr < PERMISSIONS_COUNT){
                if(checkSelfPermission(PERMISSIONS[permissionPtr])!=
                        PackageManager.PERMISSION_GRANTED){
                    return true;
                }permissionPtr++;
            }

        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode , String[] permissions,
    int[] grantResult){
      super.onRequestPermissionsResult(requestCode,permissions,grantResult);
      if(requestCode==REQUEST_PERMISSIONS&&grantResult.length>0){
          if(notPermissions()){
              ((ActivityManager) this.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
    recreate();
          }else{
loadData();
          }
      }
    }
    void loadData(){
        data.clear();
        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
File[] files =downloadFolder.listFiles();
for(int i =0; i < files.length; i++){
    String fileneme = files[i].getAbsolutePath();
   if(fileneme.endsWith(".pdf")){
        data.add(fileneme);
    }
}
adapter.setData(data);
    }
    class Adapter extends BaseAdapter{
        List<String> data = new ArrayList<>();
        void  setData(List<String> mData){
            data.clear();
            data.addAll(mData);
            notifyDataSetChanged();
        }
        @Override
        public int getCount(){
            return data.size();
        }
        @Override
        public Object getItem(int position){
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position , View convertView , ViewGroup parent ){
           if(convertView== null){
               LayoutInflater inflater =(LayoutInflater) MainActivity.this
                       .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               convertView =inflater.inflate(R.layout.item ,parent ,false);
           }
            TextView textView = convertView.findViewById(R.id.item);
textView.setText(data.get(position));
return convertView;    }
    }
}
