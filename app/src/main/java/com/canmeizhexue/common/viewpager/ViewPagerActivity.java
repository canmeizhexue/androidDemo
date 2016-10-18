package com.canmeizhexue.common.viewpager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;
import com.canmeizhexue.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silence on 2016/3/28.
 */
public class ViewPagerActivity extends BaseActivity {
    private ViewPager viewPager;
    private ImageView imageView;
    private List<View> lists = new ArrayList<View>();
    private MyAdapter myAdapter;
    private Bitmap cursor;
    private int offSet;
    private int currentItem;
    private Matrix matrix = new Matrix();
    private int bmWidth;
    private Animation animation;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_viewpager);
        imageView = (ImageView) findViewById (R.id.cursor);
        textView1 = (TextView) findViewById (R.id.textView1);
        textView2 = (TextView) findViewById (R.id.textView2);
        textView3 = (TextView) findViewById (R.id.textView3);

        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout1, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout2, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout3, null));
        // 可以看到是先销毁不要的页面，然后再生成需要的页面
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout1, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout2, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout3, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout1, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout2, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout3, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout1, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout2, null));
        lists.add(getLayoutInflater().inflate(R.layout.viewpager_layout3, null));

        initeCursor();

        myAdapter = new MyAdapter(lists);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(myAdapter);
//        viewPager.setOffscreenPageLimit(2);//当前这一项的前面和后面分别可以预加载的page的个数
//       ,page个数和item个数不是同一个概念，只有当viewpager的子view都和viewpager一样宽的时候，才是一样的
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                //当滑动式，顶部的imageView是通过animation缓慢的滑动
                LogUtils.d("silence","-onPageSelected-----"+arg0);
                // TODO Auto-generated method stub
                switch (arg0)
                {
                    // 这些位置信息都是相对于view原来的位置的
                    case 0:
                        if (currentItem == 1)
                        {
                            animation = new TranslateAnimation(
                                    offSet * 2 + bmWidth, 0 , 0, 0);
                        }
                        else if(currentItem == 2)
                        {
                            animation = new TranslateAnimation(
                                    offSet * 4 + 2 * bmWidth, 0, 0, 0);
                        }
                        break;
                    case 1:
                        if (currentItem == 0)
                        {
                            animation = new TranslateAnimation(
                                    0, offSet * 2 + bmWidth, 0, 0);
                        }
                        else if (currentItem == 2)
                        {
                            animation = new TranslateAnimation(
                                    4 * offSet + 2 * bmWidth, offSet * 2 + bmWidth, 0, 0);
                        }
                        break;
                    case 2:
                        if (currentItem == 0)
                        {
                            animation = new TranslateAnimation(
                                    0, 4 * offSet + 2 * bmWidth, 0, 0);
                        }
                        else if (currentItem == 1)
                        {
                            animation = new TranslateAnimation(
                                    offSet * 2 + bmWidth, 4 * offSet + 2 * bmWidth, 0, 0);
                        }
                }
                currentItem = arg0;

                animation.setDuration(500);
                animation.setFillAfter(true);
                imageView.startAnimation(animation);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(0);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(1);
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(2);
            }
        });
    }
    private void initeCursor()
    {
        cursor = BitmapFactory.decodeResource(getResources(), R.drawable.cursor);
        bmWidth = cursor.getWidth();

        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();

        offSet = (dm.widthPixels - 3 * bmWidth) / 6;
        matrix.setTranslate(offSet, 0);
        imageView.setImageMatrix(matrix);                                             //需要iamgeView的scaleType为matrix
        currentItem = 0;
    }
    public class MyAdapter extends PagerAdapter {

        List<View> viewLists;

        public MyAdapter(List<View> lists)
        {
            viewLists = lists;
        }

        @Override
        public int getCount() {                                                                 //获得size
            // TODO Auto-generated method stub
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public float getPageWidth(int position) {
            return 0.33f;
        }

        @Override
        public void destroyItem(View view, int position, Object object)                       //销毁Item
        {
            LogUtils.d("silence","destroyItem----"+position);
            ((ViewPager) view).removeView(viewLists.get(position));
        }

        @Override
        public Object instantiateItem(View view, int position)                                //实例化Item
        {
            LogUtils.d("silence","instantiateItem----"+position);
                    ((ViewPager) view).addView(viewLists.get(position)/*, 0*/);
            return viewLists.get(position);
        }

    }
}
