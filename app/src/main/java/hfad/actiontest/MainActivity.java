package hfad.actiontest;

import android.app.ListActivity;
import android.content.ContentValues;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor allCursor;
    public static final String TAG = "TIME";
    public ListView listActs;
    public ArrayList<String> actionList;
    public int old_position;
    public int time_add_counter = 0;
    private ArrayAdapter<String> arrayAdapter;

    public static final int RADIO_ID_FORWARD = 2131492947;
    public static final int RADIO_ID_LEFT = 2131492948;
    public static final int RADIO_ID_RIGHT = 2131492949;
    public static final int RADIO_ID_BACK = 2131492950;

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
            final int pos = 0;

            // set the cursor to the beginning of the iterator
            allCursor.moveToFirst();
            actionList = new ArrayList<String>();

            // add everything to the actionList to be edited
            while (!allCursor.isLast()) {
                actionList.add(allCursor.getString(1));
                allCursor.moveToNext();
            }
            actionList.add(allCursor.getString(1));
            old_position = actionList.size();
            // got every record from db

            /* deprecate for now
            CursorAdapter favoriteAdapter =
                    new SimpleCursorAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            allCursor,
                            new String[]{"NAME"},
                            new int[]{android.R.id.text1}, 0);
                            */
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, actionList);
            listActs.setAdapter(arrayAdapter);
            listActs.setLongClickable(true);
            listActs.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            listActs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    Toast toast = Toast.makeText(MainActivity.this, String.format("The %s is removed", actionList.get(pos)), Toast.LENGTH_SHORT);
                    toast.show();
                    actionList.remove(pos);
                    arrayAdapter.notifyDataSetChanged();
                    old_position = pos;
                    return true;
                }
            });


            listActs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                    Log.d(TAG, String.format("old_position = %d pos = %d", old_position, pos));
                    Log.d(TAG, String.format("Checked = %d", listActs.getSelectedItemPosition()));

                    if (old_position == pos) {
                        listActs.setItemChecked(pos, false);
                        //use for moving old_position to unreal pos allowing toggle
                        old_position = actionList.size();
                    } else {
                        listActs.setItemChecked(pos, true);
                        old_position = pos;
                    }

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


    public void AddButtonOnClick(View view) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        int id = rg.getCheckedRadioButtonId();
        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);

        StringBuilder commandBuilder = new StringBuilder();

        switch (id){
            case RADIO_ID_FORWARD:
                commandBuilder.append("FORWARD");
                break;
            case RADIO_ID_LEFT:
                commandBuilder.append("LEFT");
                break;
            case RADIO_ID_RIGHT:
                commandBuilder.append("RIGHT");
                break;
            case RADIO_ID_BACK:
                commandBuilder.append("BACK");
                break;
            default:
                Toast toast = Toast.makeText(MainActivity.this, "Please select the direction", Toast.LENGTH_SHORT);
                toast.show();
                return;
        }
        commandBuilder.append(" ");
        commandBuilder.append(np.getValue() + "m");

        if (old_position != actionList.size()) {
            actionList.add(old_position + 1, commandBuilder.toString());
        } else {
            actionList.add(old_position, commandBuilder.toString());
        }

        arrayAdapter.notifyDataSetChanged();
        Toast toast = Toast.makeText(MainActivity.this, commandBuilder.toString(), Toast.LENGTH_SHORT);
        toast.show();

        old_position++;
        time_add_counter++;
    }

    public void SaveButtonOnClick(View view) {
        SQLiteOpenHelper actDatabaseHelper = new ActDatabaseHelper(this);
        db = actDatabaseHelper.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS "+ActDatabaseHelper.dbName);

        String str = String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, ", ActDatabaseHelper.dbName);
        db.execSQL(str + "NAME TEXT);");

        ContentValues actValues = new ContentValues();
        for (String eachCommand : actionList) {
            actValues.put("NAME", eachCommand);
            db.insert(ActDatabaseHelper.dbName, null, actValues);
        }
    }

}
