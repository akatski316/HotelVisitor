package arc.visitor.rooms.rooms_fragment;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import arc.visitor.R;
import arc.visitor.SqliteRoom.HotelAppViewModel;

import static arc.visitor.HotelApp.checkIn;
import static arc.visitor.HotelApp.checkOut;
import static arc.visitor.HotelApp.hotelId;
import static arc.visitor.HotelApp.hotelName;


public class RoomsFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    View view;
    TextView textView;
    HotelAppViewModel viewModel;

    public static boolean updatingUi = false;

    public static List<Room> roomList = new ArrayList<>();


    private static final String TAG = "RoomsFragment";

    private SwipeRefreshLayout swipeRefreshLayout;

    GridView gridView;
    DatePickerDialog datePickerDialog;
    RoomCardViewAdapter roomCardViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView(): ");
        view = inflater.inflate(R.layout.fragment_rooms, container, false);
        setHasOptionsMenu(true);
        swipeRefreshLayout = view.findViewById(R.id.swipetorefresh);
        textView = view.findViewById(R.id.hotelName);
        viewModel = ViewModelProviders.of(this).get(HotelAppViewModel.class);
        updatingUi = false;
        gridView = view.findViewById(R.id.grid);

        textView.setText(hotelName);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoadingRoomData();
            }
        });


        return view;
    }

    private void startLoadingRoomData()
    {
        swipeRefreshLayout.setRefreshing(true);
        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"visitor/getRooms")
                .addBodyParameter("startDate",checkIn)
                .addBodyParameter("endDate",checkOut)
                .addBodyParameter("hotelId",hotelId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: "+response);
                        swipeRefreshLayout.setRefreshing(false);
                        roomList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                Room room = Room.JSONtoRoomObject(response.getJSONObject(i));
                                roomList.add(room);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        roomCardViewAdapter = new RoomCardViewAdapter(roomList,getContext(),getActivity());
                        gridView.setAdapter(roomCardViewAdapter);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_rooms, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.action_filter:
                checkIn = null;
                checkOut = null;
                Toast.makeText(getContext(), "pick checkIn Time", Toast.LENGTH_SHORT).show();
                datePickerDialog = new DatePickerDialog(getContext(),this
                        , Calendar.getInstance().get(Calendar.YEAR)
                        ,Calendar.getInstance().get(Calendar.MONTH)
                        ,Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(checkIn == null)
        {
            checkIn = year+"-"+month+"-"+dayOfMonth;
            Toast.makeText(getContext(), "pick checkOut Time", Toast.LENGTH_SHORT).show();
            datePickerDialog = new DatePickerDialog(getContext(),this
                    , Calendar.getInstance().get(Calendar.YEAR)
                    ,Calendar.getInstance().get(Calendar.MONTH)
                    ,Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        }
        else
        {
            checkOut = year+"-"+month+"-"+dayOfMonth;
            startLoadingRoomData();
        }
    }
}
