package com.canmeizhexue.common.image;

/**
 * <p>自定义大小</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2016-4-5)
 */
public class CustomImageSizeModel implements CustomGlideModule.CustomImageSizeModel {
    String baseImageUrl;

    /**
     * 构造
     * @param baseImageUrl url
     */
    public CustomImageSizeModel(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
    }
    @Override
    public String requestCustomSizeUrl(int width, int height) {
        return baseImageUrl.replace(".jpg","_"+height+"x"+width+".jpg");
    }
}
