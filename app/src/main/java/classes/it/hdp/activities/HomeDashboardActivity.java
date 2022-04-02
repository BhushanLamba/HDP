package classes.it.hdp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import classes.it.hdp.R;
import classes.it.hdp.fragments.HomeFragment;

public class HomeDashboardActivity extends AppCompatActivity {

    LinearLayout bottomHomeLayout,bottomReportLayout,bottomBankLayout,bottomAboutLayout,bottomSupport;
    ImageView imgHome,imgReport,imgBank,imgAbout,imgSupport;
    TextView tvHome,tvReport,tvBank,tvAbout,tvSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);
        initViews();

        loadFragment(new HomeFragment());
        handleClickEvents();



    }

    private void handleClickEvents() {
        bottomHomeLayout.setOnClickListener(v->
        {
            imgHome.setImageResource(R.drawable.home);
            imgReport.setImageResource(R.drawable.report_unselected);
            imgBank.setImageResource(R.drawable.bank_unslected);
            imgAbout.setImageResource(R.drawable.about_us_unselected);
            imgSupport.setImageResource(R.drawable.support_unslected);

            tvHome.setTextColor(getResources().getColor(R.color.purple_200));
            tvReport.setTextColor(getResources().getColor(R.color.gray));
            tvBank.setTextColor(getResources().getColor(R.color.gray));
            tvAbout.setTextColor(getResources().getColor(R.color.gray));
            tvSupport.setTextColor(getResources().getColor(R.color.gray));
        });

        bottomReportLayout.setOnClickListener(v->
        {
            imgHome.setImageResource(R.drawable.home_unselected);
            imgReport.setImageResource(R.drawable.report);
            imgBank.setImageResource(R.drawable.bank_unslected);
            imgAbout.setImageResource(R.drawable.about_us_unselected);
            imgSupport.setImageResource(R.drawable.support_unslected);

            tvHome.setTextColor(getResources().getColor(R.color.gray));
            tvReport.setTextColor(getResources().getColor(R.color.purple_200));
            tvBank.setTextColor(getResources().getColor(R.color.gray));
            tvAbout.setTextColor(getResources().getColor(R.color.gray));
            tvSupport.setTextColor(getResources().getColor(R.color.gray));
        });

        bottomBankLayout.setOnClickListener(v->
        {
            imgHome.setImageResource(R.drawable.home_unselected);
            imgReport.setImageResource(R.drawable.report_unselected);
            imgBank.setImageResource(R.drawable.bank);
            imgAbout.setImageResource(R.drawable.about_us_unselected);
            imgSupport.setImageResource(R.drawable.support_unslected);

            tvHome.setTextColor(getResources().getColor(R.color.gray));
            tvReport.setTextColor(getResources().getColor(R.color.gray));
            tvBank.setTextColor(getResources().getColor(R.color.purple_200));
            tvAbout.setTextColor(getResources().getColor(R.color.gray));
            tvSupport.setTextColor(getResources().getColor(R.color.gray));
        });

        bottomAboutLayout.setOnClickListener(v->
        {
            imgHome.setImageResource(R.drawable.home_unselected);
            imgReport.setImageResource(R.drawable.report_unselected);
            imgBank.setImageResource(R.drawable.bank_unslected);
            imgAbout.setImageResource(R.drawable.about_us);
            imgSupport.setImageResource(R.drawable.support_unslected);

            tvHome.setTextColor(getResources().getColor(R.color.gray));
            tvReport.setTextColor(getResources().getColor(R.color.gray));
            tvBank.setTextColor(getResources().getColor(R.color.gray));
            tvAbout.setTextColor(getResources().getColor(R.color.purple_200));
            tvSupport.setTextColor(getResources().getColor(R.color.gray));
        });

        bottomSupport.setOnClickListener(v->
        {
            imgHome.setImageResource(R.drawable.home_unselected);
            imgReport.setImageResource(R.drawable.report_unselected);
            imgBank.setImageResource(R.drawable.bank_unslected);
            imgAbout.setImageResource(R.drawable.about_us_unselected);
            imgSupport.setImageResource(R.drawable.support);

            tvHome.setTextColor(getResources().getColor(R.color.gray));
            tvReport.setTextColor(getResources().getColor(R.color.gray));
            tvBank.setTextColor(getResources().getColor(R.color.gray));
            tvAbout.setTextColor(getResources().getColor(R.color.gray));
            tvSupport.setTextColor(getResources().getColor(R.color.purple_200));
        });
    }

    private void initViews() {
        bottomHomeLayout=findViewById(R.id.home_layout);
        bottomReportLayout=findViewById(R.id.reports_layout);
        bottomBankLayout=findViewById(R.id.bank_details_layout);
        bottomAboutLayout=findViewById(R.id.about_us_layout);
        bottomSupport=findViewById(R.id.assistance_layout);

        imgHome=findViewById(R.id.img_home);
        imgReport=findViewById(R.id.img_report);
        imgBank=findViewById(R.id.img_bank);
        imgAbout=findViewById(R.id.img_about_us);
        imgSupport=findViewById(R.id.img_support);

        tvHome=findViewById(R.id.tv_home);
        tvReport=findViewById(R.id.tv_report);
        tvBank=findViewById(R.id.tv_bank);
        tvAbout=findViewById(R.id.tv_about_us);
        tvSupport=findViewById(R.id.tv_support);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
    }

}