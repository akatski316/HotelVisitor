package arc.visitor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import arc.visitor.mybookings.MyBookingsFragment;
import arc.visitor.rooms.rooms_fragment.RoomsFragment;

public class MainActivity extends AppCompatActivity{

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    Toolbar toolbar;
    TextView name;
    CoordinatorLayout coordinatorLayout;


    private String TAG = "MainActivity";
    boolean doubleBackToExitPressedOnce = false;

    //..an object that instantiates a userdefined BroadcastReceiver for making sure internet is connected
    Internet_BroadcastReceiver internetBroadcastReceiver = new Internet_BroadcastReceiver() {
        @Override
        public Snackbar showSnackBar() {
            return Snackbar.make(coordinatorLayout,"You are not connected",Snackbar.LENGTH_INDEFINITE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if(HotelApp.themeDark)
        {
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator);

        mOnNavigationItemSelectedListener= item -> {
            switch (item.getItemId()) {

                case R.id.navigation_events:
                    Log.d("oncreate","replacing events");
                    setFragment(new RoomsFragment());
                    name = toolbar.findViewById(R.id.fragment_name);
                    name.setText("Rooms");
                    return true;

                case R.id.navigation_booking:
                    setFragment(new MyBookingsFragment());
                    name = toolbar.findViewById(R.id.fragment_name);
                    name.setText("Bookings");
                    return true;
            }

            return false;
        };

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState == null) {
            setFragment(new RoomsFragment());
            navigation.setSelectedItemId(R.id.navigation_events);
            name = toolbar.findViewById(R.id.fragment_name);
            name.setText("Rooms");
        }
    }

    public void setFragment(Fragment frag)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,frag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetBroadcastReceiver, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //..whenever activity is not in front it unregisters our broadcastreceiver
        unregisterReceiver(internetBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.action_status:
                Toast.makeText(this, "Home Status Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "refreshing notice", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

}
