package arc.visitor.SqliteRoom;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import arc.visitor.SqliteRoom.Tables.Booking;

/** Part of room persistence library that creates database of flowing annotated tables
 * Youtube channel named "Coding in flow" is reference as tutorial*/

@Database(entities = {Booking.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase
{
    public static AppDatabase instance;

    public abstract MyDAO myDAO();

    public static synchronized AppDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,"hotel_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback()
    {
        //..this method is called whenever database created for the first time
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

}
