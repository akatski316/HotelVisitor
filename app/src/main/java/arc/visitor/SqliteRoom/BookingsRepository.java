package arc.visitor.SqliteRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

import arc.visitor.SqliteRoom.Tables.Booking;

public class BookingsRepository {

    private MyDAO myDAO;
    private LiveData<List<Booking>> allbookings;

    private static final String TAG = "UserRepository";
    private Application application;

    public BookingsRepository(Application application) {
        AppDatabase appDatabase =  AppDatabase.getInstance(application);
        myDAO = appDatabase.myDAO();
        this.application = application;
        allbookings = myDAO.getAllBookings();
    }

    public LiveData<List<Booking>> getAllBookings() {
        return allbookings;
    }


    public void Insert(Booking booking)
    {
        new InsertAsyncTask(myDAO).execute(booking);
    }

    public class InsertAsyncTask extends AsyncTask<Booking, Void, Void>
    {
        public MyDAO myDAO;

        public InsertAsyncTask(MyDAO myDAO) {
            this.myDAO = myDAO;
        }

        @Override
        protected Void doInBackground(Booking... bookings) {
            myDAO.insertBooking(bookings[0]);
            return null;
        }
    }


}
