package classes.it.hdp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import classes.it.hdp.R;

public class RechargeStatusActivity extends AppCompatActivity {

    TextView tvTransactionId,tvNumber,tvOperator,tvAmount,tvDate,tvTime;
    ImageView imgStatus;
    AppCompatButton btnOk,btnShare;

    String transactionId,number,operator,amount,date,time,status;

    SharedPreferences sharedPreferences;
    TextView tvShopDetails;
    int FILE_PERMISSION = 45;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_status);

        intitViews();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RechargeStatusActivity.this);
        String ownerName = sharedPreferences.getString("username", null);
        String mobile = sharedPreferences.getString("mobileno", null);
        String role = sharedPreferences.getString("usertype", null);
        tvShopDetails.setText("Name " + ownerName + "(" + role + ")" + "\n" + "Contact No. " + mobile);

        transactionId=getIntent().getStringExtra("responseTransactionId");
        number=getIntent().getStringExtra("responseNumber");
        operator=getIntent().getStringExtra("responseOperator");
        amount=getIntent().getStringExtra("responseAmount");
        date=getIntent().getStringExtra("outputDate");
        time=getIntent().getStringExtra("outputTime");
        status=getIntent().getStringExtra("responseStatus");

        tvTransactionId.setText(transactionId);
        tvNumber.setText(number);
        tvOperator.setText(operator);
        tvAmount.setText(amount);
        tvDate.setText(date);
        tvTime.setText(time);

        if (status.equalsIgnoreCase("Success") || status.equalsIgnoreCase("Successful"))
            imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.success));
        else if (status.equalsIgnoreCase("Pending"))
            imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.pending));
        else
            imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.failed));

        btnOk.setOnClickListener(v->
        {
            finish();
        });

        btnShare.setOnClickListener(v->
        {
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, FILE_PERMISSION);

        });


    }

    public void checkPermission(String writePermission, String readPermission, int requestCode) {
        if (ContextCompat.checkSelfPermission(RechargeStatusActivity.this, writePermission) == PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(RechargeStatusActivity.this, readPermission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(RechargeStatusActivity.this, new String[]{writePermission, readPermission}, requestCode);
        } else {
            //takeAndShareScreenShot();
            Bitmap bitmap = getScreenBitmap();
            shareReceipt(bitmap);

        }
    }

    public Bitmap getScreenBitmap() {
        Bitmap b = null;
        try {
            LinearLayout shareReportLayout = findViewById(R.id.share_report_layout);
            Bitmap bitmap = Bitmap.createBitmap(shareReportLayout.getWidth(),
                    shareReportLayout.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            shareReportLayout.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                final AlertDialog.Builder permissionDialog = new AlertDialog.Builder(RechargeStatusActivity.this);
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

    private void intitViews() {
        tvTransactionId=findViewById(R.id.tv_transaction_id);
        tvNumber=findViewById(R.id.tv_number);
        tvOperator=findViewById(R.id.tv_operator);
        tvAmount=findViewById(R.id.tv_amount);
        tvDate=findViewById(R.id.tv_date);
        tvTime=findViewById(R.id.tv_time);
        imgStatus=findViewById(R.id.img_status);
        btnOk=findViewById(R.id.btn_ok);
        btnShare=findViewById(R.id.btn_share);
        tvShopDetails = findViewById(R.id.tv_shop_details);

    }
}