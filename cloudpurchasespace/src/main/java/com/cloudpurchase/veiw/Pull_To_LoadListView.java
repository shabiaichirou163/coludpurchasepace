package com.cloudpurchase.veiw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.cloudpurchase.cloudpurchase.R;

/**
 * listview下拉刷新以及上拉加载更多
 *
 * */
public class Pull_To_LoadListView extends ListView implements OnScrollListener{

	private final static int DONE = 0;//完成状态
	private final static int PULL_TO_REFRASH = 1;//下拉刷新状态
	private final static int RELASE_REFRASH = 2;//释放立即刷新状态
	private final static int REFRASHING = 3;//正在刷新状态
	private final static int UP_TO_LOAD = 4;//上拉加载状态
	private final static int RELASE_LOAD = 5;//释放加载状态
	private final static int LOADING = 6;//正在加载
	private int totalItemCount;
	private int mode = DONE;//listview当前状态
	private View headView;//listview下拉刷新头部
	private ImageView headImg;
	private TextView headTv;
	private ProgressBar headPrg;
	private View footView;//listview上拉加载脚
	private ImageView footImg;
	private TextView footTv;
	private ProgressBar footPrg;
	private int height = 0;//头部布局的高度->
	//下拉刷新状态切换到释放立即刷新状态标志
	private int scrollstate = 1;//记录listview位置0:下拉刷新1:listview滑动;2:上拉加载
	private int startY = 0;//手指按下时的Y坐标
	private int scale = 3;//实际滑动距离预头部背书
	private PullToLoad call;
	private RotateAnimation rotate1;
	private RotateAnimation rotate2;
	private boolean R_To_P = false;//释放刷新回到下拉刷新动画开启标志
	private boolean R_To_U = false;//释放加载回到上拉加载动画开启标志

	public void setPullToLoad(PullToLoad call){
		this.call = call;
	}

