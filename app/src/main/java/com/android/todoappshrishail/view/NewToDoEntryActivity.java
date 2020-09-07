package com.android.todoappshrishail.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.android.todoappshrishail.R;
import com.android.todoappshrishail.model.MyDatabase;
import com.android.todoappshrishail.model.NewTodoData;
import com.android.todoappshrishail.model.Todo;
import com.android.todoappshrishail.presenter.TodoActivityPresenter;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;

public class NewToDoEntryActivity extends Activity implements TodoActivityPresenter.ITodoActivityDelegate {

    private static final int YOUR_IMAGE_CODE = 11;
    private EditText mNewTodoTitle;
    private EditText mNewTodoDescription;
    private ImageView mNewImage;
    private Button mSubmit;
    private Button mCancel;
    private TodoActivityPresenter mNewTodoActivityPresenter;
    private NewTodoData mNewTodoData;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_todo_entry);
        mNewTodoActivityPresenter = new TodoActivityPresenter(NewToDoEntryActivity.this);
        initView();

        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).build();
        mNewImage.setOnClickListener(l -> mNewTodoActivityPresenter.onClickOfAddImage());
        mSubmit.setOnClickListener(l -> mNewTodoActivityPresenter.onSubmitClick());
        mCancel.setOnClickListener(l -> mNewTodoActivityPresenter.onCancelClick());
    }

    private void initView() {
        mNewImage  = findViewById(R.id.im_new_todo_image);
        mNewTodoTitle = findViewById(R.id.et_new_todo_title);
        mNewTodoDescription = findViewById(R.id.et_new_todo_description);
        mSubmit = findViewById(R.id.but_submit);
        mCancel = findViewById(R.id.but_cancel);
        mNewTodoData = new NewTodoData();
    }

    @Override
    public void selectImageAndSave() {
        mNewTodoActivityPresenter.selectImageforTodo();
    }

    @Override
    public void saveTodoNewData() {
        String title = mNewTodoTitle.getText().toString();
        String description = mNewTodoDescription.getText().toString();
        if (null!=title && null!=description) {
            mNewTodoData.setmNewTodoTitle(title);
            mNewTodoData.setmNewTodoDesciption(description);
            Todo todo = new Todo();
            todo.todo_name = mNewTodoData.getmNewTodoTitle();
            todo.todo_description = mNewTodoData.getmNewTodoDesciption();
            todo.todo_imageUri = mNewTodoData.getmImageUri();
            todo.todo_is_checked = mNewTodoData.ismIsChecked();
            insertRow(todo);
        }else{
            Toast.makeText(NewToDoEntryActivity.this,"in complete Data !!! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selectAndSaveImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select a picture"), YOUR_IMAGE_CODE);
    }

    @Override
    public void onCancelClick() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_IMAGE_CODE) {
            if(resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                mNewTodoData.setmImageUri(selectedImageUri.toString());
                if (isStoragePermissionGranted()) {
                    Picasso.with(NewToDoEntryActivity.this).load(Uri.parse(selectedImageUri.toString())).fit().centerCrop()
                            .error(R.drawable.error)
                            .into(mNewImage);
                }
            }
        }
    }

    private void insertRow(Todo todo) {
        new AsyncTask<Todo, Void, Long>() {
            @Override
            protected Long doInBackground(Todo... params) {
                return myDatabase.daoAccess().insertTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);
                Toast.makeText(NewToDoEntryActivity.this,"Data collected Successfully!",Toast.LENGTH_SHORT).show();
                finish();
            }
        }.execute(todo);

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(NewToDoEntryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
}