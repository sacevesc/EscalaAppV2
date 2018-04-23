package mx.iteso.escalaapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mx.iteso.escalaapp.beans.Climber;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentClimbers extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference climbersDatabase;
    private RecyclerView climbersList;

    public FragmentClimbers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_climbers, container, false);
        climbersList = view.findViewById(R.id.fragment_climbers_recycler_view);
        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mLayoutManager = new LinearLayoutManager(getActivity());

        climbersList.setHasFixedSize(true);
        climbersList.setLayoutManager(mLayoutManager);

        ImageView imageView = view.findViewById(R.id.activity_main_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityProfile.class);
                startActivity(intent);
            }
        });

        climbersDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers");


       /* ArrayList<Climber> climbers = new ArrayList<>();
        climbers.add(new Climber("Arturo", "Garcia", "Mantis", "0"));
        climbers.add(new Climber("German", "Sanchez", "", "1"));
        climbers.add(new Climber("Edric", "Freyria", "Motion Boulder", "2"));
        climbers.add(new Climber("Luis", "Vazquez", "", "3"));
        climbers.add(new Climber("Sebastián", "Aceves", "Rocodromo Ameyalli", "4"));

        AdapterClimber adapterClimber = new AdapterClimber(climbers);
        climbersList.setAdapter(adapterClimber);
        */
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Climber, ClimberViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Climber, ClimberViewHolder>(
                Climber.class, R.layout.item_climber, ClimberViewHolder.class, climbersDatabase) {
            @Override
            protected void populateViewHolder(ClimberViewHolder viewHolder, Climber model, int position) {
                viewHolder.setClimberData(model.getFirstname(), model.getLastname(), model.getGym(), model.getPhoto());
            }

        };
        climbersList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_activity_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_judging) {
            Intent intent = new Intent(getActivity(), ActivityJudging.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_results) {
            Intent intent = new Intent(getActivity(), ActivityResults.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), ActivitySplashScreen.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class ClimberViewHolder extends RecyclerView.ViewHolder {
        public TextView mFirstName;
        public TextView mLastName;
        public TextView mGym;
        public SimpleDraweeView draweeView;
        public RelativeLayout mDetail;
        View v;

        public ClimberViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            mFirstName = v.findViewById(R.id.item_climber_firstname);
            mLastName = v.findViewById(R.id.item_climber_lastname);
            draweeView = (SimpleDraweeView) v.findViewById(R.id.item_climber_profile_picture);
            mGym = v.findViewById(R.id.item_climber_gym);
            mDetail = v.findViewById(R.id.item_climber_relative);
            mDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ActivityProfile.class);
                    v.getContext().startActivity(intent);
                }
            });

        }

        public void setClimberData(String firstName, String lastname, String gym, String image) {
            mFirstName.setText(firstName);
            mLastName.setText(lastname);
            mGym.setText(gym);
            Uri imageUri = Uri.parse(image);
            draweeView.setImageURI(imageUri);

        }

    }
}