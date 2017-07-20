package com.example.sid.marwadishaadi.User_Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Search.BottomSheet;
import com.example.sid.marwadishaadi.Similar_Profiles.SimilarActivity;
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPersonalDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class ProfilePersonalDetailsFragment extends Fragment {

    private static int casebreak;
    TextView name_age, maritalStatus, birthdate, gender, address, mobileNo, caste, height, weight, complexion_build, physicalStatus, education, educationDegree, collegeName_collegeLocation, currentOccupation, designation_companyName, companyLocation, annualIncome;
    private TextView edit_individual;
    private TextView edit_education;
    private TextView edit_profession;
    private Button similar;
    private String clickedID, customer_id;
    private String res = "";
    private boolean isPaidMember;


    public ProfilePersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mview = inflater.inflate(R.layout.fragment_profile__personal__details, container, false);

        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        clickedID = customer_id;

        edit_individual = (TextView) mview.findViewById(R.id.individual_clear);
        edit_education = (TextView) mview.findViewById(R.id.edu_clear);
        edit_profession = (TextView) mview.findViewById(R.id.profession_clear);
        similar = (Button) mview.findViewById(R.id.similar);

        name_age = (TextView) mview.findViewById(R.id.name_age);
        maritalStatus = (TextView) mview.findViewById(R.id.marital_status);
        birthdate = (TextView) mview.findViewById(R.id.birthdate);
        gender = (TextView) mview.findViewById(R.id.gender);
        address = (TextView) mview.findViewById(R.id.address);
        mobileNo = (TextView) mview.findViewById(R.id.contact_mob);
        caste = (TextView) mview.findViewById(R.id.caste);
        height = (TextView) mview.findViewById(R.id.height);
        weight = (TextView) mview.findViewById(R.id.weight);
        complexion_build = (TextView) mview.findViewById(R.id.complexion_build);
        physicalStatus = (TextView) mview.findViewById(R.id.physical_status);
        education = (TextView) mview.findViewById(R.id.degree);
        educationDegree = (TextView) mview.findViewById(R.id.edu_degree);
        collegeName_collegeLocation = (TextView) mview.findViewById(R.id.collegeName_collegeLocation);
        currentOccupation = (TextView) mview.findViewById(R.id.current_occup);
        designation_companyName = (TextView) mview.findViewById(R.id.occupDesignation_occupCompany);
        companyLocation = (TextView) mview.findViewById(R.id.occup_location);
        annualIncome = (TextView) mview.findViewById(R.id.annual_income);

        String[] array = getResources().getStringArray(R.array.communities);

        SharedPreferences communityChecker = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {

            if (communityChecker.getString(array[i], null).contains("Yes") && array[i].toCharArray()[0] != customer_id.toCharArray()[0]) {
                isPaidMember = true;
            }
        }

        name_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    name_age.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        maritalStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    maritalStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        birthdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    birthdate.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    gender.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    address.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 9 && customer_id != clickedID) {
                    mobileNo.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        caste.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    caste.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    height.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    weight.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        complexion_build.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace(" in Complexion, has ", "").replace(" body type", "").trim().length() == 0 && customer_id != clickedID) {
                    complexion_build.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        physicalStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace(" (Physical Status)", "").length() == 0 && customer_id != clickedID) {
                    physicalStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        education.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    education.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        educationDegree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    education.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        collegeName_collegeLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    collegeName_collegeLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        currentOccupation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    currentOccupation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        designation_companyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    designation_companyName.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        companyLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    companyLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        annualIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    annualIncome.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!isPaidMember) {
            mobileNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar snackbar = Snackbar
                            .make(mview, "Become a paid member.", Snackbar.LENGTH_LONG)
                            .setAction("GO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), UpgradeMembershipActivity.class);
                                    getContext().startActivity(intent);
                                }
                            });

