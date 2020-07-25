package com.example.rs3_app.utility;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rs3_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SkillGridAdapter extends RecyclerView.Adapter<SkillGridAdapter.ViewHolder> {

    private List<String> names;
    LayoutInflater inflater;
    private Resources r;
    private String packageName;

    public SkillGridAdapter(Context c, List<String> names) {
        this.names = names;
        inflater = LayoutInflater.from(c);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.skill_card, parent, false);
        r = v.getResources();
        packageName = v.getContext().getPackageName();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.skillName.setText(names.get(position));
        String iconString = "ic_skill_" + position;
        int currentId = r.getIdentifier(iconString, "drawable", packageName);
        Picasso.get().load(currentId).fit().centerInside().into(holder.skillIcon);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView skillName;
        ImageView skillIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            skillName = itemView.findViewById(R.id.skill_name);
            skillIcon = itemView.findViewById(R.id.skill_icon);
        }
    }

}
