package utility;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sheeptracker.DatabaseHelper;
import com.example.sheeptracker.OfflineMapActivity;
import com.example.sheeptracker.R;

import java.util.ArrayList;

public class MapRVAdapter extends RecyclerView.Adapter<MapRVAdapter.ViewHolder>{

    private ArrayList<String> folderList = new ArrayList<>();
    private Context mContext;
    double NW_lat, NW_lon, SE_lat, SE_lon;


    public MapRVAdapter(ArrayList<String> folderList, Context mContext) {
        this.folderList = folderList;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_map, parent, false);
        ViewHolder holder = new ViewHolder(view);
        mContext = view.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.folder.setText(folderList.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView folder = (TextView) v.findViewById(R.id.tv_item);
                Intent intent = new Intent(mContext, OfflineMapActivity.class);
                fillMapLatLngs(folder.getText().toString());
                Bundle b = new Bundle();
                b.putString("folder", folder.getText().toString());
                b.putDouble("NW_lat", NW_lat);
                b.putDouble("NW_lon", NW_lon);
                b.putDouble("SE_lat", SE_lat);
                b.putDouble("SE_lon", SE_lon);
                intent.putExtras(b);
                mContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView folder;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            folder = itemView.findViewById(R.id.tv_item);
            parentLayout = itemView.findViewById(R.id.map_parent_layout);
        }
    }

    private void fillMapLatLngs(String folder) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        int mapID = databaseHelper.getMapID(folder);
        Cursor c = databaseHelper.readMapData(mapID);
        if (c.getCount() == 0 ) {
            Toast.makeText(mContext, "No map registered", Toast.LENGTH_SHORT).show();
        } else {
            while (c.moveToNext()) {
                NW_lat = c.getDouble(2);
                NW_lon = c.getDouble(3);
                SE_lat = c.getDouble(4);
                SE_lon = c.getDouble(5);
            }
        }
    }
}
