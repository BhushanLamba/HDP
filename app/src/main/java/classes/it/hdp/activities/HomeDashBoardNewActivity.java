package classes.it.hdp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import classes.it.hdp.BuildConfig;
import classes.it.hdp.R;
import classes.it.hdp.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDashBoardNewActivity extends AppCompatActivity {

    ImageSlider imageSlider;
    ArrayList<SlideModel> mySliderList;
    LinearLayout mobileLayout,dthLayout,aepsLayout;
    TextView tvBalance,tvUserName,tvShortName;
    public static String userId;
    String balance,userName,mobile;
    SharedPreferences sharedPreferences;
    ImageView imgMenu;
    String versionCodeStr;
    FusedLocationProviderClient mFusedLocationClient;
    String  lat = "0.0", longi = "0.0";



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dash_board_new);
        inhitViews();

        versionCodeStr= BuildConfig.VERSION_NAME;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(HomeDashBoardNewActivity.this);


        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(HomeDashBoardNewActivity.this);
        userId=sharedPreferences.getString("userid",null);
        userName=sharedPreferences.getString("username",null);
        mobile=sharedPreferences.getString("mobileno",null);
        tvUserName.setText("hello "+userName);

        String shortName=userName.substring(0,2);
        shortName=shortName.toUpperCase();
        tvShortName.setText(shortName);

        setImageSlider();

        handleClickEvents();
        handleNavigationMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBalance();
    }

    private void handleClickEvents() {
        mobileLayout.setOnClickListener(v->
        {
            Intent intent=new Intent(HomeDashBoardNewActivity.this,RechargeActivity.class);
            intent.putExtra("service", "PREPAID");
            startActivity(intent);
        });

        dthLayout.setOnClickListener(v->
        {
            /*Intent intent=new Intent(HomeDashBoardNewActivity.this,RechargeActivity.class);
            intent.putExtra("service", "PREPAID");
            startActivity(intent);*/
        });

        aepsLayout.setOnClickListener(v->
        {
            getLastLocation();
        });
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
                                    launchAeps();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(HomeDashBoardNewActivity.this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void launchAeps() {
        Intent intent=new Intent(HomeDashBoardNewActivity.this,AepsActivity.class);
        intent.putExtra("balance",balance);
        intent.putExtra("lat",lat);
        intent.putExtra("long",longi);
        startActivity(intent);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(HomeDashBoardNewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(HomeDashBoardNewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(HomeDashBoardNewActivity.this,
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
            launchAeps();

        }
    };

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(HomeDashBoardNewActivity.this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    @SuppressLint("SetTextI18n")
    private void handleNavigationMenu() {

        Dialog navMenuDialog = new Dialog(HomeDashBoardNewActivity.this, R.style.DialogTheme);
        navMenuDialog.setContentView(R.layout.navigation_menu);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.72);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 1.0);

        navMenuDialog.getWindow().setLayout(width, height);

        navMenuDialog.getWindow().setGravity(Gravity.START);
        navMenuDialog.getWindow().setWindowAnimations(R.style.SlidingNavDialog);

        imgMenu.setOnClickListener(view -> {

            if (!navMenuDialog.isShowing()) {
                navMenuDialog.show();

            }

            TextView tvOwnerName = navMenuDialog.findViewById(R.id.tv_owner_name);
            TextView tvUserName = navMenuDialog.findViewById(R.id.tv_user_name);
            TextView tvMobileNumber = navMenuDialog.findViewById(R.id.tv_mobile_number);
            TextView tvVersion = navMenuDialog.findViewById(R.id.tv_version);
            LinearLayout myCommissionLayout = navMenuDialog.findViewById(R.id.my_commission_layout);
            LinearLayout changePasswordLayout = navMenuDialog.findViewById(R.id.change_password_layout);
            LinearLayout changeMpinLayout = navMenuDialog.findViewById(R.id.change_mpin_layout);
            LinearLayout profileLayout = navMenuDialog.findViewById(R.id.profile_layout);
            LinearLayout reportLayout = navMenuDialog.findViewById(R.id.nav_report_layout);
            LinearLayout creditLayout = navMenuDialog.findViewById(R.id.credit_layout);
            LinearLayout debitLayout = navMenuDialog.findViewById(R.id.debit_layout);
            LinearLayout fundRequestLayout = navMenuDialog.findViewById(R.id.fund_transfer_layout);
            LinearLayout addUserLayout = navMenuDialog.findViewById(R.id.add_user_layout);
            LinearLayout logoutLayout = navMenuDialog.findViewById(R.id.logout_layout);


            tvOwnerName.setText(userName);
            tvUserName.setText("*********");
            tvMobileNumber.setText(mobile);
            tvVersion.setText("Version : " + versionCodeStr);

            myCommissionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navMenuDialog.dismiss();
                    //startActivity(new Intent(HomeDashBoardActivity.this, MyCommissionActivity.class));
                }
            });

            changePasswordLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navMenuDialog.dismiss();
                    //startActivity(new Intent(HomeDashBoardActivity.this, ChangePasswordActivity.class));
                }
            });

            changeMpinLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navMenuDialog.dismiss();
                   /* Intent intent=new Intent(HomeDashBoardActivity.this,ChangeMpinActivity.class);
                    intent.putExtra("pinType","mpin");
                    startActivity(intent);*/
                }
            });

            profileLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navMenuDialog.dismiss();
                    //startActivity(new Intent(HomeDashBoardActivity.this, ProfileActivity.class));
                }
            });

            reportLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeDashBoardNewActivity.this, ReportsActivity.class));
                    navMenuDialog.dismiss();
                }
            });

            addUserLayout.setOnClickListener(v -> {
                navMenuDialog.dismiss();
                /*if (!userType.equalsIgnoreCase("retailer")) {
                    startActivity(new Intent(HomeDashBoardActivity.this, AddUserActivity.class));
                }*/
            });

            creditLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navMenuDialog.dismiss();

