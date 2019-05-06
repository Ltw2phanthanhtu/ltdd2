package net.pmsv.diadiemcaobang.DAL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by may38 on 5/30/2017.
 */

public class SQLiteDataAccessHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "DLCaoBang.sqlite";
    public static final String DBLOCATION = "/data/data/net.pmsv.diadiemcaobang/databases/";//Nơi chứa CSDL trong điện thoại
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public SQLiteDataAccessHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;

        boolean db_exist = checkdatabase();
        if (db_exist) {
            Log.d("asd db","db exist: " + db_exist );
            openDatabase();
        } else {
            Log.d("asd db","db exist: " + db_exist );
            try {
                createDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createDatabase() throws IOException {
        boolean db_exist = checkdatabase();
        if (db_exist) {
            Log.d("check when create db: ", "DB exist");
        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyDatabase() throws IOException {
        InputStream inputStream = mContext.getAssets().open(DBNAME);
        String outFile = DBLOCATION + DBNAME;
        OutputStream outputStream = new FileOutputStream(outFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private boolean checkdatabase() {
        boolean check_db = false;
        try {
            String myPath = DBLOCATION + DBNAME;
            File db_file = new File(myPath);
            check_db = db_file.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return check_db;
    }


    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    //truy vấn không trả kết quà: CREATE, INSERT, UPDATE, DELETE...
    public void queryData(String sql) {
//        openDatabase();
        mDatabase.execSQL(sql);
    }

    //truy vấn có trả kết qua: SELECT.
    public Cursor getData(String sql) {
//        openDatabase();
        mDatabase = getReadableDatabase();
        Cursor cursor = mDatabase.rawQuery(sql, null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
