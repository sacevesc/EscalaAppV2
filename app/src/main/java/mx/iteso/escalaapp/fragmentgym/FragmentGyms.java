package mx.iteso.escalaapp.fragmentgym;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.activities.ActivityProfile;
import mx.iteso.escalaapp.beans.Climber;
import mx.iteso.escalaapp.beans.Gym;
import mx.iteso.escalaapp.fragmentclimber.AdapterClimber;
import mx.iteso.escalaapp.settings.ActivitySettings;


public class FragmentGyms extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView gymsList;
    private EditText searchText;

    public FragmentGyms() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gyms, container, false);
        gymsList = view.findViewById(R.id.fragment_recycler_view);
        ImageButton searchButton = view.findViewById(R.id.search_button);
        ImageButton clearButton = view.findViewById(R.id.clear_button);
        searchText = view.findViewById(R.id.search_text);
        setHasOptionsMenu(true);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        gymsList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        gymsList.setLayoutManager(mLayoutManager);

        searchText.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                        keyCode == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    Query gymsDatabase = FirebaseDatabase.getInstance().getReference().child("Gyms").orderByChild("name");
                    gymsDatabase.addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<Gym> gyms = new ArrayList<>();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Gym gym = postSnapshot.getValue(Gym.class);
                                gym.setKey(postSnapshot.getKey());
                                if(gym.getName().toLowerCase().contains(searchText.getText().toString().toLowerCase()))
                                    gyms.add(gym);
                            }
                            AdapterGym adapterGyms = new AdapterGym(gyms);
                            gymsList.setAdapter(adapterGyms);
                        }

                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Gymslist", "loadPost:onCancelled", databaseError.toException());
                        }
                    });
                    return true;
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query gymsDatabase = FirebaseDatabase.getInstance().getReference().child("Gyms").orderByChild("name");
                gymsDatabase.addValueEventListener(new ValueEventListener()  {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Gym> gyms = new ArrayList<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Gym gym = postSnapshot.getValue(Gym.class);
                            gym.setKey(postSnapshot.getKey());
                            if(gym.getName().toLowerCase().contains(searchText.getText().toString().toLowerCase()))
                                gyms.add(gym);
                        }
                        AdapterGym adapterGyms = new AdapterGym(gyms);
                        gymsList.setAdapter(adapterGyms);
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Gymslist", "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query gymsDatabase = FirebaseDatabase.getInstance().getReference().child("Gyms").orderByChild("name");
                gymsDatabase.addValueEventListener(new ValueEventListener()  {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Gym> gyms = new ArrayList<>();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Gym gym = postSnapshot.getValue(Gym.class);
                            gym.setKey(postSnapshot.getKey());
                            gyms.add(gym);
                        }
                        AdapterGym adapterGyms = new AdapterGym(gyms);
                        gymsList.setAdapter(adapterGyms);
                    }
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Gymslist", "loadPost:onCancelled", databaseError.toException());
                    }
                });
                searchText.setText("");
            }
        });
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
        Query gymsDatabase = FirebaseDatabase.getInstance().getReference().child("Gyms").orderByChild("name");
        gymsDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Gym> gyms = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Gym gym = postSnapshot.getValue(Gym.class);
                    gym.setKey(postSnapshot.getKey());
                    if(gym.getName().toLowerCase().contains(searchText.getText().toString().toLowerCase()))
                        gyms.add(gym);
                }
                AdapterGym adapterGyms = new AdapterGym(gyms);
                gymsList.setAdapter(adapterGyms);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("GymsList", "load:onCancelled", databaseError.toException());
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
        return super.onOptionsItemSelected(item);
    }
}
