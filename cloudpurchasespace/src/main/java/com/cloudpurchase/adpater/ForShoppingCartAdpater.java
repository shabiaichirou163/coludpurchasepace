package com.cloudpurchase.adpater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cloudpurchase.cloudpurchase.MyApplication;
import com.cloudpurchase.cloudpurchase.R;
import com.cloudpurchase.db.DBWrapper;
import com.cloudpurchase.entity.GoodsDetails;
import com.cloudpurchase.entity.RequestInfo;
import com.cloudpurchase.net.HttpImgLoader;
import com.cloudpurchase.net.HttpRequest;
import com.cloudpurchase.utils.Constants;
import com.cloudpurchase.utils.JsonReslove;
import com.cloudpurchase.utils.LogUtils;
import com.cloudpurchase.utils.PersonNumChangeListener;
import com.cloudpurchase.utils.RequestResultIn;
import com.cloudpurchase.utils.ShoppingCartDeleteListener;
import com.cloudpurchase.utils.ShoppingClickListener;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oscar on 2016/6/6.
 *购物车
 */
public class ForShoppingCartAdpater extends BaseAdapter {
    private Activity mContext;
    private List<GoodsDetails> mData;
    private DBWrapper mDBWrapper;
    private Animation mAnimation;
    private String mEdit;//记录edit的值
    private HashMap<Integer,EditText> map;//绑定对应的EditText
    private HashMap<String,String> mNetMap;//网络请求参数
    private String mFlag;//"add" 添加   "all" 包尾    "deleteALl" 删除所有  "more" 输入超出  "no" 什么都不做
    private JsonReslove mReslove;
    public ForShoppingCartAdpater(Activity context, List<GoodsDetails> data) {
        mContext = context;
        mData = data;
        mDBWrapper = new DBWrapper(context);
        mNetMap=new HashMap<String,String>();
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale_text);
        map=new HashMap<Integer,EditText>();
        mReslove=new JsonReslove(mContext);
    }

    @Override
    public int getCount() {
        return null != mData ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    ViewHolder viewHoder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            viewHoder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adpater_shopping_cart, null);
            viewHoder.goodsType = (TextView) convertView.findViewById(R.id.adpaer_shopping_cart_goods_type);
            viewHoder.delete = (ImageButton) convertView.findViewById(R.id.adpaer_shopping_cart_delete_goods);
            viewHoder.goodsImg = (ImageView) convertView.findViewById(R.id.adpaer_shopping_cart_goods_img);
            viewHoder.reduce = (ImageButton) convertView.findViewById(R.id.adpaer_shopping_cart_reduce_btn);
            viewHoder.add = (ImageButton) convertView.findViewById(R.id.adpaer_shopping_cart_add_btn);
            viewHoder.needPerson = (TextView) convertView.findViewById(R.id.adpaer_shopping_cart_need_person);
            viewHoder.personNum = (EditText) convertView.findViewById(R.id.adpaer_shopping_cart_num_txt);
            viewHoder.allBtn = (Button) convertView.findViewById(R.id.adpaer_shopping_cart_baowei_btn);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHolder) convertView.getTag();
        }
        final GoodsDetails goods = mData.get(position);
        viewHoder.goodsType.setText(goods.getGoodsName());
        HttpImgLoader.getHttpImgLoader().initImgNoBitmap(goods.getIcon(), viewHoder.goodsImg);
        viewHoder.needPerson.setText("总需:" + goods.getTotal() + "人次,剩余" + goods.getRemaining() + "人次");
        if (goods.getRemaining()<goods.getPersonNum()){
            viewHoder.personNum.setText(goods.getRemaining() + "");
        }else{
            viewHoder.personNum.setText(goods.getPersonNum() + "");
        }

        map.put(position,viewHoder.personNum);
        viewHoder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("确定要删除吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       deleteData(goods);
                }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();
            }
        });
        viewHoder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=map.get(position);
                int personNun = goods.getPersonNum();
                if (personNun > 1) {
                     editText.startAnimation(mAnimation);
                     reduceData(goods);
                }
            }
        });
        viewHoder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=map.get(position);
                int personNun = goods.getPersonNum();
                if (personNun < goods.getRemaining()) {
                    editText.startAnimation(mAnimation);
                    mFlag="add";
                    addData(goods);
                }
            }
        });
        viewHoder.allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlag="all";
                addData(goods);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"购物车Item"+position,Toast.LENGTH_SHORT).show();
                shoppingClickListener.ShoppingClickListener(mData.get(position));
            }
        });
        viewHoder.personNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ((EditText) v).setSelection(0,((EditText) v).getText().length());
                     mEdit = ((EditText) v).getText().toString();
                }
            }
        });
        viewHoder.personNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                mEdit = s.toString();
            }
        });
        viewHoder.personNum.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mEdit != null) {
                        if (mEdit.length() < 0 || "".equals(mEdit)) {
                            mFlag="deleteALl";
                            addData(goods);
                        }
                        if (mEdit.length() > 0 && (Integer.parseInt(mEdit)) >= goods.getRemaining()) {
                            mFlag="more";
                            addData(goods);
                        }
                        if (mEdit.length() > 0 && (Integer.parseInt(mEdit)) < goods.getRemaining()) {
                            //goods.setPersonNum(Integer.parseInt(mEdit));
                            mFlag="no";
                            addData(goods);
                        }
                        personNumChangeListener.setPersonNumChangeListener();
                    }
                    viewHoder.personNum.clearFocus();
                }
                return false;
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView goodsType, needPerson;
        public EditText personNum;
        ImageButton delete, reduce, add;
        ImageView goodsImg;
        Button allBtn;//包尾
    }


    private ShoppingCartDeleteListener shoppingCartDeleteListener;
    public void getDeleteFinish(ShoppingCartDeleteListener shoppingCartDeleteListener) {
        this.shoppingCartDeleteListener = shoppingCartDeleteListener;
    }
    ShoppingClickListener shoppingClickListener;
    public void getShoppingClickListener(ShoppingClickListener shoppingClickListener){
        this.shoppingClickListener=shoppingClickListener;
    }
    private PersonNumChangeListener personNumChangeListener;
    public void  getPersonNumChangeListener(PersonNumChangeListener personNumChangeListener){
         this.personNumChangeListener=personNumChangeListener;
    }

    /**
     * 删除时网络请求
     * @param goodsDetails
     */
    public void deleteData(final GoodsDetails goodsDetails){
        String url= Constants.DELETE_SHOPPING_CART;
        if (mNetMap!=null){
            mNetMap.clear();
        }
        mNetMap.put("id",goodsDetails.getShoppingCartId()+"");
        mNetMap.put("token", MyApplication.USER_TOKEN);
        HttpRequest.getHttpRequest().requestPOST(url, null, mNetMap, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                LogUtils.e(result);//测试用
                RequestInfo info=mReslove.resloverIsSuff(result);
                if (info!=null&&info.getCode()==0){
                        Toast.makeText(mContext, "删除的是" + goodsDetails.getActivityId(), Toast.LENGTH_SHORT).show();
                        shoppingCartDeleteListener.deleteFinish();
                }
            }
            @Override
            public void requstError(VolleyError error) {

            }
        });
    }
    /**
     * 增加时网络请求
     */
    private int mNumber;

    public void addData(final GoodsDetails goodsDetails){
        String url=Constants.ADD_SHOPPING_CART;
        if (mNetMap!=null){
            mNetMap.clear();
        }
        switch (mFlag){
            case "add":
                mNumber = goodsDetails.getPersonNum() + 1;
                break;
            case "all":
            case "more":
                mNumber=goodsDetails.getRemaining();
                break;
            case "deleteALl":
                mNumber=1;
                break;
            case "no":
                mNumber = goodsDetails.getPersonNum();
                break;
        }
        mNetMap.put("memberId",7+"");
        mNetMap.put("activityId",goodsDetails.getActivityId());
        mNetMap.put("goodsnumber",mNumber+"");
        mNetMap.put("token", MyApplication.USER_TOKEN);
        HttpRequest.getHttpRequest().requestPOST(url, null, mNetMap, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                RequestInfo info=mReslove.resloverIsSuff(result);
                if (info!=null&&info.getCode()==0){
                    goodsDetails.setPersonNum(mNumber);
                    viewHoder.personNum.setText(goodsDetails.getPersonNum() + "");
                    notifyDataSetChanged();
                    personNumChangeListener.setPersonNumChangeListener();
                }
            }

            @Override
            public void requstError(VolleyError error) {

            }
        });

    }
    /**
     * 增加时减少请求
     */

    public void reduceData(final GoodsDetails goodsDetails){
        String url=Constants.REDUCE_SHOPPING_CART;
        if (mNetMap!=null){
            mNetMap.clear();
        }
        mNumber = goodsDetails.getPersonNum() - 1;
        mNetMap.put("memberId",7+"");
        mNetMap.put("activityId",goodsDetails.getActivityId());
        mNetMap.put("goodsnumber",mNumber+"");
        mNetMap.put("token", MyApplication.USER_TOKEN);
        HttpRequest.getHttpRequest().requestPOST(url, null, mNetMap, new RequestResultIn() {
            @Override
            public void requstSuccful(String result) {
                RequestInfo info=mReslove.resloverIsSuff(result);
                if (info!=null&&info.getCode()==0){
                    goodsDetails.setPersonNum(mNumber);
                    viewHoder.personNum.setText(goodsDetails.getPersonNum() + "");
                    notifyDataSetChanged();
                    personNumChangeListener.setPersonNumChangeListener();
                }
            }

            @Override
            public void requstError(VolleyError error) {

            }
        });

    }

    /**
     * 删除后刷新适配器
     * @param data
     */
    public void refreshData(List<GoodsDetails> data){
        mData=data;
        notifyDataSetChanged();
    }
}
