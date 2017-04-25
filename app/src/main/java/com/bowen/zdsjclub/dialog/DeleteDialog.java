package com.bowen.zdsjclub.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.bowen.zdsjclub.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @创建者 xiao
 * @创建时间 2017.04.12
 * @描述 登录失败的显示
 */
public class DeleteDialog {

    private Activity mActivity;

    Dialog mDialog;

    public DeleteDialog(Activity context) {
        mActivity = context;
    }


    public void showDialog(){
        View inflate = View.inflate(mActivity, R.layout.dialog_delete_warn, null);
        ButterKnife.bind(this, inflate);
        if(mDialog == null){
            //对话框
            mDialog = new AlertDialog.Builder(mActivity,R.style.delete_Dialog).create();
        }
        mDialog.show();
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.68f + 0.5f);
        params.height =  (int) (params.width * 0.6 + 0.5f);
        //                params.height = 200 ;
        mDialog.getWindow().setAttributes(params);
        mDialog.getWindow().setContentView(inflate);
        //点击外面是否可以关闭页面
        mDialog.setCanceledOnTouchOutside(true);
    }


    @OnClick({
        R.id.btn_cancel

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    private View.OnClickListener mBtnClick;

    public void setmBtnClick(View.OnClickListener mBtnClick) {
        this.mBtnClick = mBtnClick;
    }

    //消失
    public void dismiss() {
        if(mDialog != null){
            mDialog.dismiss();
        }
    }
}
