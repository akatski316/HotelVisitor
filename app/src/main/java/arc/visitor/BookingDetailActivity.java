package arc.visitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import arc.visitor.rooms.rooms_fragment.Room;

import static arc.visitor.HotelApp.user_email;

public class BookingDetailActivity extends AppCompatActivity {

    public static final String HOTELNAME = "HOTELNAME"
            ,VISITORNAME = "VISITORNAME"
            ,ROOMNUMBER = "ROOMNUMBER"
            ,ROOMID = "ROOMID"
            ,CHECKIN = "CHECKIN"
            ,CHECKOUT = "CHECKOUT"
            ,GUESTS = "GUESTS";
    private static final String TAG = "BookingDetailActivity";
    Intent intent;
    String roomId,roomNumber,hotelName,checkin,checkout,roomType,guests;
    TextView textview_name, textview_hotelname,textview_email,textview_roomType,textview_no_of_guest,textview_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        intent = getIntent();
        roomId = intent.getStringExtra(ROOMID);
        getRoom(roomId);
        checkin = intent.getStringExtra(CHECKIN);
        checkout = intent.getStringExtra(CHECKOUT);
        hotelName = intent.getStringExtra(HOTELNAME);
        guests = intent.getStringExtra(GUESTS);
    }

    void getRoom(String roomId)
    {
        Log.d(TAG, "getRoom: "+roomId);
        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"/getRoom")
                .addBodyParameter("email", user_email)
                .addBodyParameter("roomId",roomId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Room room = Room.JSONtoRoomObject(response);

                            roomNumber = room.getRoomNumber();
                            roomType = room.getRoomType();
                            showOnUI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });
    }

    private void showOnUI() {
        Log.d(TAG, "showOnUI: name = " +HotelApp.name+" "+hotelName);
        textview_email = findViewById(R.id.email);
        textview_hotelname = findViewById(R.id.hotelName);
        textview_name = findViewById(R.id.name);
        textview_no_of_guest = findViewById(R.id.no_of_guest);
        textview_roomType = findViewById(R.id.roomType);
        textview_date = findViewById(R.id.date);

        textview_email.setText(user_email);
        textview_roomType.setText(roomType);
        textview_no_of_guest.setText(guests);
        textview_hotelname.setText(hotelName);
        textview_name.setText(HotelApp.name);
        textview_date.setText("from "+checkin+" to "+checkout);

    }
}
