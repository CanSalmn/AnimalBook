package com.cansalman.artbookfragment.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cansalman.artbookfragment.R;
import com.cansalman.artbookfragment.dataBase.ArtDao;
import com.cansalman.artbookfragment.dataBase.ArtDataBase;
import com.cansalman.artbookfragment.databinding.FragmentDetails2Binding;
import com.cansalman.artbookfragment.model.Art;

import java.util.Base64;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DetailsFragment extends Fragment {
    private FragmentDetails2Binding binding;
    ArtDataBase artDataBase;
    ArtDao artDao;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    int id ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artDataBase = Room.databaseBuilder(requireContext(),ArtDataBase.class,"Art").build();
        artDao= artDataBase.artDao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentDetails2Binding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null){

            String name=DetailsFragmentArgs.fromBundle(getArguments()).getName();
            String painter = DetailsFragmentArgs.fromBundle(getArguments()).getPainter();
            String year = DetailsFragmentArgs.fromBundle(getArguments()).getYear();
            String image = DetailsFragmentArgs.fromBundle(getArguments()).getImage();
            id= DetailsFragmentArgs.fromBundle(getArguments()).getId();
            byte[] bytesImage= Base64.getDecoder().decode(image);
            Bitmap imageToBitmap = BitmapFactory.decodeByteArray(bytesImage,0,bytesImage.length);
            binding.textView.setText(name);
            binding.textView2.setText(painter);
            binding.textView3.setText(year);
            binding.imageView2.setImageBitmap(imageToBitmap);
        }

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(v);
            }
        });

    }
    public  void delete(View view ){
            compositeDisposable.add(artDao.delete(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(DetailsFragment.this::handleResponse)
            );


    }


    public void handleResponse(){
        NavDirections action =   DetailsFragmentDirections.actionDetailsFragmentToMainFragment();
        Navigation.findNavController(requireView()).navigate(action);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}