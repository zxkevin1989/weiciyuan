package org.qii.weiciyuan.ui.browser;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import org.qii.weiciyuan.R;
import org.qii.weiciyuan.bean.MessageBean;
import org.qii.weiciyuan.ui.Abstract.AbstractAppActivity;
import org.qii.weiciyuan.ui.Abstract.IToken;
import org.qii.weiciyuan.ui.Abstract.IWeiboMsgInfo;
import org.qii.weiciyuan.ui.basefragment.AbstractTimeLineFragment;
import org.qii.weiciyuan.ui.main.MainTimeLineActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jiang Qi
 * Date: 12-8-1
 */
public class BrowserWeiboMsgActivity extends AbstractAppActivity implements IWeiboMsgInfo, IToken {

    private MessageBean msg;
    private String token;


    private String comment_sum = "";
    private String retweet_sum = "";

    private ViewPager mViewPager = null;

    private TimeLinePagerAdapter adapter;

    private AbstractTimeLineFragment commentFragment;
    private AbstractTimeLineFragment repostFragment;


    public void setCommentFragment(AbstractTimeLineFragment commentFragment) {
        this.commentFragment = commentFragment;
    }

    public void setRepostFragment(AbstractTimeLineFragment repostFragment) {
        this.repostFragment = repostFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        msg = (MessageBean) intent.getSerializableExtra("msg");
        setContentView(R.layout.maintimelineactivity_viewpager_layout);

        buildViewPager();
        buildActionBarAndViewPagerTitles();
    }

    private void buildViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new TimeLinePagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(onPageChangeListener);

    }

    private void buildActionBarAndViewPagerTitles() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.homepage));

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.weibo))
                .setTabListener(tabListener));

        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.comments))
                .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.repost))
                .setTabListener(tabListener));

    }

    ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            getActionBar().setSelectedNavigationItem(position);
            switch (position) {
                case 1:
                    ((CommentsByIdTimeLineFragment) commentFragment).load();
                    break;
                case 2:
                    ((RepostsByIdTimeLineFragment) repostFragment).load();
                    break;
            }

        }
    };

    ActionBar.TabListener tabListener = new ActionBar.TabListener() {

        public void onTabSelected(ActionBar.Tab tab,
                                  FragmentTransaction ft) {

            mViewPager.setCurrentItem(tab.getPosition());
            if (commentFragment != null)
                commentFragment.clearActionMode();
            if (repostFragment != null)
                repostFragment.clearActionMode();

        }

        public void onTabUnselected(ActionBar.Tab tab,
                                    FragmentTransaction ft) {

        }

        public void onTabReselected(ActionBar.Tab tab,
                                    FragmentTransaction ft) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainTimeLineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class TimeLinePagerAdapter extends
            FragmentPagerAdapter {

        List<Fragment> list = new ArrayList<Fragment>();


        public TimeLinePagerAdapter(FragmentManager fm) {
            super(fm);
            list.add(new BrowserWeiboMsgFragment(msg));
            list.add(new CommentsByIdTimeLineFragment(token, msg.getId()));
            list.add(new RepostsByIdTimeLineFragment(token, msg.getId(), msg));
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.browserweibomsgactivity_menu, menu);
//
//        menu.getItem(0).setTitle(menu.getItem(0).getTitle() + "(" + retweet_sum + ")");
//        menu.getItem(1).setTitle(menu.getItem(1).getTitle() + "(" + comment_sum + ")");
//
//        boolean fav = msg.isFavorited();
//        if (fav) {
//            menu.findItem(R.id.menu_fav).setIcon(R.drawable.fav_un_black);
//        } else {
//            menu.findItem(R.id.menu_fav).setIcon(R.drawable.fav_en_black);
//        }
//
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public MessageBean getMsg() {
        return msg;
    }


}
