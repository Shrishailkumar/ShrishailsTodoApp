package com.android.todoappshrishail.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.todoappshrishail.R;
import com.android.todoappshrishail.model.NewTodoData;
import com.android.todoappshrishail.model.Todo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();
    private List<Todo> todoList = new ArrayList<>();
    private ITodoCheckedListener iTodoCheckedListener;
    Context context;
    NewTodoData mNewTodoData;

    public RecyclerViewAdapter(ITodoCheckedListener ITodoCheckedListener, List<Todo> list) {
        this.iTodoCheckedListener = ITodoCheckedListener;
        this.todoList = list;

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        this.context = parent.getContext();
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.title.setText(todo.todo_name);
        holder.description.setText(todo.todo_description);
        if (isStoragePermissionGranted()) {
            Picasso.with(context).load(Uri.parse(todo.todo_imageUri)).fit().centerCrop()
                    .error(R.drawable.error)
                    .into(holder.image);

        }
        holder.checkBoxComplted.setChecked(todo.todo_is_checked);
        if (todo.todo_is_checked) {
            holder.mLayout.setBackgroundColor(R.color.lightGrey);
            holder.checkBoxComplted.setEnabled(false);
            holder.checkBoxComplted.setActivated(false);
        } else {
            holder.checkBoxComplted.setEnabled(true);
            holder.checkBoxComplted.setActivated(true);
        }
        holder.checkBoxComplted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                todo.todo_is_checked = b;
                iTodoCheckedListener.onTodoTaskChecked(todo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void updateTodoList(List<Todo> list) {
        todoList.clear();
        todoList.addAll(list);
        notifyDataSetChanged();
    }

    public interface ITodoCheckedListener {
        void onTodoTaskChecked(Todo todo);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public ImageView image;
        public CheckBox checkBoxComplted;
        public View mLayout;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.tv_todo_title);
            image = view.findViewById(R.id.im_image_todo);
            description = view.findViewById(R.id.et_description);
            checkBoxComplted = view.findViewById(R.id.cb_checkBox);
            mLayout = view.findViewById(R.id.parentLayoutIdTodoRow);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

}
