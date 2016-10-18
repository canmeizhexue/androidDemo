package com.canmeizhexue.common.emojiexpression;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.canmeizhexue.common.utils.LogUtils;
import com.canmeizhexue.common.utils.SharedPreferencedUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**表情fragment
 * Created by silence on 2016-10-9.
 */
public class EmotionMainFragment extends BaseFragment implements TextWatcher{

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

    private EditText bindedEditText;

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
        if(isBindToBarEditText){
            bindedEditText = (EditText) rootView.findViewById(R.id.bar_edit_text);
        }else{
            bindedEditText = (EditText) contentView;
        }
        bindedEditText.addTextChangedListener(this);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
    private int currentEmotionType;
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
            currentEmotionType = emojiEventParam.emojiType;

/*            String emotionName = emojiEventParam.emojiName;
            int curPosition = editText.getSelectionStart();
            Editable editable = editText.getText();
            //指定位置插入文字，触发textwatcher
            editable.insert(curPosition,emotionName);*/
            if(emojiEventParam.resId==EmotionManager.EMOTION_DELETE_RES_ID || EmotionManager.EMOTION_DELETE.equals(emojiEventParam.emojiName)){
                // 如果点击了最后一个回退按钮,则调用删除键事件
                editText.dispatchKeyEvent(new KeyEvent(
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }else{
                // 如果点击了表情,则添加到输入框中
                String emotionName = emojiEventParam.emojiName;


                // 获取当前光标位置,在指定位置上添加表情图片文本
                int curPosition = editText.getSelectionStart();
//                StringBuilder sb = new StringBuilder(editText.getText().toString());
//                sb.insert(curPosition, emotionName);
//
//                // 特殊文字处理,将表情等转换一下
//                editText.setText(SpanStringUtils.getEmotionContent(emojiEventParam.emojiType,
//                        getActivity(), editText, sb.toString()));
                Editable editable = editText.getText();
                editable.insert(curPosition,emotionName);
                // 将光标设置到新增完表情的右侧
//                editText.setSelection(curPosition + emotionName.length());
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
        final SharedPreferencedUtils sharedPreferencedUtils = new SharedPreferencedUtils(getActivity(),"emotion");
        sharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, currentPosition);

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
                int oldPosition = sharedPreferencedUtils.getInteger(getActivity(), CURRENT_POSITION_FLAG, 0);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                currentPosition = position;
                datas.get(currentPosition).isSelected = true;
                sharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, currentPosition);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        /*   此时charSequence并没有包含变化的内容     这句话是说，从start位置开始有count个字符，将要被after个字符代替。
        如果你添加了一个字符，就从start位置开始，after=1 。如果你删了一个字符，start同样的意思，after=0就是1个字符被替代0个字符替代;*/
        currentSelection = bindedEditText.getSelectionStart();
        LogUtils.d("silence","beforeTextChanged---charSequence---"+charSequence+"------start---"+start+"-------count---"+count+"-------after----"+after);
    }
    private CharSequence charSequence;
    private int start,count,currentSelection;
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /* 此时charSequence已经包含变化的内容 并且这里不要对CharSequence进行改变      这句话是说，从start位置开始有count个字符，替代了旧的文本框内的before个字符。
        如果你添加了，一个字符，就从start位置开始，before=0 ,之前并没有什么被替代。如果你删了一个字符，start同样的意思，before=1就是1个字符被替代0个字符替代，所以count=0;*/
        if(count==0){
            // 删除字符
            LogUtils.d("silence","delete");
        }
        charSequence = s;
        this.start = start;
        this.count = count;
        CharSequence  changed = s.subSequence(start,start+count);

        LogUtils.d("silence","changed---"+changed);
        LogUtils.d("silence","onTextChanged--------"+s+"--start----"+start+"----before----"+before+"-------count-----"+count);
/*        if(!TextUtils.isEmpty(changed) && "2".equals(changed.toString())){
            int selection = bindedEditText.getSelectionStart();
            bindedEditText.setText(s.subSequence(0,start).toString()+s.subSequence(start+count,s.length()).toString());
            bindedEditText.setSelection(selection-1);
        }*/


    }

    @Override
    public void afterTextChanged(Editable editable) {
        LogUtils.d("silence","afterTextChanged--------"+editable.toString());
        CharSequence  changed = charSequence.subSequence(start,start+count);
/*        if(!TextUtils.isEmpty(changed) && "2".equals(changed.toString())){
            int selection = bindedEditText.getSelectionStart();
            bindedEditText.setText(s.subSequence(0,start).toString()+s.subSequence(start+count,s.length()).toString());
            bindedEditText.setSelection(selection-1);
        }*/
        if(!TextUtils.isEmpty(changed)){
            EmotionManager.handleEmotion(bindedEditText,charSequence,start,count,currentEmotionType,currentSelection);
        }
/*        if (TextUtils.isEmpty(etInput.getText().toString())) {
            ivSend.setEnabled(false);
        } else {
            ivSend.setEnabled(true);
        }*/
    }
}


