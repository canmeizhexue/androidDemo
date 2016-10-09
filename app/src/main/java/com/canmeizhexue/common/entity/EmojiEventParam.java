package com.canmeizhexue.common.entity;

/**
 * Created by silence on 2016-10-9.
 */
public class EmojiEventParam {
    public int emojiType;
    public String emojiName;
    public int resId;
    public EmojiEventParam(String emojiName, int emojiType,int resId) {
        this.emojiName = emojiName;
        this.emojiType = emojiType;
        this.resId = resId;
    }
}
