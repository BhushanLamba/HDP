package classes.it.hdp.classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import classes.it.hdp.R;


public class MessageDialogHandler {
    @SuppressLint("UseCompatLoadingForDrawables")
    public static void showMessageDialog(Context context, Activity activity, String message,
                                         String imageType, boolean isFinish) {
        final android.app.AlertDialog messageDialog = new android.app.AlertDialog.Builder(context).create();
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.message_dialog_layout, null);
        messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.setView(convertView);
        messageDialog.setCancelable(false);
        messageDialog.show();

        ImageView imgClose = convertView.findViewById(R.id.img_close);
        ImageView imgStatus = convertView.findViewById(R.id.img_status);
        TextView tvMessage = convertView.findViewById(R.id.tv_message);
        Button btnOk = convertView.findViewById(R.id.btn_ok);

        imgClose.setOnClickListener(v ->
        {
            if (isFinish)
                activity.finish();
            else
                messageDialog.dismiss();
        });

        btnOk.setOnClickListener(v ->
        {
            if (isFinish)
                activity.finish();
            else
                messageDialog.dismiss();
        });

        tvMessage.setText(message);

        if (imageType.equalsIgnoreCase("SUCCESS"))
            imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.success));
        else if (imageType.equalsIgnoreCase("PENDING"))
            imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.pending));
        else
            imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.failed));


    }


}
