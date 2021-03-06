package mx.iteso.escalaapp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.activities.ActivitySignGym;

/**
 * Created by aceve on 23/04/2018.
 */

public class FragmentGymSettings extends Fragment {
    TextView gymProfile, createCompetition, payment, help, addGym;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser currentUser;
    String isowner = "true";

    public FragmentGymSettings() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gym_settings, container, false);
        setHasOptionsMenu(true);

        gymProfile = view.findViewById(R.id.setting_gym_profile);
        createCompetition = view.findViewById(R.id.setting_gym_create_comp);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();

        Query firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(currentUid).child("owner");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isowner = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        if (isowner.equals("true")) {
            createCompetition.setVisibility(View.VISIBLE);
            //Toast.makeText(getActivity(), "Create compettition", Toast.LENGTH_SHORT).show();
        } else {
            createCompetition.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Only gym owners can create a competition " + isowner, Toast.LENGTH_SHORT).show();
        }


        payment = view.findViewById(R.id.setting_gym_payment);
        help = view.findViewById(R.id.setting_gym_help);
        addGym = view.findViewById(R.id.create_gym);
        addGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createGymIntent = new Intent(getActivity(), ActivitySignGym.class);
                startActivity(createGymIntent);
            }
        });

        createCompetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createGymIntent = new Intent(getActivity(), ActivityCreateCompetition.class);
                startActivity(createGymIntent);
            }
        });

        return view;
    }

}
