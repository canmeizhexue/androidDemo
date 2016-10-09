package com.canmeizhexue.common.emojiexpression;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseFragment;
import com.canmeizhexue.common.entity.EmojiEventParam;
import com.canmeizhexue.common.entity.EventBusParams;
import com.canmeizhexue.common.utils.SharedPreferencedUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**表情fragment
 * Created by silence on 2016-10-9.
 */
public class EmotionMainFragment extends BaseFragment {

    //是否绑定当前Bar的编辑框的flag
    public static final String BIND_TO_EDITTEXT="bind_to_edittext";
    //是否隐藏bar上的编辑框和发生按钮
    public static final String HIDE_BAR_EDITTEXT_AND_BTN="hide bar's editText and btn";

    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG="CURRENT_POSITION_FLAG";
    private int currentPosition =0;
    //底部水平tab
    private RecyclerView recyclerview_horizontal;
    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;
    //表情面板
    private EmotionKeyboard mEmotionKeyboard;

    private EditText bar_edit_text;
    private ImageView bar_image_add_btn;
    private Button bar_btn_send;
    private LinearLayout rl_editbar_bg;

    //需要绑定的内容view
    private View contentView;
    private View rootView;

    //不可横向滚动的ViewPager
    private NoScrollViewPager viewPager;

    //是否绑定当前Bar的编辑框,默认true,即绑定。
    //false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
    private boolean isBindToBarEditText=true;

    //是否隐藏bar上的编辑框和发生按钮,默认不隐藏
    private boolean isHidenBarEditTextAndBtn=false;

    List<Fragment> fragments=new ArrayList<>();

    public static Fragment getInstance(boolean hideBarEdittextAndBtn,boolean bindToEdittext){
        Fragment fragment = new EmotionMainFragment();
        Bundle args = new Bundle();
        args.putBoolean(HIDE_BAR_EDITTEXT_AND_BTN,hideBarEdittextAndBtn);
        args.putBoolean(BIND_TO_EDITTEXT,bindToEdittext);
        fragment.setArguments(args);
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
         rootView = inflater.inflate(R.layout.fragment_main_emotion, container, false);
        Bundle args = getArguments();
        if(args!=null){
            isHidenBarEditTextAndBtn= args.getBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN);
            //获取判断绑定对象的参数
            isBindToBarEditText=args.getBoolean(EmotionMainFragment.BIND_TO_EDITTEXT);
        }
        initView(rootView);
        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(rootView.findViewById(R.id.ll_emotion_layout))//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(!isBindToBarEditText ? ((EditText) contentView) : ((EditText) rootView.findViewById(R.id.bar_edit_text)))//判断绑定那种EditView
                .bindToEmotionButton(rootView.findViewById(R.id.emotion_button))//绑定表情按钮
                .build();
        initListener();
        initDatas();
        //创建全局监听
//        GlobalOnItemClickManagerUtils globalOnItemClickManager= GlobalOnItemClickManagerUtils.getInstance(getActivity());

