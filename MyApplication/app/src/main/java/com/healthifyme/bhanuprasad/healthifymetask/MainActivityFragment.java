package com.healthifyme.bhanuprasad.healthifymetask;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    RequestQueue queue;
    ArrayList<SlotsObject> slotsObjects ;
    ProgressBar progressBar;
    TextView tv;
    TextView month;
    ViewPager pager;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        pager = (ViewPager) view.findViewById(R.id.viewpager);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        tv = (TextView) view.findViewById(R.id.tv);
        month = (TextView) view.findViewById(R.id.month);
        queue = Volley.newRequestQueue(getActivity());
        if (isNetworkAvailable())
            getDetailsRequest();
        else
            showSnackBar("No Internet connection");

        return view;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String findMonth(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateString);
//        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
//        String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
//        String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
//        String day = (String) android.text.format.DateFormat.format("dd", date); //20
        return stringMonth;
//        Log.d("DATE",dayOfTheWeek);
//        Calendar calendar = new GregorianCalendar(2008, 01, 01); // Note that Month value is 0-based. e.g., 0 for January.
//        int result = calendar.get(Calendar.DAY_OF_WEEK);
//        String day = null;
//        switch (result) {
//            case Calendar.MONDAY:
//                day = "Mon";
//                break;
//            case Calendar.TUESDAY:
//                day = "Tue";
//                break;
//            case Calendar.WEDNESDAY:
//                day = "Wed";
//                break;
//            case Calendar.THURSDAY:
//                day = "Thu";
//                break;
//            case Calendar.FRIDAY:
//                day = "Fri";
//                break;
//            case Calendar.SATURDAY:
//                day = "Sat";
//                break;
//            case Calendar.SUNDAY:
//                day = "Sun";
//                break;
//        }
//        return day;
    }

    public void showSnackBar(String string) {
        progressBar.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        tv.setText("No Internet Connection");
        pager.setVisibility(View.GONE);
        Snackbar.make(tv, string, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isNetworkAvailable()) {
                            getDetailsRequest();
                        } else
                            showSnackBar("No Internet Connection");
                    }
                }).show();

    }

    private void getDetailsRequest() {
        final String baseUrl = "http://108.healthifyme.com/api/v1/booking/slots/all?username=alok%40x.coz" +
                "&api_key=a4aeb4e27f27b5786828f6cdf00d8d2cb44fe6d7&vc=276&expert_username=neha%40healthifyme.com&format=json";
        progressBar.setVisibility(View.VISIBLE);
        tv.setVisibility(View.GONE);
        pager.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, baseUrl,
                new com.android.volley.Response.Listener<String>() {
                    /**
                     * Called when a response is received.
                     *
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject slots = jsonObject.getJSONObject("slots");
                            Log.d("Json", jsonObject.toString());
                            Iterator iterator = slots.keys();
                            slotsObjects = new ArrayList<SlotsObject>();
                            while (iterator.hasNext()) {
                                String date = (String) iterator.next();
                                month.setText(findMonth(date));
                                SlotsObject slotsObject = new SlotsObject();
                                slotsObject.date = date;
                                slotsObject.afternoon = new ArrayList<>();
                                slotsObject.evening = new ArrayList<>();
                                slotsObject.morning = new ArrayList<>();
                                Log.d("Dates", date);
                                JSONObject Date = slots.getJSONObject(date);
                                JSONArray afternoon;
                                if (!Date.isNull("afternoon")) {
                                    afternoon = Date.getJSONArray("afternoon");
                                    int count = 0;
                                    for (int i = 0; i < afternoon.length(); i++) {
                                        JSONObject jsonObject1 = afternoon.getJSONObject(i);
                                        DayObject dayObject = new DayObject();
                                        dayObject.end_time = jsonObject1.getString("end_time");
                                        dayObject.is_booked = jsonObject1.getBoolean("is_booked");
                                        dayObject.is_expired = jsonObject1.getBoolean("is_expired");
                                        if (!(dayObject.is_booked && dayObject.is_expired))
                                            count++;
                                        dayObject.slot_id = jsonObject1.getString("slot_id");
                                        dayObject.start_time = jsonObject1.getString("start_time");
                                        slotsObject.afternoon.add(dayObject);
                                    }
                                    slotsObject.afternoon_slots = count;

                                }
                                JSONArray morning;
                                if (!Date.isNull("morning")) {
                                    morning = Date.getJSONArray("morning");
                                    int count = 0;
                                    for (int i = 0; i < morning.length(); i++) {
                                        JSONObject jsonObject1 = morning.getJSONObject(i);
                                        DayObject dayObject = new DayObject();
                                        dayObject.end_time = jsonObject1.getString("end_time");
                                        dayObject.is_booked = jsonObject1.getBoolean("is_booked");
                                        dayObject.is_expired = jsonObject1.getBoolean("is_expired");
                                        if (!(dayObject.is_booked && dayObject.is_expired))
                                            count++;
                                        dayObject.slot_id = jsonObject1.getString("slot_id");
                                        dayObject.start_time = jsonObject1.getString("start_time");
                                        slotsObject.morning.add(dayObject);
                                    }
                                    slotsObject.morning_slots = count;
                                }
                                JSONArray evening;
                                if (!Date.isNull("evening")) {
                                    evening = Date.getJSONArray("evening");
                                    int count = 0;
                                    for (int i = 0; i < evening.length(); i++) {
                                        JSONObject jsonObject1 = evening.getJSONObject(i);
                                        DayObject dayObject = new DayObject();
                                        dayObject.end_time = jsonObject1.getString("end_time");
                                        dayObject.is_booked = jsonObject1.getBoolean("is_booked");
                                        dayObject.is_expired = jsonObject1.getBoolean("is_expired");
                                        if (!(dayObject.is_booked && dayObject.is_expired))
                                            count++;
                                        dayObject.slot_id = jsonObject1.getString("slot_id");
                                        dayObject.start_time = jsonObject1.getString("start_time");
                                        slotsObject.evening.add(dayObject);
                                    }
                                    slotsObject.evening_slots = count;
                                }
                                slotsObjects.add(slotsObject);
                            }

                        } catch (Exception e) {

                        }
                        Log.d("Size", slotsObjects.size() + "");
                        progressBar.setVisibility(View.GONE);
                        pager.setVisibility(View.VISIBLE);
                        pager.setAdapter(new MainViewPagerAdapter(getChildFragmentManager(), slotsObjects));
                        tabLayout.setupWithViewPager(pager);
                        tabLayout.setTabTextColors(getResources().getColor(R.color.text_tab_selected),
                                getResources().getColor(R.color.text_tab_selected));
                    }

                }, new com.android.volley.Response.ErrorListener() {
            /**
             * Callback method that an error has been occurred with the
             * provided error code and optional user-readable message.
             *
             * @param error
             */
            @Override

            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    showSnackBar("No internet connection.");
                } else
                    showSnackBar(error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_retry){
//            if (isNetworkAvailable())
//                getDetailsRequest();
//            else
//                showSnackBar("No Internet connection");
//        }
        return super.onOptionsItemSelected(item);
    }
}
