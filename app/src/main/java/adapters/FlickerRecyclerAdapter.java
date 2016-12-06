package adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bartovapps.simplewebserv.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by BartovMoti on 11/08/15.
 */
public class FlickerRecyclerAdapter extends RecyclerView.Adapter<FlickerRecyclerAdapter.RecyclerViewHolder> {

    private static final String LOG_TAG = FlickerRecyclerAdapter.class.getSimpleName();
    LayoutInflater inflater;
    ArrayList<Uri> data = new ArrayList<>();
    Activity context;

    public FlickerRecyclerAdapter(Activity context, ArrayList<Uri> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_view_item, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Log.i(LOG_TAG, "onBindViewHolder was called");

        Picasso.with(context).load(data.get(position)).fit().centerCrop().into(holder.flickerImage);
      //  holder.itemView.setActivated(selectedItems.get(position, false));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView flickerImage;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            flickerImage = (ImageView) itemView.findViewById(R.id.ivListItemImage);
            //drawerRowIcon.setOnClickListener(this);

        }

    }


    public void updateList(ArrayList<Uri> data){
        this.data.clear();
        this.data.addAll(data);
        Log.i(LOG_TAG, "data size: " + this.data.size());
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }


}
