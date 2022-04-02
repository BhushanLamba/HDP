package classes.it.hdp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import classes.it.hdp.R;
import classes.it.hdp.adapters.PaySprintMiniStatementAdapter;
import classes.it.hdp.models.MiniStatementModel;

public class MiniStatementPaySprintActivity extends AppCompatActivity {

    String transactionType, bankName, responseMobileNumber, responseAadharNumber, responseBankRRN,
            transactionId, status, responseAmount, outputDate, oldBalance, commission, newBalance,
            outputTime, accountBalance, miniStatement, message;

    TextView tvTransactionType, tvBankName, tvMobileNumber,
            tvAadharNumber, tvBankRRN, tvTransactionId, tvDateTime, tvBankBalance,
            tvMessage, tvOldBalance, tvNewBalance, tvCommission;
    RecyclerView miniStatementRecycler;

    AppCompatButton btnDone;
    ImageView imgShare, imgStatus;
    int FILE_PERMISSION = 45;

    SharedPreferences sharedPreferences;
    TextView tvShopDetails;

    ConstraintLayout hideLayout;

    TextView tvTopMessage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_statement_pay_sprint);
        initViews();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MiniStatementPaySprintActivity.this);
        String ownerName = sharedPreferences.getString("username", null);
        String mobile = sharedPreferences.getString("mobileno", null);
        String role = sharedPreferences.getString("usertype", null);
        tvShopDetails.setText("Name " + ownerName + "(" + role + ")" + "\n" + "Contact No. " + mobile);

        transactionType = getIntent().getStringExtra("transactionType");
        bankName = getIntent().getStringExtra("bankName");
        responseMobileNumber = getIntent().getStringExtra("responseMobileNumber");
        responseAadharNumber = getIntent().getStringExtra("responseAadharNumber");
        responseBankRRN = getIntent().getStringExtra("responseBankRRN");
        transactionId = getIntent().getStringExtra("transactionId");
        status = getIntent().getStringExtra("status");
        responseAmount = getIntent().getStringExtra("responseAmount");
        message = getIntent().getStringExtra("message");
        outputDate = getIntent().getStringExtra("outputDate");
        outputTime = getIntent().getStringExtra("outputTime");
        accountBalance = getIntent().getStringExtra("accountBalance");
        miniStatement = getIntent().getStringExtra("miniStatement");
        oldBalance = getIntent().getStringExtra("oldBalance");
        commission = getIntent().getStringExtra("commission");
        newBalance = getIntent().getStringExtra("newBalance");

        try {
            JSONArray miniStatementArray = new JSONArray(miniStatement);
            ArrayList<MiniStatementModel> miniStatementModelArrayList = new ArrayList<>();
            for (int i = 0; i < miniStatementArray.length(); i++) {
                JSONObject miniStatementObject = miniStatementArray.getJSONObject(i);

                String item = miniStatementObject.getString("item");
                JSONObject jsonObject1 = new JSONObject(item);
                MiniStatementModel miniStatementModel = new MiniStatementModel();

                String date = jsonObject1.getString("date");
                String txnType = jsonObject1.getString("txnType");
                String amount = jsonObject1.getString("amount");
                String narration = jsonObject1.getString("narration");

                miniStatementModel.setDate(date);
                miniStatementModel.setTxnType(txnType);
                miniStatementModel.setAmount(amount);
                miniStatementModel.setDescription(narration);

                miniStatementModelArrayList.add(miniStatementModel);

                PaySprintMiniStatementAdapter adapter = new PaySprintMiniStatementAdapter(miniStatementModelArrayList);
                miniStatementRecycler.setLayoutManager(new LinearLayoutManager(MiniStatementPaySprintActivity.this, RecyclerView.VERTICAL, false));
                miniStatementRecycler.setAdapter(adapter);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvTransactionType.setText(transactionType);
        tvBankName.setText(bankName);
        tvMobileNumber.setText(responseMobileNumber);
        tvAadharNumber.setText("XXXX XXXX " + responseAadharNumber.substring(8, 12));
        tvBankRRN.setText(responseBankRRN);
        tvTransactionId.setText(transactionId);
        tvMessage.setText(message);
        tvTopMessage.setText(message);
        tvOldBalance.setText("₹ " + oldBalance);
        tvCommission.setText("₹ " + commission);
        tvNewBalance.setText("₹ " + newBalance);

        tvDateTime.setText(outputDate + "," + outputTime);

        btnDone.setOnClickListener(view -> {
            finish();
        });

        if (status.equalsIgnoreCase("SUCCESS") || status.equalsIgnoreCase("successful")
                || status.equalsIgnoreCase("complete") || status.equalsIgnoreCase("completed")
                || status.equalsIgnoreCase("done")) {
            tvBankBalance.setText("₹ " + accountBalance);
            imgStatus.setImageResource(R.drawable.success);
        } else if (status.equalsIgnoreCase("PENDING")) {
            imgStatus.setImageResource(R.drawable.pending);
            tvBankBalance.setText("₹ 0");

        } else {
            imgStatus.setImageResource(R.drawable.failed);
            tvBankBalance.setText("₹ 0");


        }

        imgShare.setOnClickListener(view -> {
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, FILE_PERMISSION);

        });

    }

    public void checkPermission(String writePermission, String readPermission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MiniStatementPaySprintActivity.this, writePermission) == PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(MiniStatementPaySprintActivity.this, readPermission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(MiniStatementPaySprintActivity.this, new String[]{writePermission, readPermission}, requestCode);
        } else {
            //takeAndShareScreenShot();
            hideLayout.setVisibility(View.GONE);
            Bitmap bitmap = getScreenBitmap();
            shareReceipt(bitmap);

        }
    }

    public Bitmap getScreenBitmap() {
        Bitmap b = null;
        try {
            ConstraintLayout shareReportLayout = findViewById(R.id.share_report_layout);
            Bitmap bitmap = Bitmap.createBitmap(shareReportLayout.getWidth(),
                    shareReportLayout.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            shareReportLayout.draw(canvas);
            hideLayout.setVisibility(View.VISIBLE);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideLayout.setVisibility(View.VISIBLE);
        return b;
    }

    private void shareReceipt(Bitmap bitmap) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            Uri imageUri = Uri.parse(path);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(share, "Share link!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FILE_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                final AlertDialog.Builder permissionDialog = new AlertDialog.Builder(MiniStatementPaySprintActivity.this);
                permissionDialog.setTitle("Permission Required");
                permissionDialog.setMessage("You can set permission manually." + "\n" + "Settings-> App Permission -> Allow Storage permission.");
                permissionDialog.setCancelable(false);
                permissionDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                permissionDialog.show();

            }
        }
    }

    private void initViews() {
        tvTransactionType = findViewById(R.id.tv_transaction_type);
        tvBankName = findViewById(R.id.tv_bank_name);
        tvMobileNumber = findViewById(R.id.tv_mobile_number);
        tvAadharNumber = findViewById(R.id.tv_aadhar_no);
        tvBankRRN = findViewById(R.id.tv_rrn);
        tvTransactionId = findViewById(R.id.tv_transaction_id);
        tvDateTime = findViewById(R.id.tv_date_time);
        tvBankBalance = findViewById(R.id.tv_bank_balance);
        tvMessage = findViewById(R.id.tv_message);
        tvOldBalance = findViewById(R.id.tv_old_balance);
        tvCommission = findViewById(R.id.tv_commission);
        tvNewBalance = findViewById(R.id.tv_new_balance);

        miniStatementRecycler = findViewById(R.id.mini_statement_recycler);

        btnDone = findViewById(R.id.btn_done);

        imgShare = findViewById(R.id.img_share);
        imgStatus = findViewById(R.id.img_status);

        tvShopDetails = findViewById(R.id.tv_shop_details);
        hideLayout = findViewById(R.id.layout1);


        tvTopMessage = findViewById(R.id.tv_status_text);

    }
}