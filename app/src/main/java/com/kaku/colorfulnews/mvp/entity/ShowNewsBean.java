package com.kaku.colorfulnews.mvp.entity;

/**
 * Created by zx on 2018/1/8 0008.
 */

public class ShowNewsBean {

    /**
     * appid : 12181758
     * appname :
     * isshowwap : 1
     * wapurl : https://93106.com
     * status : 1
     * desc : 成功返回数据
     */

    private String appid;
    private String appname;
    private String isshowwap;
    private String wapurl;
    private int status;
    private String desc;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getIsshowwap() {
        return isshowwap;
    }

    public void setIsshowwap(String isshowwap) {
        this.isshowwap = isshowwap;
    }

    public String getWapurl() {
        return wapurl;
    }

    public void setWapurl(String wapurl) {
        this.wapurl = wapurl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
