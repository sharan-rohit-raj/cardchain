package com.example.cardchain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogMessage {
    public static final String SUCCESS_MESSAGE_TYPE = "success";
    public static final String ERROR_MESSAGE_TYPE = "error";
    public static void display(final Context ctx, String title, String message_type){
        final Dialog dialog = new Dialog(ctx);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        Button dialog_button = dialog.findViewById(R.id.ok_btn);
        TextView dialog_text = dialog.findViewById(R.id.dialog_txt);
        ImageView dialog_icon = dialog.findViewById(R.id.dialog_image);
        switch(message_type) {
            case SUCCESS_MESSAGE_TYPE:
                dialog_icon.setImageResource(R.drawable.tick);
                break;
            case ERROR_MESSAGE_TYPE:
                dialog_icon.setImageResource(R.drawable.x_mark);
                break;
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog_text.setText(title.trim());
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, HomeActivity.class);
                dialog.dismiss();
                ctx.startActivity(intent);
            }
        });
        dialog.show();

    }
}
