package mx.iteso.escalaapp.FragmentGyms;

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

import mx.iteso.escalaapp.Activities.ActivityGym;
import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.Gym;

/**
 * Created by aceve on 03/03/2018.
 */


public class AdapterGym extends RecyclerView.Adapter<AdapterGym.ViewHolder> {

    public Uri uri;
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
       /* switch (gyms.get(position).getPhoto()) {
            case 0:
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.ameyalli)).build();
                break;
            case 1:
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.motion)).build();
                break;
            case 2:
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.bloce)).build();
                break;
            default:
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.bloce)).build();
                break;
        }
        holder.draweeView.setImageURI(uri);
*/
        holder.mDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(),ActivityGym.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return gyms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public TextView mCity;
        public RelativeLayout mDetail;
        public SimpleDraweeView draweeView;


        public ViewHolder(View v) {
            super(v);
            mName = v.findViewById(R.id.item_gym_name);
            mCity = v.findViewById(R.id.item_gym_city);
            mDetail = v.findViewById(R.id.item_gym_relative);
            draweeView = (SimpleDraweeView) v.findViewById(R.id.item_gym_profile_picture);
        }
    }

}


