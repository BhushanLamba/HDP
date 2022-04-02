package classes.it.hdp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Spinner;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

import classes.it.hdp.R;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

public class MPINActivity extends AppCompatActivity {
    ImageSlider imageSlider;
    ArrayList<SlideModel> mySliderList;
    Spinner spinnerLanguage;

    ArrayList<String> languageList;
    PinView mpinView;
    TextView tvName;
    SharedPreferences sharedPreferences;
    AppCompatButton btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpinactivity);
        inhitViews();
        setLanguageSpinner();
        setImageSlider();

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MPINActivity.this);
        String name=sharedPreferences.getString("username","");
        tvName.setText(name);

        mpinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String mpin=mpinView.getText().toString().trim();
                if (mpin.length()==4) {
                    startActivity(new Intent(MPINActivity.this, HomeDashBoardNewActivity.class));
                    finish();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnLogout.setOnClickListener(v->
        {
            new androidx.appcompat.app.AlertDialog.Builder(MPINActivity.this)
                    .setCancelable(false)
                    .setMessage("Are you sure ? ")
                    .setTitle("Confirmation")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MPINActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            startActivity(new Intent(MPINActivity.this, LoginActivity.class));
                            finish();
                        }
                    }).show();
        });


    }

    private void setLanguageSpinner() {
        languageList=new ArrayList<>();
        languageList.add("en");
        languageList.add("hi");

        HintSpinner<String> hintSpinner = new HintSpinner<>(
                spinnerLanguage,
                new HintAdapter<String>(MPINActivity.this, "en"
                        , languageList),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                    }
                });

        hintSpinner.init();
    }

    private void inhitViews() {
        imageSlider=findViewById(R.id.image_slider);
        spinnerLanguage=findViewById(R.id.spinner_language);
        mpinView=findViewById(R.id.otp_pin_view);
        tvName=findViewById(R.id.tv_name);
        btnLogout=findViewById(R.id.btn_logout);

    }

    private void setImageSlider() {
        mySliderList = new ArrayList<>();
        mySliderList.add(new SlideModel(R.drawable.slider1, ScaleTypes.FIT));
        mySliderList.add(new SlideModel(R.drawable.slider2, ScaleTypes.FIT));
        mySliderList.add(new SlideModel(R.drawable.slider3, ScaleTypes.FIT));

        imageSlider.setImageList(mySliderList, ScaleTypes.FIT);
    }
}