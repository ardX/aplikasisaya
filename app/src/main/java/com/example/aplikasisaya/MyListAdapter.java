package com.example.aplikasisaya;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private ArrayList<MyListData> listdata = new ArrayList<>();

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<MyListData> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyListData myListData = listdata.get(position);
        holder.tanggaljam.setText(myListData.getTanggal() + " " + myListData.getJam());
        holder.lintangbujur.setText(myListData.getLintang() + " " + myListData.getBujur());
        holder.magnitudekedalaman.setText(myListData.getMagnitude() + " " + myListData.getKedalaman());
        holder.wilayah.setText(myListData.getWilayah());
        Picasso.get().load(myListData.getImgId() == 1 ? "https://image.flaticon.com/icons/png/512/1684/1684394.png" : "https://image.flaticon.com/icons/png/512/1684/1684343.png").into(holder.imageView);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GempaActivity.class);
                intent.putExtra("tanggaljam", myListData.getTanggal() + " " + myListData.getJam());
                intent.putExtra("lintangbujur", myListData.getLintang() + " " + myListData.getBujur());
                intent.putExtra("magnitudekedalaman", myListData.getMagnitude() + " " + myListData.getKedalaman());
                intent.putExtra("wilayah", myListData.getWilayah());
                intent.putExtra("url", myListData.getImgId() == 1 ? "https://image.flaticon.com/icons/png/512/1684/1684394.png" : "https://image.flaticon.com/icons/png/512/1684/1684343.png");
                view.getContext().startActivity(intent);

                Toast.makeText(view.getContext(), "click on item: " + myListData.getTanggal() + " " + myListData.getJam() + " " + myListData.getWilayah(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tanggaljam;
        public TextView lintangbujur;
        public TextView magnitudekedalaman;
        public TextView wilayah;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.tanggaljam = (TextView) itemView.findViewById(R.id.tanggaljam);
            this.lintangbujur = (TextView) itemView.findViewById(R.id.lintangbujur);
            this.magnitudekedalaman = (TextView) itemView.findViewById(R.id.magnitudekedalaman);
            this.wilayah = (TextView) itemView.findViewById(R.id.wilayah);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }
    }
}