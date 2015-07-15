package net.duohuo.dhroid.image;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;

import net.duohuo.dhroid.net.HttpManager;
import org.apache.http.client.methods.HttpGet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * @description 本类可实现图片的 本地加载 或 异步网络加载 内存缓存  本地缓存 
 * @author 
 * @deprecated 不再使用等待剔除 使用com.nostra13.universalimageloader.core.ImageLoader替换
 * @company www.duohuo.net
 * ****************************************************
 ***/
@Deprecated
public class ImageLoad {
//		内存软引用缓存 
		static HashMap<String, SoftReference<Bitmap>>  imageCache = new HashMap<String, SoftReference<Bitmap>>();;
		
		/**
		 * 获取图片 参照 load(....);
		 * @param url
		 * @param imageView
		 * @param imageCallback
		 */
		public static void load(final String url,final ImageView imageView, final ImageCallback imageCallback){
			load(url, 150, 150, imageView, imageCallback);
		}
		
		/**
		 * 获取图片并自动覆盖imageView
		 * @param url
		 * @param imageView
		 */
		public static void load(final String url,final ImageView imageView){
			load(url, 150, 150, imageView, null);
		}
		/**
		 * 
		 * @param url
		 * @param width
		 * @param height
		 * @param imageView
		 */
		public static void load(final String url,final int width ,final int height,final ImageView imageView){
			load(url, width, height,imageView,null);
		}
		
		/***
		 * 通过各种途径获取图片 当图片在app中时 已 loal:开头 <br/>
		 * 当 imageCallback 为空时 imageView会自动设置获取的图片 <br/>
		 * 不为空时 不设置 调用imageCallback.callback<br/>		 
		 * @param key
		 * @param url
		 * @param imageView
		 * @param imageCallbac
		 */
		public static Bitmap load(final String url,final int width ,final int height,final ImageView imageView, final ImageCallback imageCallback){
			Bitmap bm = null;
			//空链接
			if(TextUtils.isEmpty(url)||!url.startsWith("http://")) return null; 
			final String key= url+"@"+width+"*"+height;
			
//			1内存缓存中获取
	    	if (imageCache.containsKey( key)) {
	            //从内存缓存中获取
	            SoftReference<Bitmap> softReference = imageCache.get( key);
	            bm = softReference.get();	       
	    	}	
	    	//本地缓存中获取
	    	if(bm==null){
	    		ImageCacheManager lsd=ImageCacheManager.getInstanse();
	    		bm=lsd.getLocalImage(key,width,height);
	    	}
	    	if(bm!=null){
//	    		将图片放入软引用
	    		if(!imageCache.containsKey(key)||imageCache.get(key).get()==null){
	    			imageCache.put(key, new SoftReference<Bitmap>(bm));
	    		}
	    		
	    		if(imageCallback!=null){
            		imageCallback.callback(bm, imageView);
            	}else{
            		imageView.setImageBitmap(bm);
            	}
	    		return bm;
	    	}
	    
	    	else{
				 final Handler  handler = new Handler() {
						public void handleMessage(Message message) {
			            	if(imageCallback!=null){
			            		imageCallback.callback((Bitmap) message.obj, imageView);
			            	}else{
			            		imageView.setImageBitmap((Bitmap) message.obj);
			            	}
			            }
			        };
			        //建立新一个新的线程下载图片
			        new Thread(new Runnable()  {
			            public void run() {
			            	Bitmap	bmm = null;
			            	bmm=getBitmap(url,width,height);
			            	if(bmm!=null){
			            		ImageCacheManager lsd=ImageCacheManager.getInstanse();
				        		lsd.saveLocalImage(bmm, key,width,height);
				        		imageCache.put(key, new SoftReference<Bitmap>(bmm));
			            	}
			            	   Message message = handler.obtainMessage(0, bmm);
				                handler.sendMessage(message);
			            }
			        }).start();
			}
	    	return null;
		}
		
		
		
		
		
	/**
	 * 图片回调接口
	 * @author Administrator
	 *
	 */
    public interface ImageCallback {
    	public void callback(Bitmap bm,ImageView imageView);
    }
    
	 /**
     * 获取assert中的图片
     * @param url url
     * @return
     */
	public static Bitmap getLocalImage(Context context,String url) { 
		try {
			return	BitmapFactory.decodeStream(context.getAssets().open(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 由同步径获取图片
	 * @param url
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmap(String url,int width,int height){
		HttpURLConnection conn;
		try {
			HttpGet get=new HttpGet(url);
			InputStream in=HttpManager.execute(get).getEntity().getContent();
			BitmapFactory.Options options = new BitmapFactory.Options();    
	        options.inJustDecodeBounds = true;    
	        BitmapFactory.decodeStream(in, null, options); 
	        int sWidth  = width;    
	        int sHeight = height;  
	        int mWidth = options.outWidth;    
	        int mHeight = options.outHeight;
	        int s = 1;    
	        while ((mWidth / s > sWidth * 2) || (mHeight / s > sHeight * 2))    
	        {    
	            s *= 2;    
	        }    
	        options = new BitmapFactory.Options();    
	        options.inSampleSize = s;   
	        in.close();
	        //再次获取
			in=HttpManager.execute(get).getEntity().getContent();
			Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);    
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}     
		return null;
	}

  }




