package com.neykov.mvp.support;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.neykov.mvp.Presenter;
import com.neykov.mvp.PresenterFactory;
import com.neykov.mvp.PresenterLifecycleHelper;
import com.neykov.mvp.ViewWithPresenter;

@SuppressWarnings("unused")
public abstract class DialogViewFragment<P extends Presenter> extends DialogFragment
        implements ViewWithPresenter<P>, PresenterFactory<P> {

    private PresenterLifecycleHelper<P> presenterDelegate;

    public void setUnbindOnStateSaved(boolean unbind){
        if (presenterDelegate == null){
            throw new IllegalStateException("setUnbindOnStateSaved() should be called inside or after onCreate().");
        }
        presenterDelegate.setUnbindOnStateSaved(unbind);
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate = new PresenterLifecycleHelper<>(this,
                FragmentPresenterStorage.from(getActivity().getSupportFragmentManager()));
        presenterDelegate.restoreState(savedInstanceState);
        presenterDelegate.markViewStateRestored();
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        presenterDelegate.saveState(bundle);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.destroy(presenterShouldBeDestroyed());
    }

    @Override
    public void onStart() {
        super.onStart();
        presenterDelegate.markViewStateRestored();
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.markViewStateRestored();
        presenterDelegate.bindView(this);
    }

    @CallSuper
    @Override
    public void onPause() {
        presenterDelegate.unbindView(presenterShouldBeDestroyed());
        super.onPause();
    }

    @Override
    public final P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    protected boolean presenterShouldBeDestroyed() {
        return getActivity().isFinishing();
    }
}