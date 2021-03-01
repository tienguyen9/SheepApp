package utility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sheeptracker.R;
import com.example.sheeptracker.TrackingHistoryActivity;

import java.util.ArrayList;

public class TripRVAdapter extends RecyclerView.Adapter<TripRVAdapter.ViewHolder> {

    Context context;
    ArrayList tripIDs, tripMaps, tripDateTimes;

    public TripRVAdapter(Context context, ArrayList tripIDs, ArrayList tripMaps, ArrayList tripDateTimes) {
        this.context = context;
        this.tripIDs = tripIDs;
        this.tripMaps = tripMaps;
        this.tripDateTimes = tripDateTimes;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_trip, parent, false);
        ViewHolder holder = new ViewHolder(view);
        //context = view.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_tripId.setText(String.valueOf(tripIDs.get(position)));
        holder.tv_tripMap.setText(String.valueOf(tripMaps.get(position)));
        holder.tv_tripDateTime.setText(String.valueOf(tripDateTimes.get(position)));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tripID = (TextView) v.findViewById(R.id.tv_trip_id);
                Intent intent = new Intent(context, TrackingHistoryActivity.class);
                Bundle b = new Bundle();
                b.putInt("tripID", Integer.parseInt(tripID.getText().toString()));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tripId, tv_tripMap, tv_tripDateTime;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tripId = itemView.findViewById(R.id.tv_trip_id);
            tv_tripMap = itemView.findViewById(R.id.tv_trip_map);
            tv_tripDateTime = itemView.findViewById(R.id.tv_trip_datetime);
            parentLayout = itemView.findViewById(R.id.trip_parent_layout);
        }
    }
}