	public Pull_To_LoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initAnim();
	}

	private void initAnim(){
		rotate1 = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.rotate1);
		rotate2 = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.rotate2);
		footImg.startAnimation(rotate1);//保证箭头向上
	}

	//listview关闭下拉刷新活着上拉加载方法
	public void complate(){
		mode = DONE;
		select();
	}

	private void initView(){
		setOnScrollListener(this);
		headView = View.inflate(getContext(), R.layout.view_child,null);
		headImg = (ImageView) headView.findViewById(R.id.ChildView_ArrowImg);
		headTv = (TextView) headView.findViewById(R.id.ChildView_Tv);
		headPrg = (ProgressBar) headView.findViewById(R.id.Prg);
		headView.measure(0, 0);
		height = headView.getMeasuredHeight();
		footView = View.inflate(getContext(), R.layout.view_child,null);
		footImg = (ImageView) footView.findViewById(R.id.ChildView_ArrowImg);
		footTv = (TextView) footView.findViewById(R.id.ChildView_Tv);
		footPrg = (ProgressBar) footView.findViewById(R.id.Prg);
		headView.setPadding(0,-height, 0,0);
		footView.setPadding(0, 0, 0, -height);
		addHeaderView(headView,null,false);
		this.addFooterView(footView,null,false);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
			startY = (int) ev.getY();
		}else if(ev.getAction() == MotionEvent.ACTION_MOVE){
			int nowY = (int) ev.getY();
			int dis = (nowY - startY)/scale;
			if(scrollstate==1){
				return super.dispatchTouchEvent(ev);
			}else if(scrollstate == 0){
				if(dis<0){
					scrollstate=1;
					return super.dispatchTouchEvent(ev);
				}
				//下拉刷新状态
				this.setSelection(0);
				if(mode != REFRASHING){
					if(mode == PULL_TO_REFRASH){
						if(dis<=0){
							mode = DONE;
							select();
						}
						if(dis>height){
							mode = RELASE_REFRASH;
							select();
						}
					}
					if(mode == RELASE_REFRASH){
						if(dis < height){
							mode = PULL_TO_REFRASH;
							R_To_P = true;
							select();
						}
					}
				}

				if(mode == DONE){
					if(dis > 0){
						mode = PULL_TO_REFRASH;
						select();
					}
				}

				if(mode == PULL_TO_REFRASH){
					headView.setPadding(0, dis-height, 0, 0);
				}

				if(mode == RELASE_REFRASH){
					headView.setPadding(0, dis-height, 0, 0);
				}
				return true;
			}else if(scrollstate == 2){
				this.setSelection((totalItemCount-1));
				//上拉加载状态
				//区分上拉加载还是向上滑动
				if(dis>0){
					scrollstate = 1;
					return super.dispatchTouchEvent(ev);
				}
				if(mode != LOADING){
					if(mode==UP_TO_LOAD){
						if(dis>=0){
							mode = DONE;
							select();
						}

						if(dis<-height){
							mode = RELASE_LOAD;
							select();
						}

					}

					if(mode == RELASE_LOAD){
						if(dis>-height){
							mode = UP_TO_LOAD;
							R_To_U = true;
							select();
						}
					}

				}
			}

			if(mode == DONE){
				if(dis < 0){
					mode=UP_TO_LOAD;
					select();
				}
			}
			Log.e("", "dis:"+dis+"&&height:"+height+(Math.abs(dis)-height));
			if(mode == UP_TO_LOAD){
				//				Log.e("", ""+(Math.abs(dis)-height));
				footView.setPadding(0, 0, 0,(Math.abs(dis)-height));
				//				footView.requestLayout();
				//				requestLayout();
			}

			if(mode == RELASE_LOAD){
				footView.setPadding(0, 0, 0,(Math.abs(dis)-height));
				//				footView.requestLayout();
				//				requestLayout();
			}
			return true;
		}else if(ev.getAction() == MotionEvent.ACTION_UP){
			if(mode == RELASE_REFRASH){
				mode = REFRASHING;
				select();
			}else if(mode == RELASE_LOAD){
				mode = LOADING;
				select();
			}else{
				mode = DONE;
				select();
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	//根据listview当前状态选择头部或底部textview显示的文字
	private void select(){
		switch(mode){
			case DONE:
				headTv.setText("下拉刷新");
				footTv.setText("上拉加载");
				headView.setPadding(0, -height, 0, 0);
				footView.setPadding(0, 0, 0, -height);
				headImg.setVisibility(View.VISIBLE);
				footImg.setVisibility(View.VISIBLE);
				footImg.clearAnimation();
				footImg.startAnimation(rotate1);
				break;
			case PULL_TO_REFRASH:
				headTv.setText("下拉刷新");
				headPrg.setVisibility(View.GONE);
				headImg.clearAnimation();
				if(R_To_P){
					headImg.startAnimation(rotate2);
				}
				R_To_P = false;
				break;
			case RELASE_REFRASH:
				headTv.setText("释放刷新");
				headImg.clearAnimation();
				headImg.startAnimation(rotate1);//箭头向上
				break;
			case REFRASHING:
				headImg.clearAnimation();
				headView.setPadding(0, 0, 0, 0);
				headTv.setText("正在刷新");
				headPrg.setVisibility(View.VISIBLE);
				headImg.setVisibility(View.GONE);
				//通知调用的listview
				call.Pull();
				break;
			case UP_TO_LOAD:
				footTv.setText("上拉加载");
				footPrg.setVisibility(View.GONE);
				if(R_To_U){
					footImg.startAnimation(rotate1);
				}
				R_To_U = false;
				break;
			case RELASE_LOAD:
				footTv.setText("释放加载");
				footImg.clearAnimation();
				footImg.startAnimation(rotate2);
				break;
			case LOADING:
				footView.setPadding(0, 0, 0, 0);
				footPrg.setVisibility(View.VISIBLE);
				footTv.setText("正在加载");
				footImg.clearAnimation();
				footImg.setVisibility(View.GONE);
				call.Load();
				break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		Log.e("", "onScroll:scrollstate:"+scrollstate);
		this.totalItemCount = totalItemCount;
		//50+1头+1脚=52 52-2
		if(firstVisibleItem == 0){
			//listview处于界面顶部
			scrollstate = 0;
		}else if((firstVisibleItem+visibleItemCount)
				>=(totalItemCount-2)){
			//listview处于界面底部
			scrollstate = 2;
		}else{
			//listview中间
			scrollstate = 1;
		}
	}

	public interface PullToLoad{
		public void Pull();
		public void Load();
	}

}
