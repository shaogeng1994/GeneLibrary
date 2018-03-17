package com.shao.genelibrary.sample;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shao.databindingadapter.library.SimpleAdapter;
import com.shao.databindingadapter.library.base.DataBindingAdapter;
import com.shao.genelibrary.sample.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = new MainViewModel();
        mBinding.setMainViewModel(mViewModel);


        mBinding.mainRecycler.setAdapter(new SimpleAdapter(R.layout.item_button, BR.mainItemViewModel));
        mBinding.mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.mainRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = 10;
                outRect.bottom = 10;
            }
        });

    }



    @BindingAdapter({"bind:items"})
    public static void setItems(RecyclerView recyclerView, List list) {
        if (recyclerView == null) return;
        DataBindingAdapter adapter = (DataBindingAdapter) recyclerView.getAdapter();
        adapter.setNewData(list);
    }
}
