package classes.it.hdp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import classes.it.hdp.R;
import classes.it.hdp.classes.MessageDialogHandler;
import classes.it.hdp.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ImageSlider imageSlider;
    ArrayList<SlideModel> mySliderList;
    EditText etUserId, etUserPassword;

    Button btnLogin,  btnForgetPassword;

    String userid, username, usertype, mobileno, pancard, aadharCard, emailId,address,city,state,pincode,shopName,lastName;
    String loginUserName,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inhitViews();

        setImageSlider();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputs()) {
                    if (checkInternetState()) {

                        login();
                        //startActivity(new Intent(LoginActivity.this,HomeDashboardActivity.class));


                    } else {
                        showSnackbar();
                    }
                }
            }
        });

    }

    private void inhitViews() {
        imageSlider=findViewById(R.id.image_slider);
        etUserId = findViewById(R.id.et_user_id);
        etUserPassword = findViewById(R.id.et_user_password);
        btnLogin = findViewById(R.id.btn_login);
        btnForgetPassword = findViewById(R.id.btn_forget_password);
    }

    private void setImageSlider() {
        mySliderList = new ArrayList<>();
        mySliderList.add(new SlideModel(R.drawable.slider1, ScaleTypes.FIT));
        mySliderList.add(new SlideModel(R.drawable.slider2, ScaleTypes.FIT));
        mySliderList.add(new SlideModel(R.drawable.slider3, ScaleTypes.FIT));

        imageSlider.setImageList(mySliderList, ScaleTypes.FIT);
    }

    private boolean checkInputs() {
        if (!TextUtils.isEmpty(etUserId.getText())) {
            if (!TextUtils.isEmpty(etUserPassword.getText())) {

                return true;
            } else {
                etUserPassword.setError("Password can't be empty.");
                return false;
            }
        } else {
            etUserId.setError("User ID can't be empty.");
            return false;
        }
    }

    private boolean checkInternetState() {

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

    private void showSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.loginLayout), "No Internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void login() {

        final android.app.AlertDialog progressDialog = new android.app.AlertDialog.Builder(LoginActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_progress_dialog, null);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setView(convertView);
        progressDialog.setCancelable(false);
        progressDialog.show();

        loginUserName = etUserId.getText().toString().trim();
        password = etUserPassword.getText().toString().trim();

        Call<JsonObject> call = RetrofitClient.getInstance().getApi().login(loginUserName, password);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(String.valueOf(response.body()));

                        String statuscode = jsonObject1.getString("Status");

                        if (statuscode.equalsIgnoreCase("SUCCESS")) {


                            JSONArray dataArray = jsonObject1.getJSONArray("Data");
                            JSONObject jsonObject = dataArray.getJSONObject(0);
                            userid = jsonObject.getString("UserKey");
                            username = jsonObject.getString("FirstName");
                            lastName = jsonObject.getString("LastName");
                            usertype = jsonObject.getString("UserRole");
                            mobileno = jsonObject.getString("UserMob");
                            pancard = jsonObject.getString("PAN");
                            aadharCard = jsonObject.getString("Aadhaar");
                            emailId = jsonObject.getString("Email");
                            address = jsonObject.getString("Address");
                            city = jsonObject.getString("City");
                            state = jsonObject.getString("State");
                            pincode = jsonObject.getString("PINCode");
                            shopName = jsonObject.getString("ShopName");

                            progressDialog.dismiss();
                            //sendOtp();

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userid", userid);
                            editor.putString("username", username);
                            editor.putString("pancard", pancard);
                            editor.putString("mobileno", mobileno);
                            editor.putString("usertype", usertype);
                            editor.putString("adharcard", aadharCard);
                            editor.putString("loginUserName", loginUserName);
                            editor.putString("password", password);
                            editor.putString("email", emailId);
                            editor.apply();

                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MPINActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                            String message = jsonObject1.getString("Data");
                            MessageDialogHandler.showMessageDialog(LoginActivity.this, LoginActivity.this,
                                    message, "FAILED", false);
                        }

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        MessageDialogHandler.showMessageDialog(LoginActivity.this, LoginActivity.this,
                                "Please try again later!!!", "FAILED", false);
                    }

                } else {
                    progressDialog.dismiss();
                    MessageDialogHandler.showMessageDialog(LoginActivity.this, LoginActivity.this,
                            "Please try again later!!!", "FAILED", false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressDialog.dismiss();
                MessageDialogHandler.showMessageDialog(LoginActivity.this, LoginActivity.this,
                        "Please try after some time", "FAILED", false);

            }
        });
    }


}