        if(isBindToBarEditText){
            //绑定当前Bar的编辑框
//            globalOnItemClickManager.attachToEditText(bar_edit_text);

        }else{
            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
//            globalOnItemClickManager.attachToEditText((EditText) contentView);
            mEmotionKeyboard.bindToEditText((EditText)contentView);
        }
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
    @Subscribe
    public void onEventMainThread(EventBusParams eventBusParams){
        if(eventBusParams!=null && EmojiConstant.EVENT_EMOJI_CLICKED.equals(eventBusParams.EVENT_BUS_TYPE)){
            EmojiEventParam emojiEventParam = (EmojiEventParam) eventBusParams.eventBusParam;
            EditText editText ;
            if(isBindToBarEditText){
                editText = (EditText) rootView.findViewById(R.id.bar_edit_text);
            }else{
                editText = (EditText) contentView;
            }
            if(emojiEventParam.resId==EmotionManager.EMOTION_DELETE_RES_ID || EmotionManager.EMOTION_DELETE.equals(emojiEventParam.emojiName)){
                // 如果点击了最后一个回退按钮,则调用删除键事件
                editText.dispatchKeyEvent(new KeyEvent(
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }else{
                // 如果点击了表情,则添加到输入框中
                String emotionName = emojiEventParam.emojiName;

                // 获取当前光标位置,在指定位置上添加表情图片文本
                int curPosition = editText.getSelectionStart();
                StringBuilder sb = new StringBuilder(editText.getText().toString());
                sb.insert(curPosition, emotionName);

                // 特殊文字处理,将表情等转换一下
                editText.setText(SpanStringUtils.getEmotionContent(emojiEventParam.emojiType,
                        getActivity(), editText, sb.toString()));

                // 将光标设置到新增完表情的右侧
                editText.setSelection(curPosition + emotionName.length());
            }

        }
    }
    /**
     * 绑定内容view
     * @param contentView
     * @return
     */
    public void bindToContentView(View contentView){
        this.contentView=contentView;
    }

    /**
     * 初始化view控件
     */
    protected void initView(View rootView){
        viewPager= (NoScrollViewPager) rootView.findViewById(R.id.vp_emotionview_layout);
        recyclerview_horizontal= (RecyclerView) rootView.findViewById(R.id.recyclerview);
        bar_edit_text= (EditText) rootView.findViewById(R.id.bar_edit_text);
        bar_image_add_btn= (ImageView) rootView.findViewById(R.id.bar_image_add_btn);
        bar_btn_send= (Button) rootView.findViewById(R.id.bar_btn_send);
        rl_editbar_bg= (LinearLayout) rootView.findViewById(R.id.rl_editbar_bg);
        if(isHidenBarEditTextAndBtn){//隐藏
            bar_edit_text.setVisibility(View.GONE);
            bar_image_add_btn.setVisibility(View.GONE);
            bar_btn_send.setVisibility(View.GONE);
            rl_editbar_bg.setBackgroundResource(R.color.bg_edittext_color);
        }else{
            bar_edit_text.setVisibility(View.VISIBLE);
            bar_image_add_btn.setVisibility(View.VISIBLE);
            bar_btn_send.setVisibility(View.VISIBLE);
            rl_editbar_bg.setBackgroundResource(R.drawable.shape_bg_reply_edittext);
        }
    }

    /**
     * 初始化监听器
     */
    protected void initListener(){

    }

    /**
     * 数据操作,这里是测试数据，请自行更换数据
     */
    protected void initDatas(){
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i=0 ; i<fragments.size(); i++){
            if(i==0){
                ImageModel model1=new ImageModel();
                model1.icon= getResources().getDrawable(R.mipmap.ic_emotion);
                model1.flag="经典笑脸";
                model1.isSelected=true;
                list.add(model1);
            }else {
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.mipmap.ic_plus);
                model.flag = "其他笑脸" + i;
                model.isSelected = false;
                list.add(model);
            }
        }

        //记录底部默认选中第一个
        currentPosition =0;
        SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, currentPosition);

        //底部tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(),list);
        recyclerview_horizontal.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        recyclerview_horizontal.setAdapter(horizontalRecyclerviewAdapter);
        recyclerview_horizontal.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
        horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //获取先前被点击tab
                int oldPosition = SharedPreferencedUtils.getInteger(getActivity(), CURRENT_POSITION_FLAG, 0);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                currentPosition = position;
                datas.get(currentPosition).isSelected = true;
                SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, currentPosition);
                //通知更新，这里我们选择性更新就行了
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(currentPosition);
                //viewpager界面切换
                viewPager.setCurrentItem(position,false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {
            }
        });



    }

    private void replaceFragment(){

        Fragment f1 = OneTypeEmojiFragment.getInstance(EmotionManager.EMOTION_CLASSIC_TYPE);
        fragments.add(f1);
        Bundle b=null;
        for (int i=0;i<7;i++){
            fragments.add(OneTypeEmojiFragment.getInstance(EmotionManager.EMOTION_TYPE_NONE));
        }

        NoHorizontalScrollerVPAdapter adapter =new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
    }


    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     * @return true则隐藏表情布局，拦截返回键操作
     *         false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress(){
        return mEmotionKeyboard.interceptBackPress();
    }
}