/*
                    if (!userType.equalsIgnoreCase("retailer")) {
                        Intent intent = new Intent(HomeDashBoardActivity.this, CreditDebitBalanceActivity.class);
                        intent.putExtra("title", "Credit Balance");
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeDashBoardActivity.this, "You can not use this service.", Toast.LENGTH_LONG).show();
                    }
*/
                }
            });

            debitLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navMenuDialog.dismiss();
/*
                    if (!userType.equalsIgnoreCase("retailer")) {
                        Intent intent = new Intent(HomeDashBoardActivity.this, CreditDebitBalanceActivity.class);
                        intent.putExtra("title", "Debit Balance");
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeDashBoardActivity.this, "You can not use this service.", Toast.LENGTH_LONG).show();
                    }
*/
                }
            });

            fundRequestLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navMenuDialog.dismiss();
                    //startActivity(new Intent(HomeDashBoardActivity.this, FundTransferActivity.class));
                }
            });

            logoutLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    navMenuDialog.dismiss();
                    new androidx.appcompat.app.AlertDialog.Builder(HomeDashBoardNewActivity.this)
                            .setCancelable(false)
                            .setMessage("Are you sure ? ")
                            .setTitle("Confirmation")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeDashBoardNewActivity.this);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    startActivity(new Intent(HomeDashBoardNewActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }).show();
                }
            });


        });
    }

    private void getBalance() {
        Call<JsonObject> call= RetrofitClient.getInstance().getApi().getBalance(userId);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject responseObject=new JSONObject(String.valueOf(response.body()));

                    JSONArray dataArray=responseObject.getJSONArray("Data");
                    JSONObject dataObject=dataArray.getJSONObject(0);
                    balance=dataObject.getString("CurrentBalance");

                    tvBalance.setText("₹ "+balance);

                } catch (JSONException e) {
                    e.printStackTrace();
                    tvBalance.setText("₹ 00.0");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                tvBalance.setText("₹ 00.0");
            }
        });
    }

    private void inhitViews() {
        imageSlider=findViewById(R.id.image_slider);
        mobileLayout=findViewById(R.id.mobile_layout);
        aepsLayout=findViewById(R.id.aeps_layout);
        tvBalance=findViewById(R.id.tv_balance);
        tvUserName=findViewById(R.id.tv_name);
        dthLayout=findViewById(R.id.dth_layout);
        imgMenu=findViewById(R.id.img_profile);
        tvShortName=findViewById(R.id.tv_short_name);
    }

    private void setImageSlider() {
        mySliderList = new ArrayList<>();
        mySliderList.add(new SlideModel(R.drawable.slider1, ScaleTypes.FIT));
        mySliderList.add(new SlideModel(R.drawable.slider2, ScaleTypes.FIT));
        mySliderList.add(new SlideModel(R.drawable.slider3, ScaleTypes.FIT));

        imageSlider.setImageList(mySliderList, ScaleTypes.FIT);
    }
}