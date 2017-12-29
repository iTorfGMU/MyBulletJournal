package com.torfin.mybulletjournal.taskdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by torftorf1 on 12/25/17.
 */

public class TaskDetailsActivity extends AppCompatActivity {


    public static void start(Context context, int taskPosition) {
        Intent intent = new Intent(context, TaskDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
