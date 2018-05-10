package mx.iteso.escalaapp.results;

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
import java.util.Objects;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.Boulder;
import mx.iteso.escalaapp.beans.Results;

/**
 * Created by aceve on 12/03/2018.
 */

public class FragmentSuperfinal extends Fragment {
    public String actualCategory = "Principiantes";
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView resultsList;
    private ArrayList<Results> resultsAList;


    public FragmentSuperfinal() {
        // Required empty public constructor
    }


    public void setActualCategory(String categoryReceived) {
        this.actualCategory = categoryReceived;
        //Toast.makeText(getActivity(),categoryReceived,Toast.LENGTH_LONG).show();
        Log.w("Category", categoryReceived, null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qualifications, container, false);
        resultsList = view.findViewById(R.id.fragment_recycler_view);

        resultsList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        resultsList.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query compsDatabse = FirebaseDatabase.getInstance().getReference().child("Results").child("Superfinals").child("-LBnKtYLonGvlizvO9NY").child(actualCategory).orderByChild("ranking");

        // All comps list
        compsDatabse.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsAList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final Results results = new Results();
                    results.setFirstname(Objects.requireNonNull(postSnapshot.child("firstname").getValue()).toString());
                    results.setLastname(Objects.requireNonNull(postSnapshot.child("lastname").getValue()).toString());
                    results.setRanking((Objects.requireNonNull(postSnapshot.child("ranking").getValue()).toString()));
                    results.setSum(Objects.requireNonNull(postSnapshot.child("sum").getValue()).toString());
                    String resultKey = postSnapshot.getKey();
                    results.setResultsKey(postSnapshot.getKey());

                    final Query bouldersDB = FirebaseDatabase.getInstance().getReference().child("Results").child("Superfinals").child("-LBnKtYLonGvlizvO9NY").child("Principiantes").child(resultKey).child("boulders").orderByChild("number");

                    bouldersDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<Boulder> boulderArrayList = new ArrayList<>();

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Boulder boulder = postSnapshot.getValue(Boulder.class);
                                boulderArrayList.add(boulder);
                            }

                            results.setBoulders(boulderArrayList);
                            AdapterResults adapterResults = new AdapterResults(resultsAList);
                            resultsList.setAdapter(adapterResults);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    resultsAList.add(results);
                }
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
