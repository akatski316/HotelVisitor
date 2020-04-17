package arc.visitor.rooms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import arc.visitor.BookingDetailActivity;
import arc.visitor.SqliteRoom.HotelAppViewModel;
import arc.visitor.HotelApp;
import arc.visitor.R;
import arc.visitor.SqliteRoom.Tables.Booking;

import static arc.visitor.HotelApp.checkIn;
import static arc.visitor.HotelApp.checkOut;
import static arc.visitor.HotelApp.user_email;

public class Room_Detail_Activity extends AppCompatActivity {

    TextView room_no_textview, booking_status_textview, room_type_textview, capacity_textview;
    Button booking_details_btn;
    String roomNumber, roomType, capacity, bookingId,roomId;
    boolean isBooked;
    Intent intent;

    private static final String TAG = "Room_Detail_Activity";
    public static final String ROOMNUMBER = "ROOMNUMBER", ROOMTYPE = "ROOMTYPE", CAPACITY = "CAPACITY", ISBOOKED = "ISBOOKED", BOOKINGID = "BOOKINGID",ROOMID = "ROOMID";

    HotelAppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (HotelApp.themeDark) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

        Log.d(TAG, "onCreate: " + TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        room_no_textview = findViewById(R.id.room_no);
        booking_status_textview = findViewById(R.id.booking_status);
        room_type_textview = findViewById(R.id.roomType);
        capacity_textview = findViewById(R.id.capacity);
        booking_details_btn = findViewById(R.id.details_btn);

        intent = getIntent();
        roomNumber = intent.getStringExtra(ROOMNUMBER);
        roomType = intent.getStringExtra(ROOMTYPE);
        capacity = intent.getStringExtra(CAPACITY);
        bookingId = intent.getStringExtra(BOOKINGID);
        isBooked = intent.getBooleanExtra(ISBOOKED, false);
        roomId = intent.getStringExtra(ROOMID);

        room_no_textview.setText(roomNumber);
        room_type_textview.setText(roomType);

        booking_status_textview.setTextColor(Color.GREEN);
        booking_status_textview.setText("AVAILABLE");
        booking_details_btn.setVisibility(View.GONE);

        capacity_textview.setText(capacity);

        viewModel = ViewModelProviders.of(this).get(HotelAppViewModel.class);
    }

    public void close(View v) {
        finish();
    }

    public void goToBookingDetailsActivity(View view) {
        intent = new Intent(this, BookingDetailActivity.class);
        startActivity(intent);
    }

    public void bookRoom(View view) {
        view.setBackgroundColor(Color.WHITE);
        ((Button) view).setText("Booked");
        ((Button) view).setTextColor(getResources().getColor(R.color.notice_green));
        view.setEnabled(false);

        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"visitor/bookRoom")
                .addBodyParameter("email", user_email)
                .addBodyParameter("roomId",roomId)
                .addBodyParameter("no_guest",capacity)
                .addBodyParameter("visitorName",HotelApp.name)
                .addBodyParameter("checkIn",checkIn)
                .addBodyParameter("checkOut",checkOut)
                .setTag("signin")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Booking booking = null;
                        try {
                            booking = Booking.parse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        viewModel.insertBooking(booking);
                        Log.d(TAG, "onResponse: "+response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });
    }

}
