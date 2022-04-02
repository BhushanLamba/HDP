package classes.it.hdp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import classes.it.hdp.R;

public class ReportsActivity extends AppCompatActivity {

    ConstraintLayout rechargeReportLayout,upiReportLayout,walletSummaryLayout,dmtReportLayout,
            aepsReportLayout,aepsLedgerLayout,settlementReportLayout,myCommissionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        initViews();

        rechargeReportLayout.setOnClickListener(v->
        {
            startActivity(new Intent(this, RechargeReportActivity.class));
        });

        upiReportLayout.setOnClickListener(v->
        {
            //startActivity(new Intent(getContext(), UpiReportActivity.class));
        });

        walletSummaryLayout.setOnClickListener(v->
        {
            /*Intent intent=new Intent(this,LedgerReportActivity.class);
            intent.putExtra("walletType","Main");
            startActivity(intent);*/
        });

        aepsLedgerLayout.setOnClickListener(v->
        {
            /*Intent intent=new Intent(this,LedgerReportActivity.class);
            intent.putExtra("walletType","AEPS");
            startActivity(intent);*/
        });

        dmtReportLayout.setOnClickListener(v->
        {
            //startActivity(new Intent(this, DmtReportActivity.class));
        });

        aepsReportLayout.setOnClickListener(v->
        {
            startActivity(new Intent(this, AepsReportActivity.class));
        });

        settlementReportLayout.setOnClickListener(v->
        {
            //startActivity(new Intent(this, SettlementReportActivity.class));

        });

        myCommissionLayout.setOnClickListener(v->
        {
            //startActivity(new Intent(this, MyCommissionActivity.class));

        });


    }

    private void initViews() {
        rechargeReportLayout=findViewById(R.id.recharge_reports_layout);
        upiReportLayout=findViewById(R.id.upi_report_layout);
        walletSummaryLayout=findViewById(R.id.wallet_summary_layout);
        dmtReportLayout=findViewById(R.id.money_report_layout);
        aepsReportLayout=findViewById(R.id.aeps_reports_layout);
        aepsLedgerLayout=findViewById(R.id.aeps_ledger_layout);
        settlementReportLayout=findViewById(R.id.settlement_reports_layout);
        myCommissionLayout=findViewById(R.id.commission_report_layout);
    }
}