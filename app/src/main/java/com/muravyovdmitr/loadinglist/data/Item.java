package com.muravyovdmitr.loadinglist.data;

/**
 * Created by Dima Muravyov on 23.06.2016.
 */
public class Item {
    private String mTittle;
    private int mLoadingTime;
    private boolean mIsLoad;

    public String getTittle() {
        return mTittle;
    }

    public void setTittle(String tittle) {
        mTittle = tittle;
    }

    public int getLoadingTime() {
        return mLoadingTime;
    }

    public void setLoadingTime(int loadingTime) {
        mLoadingTime = loadingTime;
    }

    public boolean isLoad() {
        return mIsLoad;
    }

    public void setLoad(boolean load) {
        mIsLoad = load;
    }
}
