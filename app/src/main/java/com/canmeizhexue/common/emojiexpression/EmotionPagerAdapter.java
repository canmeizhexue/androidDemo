package com.canmeizhexue.common.emojiexpression;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;


/**表情适配器，一种类型的表情有一个适配器，因为一页可能显示不完
 * Created by silence on 2016-10-9.
 */
public class EmotionPagerAdapter extends PagerAdapter {

	private List<GridView> gvs;

	public EmotionPagerAdapter(List<GridView> gvs) {
		this.gvs = gvs;
	}

	@Override
	public int getCount() {
		return gvs.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(gvs.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(gvs.get(position));
		return gvs.get(position);
	}

}
