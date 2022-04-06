package com.cansalman.artbookfragment.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.cansalman.artbookfragment.R;
import com.cansalman.artbookfragment.dataBase.ArtDao;
import com.cansalman.artbookfragment.dataBase.ArtDataBase;
import com.cansalman.artbookfragment.databinding.FragmentAddDataBinding;
import com.cansalman.artbookfragment.model.Art;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class AddDataFragment extends Fragment {
    private FragmentAddDataBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Bitmap convertedImage;
    ArtDataBase artDataBase;
    ArtDao artDao;
    private CompositeDisposable compositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLauncher();
        artDataBase= Room.databaseBuilder(requireContext(),ArtDataBase.class,"Art").build();
        artDao=artDataBase.artDao();
        compositeDisposable= new CompositeDisposable();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding= FragmentAddDataBinding.inflate(inflater,container,false);
       View view = binding.getRoot();
       return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveButton(v);
            }
        });
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });

    }

    public void saveButton(View view ){


        Bitmap smallerImage =makeSmallerImage(convertedImage,300);
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        smallerImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);


        byte[] byteArray =byteArrayOutputStream.toByteArray();
        String name= binding.nameInput.getText().toString();
        String painter= binding.painterNameInput.getText().toString();
        String year = binding.yearInput.getText().toString();
        Art art = new Art(name,painter,year,byteArray);


        compositeDisposable.add(artDao.insert(art)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(AddDataFragment.this::handleResponse)
        );

    }

    public void selectImage(View view){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                //permission
                Snackbar.make(view,"Permission Needed For gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                //permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else{
            //go to gallery
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }


    }


    public void registerLauncher() {
        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()== RESULT_OK  ){
                    Intent intentFromResult= result.getData();
                    if(intentFromResult !=null){
                        Uri imageUri=intentFromResult.getData();


                        try {
                            if(Build.VERSION.SDK_INT >=28){
                                ImageDecoder.Source source= ImageDecoder.createSource(requireActivity().getContentResolver(),imageUri);
                                convertedImage = ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(convertedImage);
                            }else{
                                convertedImage=MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),imageUri);
                                binding.imageView.setImageBitmap(convertedImage);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }

            }
        });


        permissionLauncher= registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }else{
                    Toast.makeText(getContext(), "Permission Needed For Gallery", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public Bitmap makeSmallerImage(Bitmap image, int maximumSize)
    {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }


    public  void handleResponse(){

        NavDirections action = AddDataFragmentDirections.actionAddDataFragmentToMainFragment();
        Navigation.findNavController(requireView()).navigate(action);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}