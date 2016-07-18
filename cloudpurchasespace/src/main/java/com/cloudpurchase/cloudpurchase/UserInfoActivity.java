package com.cloudpurchase.cloudpurchase;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpurchase.base.BaseActivity;
import com.cloudpurchase.net.HttpImgLoader;
import com.cloudpurchase.utils.EncryptionAndDecryptUtils;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.SharedFileUtils;
import com.cloudpurchase.veiw.RoundImageView;

/**\
 *
 * 用户个人资料中心界面
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private RoundImageView mHeadImg;//用户头像
    private TextView mID;
    private EditText mNickName;//昵称
    private EditText mPhone;//手机号
    private Button mPutBtn;//提交按钮
    private LinearLayout mCenterLinear;//中间点击也可以取消
    private LinearLayout mBottomLienar;//底部调用本地相册的布局
    private Button mBtn1,mBtn2;//mBtn1：调用本地相册    mBtn2：取消
    private LinearLayout.LayoutParams mBottomLp;//控制显示隐藏底部view
    private String mImagePath;//从本地选取的图片路径

    @Override
    public void initView() {
        setContentView(R.layout.activity_user_info);
        mBack = (ImageView) f(R.id.userInfo_back);
        mHeadImg = (RoundImageView) f(R.id.userInfo_headImg);
        mID = (TextView) f(R.id.userInfo_id);
        mNickName = (EditText) f(R.id.userInfo_userName);
        mPhone = (EditText) f(R.id.userInfo_phone);
        mCenterLinear = (LinearLayout) f(R.id.userInfo_centerLinear);
        mBottomLienar = (LinearLayout) f(R.id.userInfo_bottomLinear);
        mBtn1 = (Button) f(R.id.userInfo_btn1);
        mBtn2 = (Button) f(R.id.userInfo_btn2);
        mPutBtn = (Button) f(R.id.userinfo_putBtn);
        mBottomLp = (LinearLayout.LayoutParams) mBottomLienar.getLayoutParams();
        HttpImgLoader.getHttpImgLoader().initImg(MyApplication.USER_HEADIMG_URL,mHeadImg);
        mID.setText(MyApplication.USER_ID);
        mNickName.setText(MyApplication.USER_NICKNAME);
        mPhone.setText(MyApplication.USER_PHONE);
    }

    @Override
    public void setOnclick() {
        mPutBtn.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mHeadImg.setOnClickListener(this);
        mCenterLinear.setOnClickListener(this);
        mBottomLienar.setOnClickListener(this);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
    }

    @Override
    public void initList() {

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent mIntent = null;
        switch (id){
            case R.id.userInfo_back:
                finish();
                break;
            case R.id.userinfo_putBtn:
                //提交按钮
                String name = mNickName.getText().toString();
                String phone = mPhone.getText().toString();
                if(name == null && phone == null){
                    Toast.makeText(this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                }else{
                    SharedFileUtils util = new SharedFileUtils(this,"USER_INFO",RegisterActivity.MODE_PRIVATE);
                    util.saveStringFile("nickName",name);
                    util.saveStringFile("mobilephone",phone);
                    MyApplication.USER_NICKNAME = name;
                    MyApplication.USER_PHONE = phone;
                    Bundle bundle=new Bundle();
                    bundle.putString("flag","login");
                    toOtherActivity(HomeActivity.class, bundle);
                    finish();
                }
                break;
            case R.id.userInfo_headImg:
                setmCenterLinear(true);
                break;
            case R.id.userInfo_id:

                break;
            case R.id.userInfo_centerLinear:
                //取消弹出的选择按钮
                setmCenterLinear(false);

                break;
            case R.id.userInfo_bottomLinear:

                break;
            case R.id.userInfo_btn1:
                //调用相册
                getmImageBitmap();
                break;
            case R.id.userInfo_btn2:
                //取消
                setmCenterLinear(false);
                break;
        }
    }

    /*
     设置底部布局显示和隐藏
     */
    private void setmCenterLinear(boolean flag){
        if(flag){
            //调出
            mBottomLp.setMargins(0,0,0,0);
            mBottomLienar.setLayoutParams(mBottomLp);
        }else{
            //关闭
            mBottomLp.setMargins(0,0,0,getWindowManager().getDefaultDisplay().getHeight());
            mBottomLienar.setLayoutParams(mBottomLp);
        }
    }

    /*
    从本地相册获取一张图片
     */
    private void getmImageBitmap(){
        Intent mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(mIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            ContentResolver resolver= this.getContentResolver();
            Uri uri=data.getData();
            Cursor cursor = resolver.query(uri, null, null, null, null);
            if(cursor == null){
                return;
            }
            cursor.moveToNext();
            int columIndex =cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //选中图片的路径
            mImagePath=cursor.getString(columIndex);
            LogUtils.e(mImagePath);
            Bitmap bit = BitmapFactory.decodeFile(mImagePath);
            mHeadImg.setImageBitmap(bit);
            //TODO 将bit上传至服务器

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /*
    点击back键，进入此方法
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
            finish();
    }

}
