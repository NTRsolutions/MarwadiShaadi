package com.example.sid.marwadishaadi.User_Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Similar_Profiles.SimilarActivity;
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditFamilyDetailsActivity;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Search.BottomSheet;
import com.example.sid.marwadishaadi.Similar_Profiles.SimilarActivity;
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditFamilyDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;


public class ProfileFamilyDetailsFragment extends Fragment {

    private static int casebreak;
    private TextView edit_family;
    private TextView edit_relatives;
    private Button similar;
    private OnFragmentInteractionListener mListener;

    TextView fatherName, grandpaName, mamaSurname, fatherOccupation, fatherOccupationDetails, nativePlace, subcaste, familyType, familyStatus, relation, relativeName, relativeOccupation, relativeLocation, relativeMobile;

    public ProfileFamilyDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFamilyDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFamilyDetailsFragment newInstance(String param1, String param2) {
        ProfileFamilyDetailsFragment fragment = new ProfileFamilyDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_profile__family__details, container, false);
        edit_family=(TextView)mview.findViewById(R.id.family_clear);
        edit_relatives=(TextView)mview.findViewById(R.id.relatives_clear);
        similar = (Button) mview.findViewById(R.id.similar);

        fatherName=(TextView) mview.findViewById(R.id.father_name);
        fatherOccupation=(TextView) mview.findViewById(R.id.father_occupation);
        fatherOccupationDetails=(TextView) mview.findViewById(R.id.father_occupation_details);
        familyStatus=(TextView) mview.findViewById(R.id.family_status);
        familyType=(TextView) mview.findViewById(R.id.family_type);
        nativePlace=(TextView) mview.findViewById(R.id.native_place);
        subcaste=(TextView) mview.findViewById(R.id.subcaste);
        grandpaName=(TextView) mview.findViewById(R.id.grandpa_name);
        mamaSurname=(TextView) mview.findViewById(R.id.mama_surname);
        relation=(TextView) mview.findViewById(R.id.relation);
        relativeName=(TextView) mview.findViewById(R.id.relative_name);
        relativeOccupation=(TextView) mview.findViewById(R.id.relative_occupation);
        relativeLocation=(TextView) mview.findViewById(R.id.relative_location);
        relativeMobile=(TextView) mview.findViewById(R.id.relative_mobile);

        new ProfileFamilyDetails().execute();

        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SimilarActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
        edit_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditFamilyDetailsActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        edit_relatives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak=32;
                BottomSheetDialogFragment btm= new BottomSheet(3);
                btm.show(getFragmentManager(),btm.getTag());
            }
        });

        return mview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private class ProfileFamilyDetails extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://192.168.43.143:5050/profileFamilyDetails")
                    .addBodyParameter("customerNo", "A1028")
                    .setPriority(Priority.HIGH)
                    .setTag(this)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response){

                            try {
                                JSONArray result = response.getJSONArray(0);

                                String fn= result.getString(0) + " (Father)";
                                fatherName.setText(fn);
                                String fo= result.getString(1) + " (Father)";
                                fatherOccupation.setText(fo);
                                String fod=result.getString(2) + " (Father)";
                                fatherOccupationDetails.setText(fod);
                                String fs=result.getString(3) + " Family";
                                familyStatus.setText(fs);
                                String ftv=result.getString(4) + " family with " +result.getString(5)+ " values";
                                familyType.setText(ftv);
                                String nat="Natively from "+result.getString(6);
                                nativePlace.setText(nat);
                                String sc=result.getString(7) + " (Subcaste)";
                                subcaste.setText(sc);
                                String gn=result.getString(8)+" (Grandfather)";
                                grandpaName.setText(gn);
                                String ms=result.getString(9)+" (Mama's Surname)";
                                mamaSurname.setText(ms);
                                relation.setText(result.getString(10));
                                relativeName.setText(result.getString(11));
                                relativeOccupation.setText(result.getString(12));
                                String loc="Lives in "+result.getString(13);
                                relativeLocation.setText(loc);
                                String mob="Contact: "+result.getString(14);
                                relativeMobile.setText(mob);







                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError error){
                            Log.d(TAG, "onError: *****************"+ error.toString());
                        }


                    });

            return null;
        };

    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public int getCasebreak()
    {
        return this.casebreak;
    }
}
