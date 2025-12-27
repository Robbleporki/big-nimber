package com.example.financemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.financemanager.models.Transaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "finance.db";
    private static final int DATABASE_VERSION = 1;

    // جدول تراکنش‌ها
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY = "category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_DATE + " INTEGER,"
                + COLUMN_CATEGORY + " TEXT"
                + ")";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // افزودن تراکنش جدید
    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, transaction.getTitle());
        values.put(COLUMN_DESCRIPTION, transaction.getDescription());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_TYPE, transaction.getType());
        values.put(COLUMN_DATE, transaction.getDate().getTime());
        values.put(COLUMN_CATEGORY, transaction.getCategory());

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return id;
    }

    // دریافت تمام تراکنش‌ها
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                transaction.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                transaction.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                transaction.setDate(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))));
                transaction.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));

                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    // دریافت تراکنش‌ها بر اساس نوع
    public List<Transaction> getTransactionsByType(String type) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_TRANSACTIONS, 
                null, 
                COLUMN_TYPE + " = ?", 
                new String[]{type}, 
                null, null, 
                COLUMN_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                transaction.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                transaction.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                transaction.setDate(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))));
                transaction.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));

                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    // جستجو در تراکنش‌ها
    public List<Transaction> searchTransactions(String query) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selection = COLUMN_TITLE + " LIKE ? OR " + COLUMN_DESCRIPTION + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};
        
        Cursor cursor = db.query(TABLE_TRANSACTIONS, 
                null, 
                selection, 
                selectionArgs, 
                null, null, 
                COLUMN_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                transaction.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                transaction.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                transaction.setDate(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))));
                transaction.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));

                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    // دریافت مجموع درآمد و هزینه در یک ماه خاص
    public double getMonthlyTotal(String type, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;
        
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COLUMN_TYPE + " = ? AND " +
                "strftime('%m', " + COLUMN_DATE + "/1000, 'unixepoch') = ? AND " +
                "strftime('%Y', " + COLUMN_DATE + "/1000, 'unixepoch') = ?";
        
        Cursor cursor = db.rawQuery(query, 
                new String[]{type, 
                            String.format("%02d", month), 
                            String.valueOf(year)});
        
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }
}
