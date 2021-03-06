package com.neykov;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;

import com.neykov.mvp.PresenterFactory;
import com.neykov.mvp.PresenterLifecycleHelper;
import com.neykov.mvp.support.FragmentPresenterStorage;
import com.neykov.mvp.sample.R;

public class SampleActivity extends FragmentActivity implements PresenterFactory<SamplePresenter>{

    private PresenterLifecycleHelper<SamplePresenter> presenterLifecycleDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenterLifecycleDelegate = new PresenterLifecycleHelper<>(this,
                FragmentPresenterStorage.from(getSupportFragmentManager()));
        presenterLifecycleDelegate.restoreState(savedInstanceState);
        Log.d("Presenter instance: ", presenterLifecycleDelegate.getPresenter().toString());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterLifecycleDelegate.saveState(outState);
    }

    @Override
    public SamplePresenter createPresenter() {
        return new SamplePresenter();
    }
}
