package com.cansalman.artbookfragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cansalman.artbookfragment.databinding.ArtbookRecyclerRowBinding;

import com.cansalman.artbookfragment.fragment.MainFragmentDirections;
import com.cansalman.artbookfragment.model.Art;

import org.w3c.dom.ls.LSOutput;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.crypto.spec.PSource;

public class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ArtHolder> {
            List<Art> artList;
            Activity myActivity;

    public  ArtAdapter (List<Art> artList,Activity activity ){
        this.artList= artList;
        this.myActivity =activity;

    }

    @NonNull
    @Override
    public ArtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArtbookRecyclerRowBinding binding = ArtbookRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ArtHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull ArtHolder holder, int position) {
        Bitmap image = BitmapFactory.decodeByteArray(artList.get(position).image,0,artList.get(position).image.length);
        String name = artList.get(position).name;
        String painter = artList.get(position).painter;
        String year = artList.get(position).year;
        int id = artList.get(position).id;




        //make bigger image
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = 410;
            height = (int) (width * bitmapRatio);
        } else {
            height = 1500;
            width = (int) (height / bitmapRatio);
        }
        Bitmap newImage = Bitmap.createScaledBitmap(image,width,height,true);

        holder.binding.lineaLayout.setMinimumWidth(newImage.getWidth());
        holder.binding.lineaLayout.setMinimumHeight(newImage.getHeight());
        holder.binding.imageView3.setMinimumWidth(newImage.getWidth());
        holder.binding.imageView3.setMinimumHeight(newImage.getHeight());

        holder.binding.imageView3.setImageBitmap(newImage);
        holder.binding.textView4.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageToString= Base64.getEncoder().encodeToString(artList.get(position).image);
               MainFragmentDirections.ActionMainFragmentToDetailsFragment action =MainFragmentDirections.actionMainFragmentToDetailsFragment(name,painter,year,imageToString,id);
                Navigation.findNavController(v).navigate(action);

            }
        });

    }

    @Override
    public int getItemCount() {
        return artList.size();
    }

    public class ArtHolder extends RecyclerView.ViewHolder{

        ArtbookRecyclerRowBinding binding;

        public ArtHolder(ArtbookRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding= binding;

        }
    }
}
