package mx.iteso.escalaapp.results;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.activities.ActivityClimber;
import mx.iteso.escalaapp.beans.Results;

/**
 * Created by aceve on 12/03/2018.
 */

public class AdapterResults extends RecyclerView.Adapter<AdapterResults.ViewHolder> {

    ArrayList<Results> resultsList;

    public AdapterResults(ArrayList<Results> resultsList) {
        this.resultsList = resultsList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_results, parent, false);
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    public void onBindViewHolder(AdapterResults.ViewHolder holder, final int position) {
        holder.mRanking.setText(resultsList.get(holder.getAdapterPosition()).getRanking());
        String name = resultsList.get(position).getFirstname() + " " + resultsList.get(position).getLastname();
        holder.mName.setText(name);
        holder.mSum.setText(resultsList.get(position).getSum());
        int boulderPerRound = resultsList.get(position).getBoulder_round();


        for (int i = 0; i < boulderPerRound; i++) {
            boulderResult boulderResult = new boulderResult(holder.view.getContext());
            holder.top = boulderResult.findViewById(R.id.item_boulder_top_tries);
            holder.bonus = boulderResult.findViewById(R.id.item_boulder_bonus_tries);
            try {
                holder.bonus.setText(resultsList.get(position).getBoulders().get(i).getBonus());
                holder.top.setText(resultsList.get(position).getBoulders().get(i).getTop());
            } catch (Exception e) {
                Log.w("Exception", e.getMessage(), null);

            }

            holder.linearBoulders.addView(boulderResult);
        }

//        Log.w("Results", resultsList.get(position).getBoulders().get(0).getTop(), null);

        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityClimber.class);
                intent.putExtra("climber_id", resultsList.get(position).getClimber().getKey());
                v.getContext().startActivity(intent);
            }
        });

    }

    public int getItemCount() {
        return resultsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mRanking;
        TextView mName;
        TextView mSum;
        TextView bonus, top;
        LinearLayout linearBoulders;
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;

            linearBoulders = v.findViewById(R.id.item_results_linear_boulders);
            mRanking = v.findViewById(R.id.item_results_ranking);
            mName = v.findViewById(R.id.item_results_name);
            mSum = v.findViewById(R.id.item_results_sum);

        }
    }
}

class boulderResult extends RelativeLayout {

    public boulderResult(Context context) {
        super(context, null, R.layout.boulder_result);
        init();
    }

    public boulderResult(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.boulder_result);
    }

    private void init() {
        inflate(getContext(), R.layout.boulder_result, this);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
