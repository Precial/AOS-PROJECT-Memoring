package kr.sg.memorning.data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Memo.class}, version = 5, exportSchema = true)
public abstract class MemoRoomDatabase extends RoomDatabase {
    public abstract MemoDao memoDao();

    private static volatile MemoRoomDatabase INSTANCE;

    static MemoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MemoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MemoRoomDatabase.class, "memo_database").build();
                }
            }
        }
        return INSTANCE;
    }
}