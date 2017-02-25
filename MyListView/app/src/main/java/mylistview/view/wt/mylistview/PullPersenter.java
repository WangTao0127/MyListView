package mylistview.view.wt.mylistview;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wt.view.AbsPullPresenter;
import wt.view.PullListView;

/**
 * Created by Administrator on 2017/2/22.
 */

public class PullPersenter extends AbsPullPresenter {
    private View topview;
    private ImageView iv_top;
    private TextView tv_top;
    private int iv_top_id;
    private ProgressBar top_ProgressBar;
    private PullListView pullListView;
    private View bottomview;
    private ImageView iv_bottom;
    private TextView tv_bottom;
    private int iv_bottom_id;
    private ProgressBar bottom_ProgressBar;
    public PullPersenter(PullListView pullListView, Activity activity){
        super(pullListView);
        View topview=activity.getLayoutInflater().inflate(R.layout.pull_look,null);
        RelativeLayout.LayoutParams viewlp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        activity.addContentView(topview, viewlp);
        topview.setVisibility(View.INVISIBLE);
        this.pullListView=pullListView;
        iv_top= (ImageView) topview.findViewById(R.id.iv_top);
        tv_top= (TextView) topview.findViewById(R.id.tv_top);
        top_ProgressBar= (ProgressBar) topview.findViewById(R.id.top_ProgressBar);
        this.topview=topview;

        this.bottomview=topview;
        iv_bottom= iv_top;
        tv_bottom= tv_top;
        bottom_ProgressBar= top_ProgressBar;
    }
    @Override
    public void pullDown(float distance, PullListView.IAction action) {
        topview.setVisibility(View.VISIBLE);
        if(distance==0){packUp(action);return;}
       // if(true)return;
        int height=topview.getMeasuredHeight();
        Log.e("pullDown","height="+height);
        //显示下拉刷新的时机
       if(distance<height){
           tv_top.setText("下拉刷新");
          setIv_topId(R.drawable.down);
            action.setStopSpringBack(0);
           iv_top.setVisibility(View.VISIBLE);
           top_ProgressBar.setVisibility(View.GONE);
           //显示松开刷新的时机
       }else if(distance>height&&action.getFingerState()==PullListView.FINGER_TANGENT){
            if(!"正在刷新".equals(tv_top.getText().toString()))tv_top.setText("松开刷新");
           setIv_topId(R.drawable.up);
         action.setStopSpringBack(height);
           //显示显示刷新的时机
        }else if(distance==height&&action.getFingerState()==PullListView.FINGER_NONEXPOSED){
          tv_top.setText("正在刷新");
            iv_top.setVisibility(View.INVISIBLE);
           top_ProgressBar.setVisibility(View.VISIBLE);
        }else if(distance==height&&action.getFingerState()==PullListView.FINGER_TANGENT){
           action.setStopSpringBack(0);
       }
        topview.setY(this.pullListView.getY()-height);
    }
    //更改图片顶部
    private void setIv_topId(int id){
        if(iv_top_id==id){return;}
        iv_top.setImageResource(id);
        iv_top_id=id;

    }
    //更改图片底部
    private void setIv_bottomId(int id){
        if(iv_bottom_id==id){return;}
        iv_bottom.setImageResource(id);
        iv_bottom_id=id;

    }
    public void packUp(PullListView.IAction action){
        action.setStopSpringBack(0);
        action.startSpringBack();
    }
    @Override
    public void pullUp(float distance, PullListView.IAction action) {
        topview.setVisibility(View.VISIBLE);
        if(distance==0){packUp(action);return;}
        int height=bottomview.getMeasuredHeight();
        float distance2=-distance;
        //显示上拉加载的时机
        if(distance2<height){
            tv_bottom.setText("上拉加载");
            setIv_bottomId(R.drawable.up);
            action.setStopSpringBack(0);
            iv_bottom.setVisibility(View.VISIBLE);
            bottom_ProgressBar.setVisibility(View.GONE);
            //显示松开加载的时机
        }else if(distance2>height&&action.getFingerState()==PullListView.FINGER_TANGENT){
            if(!"正在加载".equals(tv_top.getText().toString()))tv_bottom.setText("松开加载");
            setIv_bottomId(R.drawable.down);
            action.setStopSpringBack(-height);
            //显示显示正在加载的时机
        }else if(distance2==height&&action.getFingerState()==PullListView.FINGER_NONEXPOSED){
            tv_bottom.setText("正在加载");
            iv_bottom.setVisibility(View.INVISIBLE);
            bottom_ProgressBar.setVisibility(View.VISIBLE);
        }else if(distance2==height&&action.getFingerState()==PullListView.FINGER_TANGENT){
            action.setStopSpringBack(0);
        }
        bottomview.setY(this.pullListView.getY()+this.pullListView.getMeasuredHeight());
    }
}
