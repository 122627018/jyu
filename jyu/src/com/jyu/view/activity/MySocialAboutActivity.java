package com.jyu.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyu.R;
import com.jyu.view.base.BaseActivity;
import com.jyu.view.base.BasePage;
import com.jyu.view.page.HasJoinSocialPage;
import com.jyu.view.page.MyOriginateSocial;

public class MySocialAboutActivity extends BaseActivity {

	private ViewPager mPager;// 页卡内容
	private List<BasePage> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
//	private TextView t1, t2, t3;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度

	private HasJoinSocialPage hpage;// 参加过的活动page
	private MyOriginateSocial mpage; // 我发起的活动page

	private TextView text1;
	private TextView text2;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_my_social);
		text1 = (TextView) findViewById(R.id.text11);
		text2 = (TextView) findViewById(R.id.text22);
		text1.setText("我参加过的活动");
		text2.setText("我发起的活动");
		initTitleBar();
		titleTv.setText("活动相关");
		InitImageView();
		InitViewPager();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void processClick(View v) {

	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		hpage = new HasJoinSocialPage(MySocialAboutActivity.this);
		listViews = new ArrayList<BasePage>();
		mpage = new MyOriginateSocial(MySocialAboutActivity.this);
		listViews.add(hpage);
		listViews.add(mpage);

		mPager.setAdapter(new MyPagerAdapter(listViews, ct));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		text1.setOnClickListener(new MyOnClickListener(0));
		text2.setOnClickListener(new MyOnClickListener(1));
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<BasePage> mListViews;

		public MyPagerAdapter(List<BasePage> mListViews, Context ct) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0)
					.removeView(mListViews.get(arg1).getContentView());
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0)
					.addView(mListViews.get(arg1).getContentView(), 0);
			return mListViews.get(arg1).getContentView();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (intent != null) {
			if (requestCode == 1) {
				mpage.updateItem(intent);
			}
			super.onActivityResult(requestCode, resultCode, intent);
		}

	}
}
