package com.canmeizhexue.common.emojiexpression;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.ArrayMap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.widget.EditText;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.utils.LogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**表情管理类
 * Created by silence on 2016-10-9.
 */
public class EmotionManager {
    public static final int EMOTION_TYPE_NONE=0x0000;//没有表情的

    /**
     * 表情类型标志符
     */
    public static final int EMOTION_CLASSIC_TYPE=0x0001;//经典表情

    /**
     * key-表情文字;
     * value-表情图片资源
     */
    public static ArrayMap<String, Integer> emptyMap = new ArrayMap<>();
    public static ArrayMap<String, Integer> emotionClassicMap;
    public static final String EMOTION_DELETE="[删除]";
    public static final int EMOTION_DELETE_RES_ID=R.mipmap.compose_emotion_delete;
    private static final String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
    private static Pattern patternEmotion = Pattern.compile(regexEmotion);
    /**
     * 根据类型和表情的名字，获取对应的资源id值
     * @param emotionType 表情类型标志符
     * @param imgName 名称
     * @return
     */
    public static int getImgByName(int emotionType,String imgName) {
        Integer integer=null;
        switch (emotionType){
            case EMOTION_CLASSIC_TYPE:
                integer = emotionClassicMap.get(imgName);
                break;
            default:
                LogUtils.e("the emojiMap is null!!");
                break;
        }
        return integer == null ? -1 : integer;
    }

