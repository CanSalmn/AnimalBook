package com.cansalman.artbookfragment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cansalman.artbookfragment.R;
import com.cansalman.artbookfragment.adapter.ArtAdapter;
import com.cansalman.artbookfragment.dataBase.ArtDao;
import com.cansalman.artbookfragment.dataBase.ArtDataBase;
import com.cansalman.artbookfragment.databinding.FragmentAddDataBinding;
import com.cansalman.artbookfragment.databinding.FragmentMainBinding;
import com.cansalman.artbookfragment.model.Art;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    ArtDataBase artDataBase;
    ArtDao artDao;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    ArtAdapter artAdapter ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artDataBase= Room.databaseBuilder(requireContext(),ArtDataBase.class,"Art").build();
        artDao=artDataBase.artDao();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentMainBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        getData();



    }
    public  void getData(){

        compositeDisposable.add(artDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MainFragment.this::handleResponse)
        );
    }

    private  void handleResponse(List<Art> artList){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        artAdapter = new ArtAdapter(artList,getActivity());
        binding.recyclerView.setAdapter(artAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}