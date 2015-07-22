package com.gongpingjia.carplay.bean;

/**
 * @Description
 * @author Administrator
 * @date 2015-7-21 下午4:48:02
 */
public class BrandDetails {

    private String mum;

    private String name;

    private String slug;

    public String getMum() {
        return mum;
    }

    public void setMum(String mum) {
        this.mum = mum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "BrandDetails [mum=" + mum + ", name=" + name + ", slug=" + slug + "]";
    }

}
