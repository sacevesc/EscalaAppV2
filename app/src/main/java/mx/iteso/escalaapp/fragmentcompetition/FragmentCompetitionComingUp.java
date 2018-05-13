package mx.iteso.escalaapp.fragmentcompetition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.Competition;

/**
 * Created by aceve on 12/03/2018.
 */

public class FragmentCompetitionComingUp extends Fragment {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView compsList;

    public FragmentCompetitionComingUp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_competition_comingup, container, false);
        compsList = view.findViewById(R.id.fragment_recycler_view);

        compsList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        compsList.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query compsDatabse = FirebaseDatabase.getInstance().getReference().child("Competitions").orderByChild("date");

        // All comps list
        compsDatabse.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Competition> comps = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Competition competition = postSnapshot.getValue(Competition.class);
                    competition.setCompKey(postSnapshot.getKey());
                    comps.add(competition);
                }
                AdapterCompetition adapterCompetition = new AdapterCompetition(comps);
                compsList.setAdapter(adapterCompetition);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("CompetitionList", "load:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
}
