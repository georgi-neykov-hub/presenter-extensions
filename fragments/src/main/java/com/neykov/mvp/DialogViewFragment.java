package com.neykov.mvp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

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
                FragmentPresenterStorage.from(getActivity().getFragmentManager()));
        presenterDelegate.restoreState(savedInstanceState);
        presenterDelegate.markSaveStateChanged(false);
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        presenterDelegate.markSaveStateChanged(true);
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
        presenterDelegate.markSaveStateChanged(false);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.markSaveStateChanged(false);
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