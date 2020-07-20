package com.example.locker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locker.Database.LockedApps;
import com.example.locker.Models.App;
import com.example.locker.R;

import java.util.ArrayList;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppsViewHolder> {
    private ArrayList<App> Apps;
    Context context;
    public AppsAdapter(ArrayList<App> Apps,Context context){this.Apps = Apps;this.context = context;}

    @NonNull
    @Override
    public AppsAdapter.AppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.lalyout_apps, parent, false);
        return new AppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppsAdapter.AppsViewHolder holder, int position) {
        final App app = Apps.get(position);
        final LockedApps db = new LockedApps(context);

        holder.app_name.setText(app.getName());
        holder.app_icon.setImageDrawable(app.getIcon());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(app.getLocked()==1)
                {
                    holder.btn.setText(R.string.unlockd);holder.btn.setBackgroundColor(0XFF6EEC73);
                    app.setLocked(0);
                    db.deleteData(app.getPackage_name());
                }
                else
                {
                    holder.btn.setText(R.string.locked);holder.btn.setBackgroundColor(0XFFEF4C4C);
                    app.setLocked(1);
                    boolean val = db.insertData(app.getPackage_name());
                    if(val) Toast.makeText(context,"Locked Successfully",Toast.LENGTH_LONG).show();
                    else Toast.makeText(context,"UnSuccessful",Toast.LENGTH_LONG).show();
                }
            }
        });
        if(app.getLocked()==1)
        {holder.btn.setText(R.string.locked);holder.btn.setBackgroundColor(0XFFEF4C4C);}
        else
        {holder.btn.setText(R.string.unlockd);holder.btn.setBackgroundColor(0XFF6EEC73);}
    }

    @Override
    public int getItemCount() {
        return Apps.size();
    }

    public static class AppsViewHolder extends RecyclerView.ViewHolder {
        TextView app_name;
        ImageView app_icon;
        Button btn;
        View view;
        public AppsViewHolder(@NonNull View itemView) {
            super(itemView);
            app_name = itemView.findViewById(R.id.app_name);
            app_icon = itemView.findViewById(R.id.app_icon);
            btn = itemView.findViewById(R.id.button);
            this.view = itemView;
        }
    }
}
