package mx.iteso.escalaapp.fragmentgym;

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
import mx.iteso.escalaapp.activities.ActivityGym;
import mx.iteso.escalaapp.beans.Gym;

/**
 * Created by aceve on 03/03/2018.
 */


public class AdapterGym extends RecyclerView.Adapter<AdapterGym.ViewHolder> {


    ArrayList<Gym> gyms;
    public AdapterGym(ArrayList<Gym> gyms) {
        this.gyms = gyms;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gym, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mName.setText(gyms.get(position).getName());
        holder.mCity.setText(gyms.get(position).getCity());
        holder.mState.setText(gyms.get(position).getState());
        final Uri image_uri = Uri.parse(gyms.get(position).getThumb());
        holder.draweeView.setImageURI(image_uri);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityGym.class);
                intent.putExtra("gym_id", gyms.get(position).getKey());
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return gyms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mCity, mState;
        public SimpleDraweeView draweeView;
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            mName = v.findViewById(R.id.item_gym_name);
            mCity = v.findViewById(R.id.item_gym_city);
            mState = v.findViewById(R.id.item_gym_state);
            draweeView = (SimpleDraweeView) v.findViewById(R.id.item_gym_profile_picture);
        }
    }

}


