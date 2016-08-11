package com.canmeizhexue.common.views.imageselector.bean;

import java.util.ArrayList;

/**
 * <p>文件夹实体</p>
 *
 * @author chenchao<br/>
 * @version 1.0 (2015-11-09)
 */
public class Folder {
    private String name;
    private String path;
    private String cover;
    private ArrayList<String> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
