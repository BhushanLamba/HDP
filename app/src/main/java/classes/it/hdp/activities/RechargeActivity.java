package classes.it.hdp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import classes.it.hdp.R;
import classes.it.hdp.adapters.OperatorAdapter;
import classes.it.hdp.classes.MessageDialogHandler;
import classes.it.hdp.models.OperatorsModel;
import classes.it.hdp.myInterface.OperatorInterface;
import classes.it.hdp.retrofit.RetrofitClient;
import ng.max.slideview.SlideView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeActivity extends AppCompatActivity {

    String service;
    TextView tvDthOperator;
    LinearLayout operatorLayout, circleLayout;
    TextView tvOperator, tvCircle;
    String userId;
    SharedPreferences sharedPreferences;
    Dialog operatorDialog, circleDialog;
    String selectedOperatorId, selectedCircleCode, selectedOperatorName = "Select Operator"
            ,selectedStateName = "select";
    AppCompatButton btnRecharge;
    EditText etRechargeNumber, etAmount;
    TextView tvTitle,tvServiceName;


    String[] stateNameArray = { "Assam", "Bihar Jharkhand", "Chennai", "Delhi NCR", "Gujarat", "Haryana",
            "Himachal Pradesh", "Jammu Kashmir", "Karnataka", "Kerala", "Kolkata", "Madhya Pradesh Chhattisgarh", "Maharashtra Goa",
            "Mumbai", "North East", "Orissa", "Punjab", "Rajasthan", "Tamil Nadu", "UP East", "UP West", "West Bengal"};

    ImageView imgClose;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initViews();
        service = getIntent().getStringExtra("service");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RechargeActivity.this);
        userId = sharedPreferences.getString("userid", null);


        tvTitle.setText(service);
        tvServiceName.setText(service+" Recharge");

        getOperators();

        imgClose.setOnClickListener(v->
        {
            finish();
        });


        btnRecharge.setOnClickListener(v -> {
            if (checkInputs()) {

                String number = etRechargeNumber.getText().toString().trim();
                final String amount = etAmount.getText().toString().trim();


                final android.app.AlertDialog confirmDialog = new android.app.AlertDialog.Builder(RechargeActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = inflater.inflate(R.layout.confirmation_dialog, null);
                confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                confirmDialog.setView(convertView);
                confirmDialog.setCancelable(false);
                confirmDialog.show();

                TextView tvMessage = convertView.findViewById(R.id.tv_message);
                ImageView imgClose = convertView.findViewById(R.id.img_close);

                imgClose.setOnClickListener(v1 ->
                {
                    confirmDialog.dismiss();
                });

                tvMessage.setText("Operator : " + selectedOperatorName + "\nNumber : " + number + "\nAmount : " + amount);

                SlideView slideView = (SlideView) convertView.findViewById(R.id.slideView);
                slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
                    @Override
                    public void onSlideComplete(SlideView slideView) {
                        // vibrate the device
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(100);
                        doRecharge();
                        confirmDialog.dismiss();
                        // go to a new activity
                    }
                });
            }
        });

        operatorLayout.setOnClickListener(view -> operatorDialog.show());


    }

    private void getOperators() {

        final android.app.AlertDialog pDialog = new android.app.AlertDialog.Builder(RechargeActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_progress_dialog, null);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.setView(convertView);
        pDialog.setCancelable(false);
        pDialog.show();

        Call<JsonObject> call = RetrofitClient.getInstance().getApi().getOperators(service);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(String.valueOf(response.body()));

                        JSONArray jsonArray = jsonObject1.getJSONArray("Data");

                        ArrayList<OperatorsModel> operatorModelArrayList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            OperatorsModel operatorModel = new OperatorsModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String operatorName = jsonObject.getString("ProdName");
                            String operatorId = jsonObject.getString("ProdKey");
                            String operatorImage = jsonObject.getString("Image");

                            operatorModel.setOperatorName(operatorName);
                            operatorModel.setOperatorId(operatorId);
                            operatorModel.setOperatorImage(operatorImage);

                            operatorModelArrayList.add(operatorModel);

                        }

                        operatorDialog = new Dialog(RechargeActivity.this, R.style.DialogTheme);
                        operatorDialog.setContentView(R.layout.operator_dialog);

                        int width = (int) (getResources().getDisplayMetrics().widthPixels * 1.0);
                        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);

                        operatorDialog.getWindow().setLayout(width, height);

                        operatorDialog.getWindow().setGravity(Gravity.BOTTOM);
                        operatorDialog.getWindow().setBackgroundDrawableResource(R.drawable.card_back);
                        operatorDialog.getWindow().setWindowAnimations(R.style.SlidingDialog);


                        RecyclerView rv = operatorDialog.findViewById(R.id.recyclerView);
                        ImageView cancelImg = operatorDialog.findViewById(R.id.cancelImg);
                        cancelImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                operatorDialog.dismiss();
                            }
                        });
                        OperatorAdapter recyclerViewItemAdapter = new OperatorAdapter(operatorModelArrayList, RechargeActivity.this);
                        rv.setLayoutManager(new LinearLayoutManager(RechargeActivity.this, RecyclerView.VERTICAL, false));
                        rv.setAdapter(recyclerViewItemAdapter);

                        recyclerViewItemAdapter.setMyInterface(new OperatorInterface() {
                            @Override
                            public void operatorData(String operatorName, String operatorId) {
                                operatorDialog.dismiss();
                                selectedOperatorId = operatorId;
                                selectedOperatorName = operatorName;
                                if (service.equalsIgnoreCase("DTH")) {
                                    tvDthOperator.setText(selectedOperatorName);
                                } else {
                                    tvOperator.setText(operatorName);

                                }
                            }
                        });

                        pDialog.dismiss();

                    } catch (JSONException e) {
                        pDialog.dismiss();
                        MessageDialogHandler.showMessageDialog(RechargeActivity.this,RechargeActivity.this,
                                "Please try again later!!!","FAILED",true);

                    }
                } else {
                    pDialog.dismiss();
                    MessageDialogHandler.showMessageDialog(RechargeActivity.this,RechargeActivity.this,
                            "Please try again later!!!","FAILED",true);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pDialog.dismiss();
                MessageDialogHandler.showMessageDialog(RechargeActivity.this,RechargeActivity.this,
                        t.getMessage(),"FAILED",true);
            }
        });

    }

    private void doRecharge() {
        final android.app.AlertDialog pDialog = new android.app.AlertDialog.Builder(RechargeActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_progress_dialog, null);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.setView(convertView);
        pDialog.setCancelable(false);
        pDialog.show();

        String number = etRechargeNumber.getText().toString().trim();
        final String amount = etAmount.getText().toString().trim();

        Call<JsonObject> call = RetrofitClient.getInstance().getApi().doRecharge(userId,amount,selectedOperatorId,
                number,service);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject1 = null;

                    try {
                        jsonObject1 = new JSONObject(String.valueOf(response.body()));
                        String statuscode = jsonObject1.getString("Status");


                        if (statuscode.equalsIgnoreCase("SUCCESS") ||
                                statuscode.equalsIgnoreCase("FAILED")) {

                            JSONArray jsonArray = jsonObject1.getJSONArray("Data");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            String responseNumber = jsonObject.getString("MobileNo");
                            String responseStatus = jsonObject.getString("Status");
                            String responseAmount = jsonObject.getString("Amount");
                            String responseTransactionId = jsonObject.getString("TxnID");
                            String responseDateTime = jsonObject.getString("TxnDate");
                            String responseOperator = jsonObject.getString("Operator");


                            @SuppressLint("SimpleDateFormat") DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                            String[] splitDate = responseDateTime.split(" ");
                            try {
                                Date date = inputDateFormat.parse(splitDate[0]);
                                Date time = simpleDateFormat.parse(splitDate[1]);


                                String outputDate = new SimpleDateFormat("dd MMM yyyy").format(date);
                                String outputTime = new SimpleDateFormat("hh:mm a").format(time);
                                Intent intent = new Intent(RechargeActivity.this, RechargeStatusActivity.class);
                                intent.putExtra("responseStatus", responseStatus);
                                intent.putExtra("responseNumber", responseNumber);
                                intent.putExtra("responseAmount", responseAmount);
                                intent.putExtra("responseTransactionId", responseTransactionId);
                                intent.putExtra("responseOperator", responseOperator);
                                intent.putExtra("outputDate", outputDate);
                                intent.putExtra("outputTime", outputTime);
                                startActivity(intent);
                                finish();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            pDialog.dismiss();
                        }

                        else {
                            pDialog.dismiss();
                            MessageDialogHandler.showMessageDialog(RechargeActivity.this,RechargeActivity.this,
                                    "Please try after sometime","FAILED",true);
                        }

                    } catch (JSONException e) {
                        pDialog.dismiss();
                        MessageDialogHandler.showMessageDialog(RechargeActivity.this,RechargeActivity.this,
                                "Please try after sometime","FAILED",true);
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                pDialog.dismiss();
                MessageDialogHandler.showMessageDialog(RechargeActivity.this,RechargeActivity.this,
                        t.getMessage(),"FAILED",true);
            }
        });
    }

    private boolean checkInputs() {
        if (!TextUtils.isEmpty(etRechargeNumber.getText())) {
            if (!TextUtils.isEmpty(etAmount.getText())) {
                if (!selectedOperatorName.equalsIgnoreCase("Select Operator")) {
                    return true;
                } else {
                    new AlertDialog.Builder(RechargeActivity.this).setMessage("Select Operator")
                            .setPositiveButton("Ok", null).show();
                    return false;
                }
            } else {
                etAmount.setError("Amount can't be empty.");
                return false;
            }
        } else {
            etRechargeNumber.setError("Enter Number.");
            return false;
        }
    }

    private void initViews() {
        operatorLayout = findViewById(R.id.operator_layout);
        circleLayout = findViewById(R.id.circle_layout);
        tvOperator = findViewById(R.id.tv_operator);
        tvCircle = findViewById(R.id.tv_circle);
        btnRecharge = findViewById(R.id.btn_recharge);
        etRechargeNumber = findViewById(R.id.et_recharge_number);
        etAmount = findViewById(R.id.et_recharge_amount);
        tvServiceName = findViewById(R.id.tv_service);
        tvTitle = findViewById(R.id.tv_title);
        imgClose = findViewById(R.id.img_close);
    }
}