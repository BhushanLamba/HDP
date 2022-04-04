package classes.it.hdp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import classes.it.hdp.R;
import classes.it.hdp.classes.MessageDialogHandler;
import classes.it.hdp.device.Opts;
import classes.it.hdp.device.Param;
import classes.it.hdp.device.PidData;
import classes.it.hdp.device.PidOptions;
import classes.it.hdp.models.ReqAeps;
import classes.it.hdp.retrofit.RetrofitClient;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AepsActivity extends AppCompatActivity {

    String userId, lat = "0.0", longi = "0.0";

    FusedLocationProviderClient mFusedLocationClient;

    SharedPreferences sharedPreferences;

    ImageView imgCashWithdrawal, imgMiniStatement,imgBalanceEnquiry,imgAadharPay;
    String selectedBankName = "select", selectedBankIIN, selectedTransactionType = "WAP";
    EditText etAmount, etMobile, etAadharCard;
    CheckBox ckbTermsAndCondition;
    ArrayList<String> bankNameArrayList, bankIINArrayList;
    TextView tvBankName;
    AppCompatButton  btnNext;
    ConstraintLayout detailsLayout;
    ////////////////DEVICE LAYOUT 3rd LAYOUT//////////////
    LinearLayout deviceLayout;
    ImageView imgMorpho, imgStartek, imgMantra;
    AppCompatButton btnProceedDeviceLayout;

    String selectedDevice = "select";
    ////////////////DEVICE LAYOUT 3rd LAYOUT//////////////

    ////////////////FINGER PRINT DATA/////////////////////
    Serializer serializer = null;
    PidData pidData = null;
    String pidDataStr = null;

    String ci, dc, dpId, errCode, errInfo = "null", fcount, hmac, mc, mi,
            nmPoints, qScore, rdsId, rdsVer,
            serialNo, sessionKey;

    String userAgentBrs = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";
    ////////////////FINGER PRINT DATA/////////////////////

    ImageView imgClose;
    String balance;
    TextView tvBalance;
    TextInputLayout tilAmount;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps);
        initViews();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AepsActivity.this);
        userId = sharedPreferences.getString("userid", null);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(AepsActivity.this);
        serializer = new Persister();

        balance=getIntent().getStringExtra("balance");
        tvBalance.setText("Wallet -:  ₹ "+balance);

        getBankDetails();
        detailLayoutListeners();
        deviceLayoutListeners();

        imgClose.setOnClickListener(v->
        {
            finish();
        });


    }

    /////////////////////STEP Ist/////////////////////////
    private void detailLayoutListeners() {
        imgCashWithdrawal.setOnClickListener(v ->
        {
            selectedTransactionType = "WAP";
            imgMiniStatement.setImageResource(R.drawable.mini_statement_unselected1);
            imgCashWithdrawal.setImageResource(R.drawable.cash_withdraw1);
            imgBalanceEnquiry.setImageResource(R.drawable.balance_enquiry1);
            imgAadharPay.setImageResource(R.drawable.aadhar_pay1);
            tilAmount.setVisibility(View.VISIBLE);

        });

        imgMiniStatement.setOnClickListener(v ->
        {
            selectedTransactionType = "SAP";
            imgMiniStatement.setImageResource(R.drawable.mini_statement1);
            imgCashWithdrawal.setImageResource(R.drawable.cash_withdraw_unselected1);
            imgBalanceEnquiry.setImageResource(R.drawable.balance_enquiry1);
            imgAadharPay.setImageResource(R.drawable.aadhar_pay1);
            tilAmount.setVisibility(View.GONE);

        });

        imgBalanceEnquiry.setOnClickListener(v ->
        {
            selectedTransactionType = "BAP";
            imgMiniStatement.setImageResource(R.drawable.mini_statement_unselected1);
            imgCashWithdrawal.setImageResource(R.drawable.cash_withdraw_unselected1);
            imgBalanceEnquiry.setImageResource(R.drawable.balance_enquiry1_selected);
            imgAadharPay.setImageResource(R.drawable.aadhar_pay1);
            tilAmount.setVisibility(View.GONE);
        });

        imgAadharPay.setOnClickListener(v ->
        {
            selectedTransactionType = "MZZ";
            imgMiniStatement.setImageResource(R.drawable.mini_statement_unselected1);
            imgCashWithdrawal.setImageResource(R.drawable.cash_withdraw_unselected1);
            imgBalanceEnquiry.setImageResource(R.drawable.balance_enquiry1);
            imgAadharPay.setImageResource(R.drawable.aadhar_pay1_selected);
            tilAmount.setVisibility(View.VISIBLE);
        });


        btnNext.setOnClickListener(v ->
        {
            if (checkInputs()) {
                if (selectedTransactionType.equalsIgnoreCase("WAP") ||
                selectedTransactionType.equalsIgnoreCase("MZZ"))
                {
                    if (!TextUtils.isEmpty(etAmount.getText()))
                    {
                        detailsLayout.setVisibility(View.GONE);
                        deviceLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        etAmount.setError("Required");                    }
                }
                else
                {
                    detailsLayout.setVisibility(View.GONE);
                    deviceLayout.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    private boolean checkInputs() {
        if (!selectedBankName.equalsIgnoreCase("select")) {
            if (!TextUtils.isEmpty(etAadharCard.getText())) {
                if (!TextUtils.isEmpty(etMobile.getText())) {
                        return ckbTermsAndCondition.isChecked();

                } else {
                    etMobile.setError("Required");
                    return false;
                }

            } else {
                etAadharCard.setError("Required");
                return false;
            }
        } else {
            Toast.makeText(AepsActivity.this, "Please select bank.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    ////////////////////STEP 2nd//////////////////////////
    /**
     * THIRD STEP
     * get device name from user and get pid data from RD Service
     * after that do final transaction
     */
    private void deviceLayoutListeners() {

        imgMantra.setOnClickListener(view -> {
            imgMantra.setImageResource(R.drawable.mantra_selected);
            imgMorpho.setImageResource(R.drawable.morpho_unselected);
            imgStartek.setImageResource(R.drawable.startek_unselected);

            selectedDevice = "Mantra";
        });

        imgStartek.setOnClickListener(view -> {
            imgMantra.setImageResource(R.drawable.mantra_unselected);
            imgMorpho.setImageResource(R.drawable.morpho_unselected);
            imgStartek.setImageResource(R.drawable.startek_selected);

            selectedDevice = "Startek";
        });

        imgMorpho.setOnClickListener(view -> {
            imgMantra.setImageResource(R.drawable.mantra_unselected);
            imgMorpho.setImageResource(R.drawable.morpho_selected);
            imgStartek.setImageResource(R.drawable.startek_unselected);


            selectedDevice = "Morpho";
        });

        btnProceedDeviceLayout.setOnClickListener(view -> {
            if (!selectedDevice.equalsIgnoreCase("select")) {
                String packageName = null;
                if (selectedDevice.equalsIgnoreCase("Morpho"))
                    packageName = "com.scl.rdservice";
                else if (selectedDevice.equalsIgnoreCase("Startek"))
                    packageName = "com.acpl.registersdk";
                else if (selectedDevice.equalsIgnoreCase("Mantra"))
                    packageName = "com.mantra.rdservice";

                try {

                    String pidOption = getPIDOptions();
                    Intent intent2 = new Intent();
                    intent2.setPackage(packageName);
                    intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                    intent2.putExtra("PID_OPTIONS", pidOption);
                    startActivityForResult(intent2, 1);
                } catch (Exception e) {
                    showMessageDialog("Please install " + selectedDevice + " Rd Service first.");
                }
            } else {
                showMessageDialog("Please Select Your Device");
            }
        });
    }

    private void showMessageDialog(String message) {
        final AlertDialog messageDialog = new AlertDialog.Builder(AepsActivity.this).create();
        final LayoutInflater inflater = LayoutInflater.from(AepsActivity.this);
        View convertView = inflater.inflate(R.layout.message_dialog, null);
        messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.setCancelable(false);
        messageDialog.setView(convertView);

        ImageView imgClose = convertView.findViewById(R.id.img_close);
        TextView tvMessage = convertView.findViewById(R.id.tv_message);
        Button btnTryAgain = convertView.findViewById(R.id.btn_try_again);

        imgClose.setOnClickListener(view -> {
            messageDialog.dismiss();
        });
        btnTryAgain.setOnClickListener(view -> {
            messageDialog.dismiss();
        });
        tvMessage.setText(message);

        messageDialog.show();
    }

    private String getPIDOptions() {

        try {
            Opts opts = new Opts();
            opts.fCount = "1";
            opts.fType = "0";
            opts.format = "0";
            opts.timeout = "15000";
            opts.wadh = "";
            opts.iCount = "0";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.pidVer = "2.0";
            opts.env = "P";
            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = "1.0";
            pidOptions.Opts = opts;
            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();

        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }

    private void getBankDetails() {
        final AlertDialog pDialog = new AlertDialog.Builder(AepsActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_progress_dialog, null);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.setView(convertView);
        pDialog.setCancelable(false);
        pDialog.show();


        Call<JsonObject> call = RetrofitClient.getInstance().getApi().getBanks(userId, "Bank");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObject = new JSONObject(String.valueOf(response.body()));
                        String status = responseObject.getString("Status");
                        if (status.equalsIgnoreCase("SUCCESS")) {
                            bankNameArrayList = new ArrayList<>();
                            bankIINArrayList = new ArrayList<>();
                            JSONArray dataArray = responseObject.getJSONArray("Data");
                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataObject = dataArray.getJSONObject(i);
                                String bankName = dataObject.getString("bank_name");
                                String bankIIN = dataObject.getString("bank_iin");
                                String aepsEnabled = dataObject.getString("aeps_enabled");

                                if (aepsEnabled.equalsIgnoreCase("1")) {
                                    bankNameArrayList.add(bankName);
                                    bankIINArrayList.add(bankIIN);
                                }
                            }


                            tvBankName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SpinnerDialog bankDialog = new SpinnerDialog(AepsActivity.this, bankNameArrayList, "Select Bank", R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
                                    bankDialog.setCancellable(true); // for cancellable
                                    bankDialog.setShowKeyboard(false);// for open keyboard by default
                                    bankDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            tvBankName.setText(item);
                                            selectedBankName = bankNameArrayList.get(position);
                                            selectedBankIIN = bankIINArrayList.get(position);
                                        }
                                    });

                                    bankDialog.showSpinerDialog();
                                }
                            });

                            pDialog.dismiss();
                        } else {
                            pDialog.dismiss();
                            MessageDialogHandler.showMessageDialog(AepsActivity.this, AepsActivity.this,
                                    "Please try after some time.", "failed", true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pDialog.dismiss();
                        MessageDialogHandler.showMessageDialog(AepsActivity.this, AepsActivity.this,
                                "Please try after some time.", "failed", true);
                    }
                } else {
                    pDialog.dismiss();
                    MessageDialogHandler.showMessageDialog(AepsActivity.this, AepsActivity.this,
                            "Please try after some time.", "failed", true);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pDialog.dismiss();
                MessageDialogHandler.showMessageDialog(AepsActivity.this, AepsActivity.this,
                        "Please try after some time.", "failed", true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result;
        List<Param> params = new ArrayList<>();
        if (data != null) {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    result = data.getStringExtra("PID_DATA");
                    Log.d("TAG", result);
                    if (result.contains("Device not ready")) {
                        showMessageDialog("Device Not Connected");
                    } else {
                        //if (result.contains("srno") && result.contains("rdsId") && result.contains("rdsVer")) {
                        try {
                               /* pidDataStr = result;
                                getLastLocation();*/

                            pidData = serializer.read(PidData.class, result);
                            pidDataStr = pidData._Data.value;
                            Log.e("xml_data_show", pidDataStr);
                            hmac = pidData._Hmac;
                            sessionKey = pidData._Skey.value;
                            dpId = pidData._DeviceInfo.dpId;
                            rdsId = pidData._DeviceInfo.rdsId;
                            rdsVer = pidData._DeviceInfo.rdsVer;
                            dc = pidData._DeviceInfo.dc;
                            mc = pidData._DeviceInfo.mc;
                            mi = pidData._DeviceInfo.mi;
                            errCode = pidData._Resp.errCode;
                            errInfo = pidData._Resp.errInfo;
                            errCode = pidData._Resp.errCode;
                            fcount = pidData._Resp.fCount;
                            qScore = pidData._Resp.qScore;
                            nmPoints = pidData._Resp.nmPoints;
                            ci = pidData._Skey.ci;
                            params = pidData._DeviceInfo.add_info.params;
                            for (int i = 0; i < params.size(); i++) {
                                String name = params.get(i).name;
                                if (name.equalsIgnoreCase("srno")) {
                                    serialNo = params.get(i).value;
                                } else if (name.equalsIgnoreCase("sysid")) {
                                    String systemId = params.get(i).value;
                                }
                            }

                                /*if (selectedDevice.equalsIgnoreCase("Evolute"))
                                {
                                    for (int i = 0; i < params.size(); i++) {
                                        String name = params.get(i).name;
                                        if (name.equalsIgnoreCase("DeviceSerial")) {
                                            serialNo = params.get(i).value;
                                        } else if (name.equalsIgnoreCase("sysid")) {
                                            String systemId = params.get(i).value;
                                        }
                                    }
                                }*/

                            //serialNo="FIMPEJCA2627";
                            getLastLocation();
                            //getAepsTxn();


                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessageDialog("There are some issues please contact to administration.");
                        }
                       /* } else {
                            showMessageDialog("Device Not Connected");
                        }*/
                    }

                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    lat = location.getLatitude() + "";
                                    longi = location.getLongitude() + "";
                                    //doAepsTransaction();
                                    //TODO: Do AEPS TRANSACTION
                                    getAepsTxn();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(AepsActivity.this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(AepsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(AepsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(AepsActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                1
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat = mLastLocation.getLatitude() + "";
            longi = mLastLocation.getLongitude() + "";
            //doAepsTransaction();
            //TODO: Do AEPS TRANSACTION
            getAepsTxn();

        }
    };

    private void getAepsTxn() {
        try {
            String aadharNo = etAadharCard.getText().toString().trim();
            String amount = etAmount.getText().toString().trim();
            String mobileNo = etMobile.getText().toString().trim();

            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            final AlertDialog pDialog = new AlertDialog.Builder(AepsActivity.this).create();
            LayoutInflater inflater = getLayoutInflater();
            View convertView = inflater.inflate(R.layout.custom_progress_dialog, null);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialog.setView(convertView);
            pDialog.setCancelable(false);
            pDialog.show();


            if (selectedTransactionType.equalsIgnoreCase("BAP") ||
                    selectedTransactionType.equalsIgnoreCase("SAP"))
                amount = "0";

            ReqAeps reqAeps = new ReqAeps();
            ReqAeps.RequestRes request = new ReqAeps.RequestRes();
            //request.setOutletId("1");
            request.setAmount(amount);
            request.setAadhaarUid(aadharNo);
            request.setBankiin(selectedBankIIN);
            request.setLatitude(lat);
            request.setLongitude(longi);
            request.setMobile(mobileNo);
            request.setAgentId("ORBIT" + timeStamp);
            request.setSpKey(selectedTransactionType);
            request.setPidDataType("X");
            request.setPidData(pidDataStr);
            request.setCi(ci);
            request.setDc(dc);
            request.setDpId(dpId);
            request.setErrCode(errCode);
            request.setErrInfo("Success");
            request.setFCount("1");
            request.setTType("null");
            request.setHmac(hmac);
            request.setICount("0");
            request.setMc(mc);
            request.setMi(mi);
            request.setNmPoints(nmPoints);
            request.setPCount("0");
            request.setPType("");
            request.setQScore(qScore);
            request.setRdsId(rdsId);
            request.setRdsVer(rdsVer);
            request.setSessionKey(sessionKey);
            request.setSrno(serialNo);
            reqAeps.setUserAgent(userAgentBrs);
            reqAeps.setRequest(request);
            //reqAeps.setToken("a86bc67b524a1f5db65464debbd91243");
            reqAeps.setMobileNo(mobileNo);
            reqAeps.setUserid(userId);

            Call<JsonObject> call = RetrofitClient.getInstance().getApi().getDirectAeps(reqAeps);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject responseObject = new JSONObject(String.valueOf(response.body()));

                        String status = responseObject.getString("Status");
                        JSONArray dataArray = responseObject.getJSONArray("Data");
                        JSONObject dataObject = dataArray.getJSONObject(0);

                        if (selectedTransactionType.equalsIgnoreCase("SAP")) {

                            JSONArray xmlList;

                            try {

                                xmlList = dataObject.getJSONArray("xmllist");
                            }
                            catch (Exception e)
                            {
                                xmlList = new JSONArray();

                            }




                            Intent intent = new Intent(AepsActivity.this, MiniStatementPaySprintActivity.class);
                            intent.putExtra("transactionType", dataObject.getString("txntype"));
                            intent.putExtra("bankName", selectedBankName);
                            intent.putExtra("responseMobileNumber", mobileNo);
                            intent.putExtra("responseAadharNumber", aadharNo);
                            intent.putExtra("responseBankRRN", dataObject.getString("bankrefno"));
                            intent.putExtra("transactionId", dataObject.getString("agentid"));
                            intent.putExtra("responseAmount", dataObject.getString("orderamount"));
                            intent.putExtra("accountBalance", dataObject.getString("acamount"));
                            intent.putExtra("message", dataObject.getString("status"));
                            intent.putExtra("oldBalance", dataObject.getString("OldBalance"));
                            intent.putExtra("newBalance", dataObject.getString("NewBalance"));
                            intent.putExtra("commission", dataObject.getString("Commission"));
                            intent.putExtra("status", status);
                            intent.putExtra("outputDate", "NA");
                            intent.putExtra("outputTime", "NA");
                            intent.putExtra("miniStatement", xmlList.toString());


                            /////////////////////////////////////////////
                            intent.putExtra("mobileNo", mobileNo);
                            intent.putExtra("lat", lat);
                            intent.putExtra("long", longi);
                            intent.putExtra("bankIIN", selectedBankIIN);
                            intent.putExtra("userId", userId);
                            intent.putExtra("isFastAeps", false);
                            /////////////////////////////////////////////
                            startActivity(intent);
                        }

                        else {

                            Intent intent = new Intent(AepsActivity.this, AepsTransactionActivity.class);
                            intent.putExtra("transactionType", dataObject.getString("txntype"));
                            intent.putExtra("bankName", selectedBankName);
                            intent.putExtra("responseMobileNumber", mobileNo);
                            intent.putExtra("responseAadharNumber", aadharNo);
                            intent.putExtra("responseBankRRN", dataObject.getString("bankrefno"));
                            intent.putExtra("transactionId", dataObject.getString("agentid"));
                            intent.putExtra("responseAmount", dataObject.getString("orderamount"));
                            intent.putExtra("accountBalance", dataObject.getString("acamount"));
                            intent.putExtra("message", dataObject.getString("status"));
                            intent.putExtra("oldBalance", dataObject.getString("OldBalance"));
                            intent.putExtra("newBalance", dataObject.getString("NewBalance"));
                            intent.putExtra("commission", dataObject.getString("Commission"));
                            intent.putExtra("status", status);
                            intent.putExtra("outputDate", "NA");
                            intent.putExtra("outputTime", "NA");

                            /////////////////////////////////////////////
                            intent.putExtra("mobileNo", mobileNo);
                            intent.putExtra("lat", lat);
                            intent.putExtra("long", longi);
                            intent.putExtra("bankIIN", selectedBankIIN);
                            intent.putExtra("userId", userId);
                            intent.putExtra("isFastAeps", false);
                            /////////////////////////////////////////////
                            startActivity(intent);
                        }
                        pDialog.dismiss();


                    } catch (Exception e) {
                        pDialog.dismiss();
                        e.printStackTrace();
                        MessageDialogHandler.showMessageDialog(AepsActivity.this, AepsActivity.this,
                                "Please try after some time", "Failed", false);

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pDialog.dismiss();
                    MessageDialogHandler.showMessageDialog(AepsActivity.this, AepsActivity.this,
                            "Please try after some time", "Failed", false);
                }
            });
        } catch (Exception e) {
            MessageDialogHandler.showMessageDialog(AepsActivity.this, AepsActivity.this,
                    "Please try after some time", "Failed", false);
        }

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(AepsActivity.this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private void initViews() {

        tvBalance=findViewById(R.id.tv_balance);
        tilAmount=findViewById(R.id.til_amount);

        imgCashWithdrawal = findViewById(R.id.img_cash_withdrawal);
        imgMiniStatement = findViewById(R.id.img_mini_statement);
        imgBalanceEnquiry = findViewById(R.id.img_balance_enquiry);
        imgAadharPay = findViewById(R.id.img_aadhar_pay);

        tvBankName = findViewById(R.id.tv_bank);
        imgClose = findViewById(R.id.img_close);

        etAmount = findViewById(R.id.et_amount);
        etMobile = findViewById(R.id.et_mobile_number);
        etAadharCard = findViewById(R.id.et_aadhar_number);


        ckbTermsAndCondition = findViewById(R.id.ckb_terms_condition);

        deviceLayout = findViewById(R.id.device_layout);
        detailsLayout = findViewById(R.id.detail_layout);

        imgMorpho = findViewById(R.id.img_morpho);
        imgMantra = findViewById(R.id.img_mantra);
        imgStartek = findViewById(R.id.img_startek);

        btnProceedDeviceLayout = findViewById(R.id.btn_proceed_device);
        btnNext = findViewById(R.id.btn_next);

    }

}