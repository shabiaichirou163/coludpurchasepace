package com.cloudpurchase.fragment;

import  android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudpurchase.base.BaseFragment;
import com.cloudpurchase.cloudpurchase.AddAddressActivity;
import com.cloudpurchase.cloudpurchase.AgoShowActivity;
import com.cloudpurchase.cloudpurchase.AllAddressActivity;
import com.cloudpurchase.cloudpurchase.LoginActivity;
import com.cloudpurchase.cloudpurchase.LuckyActivity;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.MyServiceActivity;
import com.cloudpurchase.cloudpurchase.NoticeActivity;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.cloudpurchase.RechargeActivtiy;
import com.cloudpurchase.cloudpurchase.RechargeRecordActivity;
import com.cloudpurchase.cloudpurchase.RecordActivtiy;
import com.cloudpurchase.cloudpurchase.RedPackageActivity;
import com.cloudpurchase.cloudpurchase.SettingsActivity;
import com.cloudpurchase.cloudpurchase.ShowOrderShareActivity;
import com.cloudpurchase.cloudpurchase.SmallHouseActivity;
import com.cloudpurchase.cloudpurchase.UserInfoActivity;
import com.cloudpurchase.cloudpurchase.UserTaskActivity;
import com.cloudpurchase.entity.UserFragmentEntity;
import com.cloudpurchase.net.HttpImgLoader;
import com.cloudpurchase.utils.BitmapUtils;
import com.cloudpurchase.utils.EncryptionAndDecryptUtils;
import com.cloudpurchase.utils.FileUtils;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.veiw.RoundImageView;
import com.cloudpurchase.veiw.UserView;
/**
 * 用户中心Fragment
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {
    private UserView mUserView;
    private View mTopView;//顶部View
    private ImageView mUserSettings;//设置
    private ImageView mUserNotice;//通知
    private RoundImageView mUserHead;//用户头像
    private TextView mUserNickName;//用户昵称
    private TextView mUserVipLv;//VIP等级
    private TextView mUserIntegral;//积分
    private TextView mUserBigGold;//大金币数量
    private TextView mUserSmallGold;//小金币数量
    private View mCenterView;//中部View
    private LinearLayout mUserSign;//签到
    private LinearLayout mUserRecharge;//充值
    private View mButtomView;//底部View
    private LinearLayout mAllInfo;//全部云购记录
    private LinearLayout mLuckyInfo;//幸运记录
    private LinearLayout mRedInfo;//红包
    private LinearLayout mShowInfo;//晒单
    private LinearLayout mAddress;//收货地址
    private LinearLayout mSmallHouse;//小房间
    private LinearLayout mRechargeInfo;//充值记录
    private LinearLayout mMyService;//我的客服
    private FileUtils mFile;
    @Override
    public View initView() {
        mUserView = new UserView(getActivity());
        mUserView.setVerticalScrollBarEnabled(false);//隐藏滚动条
        mTopView = View.inflate(getActivity(),R.layout.view_user_top,null);
        mUserView.setTopLinear(mTopView);
        mCenterView = View.inflate(getActivity(),R.layout.view_user_center,null);
        mUserView.setCenterLinear(mCenterView);
        mButtomView = View.inflate(getActivity(),R.layout.view_user_buttom,null);
        mUserView.setButtomLinear(mButtomView);
        mFile = new FileUtils(getActivity());
        mUserSettings = (ImageView) f(mTopView,R.id.user_settings);
        mUserNotice = (ImageView) f(mTopView,R.id.user_notice);
        mUserHead = (RoundImageView) f(mTopView,R.id.user_headImg);
        mUserNickName = (TextView) f(mTopView,R.id.user_nickname);
        mUserVipLv = (TextView) f(mTopView,R.id.user_vipLv);
        mUserIntegral = (TextView) f(mTopView,R.id.user_integral);
        mUserBigGold = (TextView) f(mTopView,R.id.user_bigGold);
        mUserSmallGold = (TextView) f(mTopView,R.id.user_smallGold);
        mUserSign = (LinearLayout) f(mCenterView,R.id.user_sign);
        mUserRecharge = (LinearLayout) f(mCenterView,R.id.user_recharge);
        mAllInfo = (LinearLayout) f(mButtomView,R.id.user_allInfo);
        mLuckyInfo = (LinearLayout) f(mButtomView,R.id.user_luckyInfo);
        mRedInfo = (LinearLayout) f(mButtomView,R.id.user_redInfo);
        mShowInfo = (LinearLayout) f(mButtomView,R.id.user_showInfo);
        mAddress = (LinearLayout) f(mButtomView,R.id.user_address);
        mSmallHouse = (LinearLayout) f(mButtomView,R.id.user_smallHouse);
        mRechargeInfo = (LinearLayout) f(mButtomView,R.id.user_rechargeInfo);
        mMyService = (LinearLayout) f(mButtomView,R.id.user_myService);
        return mUserView;
    }

    @Override
    public void setOnclick() {
        mUserSettings.setOnClickListener(this);
        mUserNotice.setOnClickListener(this);
        mUserHead.setOnClickListener(this);
        mUserSign.setOnClickListener(this);
        mUserRecharge.setOnClickListener(this);
        mAllInfo.setOnClickListener(this);
        mLuckyInfo.setOnClickListener(this);
        mRedInfo.setOnClickListener(this);
        mShowInfo.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mSmallHouse.setOnClickListener(this);
        mRechargeInfo.setOnClickListener(this);
        mMyService.setOnClickListener(this);
    }


    @Override
    public void initList() {
        if(MyApplication.USER_IS_LOGIN_FLAG){
            //表示已经登陆
            mUserNickName.setText(MyApplication.USER_NICKNAME);
            mUserVipLv.setText("lv."+MyApplication.USER_USERRANK+MyApplication.USER_RANK_NAME);
            mUserIntegral.setText(MyApplication.USER_INTEGTAL);
            mUserBigGold.setText(MyApplication.USER_BIG_GOLDEN);
            mUserSmallGold.setText(MyApplication.USER_SMALL_GOLDEN);
            HttpImgLoader.getHttpImgLoader().initImg(MyApplication.USER_HEADIMG_URL,mUserHead);
        }
    }

    /*
    隐藏手机号中间4位
     */
    private String hidePhoneNum(String str){
        if(str == null){
            return "";
        }
        String ss = str.substring(0,3)+"****"+str.substring(7,str.length());
        return ss;
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = null;
        int id = v.getId();
        switch (id){
            case R.id.user_settings:
                mIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.user_notice:
                mIntent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(mIntent);
                break;
            case R.id.user_headImg:
                mIntent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(mIntent);
                break;
            case R.id.user_sign:
                mIntent = new Intent(getActivity(), UserTaskActivity.class);
                startActivity(mIntent);
                break;
            case R.id.user_recharge:
                mIntent = new Intent(getActivity(), RechargeActivtiy.class);
                startActivity(mIntent);
                break;
            case R.id.user_allInfo:
                mIntent = new Intent(getActivity(), RecordActivtiy.class);
                startActivity(mIntent);
                break;
            case R.id.user_luckyInfo:
                mIntent = new Intent(getActivity(), LuckyActivity.class);
                startActivity(mIntent);
                break;
            case R.id.user_redInfo:
                mIntent = new Intent(getActivity(), RedPackageActivity.class);
                startActivity(mIntent);
                break;
            case R.id.user_showInfo:
                toOtherActivity(ShowOrderShareActivity.class);
                break;
            case R.id.user_address:
                mIntent = new Intent(getActivity(), AllAddressActivity.class);
                startActivity(mIntent);
                break;
            case R.id.user_smallHouse:
                mIntent = new Intent(getActivity(), SmallHouseActivity.class);
                startActivity(mIntent);
                break;
            case R.id.user_rechargeInfo:
                mIntent = new Intent(getActivity(), RechargeRecordActivity.class);
                startActivity(mIntent);
                break;
            case R.id.user_myService:
                mIntent = new Intent(getActivity(), MyServiceActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}
