package mx.iteso.escalaapp.results;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

public class FragmentSemifinal extends Fragment {
    public String actualCategory = "Principiantes";
    public String compKey = " ";
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView resultsList;
    private ArrayList<Results> resultsAList;
    String boulderSemifinals = "";
    FirebaseUser currentUser;
    DatabaseReference userDatabase, compDatabase;
    public FragmentSemifinal() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_semifinals, container, false);
        resultsList = view.findViewById(R.id.fragment_recycler_view);

        resultsList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        resultsList.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("logs", actualCategory + " Q " + compKey);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        compDatabase = FirebaseDatabase.getInstance().getReference();

        String currentUid = currentUser.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(currentUid);
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setCompKey(dataSnapshot.child("currentCompKey").getValue().toString());
                setActualCategory(dataSnapshot.child("currentCategory").getValue().toString());


                compDatabase = FirebaseDatabase.getInstance().getReference().child("Competitions").child(compKey).child("semifinals");
                compDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boulderSemifinals = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Query compsDatabse = FirebaseDatabase.getInstance().getReference().child("Results").child("Semifinals").child(compKey).child(actualCategory).orderByChild("ranking");
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
                            results.setBoulder_round(Integer.parseInt(boulderSemifinals.substring(0, 1)));
                            String resultKey = postSnapshot.getKey();
                            results.setResultsKey(postSnapshot.getKey());
                            final Query bouldersDB = FirebaseDatabase.getInstance().getReference().child("Results").child("Semifinals").child(compKey).child(actualCategory).child(resultKey).child("boulders").orderByChild("number");

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

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void setActualCategory(String categoryReceived) {
        this.actualCategory = categoryReceived;
        //Toast.makeText(getActivity(),categoryReceived,Toast.LENGTH_LONG).show();
        Log.w("logs", "FROMdb " + categoryReceived, null);
    }

    public void setCompKey(String compKey) {
        this.compKey = compKey;
        Log.w("logs", "fromDB " + compKey, null);
    }
}
