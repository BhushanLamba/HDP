package classes.it.hdp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import classes.it.hdp.R;
import classes.it.hdp.activities.ShareAepsReportActivity;
import classes.it.hdp.models.AepsModel;


public class AepsReportAdapter extends RecyclerView.Adapter<AepsReportAdapter.ViewHolder>
{
    ArrayList<AepsModel> aepsModelArrayList;
    boolean isInitialReport;
    Context context;

    public AepsReportAdapter(ArrayList<AepsModel> aepsModelArrayList, boolean isInitialReport, Context context) {
        this.aepsModelArrayList = aepsModelArrayList;
        this.isInitialReport = isInitialReport;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_aeps_report,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String transactionId=aepsModelArrayList.get(position).getTransactionId();
        String amount=aepsModelArrayList.get(position).getAmount();
        String comm=aepsModelArrayList.get(position).getComm();
        String balance=aepsModelArrayList.get(position).getNewbalance();
        String dateTime=aepsModelArrayList.get(position).getTimestamp();
        String status=aepsModelArrayList.get(position).getTxnStatus();
        String transactionType=aepsModelArrayList.get(position).getTransactionType();




        holder.tvReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ShareAepsReportActivity.class);
                intent.putExtra("transactionId",aepsModelArrayList.get(position).getTransactionId());
                intent.putExtra("amount",aepsModelArrayList.get(position).getAmount());
                intent.putExtra("comm",aepsModelArrayList.get(position).getComm());
                intent.putExtra("balance",aepsModelArrayList.get(position).getNewbalance());
                intent.putExtra("dateTime",aepsModelArrayList.get(position).getTimestamp());
                intent.putExtra("status",aepsModelArrayList.get(position).getTxnStatus());
                intent.putExtra("transactionType",aepsModelArrayList.get(position).getTransactionType());
                intent.putExtra("oldBalance",aepsModelArrayList.get(position).getOldBalance());
                intent.putExtra("aadharNo",aepsModelArrayList.get(position).getAadharNo());
                intent.putExtra("panNo",aepsModelArrayList.get(position).getPanNo());
                intent.putExtra("bankName",aepsModelArrayList.get(position).getBankName());
                intent.putExtra("surcharge",aepsModelArrayList.get(position).getSurcharge());
                intent.putExtra("status",aepsModelArrayList.get(position).getTxnStatus());
                context.startActivity(intent);
            }
        });

        if (status.equalsIgnoreCase("Failed"))
        {
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.button_back));
        }
        else
        {
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.card_back_5));
        }


        holder.tvTransactionId.setText(transactionId);
        holder.tvAmount.setText("₹ "+amount);
        holder.tvComm.setText("₹ "+comm);
        holder.tvBalance.setText("₹ "+balance);
        holder.tvDateTime.setText(dateTime);
        holder.tvStatus.setText(status);

        //TODO: Change this transaction type code

        if (transactionType.equalsIgnoreCase("SAP"))
        {
            holder.tvTransactionType.setText("Mini Statement");
        }
        else if (transactionType.equalsIgnoreCase("WAP"))
        {
            holder.tvTransactionType.setText("Cash withdrawal");
        }
        else if (transactionType.equalsIgnoreCase("BAP"))
        {
            holder.tvTransactionType.setText("Balance Enquiry");
        }
        else if (transactionType.equalsIgnoreCase("MZZ"))
        {
            holder.tvTransactionType.setText("Aadhar Pay");
        }
        else
        {
            holder.tvTransactionType.setText(transactionType);
        }

    }

    @Override
    public int getItemCount() {
        return aepsModelArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTransactionId,tvAmount,tvComm,tvTransactionType,tvBalance,tvDateTime,tvStatus,tvReceipt;
        ImageView imgReceipt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTransactionId=itemView.findViewById(R.id.tv_all_report_transaction_id);
            tvAmount=itemView.findViewById(R.id.tv_all_report_amount);
            tvComm=itemView.findViewById(R.id.tv_all_report_commission);
            tvBalance=itemView.findViewById(R.id.tv_new_balance);
            tvDateTime=itemView.findViewById(R.id.tv_all_report_date_time);
            tvStatus=itemView.findViewById(R.id.tv_all_report_status);
            tvTransactionType=itemView.findViewById(R.id.tv_transaction_type);
            imgReceipt=itemView.findViewById(R.id.img_receipt);
            tvReceipt=itemView.findViewById(R.id.tv_receipt);
        }
    }
}
