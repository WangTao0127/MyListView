package wt.view;

        import android.content.Context;
        import android.os.Handler;
        import android.os.Message;
        import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.widget.ListView;
/**
 *
 * @author 王涛 邮箱：13207498094@163.com
 *
 */
public class PullListView extends ListView{
    /*常量区*/
    //表手指接触
    public static final int FINGER_TANGENT=2;
    //表手指没接触
    public static final int FINGER_NONEXPOSED =3;
    /*变量区*/
    //能否下拉
    private boolean havePullDown=true;
    //能否上拉
    private boolean havePullUp=true;
    //本View在pull时的真正y位置是当前View的y+pully
    private float pullY=0;
    //下拉事件
    private PullDownAction mPullDownAction;
    //上拉事件
    private PullUpAction mPullUpAction;
    //本view原始Y位置
    private float initY;
    //finger的状态
    private int fingerState=FINGER_NONEXPOSED;
    //能否开启回弹
    private boolean isSpringBack=false;
    //回弹止的位置
    private int springBackPosition=0;
    //finger的起始y位置
    private float fingerOldY=0;
    //action
    private IAction action=new Action();
    //SpringBack的handel机制
    private SpringBackHandler sbHandler=new SpringBackHandler();
    private boolean isBottom=false;
    private boolean isTop=true;
    private boolean init=true;
	/*特别方法区*/
    /**
     * 初始化
     */
    private void init() {
        initY=getY();
        System.out.println("initY"+initY);
    }
    /**
     * 开始回弹
     */
    public void startSpringBack(){
        isSpringBack=true;
        sbHandler.send();

    }
    /**
     * 停止回弹
     */
    public void stopSpringBack() {
        isSpringBack=false;
    }
    /**
     *
     * @paramMotionEvent
     */
    private void pullProcess(float fingerY) {
        /***
         * 待定
         */
        //判断是否符合上拉或下拉条件
            //求当前fingerY和fingerStartY的差
            float cy = fingerY - fingerOldY;
            //当pull一开始的时候
            if (!isPullDown() && !isPullUp()) {
                if(isTop()&&havePullDown) {
                    //移动本view
                    if(cy>0) {
                        setXPullY(pullY + (cy) / 2);
                    }else{
                        if(mPullDownAction!=null){mPullDownAction.pullDownAction(0,action);}
                    }
                }else if(isBottom()&&havePullUp){
                    if(cy<0) {
                        setXPullY(pullY + (cy) / 2);
                    }else{
                        if(mPullUpAction!=null){mPullUpAction.pullUpAction(0,action);}
                    }
                }else if(getFirstVisiblePosition()==0&&cy<0){
                    if(mPullDownAction!=null){mPullDownAction.pullDownAction(0,action);}
                }else if (getLastVisiblePosition()==getCount()&&cy>0){
                    if(mPullUpAction!=null){mPullUpAction.pullUpAction(0,action);}
                }
            } else {
//			如果是下拉
                if (isPullDown()) {
                    if (cy > 0) {
                        setXPullY(pullY + (cy) / 2);
                    } else {
                        setXPullY(pullY + cy);
                    }
                }

                else{//上拉
                    if (cy < 0) {
                        setXPullY(pullY + (cy) / 2);
                    } else {
                        setXPullY(pullY + cy);
                    }
                }

             }
        fingerOldY = fingerY;

    }
    /**
     * 理顺上下拉
     * @param y
     */
    private void setXPullY(float y){
        if(y>springBackPosition-1&&y<springBackPosition+1){setPullY(springBackPosition);return;}
        if(pullY<springBackPosition&&y>springBackPosition){
            setPullY(springBackPosition);
        }else
        if(pullY>springBackPosition&&y<springBackPosition){
            setPullY(springBackPosition);
        }else{
            setPullY(y);
        }
    }
    /**
     * @param正数表下拉多少，负数表上拉多少，会出发上下拉的监听
     */
    public void setPullY(float y){
        pullY=y;
        //原始位置加拉位置等于本view位置
        this.setY(initY+pullY);
        //出发事件
        if(y>0&&mPullDownAction!=null){mPullDownAction.pullDownAction(y,action);}
        if(y<0&&mPullUpAction!=null){mPullUpAction.pullUpAction(y,action);}
    }
    /*获取状态方法区*/
    //是否處於最頂端
    public boolean isTop(){
        boolean canup=canScrollVertically(-1);
        return !canup;
    }
    //是否处于最低端
    public boolean isBottom(){
        boolean candown=canScrollVertically(1);
        return !candown;
    }
    //获取是否正在下拉
    public boolean isPullDown(){
        return (int)pullY>springBackPosition;
    }
    //获取是否正在上拉
    public boolean isPullUp(){
        return (int)pullY<springBackPosition;
    }
    /*属性get和set方法区*/
    public PullDownAction getmPullDownAction() {
        return mPullDownAction;
    }
    public void setmPullDownAction(PullDownAction mPullDownAction) {
        this.mPullDownAction = mPullDownAction;
    }
    public PullUpAction getmPullUpAction() {
        return mPullUpAction;
    }
    public void setmPullUpAction(PullUpAction mPullUpAction) {
        this.mPullUpAction = mPullUpAction;
    }
    public boolean isHavePullDown() {
        return havePullDown;
    }
    public void setHavePullDown(boolean havePullDown) {
        this.havePullDown = havePullDown;
    }
    public boolean isHavePullUp() {
        return havePullUp;
    }
    public void setHavePullUp(boolean havePullUp) {
        this.havePullUp = havePullUp;
    }
    /*构造方法*/
    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PullListView(Context context) {
        super(context);
    }
    public PullListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /*重写方法区*/
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        //更新finger的状态
        if(MotionEvent.ACTION_DOWN ==ev.getAction()){
            fingerState=FINGER_TANGENT;fingerOldY=getY()+ev.getY();}
        if(MotionEvent.ACTION_UP   ==ev.getAction()){
            fingerState=FINGER_NONEXPOSED;
            //回弹
            startSpringBack();
        }
        //拉
        if(MotionEvent.ACTION_MOVE ==ev.getAction()){pullProcess(getY()+ev.getY());}
        return super.dispatchTouchEvent(ev);
    }
    //获取位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(init) {
            init();
            init=false;
        }
    }
    /*内部接口*/
    //下拉动作接口
    public interface PullDownAction{
        void pullDownAction(float distance,IAction action);
    }
    //下拉动作接口
    public interface PullUpAction{
        void pullUpAction(float distance,IAction action);
    }
    public interface IAction{
        void setStopSpringBack(int p);
        float getPullY();
        void startSpringBack();
        int getFingerState();
    }
    /*内部类*/
    //action
    private class Action implements IAction{

        @Override
        public void setStopSpringBack(int p) {
            springBackPosition=p;
        }

        @Override
        public float getPullY() {
            return pullY;
        }

        @Override
        public void startSpringBack() {
            PullListView.this.startSpringBack();
        }

        @Override
        public int getFingerState() {
            return fingerState;
        }
    }
    private class SpringBackHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(!isSpringBack||Math.round(pullY)==springBackPosition||fingerState==FINGER_TANGENT){

                return;}
            float i=(pullY-springBackPosition)*0.2f;
            setXPullY(pullY-i);
            send();
        }

        private void send() {
            sendEmptyMessage(1);
        }
    }
}
