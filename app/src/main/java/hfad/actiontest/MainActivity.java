package hfad.actiontest;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity{

    private SQLiteDatabase db;
    private Cursor allCursor;
    public static final String TAG = "TIME";
    public ListView listActs;
    public ArrayList actionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listActs = (ListView) findViewById(R.id.list_options);

        // to add item to the list OH WOW
        try {
            SQLiteOpenHelper actDatabaseHelper = new ActDatabaseHelper(this);
            db = actDatabaseHelper.getReadableDatabase();

            // point the cursor to DB with all rows
            allCursor = db.query(ActDatabaseHelper.dbName,
                    new String[]{"_id", "NAME"}, null,
                    null, null, null, null);
            int pos = 0;

            // set the cursor to the beginning of the iterator
            allCursor.moveToFirst();
            actionList = new ArrayList<String>();

            // add everything to the actionList to be edited
            while (!allCursor.isLast()) {
                actionList.add(allCursor.getString(1));
                allCursor.moveToNext();
            }
            actionList.add(allCursor.getString(1));
            // got every record from db

            // Call the fucking Cursor again WOW
            /* deprecate for now
            CursorAdapter favoriteAdapter =
                    new SimpleCursorAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            allCursor,
                            new String[]{"NAME"},
                            new int[]{android.R.id.text1}, 0);
                            */
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,actionList);
            listActs.setAdapter(arrayAdapter);
            listActs.setLongClickable(true);
            listActs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    actionList.remove(pos);
                    arrayAdapter.notifyDataSetChanged();

                    return true;
                }

            });
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMaxValue(10);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);

    }



    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        /*actionList.remove(position);
        Log.d(TAG, "Hello im at " + position);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,actionList);
        listActs.setAdapter(arrayAdapter);*/
        Toast.makeText(this, "long clicked pos: " + position, Toast.LENGTH_LONG).show();

        return true;
    }
}