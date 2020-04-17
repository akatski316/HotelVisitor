package arc.visitor.SqliteRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import arc.visitor.SqliteRoom.Tables.Booking;

@Dao
public interface MyDAO
{
    @Query("select * from booking")
    LiveData<List<Booking>> getAllBookings();

    @Insert
    void insertBooking(Booking booking);
}
