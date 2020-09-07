package com.android.todoappshrishail.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = MyDatabase.TABLE_NAME_TODO)
public class Todo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int todo_id;
    public String todo_name;
    public String todo_description;
    public String todo_imageUri;
    public boolean todo_is_checked;


}
