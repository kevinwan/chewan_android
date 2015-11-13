package com.gongpingjia.carplay.view.dialog;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.Constant;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class PhotoSelectDialog extends AlertDialog implements android.view.View.OnClickListener
{
    
    long animduring = 250;
    
    int direction = 1;
    
    private boolean isClosed = false;
    
    View backView;
    
    View containerV;
    
    Button cameraB;
    
    Button photoB;
    
    ImageView closeI;
    
    Activity context;
    
    String photoPath;
    
    File dir;
    
    String type;
    
    OnStateChangeListener onStateChangeListener;
    
    public OnStateChangeListener getOnStateChangeListener()
    {
        return onStateChangeListener;
    }
    
    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener)
    {
        this.onStateChangeListener = onStateChangeListener;
    }
    
    public PhotoSelectDialog(Context context)
    {
        super(context);
        this.context = (Activity)context;
        dir = new File(context.getExternalCacheDir(), "gongpingjia");
        dir.mkdirs();
    }
    
    public PhotoSelectDialog(Context context, String type)
    {
        super(context);
        this.context = (Activity)context;
        this.type = type;
        dir = new File(context.getExternalCacheDir(), "gongpingjia");
        dir.mkdirs();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photo);
        init();
    }
    
    public void init()
    {
        cameraB = (Button)findViewById(R.id.camera);
        cameraB.setOnClickListener(this);
        photoB = (Button)findViewById(R.id.photo);
        photoB.setOnClickListener(this);
        backView = findViewById(R.id.backview);
        backView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (onStateChangeListener != null)
                {
                    onStateChangeListener.close(photoPath);
                }
                dismiss();
            }
        });
    }
    
    public void playAnim()
    {
        View v = findViewById(R.id.container);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ani2 =
            ObjectAnimator.ofFloat(v,
                "translationY",
                direction * 200,
                -direction * 50,
                direction * 30,
                -direction * 10,
                0).setDuration(animduring * 2);
        ObjectAnimator ani1 = ObjectAnimator.ofFloat(v, "alpha", 0.2f, 1).setDuration(animduring);
        animatorSet.play(ani1).with(ani2);
        animatorSet.start();
    }
    
    @SuppressLint("NewApi")
    public void onAttachedToWindow()
    {
        isClosed = false;
        super.onAttachedToWindow();
        playAnim();
    }
    
    public void onCamera()
    {
        photoPath = new File(dir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
        if (onStateChangeListener != null)
        {
            onStateChangeListener.close(photoPath);
        }
        Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
        context.startActivityForResult(getImageByCamera, Constant.TAKE_PHOTO);
        dismiss();
    }
    
    public void onPhoto()
    {
        photoPath = new File(dir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
        if (onStateChangeListener != null)
        {
            onStateChangeListener.close(photoPath);
        }
        Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
        getImage.addCategory(Intent.CATEGORY_OPENABLE);
        getImage.setType("image/jpeg");
        context.startActivityForResult(getImage, Constant.PICK_PHOTO);
        dismiss();
    }
    
    public interface OnStateChangeListener
    {
        void close(String photoPath);
    }
    
    public void onClose(View v)
    {
        dismiss();
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.camera:
                onCamera();
                break;
            
            case R.id.photo:
                onPhoto();
                break;
            
            default:
                break;
        }
    }
    
}
