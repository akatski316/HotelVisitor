package arc.visitor.SqliteRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import arc.visitor.SqliteRoom.Tables.Booking;

public class HotelAppViewModel extends AndroidViewModel {


    private LiveData<List<Booking>> bookings;

    private BookingsRepository bookingsRepository;

    //..this constructor is used to instantiate our ViewModel object
    public HotelAppViewModel(@NonNull Application application) {
        super(application);
        bookingsRepository = new BookingsRepository(application);
        bookings = bookingsRepository.getAllBookings();
    }

    public LiveData<List<Booking>> getBookings()
    {
        return bookings;
    }

    public void insertBooking(Booking boooking){bookingsRepository.Insert(boooking);}

}
