package comp3350.srsys.presentation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swapper.R;

import java.util.List;

import comp3350.srsys.objects.Listing;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.myViewHolder> {

    Context context;
    List <Listing> listings;
    Intent intent;
    ImageView image;
    String category;
    private IRecyclerViewClickListener clickListener;

    public RecyclerViewAdapter(Context context, List<Listing> listings, IRecyclerViewClickListener clickListener)
    {
        this.context = context;
        this.listings = listings;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listing_layout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.title.setText(listings.get(position).getTitle());
        holder.description.setText(listings.get(position).getDescription());
        category = listings.get(position).getCategory();
        ImageSetter setter = new ImageSetter(image,category);
        setter.setImage();

    }

    @Override
    public int getItemCount() {
        return listings.size();
    }


    public interface IRecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, description;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
            image = (ImageView) itemView.findViewById(R.id.itemPhoto);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }
    }

}
