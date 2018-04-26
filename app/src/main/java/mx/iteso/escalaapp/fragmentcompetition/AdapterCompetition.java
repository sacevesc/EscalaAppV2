package mx.iteso.escalaapp.fragmentcompetition;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.activities.ActivityCompetition;
import mx.iteso.escalaapp.beans.Competition;

/**
 * Created by aceve on 12/03/2018.
 */

public class AdapterCompetition extends RecyclerView.Adapter<AdapterCompetition.ViewHolder> {

    ArrayList<Competition> compComingUp;

    public AdapterCompetition(ArrayList<Competition> compComingUp) {
        this.compComingUp = compComingUp;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_competition, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(AdapterCompetition.ViewHolder holder, final int position) {
        holder.mCompName.setText(compComingUp.get(position).getName());
        holder.mGym.setText(compComingUp.get(position).getGym());
        holder.mDay.setText(compComingUp.get(position).getDay());
        holder.mMonth.setText(compComingUp.get(position).getMonth());
        holder.mYear.setText(compComingUp.get(position).getYear());
        holder.mParticipants.setText(compComingUp.get(position).getParticipants());


        final Uri image_uri = Uri.parse(compComingUp.get(position).getImage());
        holder.draweeView.setImageURI(image_uri);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityCompetition.class);
                intent.putExtra("comp_id", compComingUp.get(position).getCompKey());
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return compComingUp.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCompName;
        public TextView mGym;
        public TextView mDay, mMonth, mYear;
        public TextView mParticipants;
        public SimpleDraweeView draweeView;
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            mCompName = v.findViewById(R.id.item_comp_comingup_name);
            mGym = v.findViewById(R.id.item_comp_comingup_gym);
            mParticipants = v.findViewById(R.id.item_comp_comingup_entrants);
            draweeView = (SimpleDraweeView) v.findViewById(R.id.item_gym_profile_picture);
            mDay = v.findViewById(R.id.item_comp_comingup_day);
            mMonth = v.findViewById(R.id.item_comp_comingup_month);
            mYear = v.findViewById(R.id.item_comp_comingup_year);
        }
    }
}