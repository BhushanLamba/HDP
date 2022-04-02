package classes.it.hdp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.io.ByteArrayOutputStream;

import classes.it.hdp.R;

public class AepsTransactionActivity extends AppCompatActivity {

    String transactionType, bankName, responseMobileNumber, responseAadharNumber, responseBankRRN, transactionId, status, responseAmount, outputDate,
            outputTime, accountBalance, message,oldBalance,commission,newBalance;

    TextView tvTransactionType, tvBankName, tvMobileNumber, tvAadharNumber, tvBankRRN, tvTransactionId,
            tvStatus, tvAmount, tvDate, tvTime,
            tvAccountBalance, tvMessage,tvOldBalance,tvNewBalance,tvCommission;

    AppCompatButton btnDone, btnMiniStatement, btnCashWithdraw;
    ImageView imgShare, imgStatus;
    int FILE_PERMISSION = 45;
    boolean isFastAeps;

    String mobileNo,lat,longi,bankIin,userId;

    SharedPreferences sharedPreferences;
    TextView tvShopDetails;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps_transaction);

        initViews();


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AepsTransactionActivity.this);
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
        outputDate = getIntent().getStringExtra("outputDate");
        outputTime = getIntent().getStringExtra("outputTime");
        accountBalance = getIntent().getStringExtra("accountBalance");
        message = getIntent().getStringExtra("message");

        oldBalance = getIntent().getStringExtra("oldBalance");
        commission = getIntent().getStringExtra("commission");
        newBalance = getIntent().getStringExtra("newBalance");
        isFastAeps = getIntent().getBooleanExtra("isFastAeps", false);


        ///////////////////////////////////
        mobileNo = getIntent().getStringExtra("mobileNo");
        lat = getIntent().getStringExtra("lat");
        longi = getIntent().getStringExtra("long");
        bankIin = getIntent().getStringExtra("bankIIN");
        userId = getIntent().getStringExtra("userId");
        ///////////////////////////////////


        tvTransactionType.setText(transactionType);
        tvBankName.setText(bankName);
        tvMobileNumber.setText(responseMobileNumber);
        tvAadharNumber.setText("XXXX XXXX "+responseAadharNumber.substring(8,12));
        tvBankRRN.setText(responseBankRRN);
        tvTransactionId.setText(transactionId);
        tvStatus.setText(status);
        tvMessage.setText(message);
        tvAmount.setText("₹ " + responseAmount);
        tvDate.setText(outputDate);
        tvTime.setText(outputTime);
        tvAccountBalance.setText(accountBalance);

        tvOldBalance.setText("₹ " +oldBalance);
        tvCommission.setText("₹ " +commission);
        tvNewBalance.setText("₹ " +newBalance);

        btnDone.setOnClickListener(view -> {
            finish();
        });

        if (status.equalsIgnoreCase("SUCCESS") || status.equalsIgnoreCase("successful")
                || status.equalsIgnoreCase("complete") || status.equalsIgnoreCase("completed")
                || status.equalsIgnoreCase("done")) {
            imgStatus.setImageResource(R.drawable.success);
        } else if (status.equalsIgnoreCase("PENDING")) {
            imgStatus.setImageResource(R.drawable.pending);

        } else {
            imgStatus.setImageResource(R.drawable.failed);

        }

        if (isFastAeps || transactionType.equalsIgnoreCase("Cash Withdraw")
                || transactionType.equalsIgnoreCase("AADHAR PAY Withdraw")) {
            btnMiniStatement.setVisibility(View.GONE);
            btnCashWithdraw.setVisibility(View.GONE);
        }
        if (transactionType.equalsIgnoreCase("mini statement"))
        {
            btnMiniStatement.setVisibility(View.GONE);
        }


        btnMiniStatement.setOnClickListener(v ->
        {
           /* Intent intent = new Intent(AepsTransactionActivity.this, FastAepsActivity.class);
            intent.putExtra("mobileNo",mobileNo);
            intent.putExtra("aadharNo",responseAadharNumber);
            intent.putExtra("transactionType","SAP");
            intent.putExtra("lat",lat);
            intent.putExtra("long",longi);
            intent.putExtra("bankIIN",bankIin);
            intent.putExtra("userId",userId);
            startActivity(intent);
            finish();*/

        });

        btnCashWithdraw.setOnClickListener(v ->
        {
            /*Intent intent = new Intent(AepsTransactionActivity.this, FastAepsActivity.class);
            intent.putExtra("mobileNo",mobileNo);
            intent.putExtra("aadharNo",responseAadharNumber);
            intent.putExtra("transactionType","WAP");
            intent.putExtra("lat",lat);
            intent.putExtra("long",longi);
            intent.putExtra("bankIIN",bankIin);
            intent.putExtra("userId",userId);
            startActivity(intent);
            finish();*/
        });

        btnMiniStatement.setVisibility(View.GONE);
        btnCashWithdraw.setVisibility(View.GONE);

        imgShare.setOnClickListener(view -> {
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, FILE_PERMISSION);

        });
    }

    public void checkPermission(String writePermission, String readPermission, int requestCode) {
        if (ContextCompat.checkSelfPermission(AepsTransactionActivity.this, writePermission) == PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(AepsTransactionActivity.this, readPermission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(AepsTransactionActivity.this, new String[]{writePermission, readPermission}, requestCode);
        } else {
            //takeAndShareScreenShot();
            imgShare.setVisibility(View.GONE);
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
            imgShare.setVisibility(View.VISIBLE);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgShare.setVisibility(View.VISIBLE);
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
                final AlertDialog.Builder permissionDialog = new AlertDialog.Builder(AepsTransactionActivity.this);
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
        tvStatus = findViewById(R.id.tv_status);
        tvAmount = findViewById(R.id.tv_amount);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvAccountBalance = findViewById(R.id.tv_account_balance);
        tvMessage = findViewById(R.id.tv_message);

        tvOldBalance = findViewById(R.id.tv_old_balance);
        tvCommission = findViewById(R.id.tv_commission);
        tvNewBalance = findViewById(R.id.tv_new_balance);

        btnDone = findViewById(R.id.btn_done);
        btnMiniStatement = findViewById(R.id.btn_mini_statment);
        btnCashWithdraw = findViewById(R.id.btn_cash_withdraw);

        imgShare = findViewById(R.id.img_share);
        imgStatus = findViewById(R.id.img_status);

        tvShopDetails = findViewById(R.id.tv_shop_details);

    }
}