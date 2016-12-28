package com.canmeizhexue.common.ui.dalvik;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.canmeizhexue.common.base.BaseActivity;
import com.canmeizhexue.common.entity.classloader.SubModel;
import com.canmeizhexue.common.utils.LogUtils;
import com.canmeizhexue.common.utils.performance.ClassLoadCheckerUtil;

import java.io.File;

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
/*                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Cursor cursor=null;
                        try {
                            cursor = PhoneBookUtil.getMobilePhoneCursor(ClassLoaderActivity.this,"13822133405");
                            if(cursor!=null && cursor.moveToFirst()){
                                do{
                                    ContactEntity entity = PhoneBookUtil.parseCursor(cursor);
                                    LogUtils.d("silence","----"+entity);
                                }while (cursor.moveToNext());
                            }
                            LogUtils.d("silence","----------end");
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            if(cursor!=null){
                                cursor.close();
                            }
                        }
                    }
                }).start();*/

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
    private void printClassLoader(){
        Application application = getApplication();

        Log.d("silence","----getPackageCodePath---"+application.getPackageCodePath());
        Log.d("silence","---getPackageResourcePath----"+application.getPackageResourcePath());
        Log.d("silence","---getCacheDir----"+application.getCacheDir());
        Log.d("silence","---getExternalCacheDir----"+application.getExternalCacheDir());
        Log.d("silence","---getFilesDir----"+application.getFilesDir());
        Log.d("silence","---getObbDir----"+application.getObbDir());
        //这些目录在应用程序卸载的时候会删除
        Log.d("silence","---getExternalFilesDir----"+application.getExternalFilesDir(null));
        Log.d("silence","---getExternalFilesDir----"+application.getExternalFilesDir("hello"));

        ApplicationInfo applicationInfo=application.getApplicationInfo();
        Log.d("silence","---dataDir----"+applicationInfo.dataDir);
        Log.d("silence","---sourceDir----"+applicationInfo.sourceDir);
//        LogUtil.d("silence","---deviceProtectedDataDir----"+applicationInfo.deviceProtectedDataDir);
        Log.d("silence","---nativeLibraryDir----"+applicationInfo.nativeLibraryDir);
        File file = new File(applicationInfo.nativeLibraryDir);
        if(file.exists() && file.isDirectory()){
            String[]files=file.list();
            for(String fileName:files){
                Log.d("silence","---nativeLibraryDir--fileName--"+ fileName);
            }
        }
        Log.d("silence","---publicSourceDir----"+applicationInfo.publicSourceDir);
        Log.d("silence","---sharedLibraryFiles----"+applicationInfo.sharedLibraryFiles);
        Log.d("silence","---sourceDir----"+applicationInfo);

//        LogUtil.d("silence","---getObbDir----"+application.getObbDirs());
//        LogUtil.d("silence","---getCodeCacheDir----"+application.getCodeCacheDir());


        Log.i("DEMO", "Context的类加载加载器:"+Context.class.getClassLoader());
        Log.i("DEMO", "ListView的类加载器:"+ListView.class.getClassLoader());
        Log.i("DEMO", "应用程序默认加载器:"+getClassLoader());
        Log.i("DEMO", "系统类加载器:"+ClassLoader.getSystemClassLoader());
        Log.i("DEMO", "系统类加载器和Context的类加载器是否相等:"+(Context.class.getClassLoader()==ClassLoader.getSystemClassLoader()));
        Log.i("DEMO", "系统类加载器和应用程序默认加载器是否相等:"+(getClassLoader()==ClassLoader.getSystemClassLoader()));

        Log.i("DEMO","打印应用程序默认加载器的委派机制:");
        ClassLoader classLoader = getClassLoader();
        while(classLoader != null){
            Log.i("DEMO", "类加载器:"+classLoader);
            classLoader = classLoader.getParent();
        }

        Log.i("DEMO","打印系统加载器的委派机制:");
        classLoader = ClassLoader.getSystemClassLoader();
        while(classLoader != null){
            Log.i("DEMO", "类加载器:"+classLoader);
            classLoader = classLoader.getParent();
        }
        //俩者的BootClassLoader是同一个实例
/*        try {
            Log.i("DEMO","打印应用程序默认加载器的委派机制:"+(getClassLoader().getParent()==classLoader.getParent()));
        }catch (Exception e){

            // 这样可以加上我们自己的信息
            Exception exception = new Exception("崩溃啦--------",e );
            //加上这句话，不再打印构造上面那个Exception的堆栈信息，不然堆栈信息过长的话，可能原来有用的信息却打印不出来了，，尤其是捕捉到UnsatisfiedLinkError
            exception.setStackTrace(new StackTraceElement[0]);
            exception.printStackTrace();
        }catch (Error error){
            if(error instanceof UnsatisfiedLinkError){
            //描述信息可以加上applicationInfo.nativeLibraryDir里面是否真的存在相应文件
                Error newError = new Error("发生错误了",error);
                //加上这句话，不再打印构造上面那个Exception的堆栈信息，不然堆栈信息过长的话，可能原来有用的信息却打印不出来了，，尤其是捕捉到UnsatisfiedLinkError
                newError.setStackTrace(new StackTraceElement[0]);
                newError.printStackTrace();
            }else {
                error.printStackTrace();
            }

        }*/
    }
}