    /**
     * 根据类型获取表情数据,表情分多套
     * @param emotionType
     * @return
     */
    public static ArrayMap<String, Integer> getEmojiMap(int emotionType){
        ArrayMap emojiMap=null;
        switch (emotionType){
            case EMOTION_CLASSIC_TYPE:
                emojiMap= generateClassicType();
                break;
            default:
                emojiMap= emptyMap;
                break;
        }
        return emojiMap;
    }
    public static void handleEmotion(EditText editText,CharSequence charSequence,int start,int count,int emotionType,int currentCursorPosition){
        CharSequence changed = charSequence.subSequence(start,start+count);
        if(changed.equals(EMOTION_DELETE)){
            // 如果点击了最后一个回退按钮,则调用删除键事件
            editText.dispatchKeyEvent(new KeyEvent(
                    KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            return ;
        }
        // 不进行这些判断，会报异常的
        SpannableString changedSpannable = new SpannableString(changed);
        ImageSpan[] imageSpans=changedSpannable.getSpans(0,changedSpannable.length(),ImageSpan.class);
        if(imageSpans!=null && imageSpans.length>0){

            return ;
        }
/*        if(!TextUtils.isEmpty(changed) && changed instanceof Spannable){
            return ;
        }*/
        Matcher matcherEmotion = patternEmotion.matcher(changed);
        if(matcherEmotion.find()){
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 利用表情名字获取到对应的图片
            Integer imgRes = EmotionManager.getImgByName(emotionType,key);
            if (imgRes != null) {
                // 压缩表情图片
                int size = (int) editText.getTextSize()*13/10;
                Bitmap bitmap = BitmapFactory.decodeResource(editText.getResources(), imgRes);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);

                ImageSpan span = new ImageSpan(editText.getContext(), scaleBitmap);
                SpannableString spannableString = new SpannableString(charSequence);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText.setText(spannableString);
                editText.setSelection(currentCursorPosition+changed.length());
            }
        }
    }
    /**
     * 初始化经典表情
     * @return
     */
    private static ArrayMap<String, Integer>generateClassicType(){
        if(emotionClassicMap==null || emotionClassicMap.isEmpty()){
            emotionClassicMap = new ArrayMap<>();

            emotionClassicMap.put("[呵呵]", R.mipmap.d_hehe);
            emotionClassicMap.put("[嘻嘻]", R.mipmap.d_xixi);
            emotionClassicMap.put("[哈哈]", R.mipmap.d_haha);
            emotionClassicMap.put("[爱你]", R.mipmap.d_aini);
            emotionClassicMap.put("[挖鼻屎]", R.mipmap.d_wabishi);
            emotionClassicMap.put("[吃惊]", R.mipmap.d_chijing);
            emotionClassicMap.put("[晕]", R.mipmap.d_yun);
            emotionClassicMap.put("[泪]", R.mipmap.d_lei);
            emotionClassicMap.put("[馋嘴]", R.mipmap.d_chanzui);
            emotionClassicMap.put("[抓狂]", R.mipmap.d_zhuakuang);
            emotionClassicMap.put("[哼]", R.mipmap.d_heng);
            emotionClassicMap.put("[可爱]", R.mipmap.d_keai);
            emotionClassicMap.put("[怒]", R.mipmap.d_nu);
            emotionClassicMap.put("[汗]", R.mipmap.d_han);
            emotionClassicMap.put("[害羞]", R.mipmap.d_haixiu);
            emotionClassicMap.put("[睡觉]", R.mipmap.d_shuijiao);
            emotionClassicMap.put("[钱]", R.mipmap.d_qian);
            emotionClassicMap.put("[偷笑]", R.mipmap.d_touxiao);
            emotionClassicMap.put("[笑cry]", R.mipmap.d_xiaoku);
            emotionClassicMap.put("[doge]", R.mipmap.d_doge);
            emotionClassicMap.put("[喵喵]", R.mipmap.d_miao);
            emotionClassicMap.put("[酷]", R.mipmap.d_ku);
            emotionClassicMap.put("[衰]", R.mipmap.d_shuai);
            emotionClassicMap.put("[闭嘴]", R.mipmap.d_bizui);
            emotionClassicMap.put("[鄙视]", R.mipmap.d_bishi);
            emotionClassicMap.put("[花心]", R.mipmap.d_huaxin);
            emotionClassicMap.put("[鼓掌]", R.mipmap.d_guzhang);
            emotionClassicMap.put("[悲伤]", R.mipmap.d_beishang);
            emotionClassicMap.put("[思考]", R.mipmap.d_sikao);
            emotionClassicMap.put("[生病]", R.mipmap.d_shengbing);
            emotionClassicMap.put("[亲亲]", R.mipmap.d_qinqin);
            emotionClassicMap.put("[怒骂]", R.mipmap.d_numa);
            emotionClassicMap.put("[太开心]", R.mipmap.d_taikaixin);
            emotionClassicMap.put("[懒得理你]", R.mipmap.d_landelini);
            emotionClassicMap.put("[右哼哼]", R.mipmap.d_youhengheng);
            emotionClassicMap.put("[左哼哼]", R.mipmap.d_zuohengheng);
            emotionClassicMap.put("[嘘]", R.mipmap.d_xu);
            emotionClassicMap.put("[委屈]", R.mipmap.d_weiqu);
            emotionClassicMap.put("[吐]", R.mipmap.d_tu);
            emotionClassicMap.put("[可怜]", R.mipmap.d_kelian);
            emotionClassicMap.put("[打哈气]", R.mipmap.d_dahaqi);
            emotionClassicMap.put("[挤眼]", R.mipmap.d_jiyan);
            emotionClassicMap.put("[失望]", R.mipmap.d_shiwang);
            emotionClassicMap.put("[顶]", R.mipmap.d_ding);
            emotionClassicMap.put("[疑问]", R.mipmap.d_yiwen);
            emotionClassicMap.put("[困]", R.mipmap.d_kun);
            emotionClassicMap.put("[感冒]", R.mipmap.d_ganmao);
            emotionClassicMap.put("[拜拜]", R.mipmap.d_baibai);
            emotionClassicMap.put("[黑线]", R.mipmap.d_heixian);
            emotionClassicMap.put("[阴险]", R.mipmap.d_yinxian);
            emotionClassicMap.put("[打脸]", R.mipmap.d_dalian);
            emotionClassicMap.put("[傻眼]", R.mipmap.d_shayan);
            emotionClassicMap.put("[猪头]", R.mipmap.d_zhutou);
            emotionClassicMap.put("[熊猫]", R.mipmap.d_xiongmao);
            emotionClassicMap.put("[兔子]", R.mipmap.d_tuzi);
        }
        return emotionClassicMap;
    }
}
