package com.example.sid.marwadishaadi.Dashboard_Reverse_Matching;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.CacheHelper;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;


import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getCacheDir;


public class Reverse_MatchingFragment extends Fragment {

    private static final String TAG = "Reverse_MatchingFragmen";
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<ReverseModel> reverseModelList = new ArrayList<>();
    private RecyclerView reverseRecyclerView;
    private ReverseAdapter reverseAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String customer_id, customer_gender;
    private LinearLayout empty_view_reverse;
    private String res = "";
    private File cache = null;
    private boolean isAlreadyLoadedFromCache = false;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_reverse__matching, container, false);
        empty_view_reverse = (LinearLayout) mview.findViewById(R.id.empty_view_reverse);
        empty_view_reverse.setVisibility(View.GONE);

        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);

        cache = new File(getCacheDir() + "/" + "reversematching" +customer_id+ ".srl");

        String[] array = getResources().getStringArray(R.array.communities);

        SharedPreferences communityChecker = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        if (communityChecker!=null) {
            for (int i = 0; i < 5; i++) {

                if (communityChecker.getString(array[i], null).contains("Yes") && array[i].toCharArray()[0] != customer_id.toCharArray()[0]) {
                    res += " OR tbl_user.customer_no LIKE '" + array[i].toCharArray()[0] + "%'";
                }
            }
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics, "Reverse Matching", "view");


        mProgressBar = (ProgressBar) mview.findViewById(R.id.reverse_loading);
        mProgressBar.setVisibility(View.VISIBLE);

        reverseRecyclerView = (RecyclerView) mview.findViewById(R.id.swipe_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) mview.findViewById(R.id.swipe);

        reverseRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        reverseRecyclerView.setItemAnimator(fadeInLeftAnimator);
        reverseAdapter = new ReverseAdapter(reverseModelList, getContext());
        reverseRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        reverseRecyclerView.setAdapter(reverseAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });


        // loading cached copy
        String res = CacheHelper.retrieve("reverse_matching",cache);
        if(!res.equals("")){
            try {

                isAlreadyLoadedFromCache = true;

                // storing cache hash
                CacheHelper.saveHash(getContext(),CacheHelper.generateHash(res),"reverse_matching");

                // displaying it
                JSONArray response = new JSONArray(res);
               // .makeText(getContext(), "Loading from cache....", .LENGTH_SHORT).show();
                parseReverseMatches(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        new PrepareReverse().execute();

        return mview;
    }

    private void parseReverseMatches(JSONArray response) {

        try {
                                mProgressBar.setVisibility(View.GONE);

            reverseModelList.clear();
            reverseAdapter.notifyDataSetChanged();



            if(response.toString().contains("zero")){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        empty_view_reverse.setVisibility(View.VISIBLE);
                    }
                });
            }else {
                empty_view_reverse.setVisibility(View.GONE);


                for (int i = 0; i < response.length(); i++) {

                    JSONArray array = response.getJSONArray(i);
                    String customerNo = array.getString(0);
                    String name = array.getString(1);
                    String dateOfBirth = array.getString(2);
//                                Thu, 18 Jan 1990 00:00:00 GMT
                    DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
                    Date date = formatter.parse(dateOfBirth);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    String formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);

                    String[] partsOfDate = formatedDate.split("-");
                    int day = Integer.parseInt(partsOfDate[0]);
                    int month = Integer.parseInt(partsOfDate[1]);
                    int year = Integer.parseInt(partsOfDate[2]);
                    int age = getAge(year, month, day);
                    String education = array.getString(3);
                    String occupationLocation = array.getString(4);
                    name = name + " " + array.getString(5);

                    String imageUrl = "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + array.getString(6);

                    ReverseModel reverseModel = new ReverseModel(imageUrl, name, age , education, occupationLocation, customerNo);

                    if (!reverseModelList.contains(reverseModel) && imageUrl.contains("null")){
                        reverseModelList.add(reverseModel);
                        reverseAdapter.notifyDataSetChanged();
                    } else if (!reverseModelList.contains(reverseModel)) {
                        reverseModelList.add(0, reverseModel);
                        reverseAdapter.notifyDataSetChanged();
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void refreshContent() {
        new PrepareReverse().execute();
    }

    public int getAge(int DOByear, int DOBmonth, int DOBday) {

        int age;

        final Calendar calenderToday = Calendar.getInstance();
        int currentYear = calenderToday.get(Calendar.YEAR);
        int currentMonth = 1 + calenderToday.get(Calendar.MONTH);
        int todayDay = calenderToday.get(Calendar.DAY_OF_MONTH);

        age = currentYear - DOByear;

        if (DOBmonth > currentMonth) {
            --age;
        } else if (DOBmonth == currentMonth) {
            if (DOBday > todayDay) {
                --age;
            }
        }
        return age;
    }


    private class PrepareReverse extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://208.91.199.50:5000/prepareReverse/{customerNo}/{gender}")
                    .addPathParameter("customerNo", customer_id)
                    .addPathParameter("gender", customer_gender)
                    .addBodyParameter("membership", res)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response

                            //

                            // if no change in data
                            if (isAlreadyLoadedFromCache){

                                String latestResponseHash = CacheHelper.generateHash(response.toString());
                                String cacheResponseHash = CacheHelper.retrieveHash(getContext(),"reverse_matching");

                                //
                                //
                                //

                                if (cacheResponseHash!=null && latestResponseHash.equals(cacheResponseHash)){
                                   // .makeText(getContext(), "data same found", .LENGTH_SHORT).show();
                                    return;
                                }else{

                                    // hash not matched
                                    loadedFromNetwork(response);
                                }
                            }else{
                                // first time load
                                loadedFromNetwork(response);
                            }


                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   mProgressBar.setVisibility(View.GONE);

                                }
                            });

                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           mProgressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    public void loadedFromNetwork(JSONArray response){


        //saving fresh in cache
        CacheHelper.save("reverse_matching",response.toString(),cache);

        // marking cache
        isAlreadyLoadedFromCache = true;

        // storing latest cache hash
        CacheHelper.saveHash(getContext(),CacheHelper.generateHash(response.toString()),"reverse_matching");

        // displaying it
        parseReverseMatches(response);

    }
}
