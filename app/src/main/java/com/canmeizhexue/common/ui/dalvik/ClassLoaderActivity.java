package com.canmeizhexue.common.ui.dalvik;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.canmeizhexue.common.base.BaseActivity;
import com.canmeizhexue.common.entity.classloader.SubModel;
import com.canmeizhexue.common.utils.LogUtils;
import com.canmeizhexue.common.utils.performance.ClassLoadCheckerUtil;

/**classLoader测试类
 * Created by zengyaping on 2016-9-1.
 */
public class ClassLoaderActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("classLoader 测试类");
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        setContentView(textView,layoutParams);
        LogUtils.d("silence","-----ClassLoaderActivity-----");
        // 能够证明出现SubModel.class的时候，这个类会被加载进来
        ClassLoadCheckerUtil.checkLoaded(getClassLoader(),"com.canmeizhexue.common.entity.classloader.SuperModel");
        ClassLoadCheckerUtil.checkLoaded(getClassLoader(),"com.canmeizhexue.common.entity.classloader.SubModel");

//        SubModel.subStaticMethod();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1.引用子类的静态变量，会导致子类和父类的初始化
//                int i = SubModel.subValue;
                //2. 引用子类的静态函数,会导致子类和父类的初始化
//                SubModel.subStaticMethod();

                //3.通过子类引用父类的静态变量或者静态函数，导致父类初始化，子类加载
//                SubModel.superStaticMethod();

                //4.单个变量声明不会导致类加载，数组声明会导致子类和父类的加载，但不会初始化。
//                SubModel subModel = null;
//                SubModel[] array = new SubModel[5];
                //4.直接引用class对象会导致子类和父类的加载，但不会初始化。
//                Class<SubModel> cls = SubModel.class;

                //5. 引用常量,,不会导致类加载，，，实际上，编译之后，SubModel.subConstantValue已经存在当前这个类的常量池里面了，不会存在SubModel类的符号引用了，和它不会有联系了。
                int i=SubModel.subConstantValue;
                ClassLoadCheckerUtil.checkLoaded(getClassLoader(),"com.canmeizhexue.common.entity.classloader.SuperModel");
                ClassLoadCheckerUtil.checkLoaded(getClassLoader(),"com.canmeizhexue.common.entity.classloader.SubModel");
            }
        });

    }
}
