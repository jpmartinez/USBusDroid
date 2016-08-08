package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.List;

import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.ListViewAdapter;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.BookingShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MyBookingsSwipeActivity extends Activity {

    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext = this;
    private JSONArray myBookings;
    private List<BookingShort> bookingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_swipe);
        mListView = (ListView) findViewById(R.id.listview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("ListView");
            }
        }

        Intent father = getIntent();
        try {
            myBookings = new JSONArray(father.getStringExtra("myBookings"));
            bookingsList = BookingShort.fromJson(myBookings);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        mAdapter = new ListViewAdapter(this, bookingsList);
        mListView.setAdapter(mAdapter);
        mAdapter.setMode(Attributes.Mode.Single);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_listview) {
//            startActivity(new Intent(this, MyBookingsSwipeActivity.class));
//            finish();
//            return true;
//        } else if (id == R.id.action_gridview) {
//            startActivity(new Intent(this, GridViewExample.class));
//            finish();
//            return true;
//        } else if (id == R.id.action_recycler) {
//            startActivity(new Intent(this, RecyclerViewExample.class));
//            finish();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}