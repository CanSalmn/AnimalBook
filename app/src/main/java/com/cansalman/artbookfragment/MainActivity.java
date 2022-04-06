package com.cansalman.artbookfragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cansalman.artbookfragment.Alert.AlertWorkManager;
import com.cansalman.artbookfragment.databinding.ActivityMainBinding;
import com.cansalman.artbookfragment.databinding.FragmentMainBinding;
import com.cansalman.artbookfragment.fragment.AddDataFragmentDirections;
import com.cansalman.artbookfragment.fragment.MainFragment;
import com.cansalman.artbookfragment.fragment.MainFragmentDirections;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Notification Channel", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setName("CanTech");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }



    }

    @Override
    protected void onDestroy () {
        super.onDestroy();


        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(AlertWorkManager.class, 15, TimeUnit.SECONDS).build();

        WorkManager.getInstance(this).enqueue(periodicWorkRequest);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        if (item.getItemId() == R.id.add_new) {
                NavDirections action = MainFragmentDirections.actionMainFragmentToAddDataFragment();
            Navigation.findNavController(this, R.id.fragmentContainerView).navigate(action);

        }

        return super.onOptionsItemSelected(item);
    }

}