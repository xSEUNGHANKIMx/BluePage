package com.example.bluepage.dbmanager;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

public abstract class BluePageCallLogModelLoader<T> extends AsyncTaskLoader<T> {

    final Uri mContentUri;
    final ForceLoadContentObserver mContentObserver;

    public BluePageCallLogModelLoader(Context context, Uri contentUri) {
        super(context);
        mContentUri = contentUri;
        mContentObserver = new ForceLoadContentObserver();
    }

    @Override
    protected void onStartLoading() {
        getContext().getContentResolver().registerContentObserver(mContentUri,
            false, mContentObserver);
        if (takeContentChanged()) {
            forceLoad();
        } else {
            T prevResult = takePrevResult();
            if (prevResult != null) {
                deliverResult(prevResult);
            } else {
                forceLoad();
            }
        }
    }

    @Override
    protected void onReset() {
        getContext().getContentResolver().unregisterContentObserver(
            mContentObserver);
    }

    protected T takePrevResult() {
        return null;
    }
}
