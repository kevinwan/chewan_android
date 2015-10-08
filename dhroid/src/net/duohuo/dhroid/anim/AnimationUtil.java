package net.duohuo.dhroid.anim;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
/**
 * 
 * 一些快速简单的动画效果
 * @author duohuo-jinghao
 *
 */
public class AnimationUtil {
	
	
	/**
	 * 点击时放大
	 * @param v
	 * @param nomBg
	 * @param focusBg
	 */
	public static void touchToBig(View v,final int nomBg,final int focusBg){
		v.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					v.setBackgroundResource(focusBg);
					ScaleAnimation scale =new ScaleAnimation(1.0f, 1.11f, 1.0f, 1.11f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
					scale.setFillAfter(true);
					v.startAnimation(scale);
				}
					break;
				case MotionEvent.ACTION_UP:{
					v.setBackgroundResource(nomBg);
					ScaleAnimation scale =new ScaleAnimation(1.11f, 1.0f, 1.11f, 1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
					scale.setFillAfter(true);
					v.startAnimation(scale);
				}	
				default:
					break;
				}
				return false;
			}
		});
	}
	
	/**
	 * 下拉出现动画
	 * @param v
	 * @param during
	 */
	public static void showSlideDown(View v,Long during){
		v.setVisibility(View.VISIBLE);
		AnimationSet as=new AnimationSet(true);
		ScaleAnimation scale =new ScaleAnimation(1f, 1f, 0f, 1f); 
		scale.setInterpolator(v.getContext(),android.R.anim.decelerate_interpolator);
		scale.setDuration(during);
		as.addAnimation(scale);
		AlphaAnimation al=new AlphaAnimation(20, 100);
		al.setDuration(during);
		as.addAnimation(al);
		v.startAnimation(as);
	}
	/**
	 * 上推出现动画
	 * @param v
	 * @param during
	 */
	public static void showSlideUp(View v,Long during){
		v.setVisibility(View.VISIBLE);
		AnimationSet as=new AnimationSet(true);
		ScaleAnimation scale =new ScaleAnimation(1f, 1f, 0f, 1f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f); 
		scale.setInterpolator(v.getContext(),android.R.anim.decelerate_interpolator);
		scale.setDuration(during);
		as.addAnimation(scale);
		AlphaAnimation al=new AlphaAnimation(20, 100);
		al.setDuration(during);
		as.addAnimation(al);
		v.startAnimation(as);
	}
	
	/**
	 * 放大出现动画
	 * @param v
	 * @param during
	 */
	public static void showCenter(View v,Long during){
		v.setVisibility(View.VISIBLE);
		v.setVisibility(View.VISIBLE);
		AnimationSet as=new AnimationSet(true);
		ScaleAnimation scale =new ScaleAnimation(0f, 1f, 0f, 1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
		scale.setInterpolator(v.getContext(),android.R.anim.decelerate_interpolator);
		scale.setDuration(during);
		as.addAnimation(scale);
		AlphaAnimation al=new AlphaAnimation(20, 100);
		al.setDuration(during);
		as.addAnimation(al);
		v.startAnimation(as);
	}
	
	
	
	
	/**
	 * 下拉消失动画
	 * @param v
	 * @param during
	 */
	public static void hideSlideUp(View v,Long during){
		AnimationSet as=new AnimationSet(true);
		ScaleAnimation scale =new ScaleAnimation(1f, 1f, 1f, 0f); 
		scale.setInterpolator(v.getContext(),android.R.anim.anticipate_interpolator);
		scale.setDuration(during);
		as.addAnimation(scale);
		AlphaAnimation al=new AlphaAnimation(80, 0);
		al.setDuration(during);
		as.addAnimation(al);
		v.startAnimation(as);
		v.setVisibility(View.GONE);
	}
	
	/**
	 * 下拉消失
	 * @param v
	 * @param during
	 */
	public static void hideSlideDown(View v,Long during){
		AnimationSet as=new AnimationSet(true);
		ScaleAnimation scale =new ScaleAnimation(1f, 1f, 1f, 0f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);  
		scale.setInterpolator(v.getContext(),android.R.anim.anticipate_interpolator);
		scale.setDuration(during);
		as.addAnimation(scale);
		AlphaAnimation al=new AlphaAnimation(80, 0);
		al.setDuration(during);
		as.addAnimation(al);
		v.startAnimation(as);
		v.setVisibility(View.GONE);
	}
	
	
	
	/**
	 * 缩小消失
	 * @param v
	 * @param during
	 */
	public static void hideCenter(View v,Long during){
		AnimationSet as=new AnimationSet(true);
		ScaleAnimation scale =new ScaleAnimation(1f, 0f, 1f, 0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
		scale.setInterpolator(v.getContext(),android.R.anim.anticipate_interpolator);
		scale.setDuration(during);
		as.addAnimation(scale);
		AlphaAnimation al=new AlphaAnimation(80, 0);
		al.setDuration(during);
		as.addAnimation(al);
		v.startAnimation(as);
		v.setVisibility(View.GONE);
	}
	
	
	
	
	
}
