package com.android.todoappshrishail.presenter;

import android.content.Context;

public class TodoActivityPresenter {
    ITodoActivityDelegate iTodoActivityDelegate;

    public TodoActivityPresenter(Context ctx) {
        this.iTodoActivityDelegate = (ITodoActivityDelegate) ctx;
    }

    public void onClickOfAddImage() {
        iTodoActivityDelegate.selectImageAndSave();
    }

    public void onSubmitClick() {
        iTodoActivityDelegate.saveTodoNewData();
    }

    public void onCancelClick() {
        iTodoActivityDelegate.onCancelClick();
    }

    public void selectImageforTodo() {
        iTodoActivityDelegate.selectAndSaveImage();
    }

    public interface ITodoActivityDelegate {

        void selectImageAndSave();

        void saveTodoNewData();

        void selectAndSaveImage();

        void onCancelClick();
    }
}
