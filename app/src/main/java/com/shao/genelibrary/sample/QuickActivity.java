package com.shao.genelibrary.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2018/3/16.
 */

public class QuickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.quick_container, QuickFragment.newInstance(getIntent().getStringExtra("tag")))
                .commitAllowingStateLoss();

    }

    public static void start(String tag) {
        App.getInstance().startActivity(newIntent(tag));
    }

    public static Intent newIntent(String tag) {
        Intent intent = new Intent(App.getInstance(), QuickActivity.class);
        intent.putExtra("tag", tag);
        return intent;
    }


}
