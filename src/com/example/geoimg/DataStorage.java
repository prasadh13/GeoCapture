//The SQLite database implementation is referred from http://www.vogella.com/tutorials/AndroidSQLite/article.html 

package com.example.geoimg;


import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DataStorage extends SQLiteOpenHelper {
	
	 // Books table name
    private static final String TABLE_BOOKS = "books";

    // Books Table Columns names
   // private static final String KEY_ID = "id";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_AUTHOR = "author";

    private static final String[] COLUMNS = {KEY_LONGITUDE,KEY_LATITUDE,KEY_AUTHOR};
	
	
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "BookDB3";
 
    public DataStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE books ( " +
                "longitude DOUBLE, "+
                "latitude DOUBLE, "+
                "author VARCHAR )";
 
        // create books table
        db.execSQL(CREATE_BOOK_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS books");
 
        // create fresh books table
        this.onCreate(db);
    }
    
    
    public void addBook(DatabaseStorage book){
        //for logging
		Log.d("addBook", book.toString()); 
		
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_LATITUDE, book.getLatitude()); // get title
		values.put(KEY_LONGITUDE, book.getLongitude());
		values.put(KEY_AUTHOR, book.getAuthor()); // get author
		
		// 3. insert
		db.insert(TABLE_BOOKS, // table
		        null, //nullColumnHack
		        values); // key/value -> keys = column names/ values = column values
		
		// 4. close
		db.close(); 
    }
    
    
    
    public DatabaseStorage getBook(String title){
    	 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
     
        // 2. build query
        Cursor cursor = 
                db.query(TABLE_BOOKS, // a. table
                COLUMNS, // b. column names
                " title  = ?", // c. selections 
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
     
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
     
        // 4. build book object
        DatabaseStorage book = new DatabaseStorage();
        book.setLongitude(cursor.getDouble(0));
        book.setLatitude(cursor.getDouble(1));
        book.setAuthor(cursor.getString(2));
     
        //log 
    Log.d("getBook("+title+")", book.toString());
     
        // 5. return book
        return book;
    }
    
    public List<DatabaseStorage> getAllBooks() {
        List<DatabaseStorage> books = new ArrayList<DatabaseStorage>();
  
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_BOOKS;
  
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        // 3. go over each row, build book and add it to list
        DatabaseStorage book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new DatabaseStorage();
                book.setLongitude(cursor.getDouble(0));
                book.setLatitude(cursor.getDouble(1));
                book.setAuthor(cursor.getString(2));
  
                // Add book to books
                books.add(book);
            } while (cursor.moveToNext());
        }
  
        Log.d("getAllBooks()", books.toString());
  
        // return books
        return books;
    }
    
    
 
}