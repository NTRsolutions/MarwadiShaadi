package com.example.sid.marwadishaadi.Dashboard_Interest_Received;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;

import java.util.List;

import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;

/**
 * Created by USER on 01-06-2017.
 */

public class InterestReceivedAdapter extends RecyclerView.Adapter<InterestReceivedAdapter.MyViewHolder> {

    private static final String TAG = "InterestReceivedAdapter";
    private RecyclerView rv;
    private Context context;
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<InterestReceivedModel> interestReceivedModelList;
    private List<InterestReceivedModel> mInterestReceivedModelList;

    public InterestReceivedAdapter(Context context, List<InterestReceivedModel> interestReceivedModelList, RecyclerView rv) {
        this.context = context;
        this.mInterestReceivedModelList = interestReceivedModelList;
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.rv = rv;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        InterestReceivedModel interestReceivedModel = mInterestReceivedModelList.get(position);

        String ag = interestReceivedModel.getName() + ", " + interestReceivedModel.getAge();

        holder.customerNo.setText(interestReceivedModel.getCustomerId());

        Glide.with(context).load(interestReceivedModel.getUserImage()).into(holder.userImage);
        holder.name.setText(ag);
        holder.highestDegree.setText(interestReceivedModel.getHighestDegree());
        holder.location.setText(interestReceivedModel.getLocation());
        if (interestReceivedModel.getStatus() == 0) {
            holder.status.setText("Accepted");
            holder.status.setBackgroundColor(Color.parseColor("#00c864"));
            holder.accept.setVisibility(View.INVISIBLE);
            holder.reject.setVisibility(View.INVISIBLE);
        } else if (interestReceivedModel.getStatus() == 1) {
            holder.status.setText("Rejected");
            holder.status.setBackgroundColor(Color.parseColor("#ff0000"));
            holder.accept.setVisibility(View.INVISIBLE);
            holder.reject.setVisibility(View.INVISIBLE);
        } else if (interestReceivedModel.getStatus() == 2) {
            holder.status.setText("Pending");
            holder.status.setBackgroundColor(Color.parseColor("#7faeff"));
        }

    }

    @Override
    public int getItemCount() {
        return mInterestReceivedModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView userImage;
        public TextView name, age, highestDegree, location, status, customerNo;
        public ImageView accept, reject;

        public MyViewHolder(final View itemView) {
            super(itemView);
            userImage = (ImageView) itemView.findViewById(R.id.userImage);
            name = (TextView) itemView.findViewById(R.id.textviewName);
            highestDegree = (TextView) itemView.findViewById(R.id.textviewHighestDegree);
            location = (TextView) itemView.findViewById(R.id.textviewLocation);
            status = (TextView) itemView.findViewById(R.id.interest_status);
            accept = (ImageView) itemView.findViewById(R.id.interest_accept);
            reject = (ImageView) itemView.findViewById(R.id.interest_reject);
            customerNo = (TextView) itemView.findViewById(R.id.textViewCustomerNo);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // analytics
                    Analytics_Util.logAnalytic(mFirebaseAnalytics,"Interest Accepted","button");

                    final int position = getAdapterPosition();
                    final InterestReceivedModel interestmodel = mInterestReceivedModelList.get(position);
                    interestmodel.setStatus(0);
                    notifyItemChanged(position);
                    new PrepareInterest().execute(customerNo.getText().toString(), "Accepted");
                    Snackbar snackbar = Snackbar
                            .make(rv, "Interest Accepted !", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    interestmodel.setStatus(2);
                                    notifyItemChanged(position);
                                    new PrepareInterest().execute(customerNo.getText().toString(), "Pending");

                                    Snackbar snackbar = Snackbar.make(rv, "Interest restored!", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            });

                    snackbar.show();
                }
            });


            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // analytics
                    Analytics_Util.logAnalytic(mFirebaseAnalytics,"Interest Rejected","button");

                    final int position = getAdapterPosition();
                    final InterestReceivedModel interestmodel = mInterestReceivedModelList.get(position);
                    interestmodel.setStatus(1);
                    notifyItemChanged(position);
                    new PrepareInterest().execute(customerNo.getText().toString(), "Rejected");
                    Snackbar snackbar = Snackbar
                            .make(rv, "Interest Rejected !", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    interestmodel.setStatus(2);
                                    notifyItemChanged(position);
                                    new PrepareInterest().execute(customerNo.getText().toString(), "Pending");
                                    Snackbar snackbar = Snackbar.make(rv, "Interest restored!", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            });

                    snackbar.show();
                }
            });
        }
    }

    private class PrepareInterest extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String clickedId = params[0];
            String status = params[1];
            AndroidNetworking.post(URL + "prepareInterest")
                    .addBodyParameter("customerNo", "O1057")
                    .addBodyParameter("clickedId", clickedId)
                    .addBodyParameter("status", status)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return null;
        }
    }
}