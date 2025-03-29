package com.pizzastudio.centerpoint;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pizzastudio.centerpoint.db.model.Meet;
import java.util.List;

public class MeetAdapter extends RecyclerView.Adapter<MeetAdapter.MyViewHolder>  {

    private static final String TAG = MeetAdapter.class.getCanonicalName();
    private Context context;
    private List<Meet> meetList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView dot;
        public TextView date;

        public TextView centerpoint_name;
        public TextView participant_count;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            dot = view.findViewById(R.id.dot);
            date = view.findViewById(R.id.date);

            centerpoint_name = view.findViewById(R.id.centerpoint_name);
            participant_count = view.findViewById(R.id.participant_count);
        }
    }


    public MeetAdapter(Context context, List<Meet> meetList) {
        this.context = context;
        this.meetList = meetList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_meet_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Meet item = meetList.get(position);

        holder.name.setText(item.getName());
        holder.date.setText(item.getDate());
        holder.centerpoint_name.setText(item.getCenterpoint_name());
        Log.d(TAG, item.getName() + " / " + item.getDate() + " / " + item.getCenterpoint_name() + " / "  + item.getParticipant_count());
        holder.participant_count.setText( "     " + item.getParticipant_count() + "명");
        Log.d(TAG, "holder.participant_count text is " + holder.participant_count.getText());
        // Displaying dot from HTML character code
//        holder.dot.setText(Html.fromHtml("&#8226;"));

    }

    @Override
    public int getItemCount() {
        return meetList.size();
    }



}
