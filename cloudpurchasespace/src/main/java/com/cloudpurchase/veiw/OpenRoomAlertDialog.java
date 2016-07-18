package com.cloudpurchase.veiw;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.cloudpurchase.HomeActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.utils.ScreenUtils;

import java.math.BigDecimal;

/**
 * Created by oscar on 2016/6/9.
 * 自定义开房对话框
 */
public class OpenRoomAlertDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    public OpenRoomAlertDialog(Context context) {
        super(context);
        mContext=context;
    }

    public OpenRoomAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected OpenRoomAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    /*
     显示对话框
     */
    private Dialog dialog;
    private EditText mRoomName,mPersonNum,mRoomPwd;
    private TextView mTotalPrice,mPerPrice;
    private String mToalP;//总价个
    public void displayDialog(){
        View view=LayoutInflater.from(mContext).inflate(R.layout.fragment_smallroom_item_open,null);
        Button mCancel= (Button) view.findViewById(R.id.open_room_cancel_btn);
        Button mOk=(Button) view.findViewById(R.id.open_room_ok_btn);
        mRoomName= (EditText) view.findViewById(R.id.open_room_room_name);
        mPersonNum= (EditText) view.findViewById(R.id.open_room_use_num_edt);
        mPersonNum.addTextChangedListener(new MyEditTextChange());
        mRoomPwd= (EditText) view.findViewById(R.id.open_room_pwd_edt);
        mTotalPrice= (TextView) view.findViewById(R.id.open_room_goods_price);
        mPerPrice= (TextView) view.findViewById(R.id.open_room_pre_price);
        mToalP=mTotalPrice.getText().toString().trim();
        dialog=new Dialog(mContext,R.style.loading_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.TOP);//设置对话框显示位置为顶部
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        HomeActivity activity= (HomeActivity) mContext;
        int width=ScreenUtils.getWidth(activity);//获取屏幕的宽度
        int height=ScreenUtils.getHeight(activity);//获取屏幕的宽度
        lp.y=height/6;//设置对话框的y轴位置
        lp.width=35*width/40;//设置对话框相对屏幕的宽度
        dialog.getWindow().setAttributes(lp);
        mCancel.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }
    /**
     * 设置数据
     */
    public void setData(GoodsDetails goods){

    }



    class MyEditTextChange implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
              if (s.length()>0){
                 String personNum=s.toString().trim();
                  double perPrice=(double)(Integer.parseInt(mToalP.substring(0,mToalP.length()-1)))/
                          Integer.parseInt(personNum);
                  mPerPrice.setText((new BigDecimal(perPrice).divide(new BigDecimal(1),2,BigDecimal.ROUND_CEILING).doubleValue())+"元");
              }else {
                  mPerPrice.setText("(商品价值/人数)元");
              }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_room_cancel_btn:
                dismissDilog();
                break;
            case R.id.open_room_ok_btn:
                getData();
                break;
        }

    }

    /**
     * 取消Dilog
     */
    public void dismissDilog(){
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 获取数据
     *
     */
    public void getData(){
        String roomName=mRoomName.getText().toString().trim();
        String personNum=mPersonNum.getText().toString().trim();
        String pwd=mRoomPwd.getText().toString().trim();
        if (roomName.equals("")||personNum.equals("")||pwd.equals("")){
            Toast.makeText(mContext,"房间名称、参与人数、密码不能为空,请检查",Toast.LENGTH_SHORT).show();
        }



    }
}
