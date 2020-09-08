package com.android.todoappshrishail.model;

public class NewTodoData {

    private static String mNewTodoTitle;
    private static String mNewTodoDesciption;
    private static String mImageUri;
    private static boolean mIsChecked;

    public static boolean ismIsChecked() {
        return mIsChecked;
    }

    public static void setmIsChecked(boolean mIsChecked) {
        NewTodoData.mIsChecked = mIsChecked;
    }

    public static String getmNewTodoTitle() {
        return mNewTodoTitle;
    }

    public static void setmNewTodoTitle(String title) {
        mNewTodoTitle = title;
    }

    public static String getmNewTodoDesciption() {

        return mNewTodoDesciption;
    }

    public static void setmNewTodoDesciption(String mTodoDesciption) {
        mNewTodoDesciption = mTodoDesciption;
    }

    public static String getmImageUri() {

        return mImageUri;
    }

    public static void setmImageUri(String mImgUri) {

        mImageUri = mImgUri;
    }
}
