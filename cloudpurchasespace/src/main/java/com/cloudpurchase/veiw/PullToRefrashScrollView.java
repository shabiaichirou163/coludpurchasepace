package com.cloudpurchase.veiw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.cloudpurchase.cloudpurchase.R;

/**
 * ScroollView 下拉刷新
 *
 * */
@SuppressLint("NewApi")
public class PullToRefrashScrollView extends ScrollView{

	private final static int DONE = 0;//完成
	private final static int RELASE_REFRASH = 2;//释放刷新
	private final static int REFRASHING = 3;//正在刷新
	private final static int RELASE_LOAD = 5;//释放加载
	private final static int LOADING = 6;//正在加载
	private int mode = DONE;//scrollview当前所处模式
	private int height = 0;//滑动刷新临界值->头部或底部view的高度
	private int rote = 3;//屏幕滑动与头脚划出倍数
	private PointF startPf = new PointF();//起点位置
	private View headView;//头
	private View footerView;//脚
	private LinearLayout centerView;//中间添加控件布局
	private ImageView headImg;
	private ImageView footerImg;
	private LinearLayout linear;//scrollview中的linearlayout
	private AnimationDrawable draw_P;//下拉刷新时帧动画
	private AnimationDrawable draw_U;//上拉时帧动画
	private int[]drawables = {R.mipmap.icon_loaing_frame_1,
			R.mipmap.icon_loaing_frame_2,R.mipmap.icon_loaing_frame_3,
			R.mipmap.icon_loaing_frame_4,R.mipmap.icon_loaing_frame_5,
			R.mipmap.icon_loaing_frame_6,R.mipmap.icon_loaing_frame_7,
			R.mipmap.icon_loaing_frame_8,R.mipmap.icon_loaing_frame_9,
			R.mipmap.icon_loaing_frame_10,R.mipmap.icon_loaing_frame_11,
			R.mipmap.icon_loaing_frame_12,R.mipmap.icon_loaing_frame_13};//帧动画
	private Pull_To_Load pl;

	public PullToRefrashScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initAnim();
	}
	
	public PullToRefrashScrollView(Context context){
		super(context);
		initView();
		initAnim();
	}

	//设置下拉刷新以及上拉加载监听方法
	public void setPull_To_Load(Pull_To_Load pl){
		this.pl = pl;
	}

	//设置centerview方法
	public void setCenterView(View v){
		v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		centerView.addView(v);
	}

	//收起方法
	public void complate(){
		mode = DONE;
		select();
	}

	//初始化控件方法
	@SuppressLint("NewApi")
	private void initView(){
		linear = new LinearLayout(getContext());
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(new LayoutParams
				(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
		headView = View.inflate(getContext(), R.layout.view_head, null);
		footerView = View.inflate(getContext(), R.layout.view_head,null);
		centerView = new LinearLayout(getContext());
		centerView.setOrientation(LinearLayout.VERTICAL);
		centerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		headImg = (ImageView) headView.findViewById(R.id.HeadView_Img);
		footerImg = (ImageView) footerView.findViewById(R.id.HeadView_Img);
		measureHeight(headView);
		height = headView.getMeasuredHeight();
		headView.setPadding(0, -height, 0, 0);
		footerView.setPadding(0, 0, 0, -height);
		linear.addView(headView);
		linear.addView(centerView);
		linear.addView(footerView);
		addView(linear);
	}

	//测量头部或底部布局高度
	private void measureHeight(View v){
		v.measure(0,0);
	}

	//初始化动画方法
	private void initAnim(){
		draw_P = new AnimationDrawable();
		draw_U = new AnimationDrawable();
		for(int i = 0;i < drawables.length;i ++){
			draw_P.addFrame(getContext().
					getResources().getDrawable(drawables[i]), 30);
			draw_U.addFrame(getContext().
					getResources().getDrawable(drawables[i]), 30);
		}
		draw_P.setOneShot(false);
		draw_U.setOneShot(false);
		headImg.setBackground(draw_P);
		footerImg.setBackground(draw_U);
	}

	//根据当前滑动状态开关动画等切换方法
	private void select(){
		switch(mode){
		case DONE:
			draw_P.stop();
			draw_U.stop();
			headView.setPadding(0,-height,0,0);
			footerView.setPadding(0, 0,0,-height);
			break;
		case RELASE_REFRASH:
			draw_P.start();
			headView.setPadding(0, 0, 0, 0);
			pl.Refrash();
			break;
		case RELASE_LOAD:
			draw_U.start();
			footerView.setPadding(0, 0, 0, 0);
			pl.Load();
			break;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
			startPf.y = ev.getY();
		}else if(ev.getAction() == MotionEvent.ACTION_MOVE){
			PointF pf = new PointF();
			pf.y = ev.getY();
			float disY = (pf.y-startPf.y)/rote;
			if(getScrollY()<=0&&disY >0){
				headView.setPadding(0, (int)(disY-height), 0, 0);
				if(disY>height){
					mode = RELASE_REFRASH;
				}else{
					mode = DONE;
				}
				return true;
			}
			if((getHeight()+getScrollY())>=
					PullToRefrashScrollView.this.computeVerticalScrollRange()&&
					disY<0){
				footerView.setPadding(0, 0, 0, (int)(Math.abs(disY)-height));
				if(Math.abs(disY)>height){
					mode = RELASE_LOAD;
				}else{
					mode = DONE;
				}
				return true;
			}
		}else if(ev.getAction() == MotionEvent.ACTION_UP){
			select();
		}
		return super.dispatchTouchEvent(ev);
	}

	//下拉刷新以及上拉加载回调监听
	public interface Pull_To_Load{
		public void Refrash();
		public void Load();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
	}

	//刷新Listview高度方法
	public void refrashLv(ListView lv){
		int height = 0;//listview总高度
		ListAdapter adapter = lv.getAdapter();
		for(int i = 0;i < adapter.getCount();i ++){
			View itemView = adapter.getView(i, null, null);//获取itemview对象
			itemView.measure(0,0);
			height+=itemView.getMeasuredHeight();
		}
		height = height + lv.getDividerHeight()*(adapter.getCount()-1)
				+lv.getPaddingBottom()+lv.getPaddingTop();
		lv.setLayoutParams(new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.MATCH_PARENT, height));
		PullToRefrashScrollView.this.invalidate();
	}
}
