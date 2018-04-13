package mx.iteso.escalaapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import mx.iteso.escalaapp.beans.Competition;

/**
 * Created by aceve on 12/03/2018.
 */

public class AdapterCompetitionEnded extends RecyclerView.Adapter<AdapterCompetitionEnded.ViewHolder> {

    public Uri uri;
    ArrayList<Competition> compsEnded;

    public AdapterCompetitionEnded(ArrayList<Competition> compsEnded) {
        this.compsEnded = compsEnded;
    }

    public AdapterCompetitionEnded.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_competition_ended, parent, false);
        AdapterCompetitionEnded.ViewHolder vh = new AdapterCompetitionEnded.ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(AdapterCompetitionEnded.ViewHolder holder, final int position) {
        holder.mCompName.setText(compsEnded.get(position).getComp_name());
        holder.mGym.setText(compsEnded.get(position).getGym());
        holder.mParticipants.setText(compsEnded.get(position).getParticipants());
        holder.mDate.setText(compsEnded.get(position).getDate());

        switch (compsEnded.get(position).getImage()) {
            case 0:
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.ameyalli)).build();
                break;
            case 1:
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.bloce)).build();
                break;
            case 2:
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.motion)).build();
                break;
            default:
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.ameyalli)).build();
                break;
        }
        holder.draweeView.setImageURI(uri);

        holder.mDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityCompetition.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return compsEnded.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCompName;
        public TextView mGym;
        public TextView mDate;
        public TextView mParticipants;
        public SimpleDraweeView draweeView;
        public RelativeLayout mDetail;

        public ViewHolder(View v) {
            super(v);
            mCompName = v.findViewById(R.id.item_comp_ended_name);
            mGym = v.findViewById(R.id.item_comp_ended_gym);
            mParticipants = v.findViewById(R.id.item_comp_ended_entrants);
            mDetail = v.findViewById(R.id.item_ended_relative);
            draweeView = (SimpleDraweeView) v.findViewById(R.id.item_gym_profile_picture);
            mDate = v.findViewById(R.id.item_comp_ended_date);
        }
    }

}
