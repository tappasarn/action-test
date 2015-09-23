package hfad.actiontest;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ActDatabaseHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION =5;

    private Context context;
    public static String dbName;

    ActDatabaseHelper(Context context) {
        super(context, context.getResources().getString(R.string.database_name), null, DB_VERSION);
        dbName = context.getResources().getString(R.string.database_name);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    /*
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+dbName);
        onCreate(db);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String str = String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, ", dbName);
            db.execSQL(str + "NAME TEXT);");
            insertDrink(db, "act001");
            insertDrink(db, "act002");
            insertDrink(db, "act003");
            insertDrink(db, "act004");
            insertDrink(db, "act005");
            insertDrink(db, "act006");
            insertDrink(db, "act007");
            insertDrink(db, "act008");
            insertDrink(db, "act009");
        }
    }


    public static void insertDrink(SQLiteDatabase db, String name) {
        ContentValues actValues = new ContentValues();
        actValues.put("NAME", name);
        db.insert(dbName, null, actValues);
    }
}
