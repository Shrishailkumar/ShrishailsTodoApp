package com.android.todoappshrishail.presenter;

import android.content.Context;

public class HomeActivityPresenter {

    IHomeActvityDelegate iHomeActvityDelegate;
    public HomeActivityPresenter(Context ctx){
        this.iHomeActvityDelegate = (IHomeActvityDelegate) ctx;
    }

    public void onClickOfAddTodo() {
        iHomeActvityDelegate.launchAddTodoActivity();
    }

    public interface IHomeActvityDelegate{

        void launchAddTodoActivity();
    }


}
