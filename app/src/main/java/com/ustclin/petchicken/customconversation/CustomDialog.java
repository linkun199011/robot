package com.ustclin.petchicken.customconversation;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ustclin.robot.R;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2017/1/12:22:38
 * description
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private EditText etMaster;
        private EditText etPet;
        private DialogInterface.OnClickListener positiveBtnListener;
        private DialogInterface.OnClickListener negativeBtnListener;

        public Builder(Context context) {
            this.context = context;
        }

        public EditText getEtMaster() {
            return etMaster;
        }

        public Builder setPositiveBtnListener(DialogInterface.OnClickListener pos) {
            this.positiveBtnListener = pos;
            return this;
        }

        public Builder setNegativeBtnListener(DialogInterface.OnClickListener neg) {
            this.negativeBtnListener = neg;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context);
            // 需要在addContentView之前设置
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            View layout = inflater.inflate(R.layout.custom_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            etMaster = (EditText) layout.findViewById(R.id.masterAsk1);
//            final EditText etPet = (EditText) layout.findViewById(R.id.petAnswer);

            if (positiveBtnListener != null) {
                layout.findViewById(R.id.addCustom).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        addCustomConverToDB(etMaster.toString(), etPet.toString());
                        Log.i("dialog", etMaster.getText().toString());
                        positiveBtnListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            if (negativeBtnListener != null) {
                layout.findViewById(R.id.cancelCustom).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        negativeBtnListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }

            dialog.setContentView(layout);
            dialog.setCancelable(true);
            return dialog;
        }

        private void addCustomConverToDB(String masterStr, String petStr) {
            Toast.makeText(context, masterStr, Toast.LENGTH_SHORT).show();
        }
    }
}
