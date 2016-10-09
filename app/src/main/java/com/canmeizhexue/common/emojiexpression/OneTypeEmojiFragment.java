package com.canmeizhexue.common.emojiexpression;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseFragment;
import com.canmeizhexue.common.entity.EmojiEventParam;
import com.canmeizhexue.common.entity.EventBusParams;
import com.canmeizhexue.common.utils.ContextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**某种表情的fragment,可能会有多个viewpager页
 * Created by silence on 2016-10-9.
 */
public class OneTypeEmojiFragment extends BaseFragment {
    private EmotionPagerAdapter emotionPagerGvAdapter;
    private ViewPager viewPager;
    private EmojiIndicatorView indicatorView;//表情面板对应的点列表
    private int emotionMapType;
    private static final String EMOTION_TYPE="emotion_type";
    public static Fragment getInstance(int emotionType){
        Bundle bundle = new Bundle();
        bundle.putInt(EMOTION_TYPE,emotionType);
        Fragment fragment = new OneTypeEmojiFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    /**
     * 创建与Fragment对象关联的View视图时调用
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emotion_one_page, container, false);
        initView(rootView);
        initListener();
        return rootView;
    }

    /**
     * 初始化view控件
     */
    protected void initView(View rootView){
        emotionMapType = getArguments().getInt(EMOTION_TYPE,EmotionManager.EMOTION_CLASSIC_TYPE);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        indicatorView = (EmojiIndicatorView) rootView.findViewById(R.id.indicator);
        //获取map的类型

        initEmotion();
    }

    /**
     * 初始化监听器
     */
    protected void initListener(){

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPagerPos=0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicatorView.playByStartPointToNext(oldPagerPos,position);
                oldPagerPos=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化表情面板
     * 思路：获取表情的总数，按每行存放7个表情，动态计算出每个表情所占的宽度大小（包含间距），
     *      而每个表情的高与宽应该是相等的，这里我们约定只存放3行
     *      每个面板最多存放7*3=21个表情，再减去一个删除键，即每个面板包含20个表情
     *      根据表情总数，循环创建多个容量为20的List，存放表情，对于大小不满20进行特殊
     *      处理即可。
     */
    private void initEmotion() {
        // 获取屏幕宽度
        int screenWidth = ContextUtils.getScreenWidth(getActivity());
        // item的间距
        int spacing = ContextUtils.dp2px(getActivity(), 12);
        // 动态计算item的宽度和高度
        int itemWidth = (screenWidth - spacing * 8) / 7;
        //动态计算gridview的总高度
        int gvHeight = itemWidth * 3 + spacing * 6;

        List<GridView> emotionViews = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        // 遍历所有的表情的key
        for (String emojiName : EmotionManager.getEmojiMap(emotionMapType).keySet()) {
            emotionNames.add(emojiName);
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 20) {
                emotionNames.add(EmotionManager.EMOTION_DELETE);
                GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                emotionViews.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<>();
            }
        }

        // 判断最后是否有不足20个表情的剩余情况
        if (emotionNames.size() > 0) {
            emotionNames.add(EmotionManager.EMOTION_DELETE);
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            emotionViews.add(gv);
        }

        //初始化指示器
        indicatorView.initIndicator(emotionViews.size());
        // 将多个GridView添加显示到ViewPager中
        emotionPagerGvAdapter = new EmotionPagerAdapter(emotionViews);
        viewPager.setAdapter(emotionPagerGvAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        viewPager.setLayoutParams(params);


    }

    /**
     * 创建显示表情的GridView---也就是viewpager的一页
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        // 创建GridView
        GridView gv = new GridView(getActivity());
        //设置点击背景透明
        gv.setSelector(android.R.color.transparent);
        //设置7列
        gv.setNumColumns(7);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding * 2);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionGridViewAdapter adapter = new EmotionGridViewAdapter(getActivity(), emotionNames, itemWidth, emotionMapType);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object object=adapterView.getItemAtPosition(i);
                if(object!=null){
                    // TODO 通知
                    String name = object.toString();
                    EmojiEventParam emojiEventParam = new EmojiEventParam(name, emotionMapType,EmotionManager.getImgByName(emotionMapType,name));
                    EventBusParams eventBusParams = new EventBusParams(EmojiConstant.EVENT_EMOJI_CLICKED,emojiEventParam);
                    EventBus.getDefault().post(eventBusParams);
                }

            }
        });
        //设置全局点击事件
//        gv.setOnItemClickListener(GlobalOnItemClickManagerUtils.getInstance(getActivity()).getOnItemClickListener(emotionMapType));
        return gv;
    }



}
