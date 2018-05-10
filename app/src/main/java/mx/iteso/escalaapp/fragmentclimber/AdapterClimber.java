package mx.iteso.escalaapp.fragmentclimber;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.activities.ActivityClimber;
import mx.iteso.escalaapp.beans.Climber;

/**
 * Created by aceve on 03/03/2018.
 */


public class AdapterClimber extends RecyclerView.Adapter<AdapterClimber.ViewHolder> {

    ArrayList<Climber> climbers;

    public AdapterClimber(ArrayList<Climber> climbers) {
          this.climbers = climbers;
    }

    // private FirebaseRecyclerAdapter<Climber, ViewHolder> firebaseClimber;


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_climber, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mFirstName.setText(climbers.get(position).getFirstname());
        holder.mLastName.setText(climbers.get(position).getLastname());
        holder.mGym.setText(climbers.get(position).getGym());
        final Uri image_uri = Uri.parse(climbers.get(position).getThumb());
        holder.draweeView.setImageURI(image_uri);

        //final String climber_id = FirebaseDatabase.getInstance().getReference().child("Climbers").getKey();

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityClimber.class);
                intent.putExtra("climber_id", climbers.get(position).getKey());
                v.getContext().startActivity(intent);
            }
        });


    }


    public int getItemCount() {
        return climbers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mFirstName, mLastName, mGym;
        public SimpleDraweeView draweeView;
        public RelativeLayout mDetail;
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            mFirstName = v.findViewById(R.id.item_climber_firstname);
            mLastName = v.findViewById(R.id.item_climber_lastname);
            draweeView = (SimpleDraweeView) v.findViewById(R.id.item_climber_profile_picture);
            mGym = v.findViewById(R.id.item_climber_gym);
        }


    }

}


