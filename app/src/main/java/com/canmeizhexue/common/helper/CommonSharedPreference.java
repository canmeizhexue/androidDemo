package com.canmeizhexue.common.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <p>CommonSharedPreference类 概述，经常和sp交互</p>
 *
 * @author silence
 * @version 1.0 (2016-10-18 14:35)
 */
public class CommonSharedPreference {
    private static final String FILE_NAME="common_usage";
    public static final String BOOLEAN_IS_FIRST_USAGE="boolean_is_first_usage";
    public static final String STRING_LAST_VERSION_NAME="string_last_version_name";
    public static SharedPreferences getCommonSharedPreference(Context context){
        return context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
    }

}
