package com.cloudpurchase.veiw;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.cloudpurchase.cloudpurchase.HomeActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.utils.ScreenUtils;

/**
 * Created by oscar on 2016/6/9.
 * 自定义开房对话框
 */
public class LootAlertDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    public LootAlertDialog(Context context) {
        super(context);
        mContext=context;
    }

    public LootAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LootAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    /*
     显示对话框
     */
    private Dialog dialog;
    public void displayDialog(){
        View view=LayoutInflater.from(mContext).inflate(R.layout.fragment_smallroom_item_loot,null);
        Button mCancel= (Button) view.findViewById(R.id.loot_cancel_btn);
        Button mOk=(Button) view.findViewById(R.id.loot_ok_btn);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loot_cancel_btn:
                if (dialog!=null){
                    dialog.dismiss();
                    dialog=null;
                }
                break;
            case R.id.loot_ok_btn:
                Intent intent=new Intent(mContext,HomeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("flag","loot");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
        }
    }
}
