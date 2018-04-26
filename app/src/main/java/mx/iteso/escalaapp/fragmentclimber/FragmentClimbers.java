package mx.iteso.escalaapp.fragmentclimber;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.activities.ActivityJudging;
import mx.iteso.escalaapp.activities.ActivityProfile;
import mx.iteso.escalaapp.activities.ActivityResults;
import mx.iteso.escalaapp.activities.ActivitySettings;
import mx.iteso.escalaapp.activities.ActivitySplashScreen;
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


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query climbersDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").orderByChild("firstname");

        // All climbers list
        climbersDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Climber> climbers = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Climber climber = postSnapshot.getValue(Climber.class);
                    climber.setKey(postSnapshot.getKey());
                    climbers.add(climber);
                }

                AdapterClimber adapterClimber = new AdapterClimber(climbers);
                climbersList.setAdapter(adapterClimber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Climberlist", "loadPost:onCancelled", databaseError.toException());
                // ...
            }


        });
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
            Intent intent = new Intent(getActivity(), ActivitySettings.class);
            startActivity(intent);
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

}