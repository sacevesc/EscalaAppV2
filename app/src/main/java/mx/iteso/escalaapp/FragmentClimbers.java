package mx.iteso.escalaapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import mx.iteso.escalaapp.beans.Climber;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentClimbers extends Fragment {
    private RecyclerView.LayoutManager mLayoutManager;

    public FragmentClimbers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_climbers, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_recycler_view);
        setHasOptionsMenu(true);


        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        ImageView imageView = view.findViewById(R.id.activity_main_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityClimber.class);
                startActivity(intent);
            }
        });


        ArrayList<Climber> climbers = new ArrayList<>();
        climbers.add(new Climber("Arturo", "Garcia", "Mantis", 0));
        climbers.add(new Climber("German", "Sanchez", "", 1));
        climbers.add(new Climber("Edric", "Freyria", "Motion Boulder", 2));
        climbers.add(new Climber("Luis", "Vazquez", "", 3));
        climbers.add(new Climber("Sebastián", "Aceves", "Rocodromo Ameyalli", 4));

        climbers.add(new Climber("Arturo", "Garcia", "Mantis", 0));
        climbers.add(new Climber("German", "Sanchez", "", 1));
        climbers.add(new Climber("Edric", "Freyria", "Motion Boulder", 2));
        climbers.add(new Climber("Luis", "Vazquez", "", 3));
        climbers.add(new Climber("Sebastián", "Aceves", "Rocodromo Ameyalli", 4));
        climbers.add(new Climber("Arturo", "Garcia", "Mantis", 0));
        AdapterClimber adapterClimber = new AdapterClimber(climbers);
        recyclerView.setAdapter(adapterClimber);
        return view;
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
        return super.onOptionsItemSelected(item);
    }
}