// Changing message text color
                    snackbar.setActionTextColor(Color.RED);
                }
            });
        } else {
            mobileNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    String phoneNo = "tel:" + mobileNo.getText().toString();
                    intent.setData(Uri.parse(phoneNo));
                    startActivity(intent);

                }
            });

        }


        Intent data = getActivity().getIntent();
        String from = data.getStringExtra("from");

        if (data.getStringExtra("customerNo") != null) {

            clickedID = data.getStringExtra("customerNo");
            new PersonalDetails().execute(clickedID);
        }


        if ("suggestion".equals(from) | "recent".equals(from) | "reverseMatching".equals(from) | "favourites".equals(from) | "interestReceived".equals(from) | "interestSent".equals(from) | "similar".equals(from)) {
            edit_individual.setVisibility(View.GONE);
            edit_education.setVisibility(View.GONE);
            edit_profession.setVisibility(View.GONE);

        }


        new PersonalDetails().execute(clickedID);
        if(customer_id.equals(clickedID)){
            similar.setVisibility(View.GONE);}

        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SimilarActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        edit_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditPersonalDetailsActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        edit_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 12;


                BottomSheetDialogFragment btm = new BottomSheet(1);
                btm.show(getFragmentManager(), btm.getTag());
            }
        });


        edit_profession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 13;
                BottomSheetDialogFragment btm = new BottomSheet(1);
                btm.show(getFragmentManager(), btm.getTag());
            }
        });

        return mview;
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

    public int getCasebreak() {
        return this.casebreak;
    }

    class PersonalDetails extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            String cus = strings[0];
            AndroidNetworking.post("http://208.91.199.50:5000/profilePersonalDetails")
                    .addBodyParameter("customerNo", cus)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            
                            try {
                                JSONArray array = response.getJSONArray(0);

                                String dateOfBirth = array.getString(2);
                                // Thu, 18 Jan 1990 00:00:00 GMT
                                DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
                                Date date = null;
                                try {
                                    date = formatter.parse(dateOfBirth);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String[] str = {"January",
                                        "February",
                                        "March",
                                        "April",
                                        "May",
                                        "June",
                                        "July",
                                        "August",
                                        "September",
                                        "October",
                                        "November",
                                        "December"};
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                String formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
                                String strDate = cal.get(Calendar.DATE) + " " + str[(cal.get(Calendar.MONTH))] + " " + cal.get(Calendar.YEAR);

                                String[] partsOfDate = formatedDate.split("-");
                                int day = Integer.parseInt(partsOfDate[0]);
                                int month = Integer.parseInt(partsOfDate[1]);
                                int year = Integer.parseInt(partsOfDate[2]);
                                int a = getAge(year, month, day);

                                String na = array.getString(0) + " " + array.getString(1) + ", " + a;
                                name_age.setText(na);
                                maritalStatus.setText(array.getString(3));
                                birthdate.setText(strDate);
                                gender.setText(array.getString(4));
                                address.setText(array.getString(5));

                                if (isPaidMember) {
                                    mobileNo.setText(array.getString(6));
                                } else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Build.VERSION.SDK_INT >= 11) {
                                                mobileNo.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                                            }
                                            float radius = mobileNo.getTextSize() / 3;
                                            BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                                            mobileNo.getPaint().setMaskFilter(filter);
                                        }
                                    });
                                }


                                String[] c = array.getString(7).split("");
                                String cast = "";
                                if (c[1].equals("A")) {
                                    cast = "Agarwal";
                                } else if (c[1].equals("K")) {
                                    cast = "Khandelwal";
                                } else if (c[1].equals("J")) {
                                    cast = "Jain";
                                } else if (c[1].equals("M")) {
                                    cast = "Maheshwari";
                                } else if (c[1].equals("O")) {
                                    cast = "Other";
                                }


                                caste.setText(cast);


                                height.setText(array.getString(8));
                                String w = "Weighs " + array.getString(9) + " kgs";
                                weight.setText(w);

                                String cb = array.getString(10) + " in Complexion, has " + array.getString(11) + " body type";

                                complexion_build.setText(cb);
                                physicalStatus.setText(array.getString(12) + " (Physical Status)");
                                education.setText(array.getString(13));
                                educationDegree.setText(array.getString(14));

                                if (array.getString(16).trim().length() > 0) {
                                    String cnl = array.getString(15) + ", " + array.getString(16);
                                    collegeName_collegeLocation.setText(cnl);
                                } else {
                                    collegeName_collegeLocation.setText(array.getString(15));
                                }


                                currentOccupation.setText(array.getString(17));
                                String dc;

                                if (array.getString(20).length() == 0) {
                                    dc = array.getString(19);
                                } else {
                                    dc = array.getString(20) + " at " + array.getString(19);
                                }

                                designation_companyName.setText(dc);

                                String cl = array.getString(21);
                                companyLocation.setText(cl);


                                String annualI = array.getString(18);
                                annualI = annualI.replaceAll("[^-?0-9]+", " ");
                                List<String> incomeArray = Arrays.asList(annualI.trim().split(" "));
                                
                                if (array.getString(18).contains("Upto")) {
                                    annualI = "Upto 3L";
                                } else if (array.getString(18).contains("Above")) {
                                    annualI = "Above 50L";

                                } else if (incomeArray.size() == 3) {
                                    
                                    double first = Integer.parseInt(incomeArray.get(0)) / 100000.0;
                                    double second = Integer.parseInt(incomeArray.get(2)) / 100000.0;
                                    annualI = (int) first + "L - " + (int) second + "L";
                                } else {
                                    annualI = "No Income mentioned.";
                                }
                                annualIncome.setText("Rs. " + annualI);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError error) {

                        }
                    });

            return null;
        }
    }

    //Edit Educational details of User Personal Details


}
