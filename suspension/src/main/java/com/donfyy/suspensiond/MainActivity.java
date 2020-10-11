package com.donfyy.suspensiond;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private SuspensionWindowUtil windowUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        windowUtil = new SuspensionWindowUtil(this);

    }

    @OnClick(R.id.btn_showsuspension)
    public void onBtnShowClicked() {
        windowUtil.showSuspensionView();
    }

    @OnClick(R.id.btn_hidesuspension)
    public void onBtnHideClicked() {
        windowUtil.hideSuspensionView();
    }

    @OnClick(R.id.btn_showdialog)
    public void onBtnShowDialogClicked() {
        Context mContext = getApplicationContext();
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();
    }
}
