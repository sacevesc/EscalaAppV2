package mx.iteso.escalaapp;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import mx.iteso.escalaapp.beans.Gym;


public class FragmentGyms extends Fragment {

    //Context context = FragmentGyms.this;

    private RecyclerView.LayoutManager mLayoutManager;

    public FragmentGyms() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gyms, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_recycler_view);
        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        ImageView imageView = view.findViewById(R.id.activity_main_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityProfile.class);
                startActivity(intent);
            }
        });
        ArrayList<Gym> gyms = new ArrayList<>();
        gyms.add(new Gym("Ameyalli", "Muro de escalada en Zapopan Jalisco.", "Guadalajara", 0));
        gyms.add(new Gym("Motion", "motiva motion un lugar para boulderear", "Zapopan", 1));
        gyms.add(new Gym("Bloc-e", "Para ser el mejor, escala con los mejores", "CDMX", 2));

        AdapterGym adapterProduct = new AdapterGym(gyms);
        recyclerView.setAdapter(adapterProduct);
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
