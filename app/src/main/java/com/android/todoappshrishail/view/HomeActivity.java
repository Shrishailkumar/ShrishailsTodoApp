package com.android.todoappshrishail.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.todoappshrishail.R;
import com.android.todoappshrishail.adapter.RecyclerViewAdapter;
import com.android.todoappshrishail.model.MyDatabase;
import com.android.todoappshrishail.model.Todo;
import com.android.todoappshrishail.presenter.HomeActivityPresenter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeActivityPresenter.IHomeActvityDelegate, RecyclerViewAdapter.ITodoCheckedListener {

    Button mButtonAddNewTodoEntry;
    HomeActivityPresenter homeActivityPresenter;
    RecyclerView mRecyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    MyDatabase myDatabase;
    EditText mSearchView;
    List<Todo> mListofTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        homeActivityPresenter = new HomeActivityPresenter(HomeActivity.this);
        initViews();
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).fallbackToDestructiveMigration().build();
        mButtonAddNewTodoEntry.setOnClickListener(v -> homeActivityPresenter.onClickOfAddTodo());

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void initViews() {
        mButtonAddNewTodoEntry = findViewById(R.id.but_addTodo);
        mRecyclerView = findViewById(R.id.recyclerView_Todo);
        mSearchView = findViewById(R.id.et_searchForTodoItem);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mListofTodos = new ArrayList<>();
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
       /* recyclerViewAdapter = new RecyclerViewAdapter(HomeActivity.this, todoList);
        mRecyclerView.setAdapter(recyclerViewAdapter);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllTodos(false);
    }


    @Override
    public void launchAddTodoActivity() {
        Intent intent = new Intent(HomeActivity.this, NewToDoEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTodoTaskChecked(Todo todo) {
        new AsyncTask<Todo, Void, Integer>() {
            @Override
            protected Integer doInBackground(Todo... params) {
                return myDatabase.daoAccess().updateTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                loadAllTodos(true);
            }
        }.execute(todo);

    }

    private void loadAllTodos(boolean isFromCheckedItemOfList) {
        new AsyncTask<String, Void, List<Todo>>() {
            @Override
            protected List<Todo> doInBackground(String... params) {
                return myDatabase.daoAccess().fetchAllTodos();
            }

            @Override
            protected void onPostExecute(List<Todo> todoList) {
                if (isFromCheckedItemOfList){
                    mListofTodos = recyclerViewAdapter.sortList(todoList);
                }else{
                    recyclerViewAdapter = new RecyclerViewAdapter(HomeActivity.this, todoList);
                    mRecyclerView.setAdapter(recyclerViewAdapter);
                    mListofTodos = todoList;
                }

                //recyclerViewAdapter.updateTodoList(todoList);
            }
        }.execute();
    }

    private void filter(String todoStr) {
        List<Todo> filterdTodo = new ArrayList<>();
        if (todoStr.equals("") || todoStr.equals(" ") || todoStr.isEmpty()) {
            loadAllTodos(false);
        } else {
            for (Todo s : mListofTodos) {
                if (s.todo_name.contains(todoStr)) {
                    filterdTodo.add(s);
                }
            }
        }
        recyclerViewAdapter.updateTodoList(filterdTodo);
    }
}