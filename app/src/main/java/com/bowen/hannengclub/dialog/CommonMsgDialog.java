package com.bowen.hannengclub.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bowen.hannengclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @创建者 xiao
 * @创建时间 2017.04.12
 * @描述 登录失败的显示
 */
public class CommonMsgDialog {

    private Activity mActivity;

    @BindView(R.id.tv_common_dialog_title)
    TextView mTvTitle;

    @BindView(R.id.tv_common_dialog_msg)
    TextView mTvMsg;

    @BindView(R.id.tv_common_dialog_cancel)
    TextView mRight;

    @BindView(R.id.tv_common_dialog_sure)
    TextView mLeft;

    Dialog mDialog;

    DialogBean mBean;

    public CommonMsgDialog(Activity context,DialogBean dialogBean) {
        mBean = dialogBean;
        mActivity = context;
    }


    public void showDialog(){
        View inflate = View.inflate(mActivity, R.layout.dialog_common_msg, null);
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

        //设置要显示的内容
        setTextView(mBean.getTitle(),mTvTitle);
        setTextView(mBean.getMsg(),mTvMsg);
        setBtnText(mBean.getLeftBtn(),mLeft);
        setBtnText(mBean.getRightBtn(),mRight);
    }

    private void setTextView(String msg ,TextView view){
        boolean empty = TextUtils.isEmpty(msg);
        view.setText(msg);
        view.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private void setBtnText(String msg ,TextView view){
        boolean empty = TextUtils.isEmpty(msg);
        if(empty){return;}
        view.setText(msg);
    }


    @OnClick({
        R.id.tv_common_dialog_cancel
        ,R.id.tv_common_dialog_sure
    })
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tv_common_dialog_cancel:
                if(mRightClick != null){
                    mRightClick.onClick(view);
                }
                break;
            case R.id.tv_common_dialog_sure:
                if(mLeftClick != null){
                    mLeftClick.onClick(view);
                }
                break;
        }
    }

    private View.OnClickListener mRightClick;
    public void setRightClick(View.OnClickListener rightClick) {
        this.mRightClick = rightClick;
    }

    private View.OnClickListener mLeftClick;
    public void setLeftClick(View.OnClickListener leftClick) {
        this.mLeftClick = leftClick;
    }

    //消失
    public void dismiss() {
        if(mDialog != null){
            mDialog.dismiss();
        }
    }
}
