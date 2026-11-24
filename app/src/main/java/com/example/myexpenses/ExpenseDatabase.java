package com.example.myexpenses;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Category.class,Expense.class}, version = 1, exportSchema = false)
    public abstract class ExpenseDatabase extends RoomDatabase {

        public abstract ExpenseDao expenseDao();
        public abstract CategoryDao categoryDao();

        private static volatile ExpenseDatabase INSTANCE;
        private static final int NUMBER_OF_THREADS = 4;
        static final ExecutorService databaseWriteExecutor =
                Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        public static ExpenseDatabase getDatabase(final Context context) {
            if (INSTANCE == null) {
                synchronized (ExpenseDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        ExpenseDatabase.class, "expenses.db")
                                .build();
                    }
                }
            }
            return INSTANCE;
        }

}
