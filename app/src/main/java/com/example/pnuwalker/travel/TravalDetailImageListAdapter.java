package com.example.pnuwalker.travel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pnuwalker.R;

import java.util.ArrayList;

public class TravalDetailImageListAdapter extends RecyclerView.Adapter<TravalDetailImageListAdapter.Holder> {
    ArrayList<TravelDetailImageListItem> data = new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.detail_image_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.imageView.setImageResource(data.get(position).getImageId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(TravelDetailImageListItem d) {
        data.add(d);
    }
    public void clear() { data.clear(); }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.detail_image_item_view);
            itemView.setOnClickListener(v -> {
                int index = getAdapterPosition() ;
                System.out.println(index);
                if (index != RecyclerView.NO_POSITION) {
                    int imageId = data.get(index).getImageId();
                    Intent intent = new Intent(itemView.getContext(), TravelDetailLargeImageActivity.class);
                    intent.putExtra("image_id", imageId);
                    ContextCompat.startActivity(itemView.getContext(), intent, null);
                }
            });


        }
    }

}

