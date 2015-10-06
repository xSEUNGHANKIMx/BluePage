package com.example.bluepage;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

@ReportsCrashes(
    formKey = "", // This is required for backward compatibility but not used
    mailTo = BluePageConstants.ACRA_MAIL_TO_ADDRESS,
    mode = ReportingInteractionMode.DIALOG,
    resToastText = R.string.crash_toast_text,
    resDialogText = R.string.crash_dialog_text,
    resDialogIcon = android.R.drawable.ic_dialog_info,
    resDialogTitle = R.string.crash_dialog_title,
    resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
    resDialogOkToast = R.string.crash_dialog_ok_toast)

public class BluePageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ACRA.init(this);

        initImageLoader(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by ImageLoaderConfiguration.createDefault(this); method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
            .threadPriority(Thread.NORM_PRIORITY - 2)
            .denyCacheImageMultipleSizesInMemory()
            .diskCacheFileNameGenerator(new Md5FileNameGenerator())
            .diskCacheSize(50 * 1024 * 1024) // 50Mb
            .tasksProcessingOrder(QueueProcessingType.LIFO)
            // .writeDebugLogs() // Remove for release app
            .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
