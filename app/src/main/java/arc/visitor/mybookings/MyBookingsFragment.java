package arc.visitor.mybookings;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import arc.visitor.BookingDetailActivity;
import arc.visitor.R;
import arc.visitor.SqliteRoom.HotelAppViewModel;
import arc.visitor.SqliteRoom.Tables.Booking;

public class MyBookingsFragment extends Fragment implements MyBookingsAdapter.OnEventItemClicked{
    View view;
    RecyclerView recyclerView;
    List<Booking> my_bookings;
    MyBookingsAdapter myBookingsAdapter;

    HotelAppViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_bookings, container, false);
        recyclerView = view.findViewById(R.id.bookings);

        viewModel = ViewModelProviders.of(this).get(HotelAppViewModel.class);

        viewModel.getBookings().observe(this, new Observer<List<Booking>>() {
            @Override
            public void onChanged(List<Booking> bookings) {
                updateRecyclerView(bookings);
            }
        });
        return view;
    }

    void updateRecyclerView(List<Booking> bookings)
    {
        my_bookings = bookings;
        myBookingsAdapter = new MyBookingsAdapter(this,bookings);
        recyclerView.setAdapter(myBookingsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onItemClick(int position, List<Booking> list) {
        Booking toPass = list.get(position);
        Intent intent = new Intent(getContext(), BookingDetailActivity.class);
        intent.putExtra(BookingDetailActivity.HOTELNAME,toPass.getHotelName());
        intent.putExtra(BookingDetailActivity.ROOMID,toPass.getRoomId());
        intent.putExtra(BookingDetailActivity.CHECKIN,toPass.getCheckIn());
        intent.putExtra(BookingDetailActivity.CHECKOUT,toPass.getCheckOut());
        intent.putExtra(BookingDetailActivity.GUESTS,toPass.getNo_of_guests());
        getContext().startActivity(intent);
    }
}
