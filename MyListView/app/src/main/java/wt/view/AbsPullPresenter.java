package wt.view;

/**
 * Created by Administrator on 2017/2/22.
 */

public abstract class AbsPullPresenter {
    private PullListView mPullListView;
    public AbsPullPresenter(PullListView pullListView){
        this.mPullListView=pullListView;
        mPullListView.setmPullDownAction(new PDown());
        mPullListView.setmPullUpAction(new PUp());
    }

    public abstract void pullDown(float distance, PullListView.IAction action) ;

    public abstract void pullUp(float distance, PullListView.IAction action) ;
    private class PDown implements PullListView.PullDownAction{
        @Override
        public void pullDownAction(float distance, PullListView.IAction action) {
            AbsPullPresenter.this.pullDown(distance, action);
        }
    }

    private class PUp implements PullListView.PullUpAction{
        @Override
        public void pullUpAction(float distance, PullListView.IAction action) {
            AbsPullPresenter.this.pullUp(distance, action);
        }

    }
}
