package com.bartovapps.simplewebserv;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import adapters.FlickerRecyclerAdapter;
import decorations.DividerItemDecoration;
import parsers.FlickerJSONParser;
import utils.ConnectionManager;
import utils.Utils;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MAIN_ACTIVITY_REQ_CODE = 100;

    EditText etSearch;
    RecyclerView rvImagesRecyclerView;
    ArrayList<Uri> flickerImages;
    FlickerRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flickerImages = new ArrayList<>();

        initUiComponents();
    }


    private void initUiComponents() {
        etSearch = (EditText)findViewById(R.id.etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchInput = etSearch.getText().toString();

                if (actionId == EditorInfo.IME_ACTION_SEARCH ) {
                    if (!searchInput.equals("") ) {

                        Log.i(TAG, "Search action triggered for: " + searchInput);
//                        WebServiceGetTask webServiceGetTask = new WebServiceGetTask();
//                        webServiceGetTask.execute(searchInput);
                        getDataWithVolly(Utils.buildRestQueryString(searchInput));

                    }
                }
                    return false;
            }
        });

        rvImagesRecyclerView = (RecyclerView) findViewById(R.id.flickerRecyclerView);
        recyclerAdapter = new FlickerRecyclerAdapter(this, flickerImages);
        rvImagesRecyclerView.setAdapter(recyclerAdapter);
        rvImagesRecyclerView.setHasFixedSize(false);
        rvImagesRecyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.divider_drawable)));
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (3 - position % 3);
            }
        });

        rvImagesRecyclerView.setLayoutManager(manager);
        rvImagesRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, rvImagesRecyclerView, new ClickListener() {
            @Override
            public void onClick(View v, int position) {
                   Toast.makeText(MainActivity.this, "RecyclerView item " + position + " clicked..", Toast.LENGTH_SHORT).show();
                   Uri uri = flickerImages.get(position);
                   Log.i(TAG, "Clicked image uri: " + uri.toString());
                   Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
                   intent.setData(uri);
                   startActivityForResult(intent, MAIN_ACTIVITY_REQ_CODE);
                }

            @Override
            public void onLongClick(View v, int position) {
                //Longclick is handled by the onLongPress of the RecyclerTouchListener down in this activity..
            }
        }));
    }

    private void refreshDisplay(ArrayList<Uri> flickerImages ){
        Log.i(TAG, "About to refresh display with " + flickerImages.size() + " items");
        recyclerAdapter.updateList(flickerImages);
    }



    class WebServiceGetTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... params) {
            Uri uri = Utils.buildRestQueryString(params[0]);
            Log.i(TAG, "Query string: " + uri.toString());

            String queryResponse = ConnectionManager.getData(uri);
            return queryResponse;
        }

        @Override
        protected void onPostExecute(String s) {
      //      Log.i(TAG, "Rest query response" + s);

            s = s.substring(s.indexOf("(") + 1);
            s = s.substring(0, s.length()-1);

            Log.i(TAG, "temp: " + s);

            ArrayList<Uri> images = FlickerJSONParser.parseFeed(s);
            if(images != null){
                Log.i(TAG, "Parsed " + images.size() + " elements");
                refreshDisplay(images);
            }
        }
    }




    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private final String LOG_TAG = RecyclerTouchListener.class.getSimpleName();
        GestureDetector gestureDetector;
        ClickListener clickListener;

        public RecyclerTouchListener(final Activity context, final RecyclerView recyclerView, final ClickListener clickListener) {
            Log.i(LOG_TAG, "constructor was invoked");
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
//                    Log.i(TAG, "onSingleTapUp was invoked..: " + e);
//                    return super.onSingleTapUp(e);
                    return true;
                }


                @Override
                public void onLongPress(MotionEvent e) {
                    Log.i(LOG_TAG, "onLongPress was invoked..: " + e);
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    super.onLongPress(e);
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//            Log.i(TAG, "onInterceptTouchEvent was called: " + gestureDetector.onTouchEvent(e));
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e) == true) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.i(LOG_TAG, "onTouchEvent was called: " + e);

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void getDataWithVolly( Uri url){
        Log.i(TAG, "About the get data with Volley");
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Volley onResponse: " + response);
                        response = Utils.getJsonContentFromFlickerRespose(response);
                        // Display the first 500 characters of the response string.
                        ArrayList<Uri> images = FlickerJSONParser.parseFeed(response);
                        refreshDisplay(images);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Volley onErrorResponse: " + error);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static interface ClickListener {
        public void onClick(View v, int position);

        public void onLongClick(View v, int position);
    }
}
