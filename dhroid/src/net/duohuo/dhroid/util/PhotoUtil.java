package net.duohuo.dhroid.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.Window;

import net.duohuo.dhroid.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/***
 * 头像上传工具类 调用 getPhoto 在onactivityResult 调用
 * <p/>
 * onPhotoFromCamera
 * <p/>
 * onPhotoFromPick
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class PhotoUtil {

    /**
     * 因为处理不同
     *
     * @param takePhotoCode Uri originalUri = data.getData();
     *                      image=ImageUtil.getBitmapFromUrl(originalUri.toString());
     *                      *********************************************************************************
     * @param imageCode     Bundle extras = data.getExtras(); image = (Bitmap)
     *                      extras.get("data");
     * @param tempFile      拍照时的临时文件 需要zoom时
     **/
    public static boolean getPhoto(final Activity activity,
                                   final int takePhotoCode, final int imageCode, final File tempFile) {
        final CharSequence[] items = {"相册", "拍照"};
        AlertDialog dlg = new AlertDialog.Builder(activity).setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 1) {
                            Intent getImageByCamera = new Intent(
                                    "android.media.action.IMAGE_CAPTURE");
                            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(tempFile));
                            activity.startActivityForResult(getImageByCamera,
                                    takePhotoCode);
                        } else {
                            Intent getImage = new Intent(
                                    Intent.ACTION_GET_CONTENT);
                            getImage.addCategory(Intent.CATEGORY_OPENABLE);
                            getImage.setType("image/jpeg");
                            activity.startActivityForResult(getImage, imageCode);
                        }
                    }
                }).create();
        Window window = dlg.getWindow();
        window.setWindowAnimations(R.style.mystyle);
        dlg.show();
        return true;
    }

    /**
     * 拍照获取图片的方式
     *
     * @param context
     * @param zoomCode
     * @param temppath 拍照前生成的临时路劲
     * @return 新的路劲
     */
    public static String onPhotoFromCamera(Activity context, int zoomCode,
                                           String temppath, int aspectX, int aspectY, int outx) {
        Bitmap btp = getLocalImage(new File(temppath));
        saveLocalImage(btp, new File(temppath + "temp"));
        photoZoom(context, Uri.fromFile(new File(temppath + "temp")),
                Uri.fromFile(new File(temppath)), zoomCode, aspectX, aspectY,
                outx);
        return temppath + "temp";
    }

    public static String onPhotoFromCamera(Activity context, int zoomCode,
                                           String temppath, int aspectX, int aspectY, int outx, String newPath) {
        String path;
        Bitmap btp = getLocalImage(new File(temppath));
        int degree = getBitmapDegree(temppath);
        if (degree == 0) {
            saveLocalImage(btp, new File(temppath));
            path = temppath;
        } else {
            Bitmap btpnew = rotateBitmapByDegree(btp, degree);
            saveLocalImage(btpnew, new File(newPath));
            path = newPath;
        }
        btp.recycle();
        photoZoom(context, Uri.fromFile(new File(path)),
                Uri.fromFile(new File(path)), zoomCode, aspectX, aspectY, outx);
        return path;
    }

    /**
     * 相册获取图片
     *
     * @param context
     * @param zoomCode
     * @param temppath 希望生成的路劲
     * @param data
     */
    public static void onPhotoFromPick(Activity context, int zoomCode,
                                       String temppath, Intent data, int aspectX, int aspectY, int outx) {
        Bitmap btp = checkImage(context, data);
        saveLocalImage(btp, new File(temppath));
        PhotoUtil.photoZoom(context, Uri.fromFile(new File(temppath)),
                Uri.fromFile(new File(temppath)), zoomCode, aspectX, aspectY,
                outx);
    }

    public static void onPhotoFromPick(Activity context, int zoomCode,
                                       String temppath,  Bitmap btp, int aspectX, int aspectY, int outx) {
        saveLocalImage(btp, new File(temppath));
        PhotoUtil.photoZoom(context, Uri.fromFile(new File(temppath)),
                Uri.fromFile(new File(temppath)), zoomCode, aspectX, aspectY,
                outx);
    }

    public static String onPhotoFromPick(Activity context, int zoomCode,
                                         String temppath, Intent data, int aspectX, int aspectY, int outx,
                                         String newPath) {
        String path;
        Bitmap btp = checkImage(context, data);
        int degree = getBitmapDegree(temppath);
        if (degree == 0) {
            saveLocalImage(btp, new File(temppath));
            path = temppath;
        } else {
            Bitmap btpnew = rotateBitmapByDegree(btp, degree);
            saveLocalImage(btpnew, new File(newPath));
            path = newPath;
        }
        btp.recycle();

        PhotoUtil.photoZoom(context, Uri.fromFile(new File(path)),
                Uri.fromFile(new File(path)), zoomCode, aspectX, aspectY, outx);

        return path;
    }

    /**
     * data 中检出图片
     *
     * @param activity
     * @param data
     * @return
     */
    public static Bitmap checkImage(Activity activity, Intent data) {
        Bitmap bitmap = null;
        try {
            Uri originalUri = data.getData();
            String path = getPath(activity, originalUri);
            File f = activity.getExternalCacheDir();
            String pp = f.getAbsolutePath();
            if (path.indexOf(pp) != -1) {
                path = path.substring(path.indexOf(pp), path.length());
            }
            bitmap = getLocalImage(new File(path));
        } catch (Exception e) {
        } finally {
            return bitmap;
        }
    }

    /**
     * 通过URI 获取真实路劲
     *
     * @param activity
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Activity activity, Uri contentUri) {
        Cursor cursor = null;
        String result = contentUri.toString();
        String[] proj = {MediaColumns.DATA};
        cursor = activity.managedQuery(contentUri, proj, null, null, null);
        if (cursor == null)
            throw new NullPointerException("reader file field");
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                cursor.close();
            }
        }
        return result;
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * 保存临时图片到本地
     *
     * @param bm
     * @param f
     */
    public static void saveLocalImage(Bitmap bm, File f) {
        if (bm == null)
            return;
        File file = f;
        try {
            file.createNewFile();

            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // // bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);//
            // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            // int options = 100;
            // while (baos.toByteArray().length / 1024 > 100)
            // { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            // baos.reset();// 重置baos即清空baos
            // bm.compress(Bitmap.CompressFormat.JPEG, options, baos);//
            // 这里压缩options%，把压缩后的数据存放到baos中
            // options -= 10;// 每次都减少10
            // }
            // baos.reset();
            // baos.flush();
            // baos.close();

            OutputStream outStream = new FileOutputStream(file);
            compressImage(bm, outStream);
            outStream.flush();
            outStream.close();
            bm.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //保存照片为正方形
    public static void saveLocalImageSquare(Bitmap bm, File f) {
        if (bm == null)
            return;
        File file = f;
        try {
            file.createNewFile();

            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // // bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);//
            // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            // int options = 100;
            // while (baos.toByteArray().length / 1024 > 100)
            // { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            // baos.reset();// 重置baos即清空baos
            // bm.compress(Bitmap.CompressFormat.JPEG, options, baos);//
            // 这里压缩options%，把压缩后的数据存放到baos中
            // options -= 10;// 每次都减少10
            // }
            // baos.reset();
            // baos.flush();
            // baos.close();
            Bitmap squareBitmap = ImageCrop(bm);

            OutputStream outStream = new FileOutputStream(file);
            compressImage(squareBitmap, outStream);
            outStream.flush();
            outStream.close();
            bm.recycle();
            squareBitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //将照片存到本地相册(保存的图片为正方形)
    public static String saveLocalImage(Bitmap bm, int degree, Context mContext) {
        if (bm == null)
            return null;
        try {

            File appDir = new File(Environment
                    .getExternalStorageDirectory(), "carplay");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);

            if (degree != 0) {
                bm = rotateBitmapByDegree(bm, degree);
            }
            Bitmap squareBitmap = ImageCrop(bm);
            OutputStream outStream = new FileOutputStream(file);
            compressImage(squareBitmap, outStream);
            outStream.flush();
            outStream.close();
            bm.recycle();
            squareBitmap.recycle();
            Intent intent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            mContext.sendBroadcast(intent);

            return file.getPath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //拍照存储照片
    public static void saveLocalImage(Bitmap bm, File f, int degree) {
        if (bm == null)
            return;
        File file = f;
        try {
//
//            File appDir = new File(Environment
//                    .getExternalStorageDirectory(), "carplay");
//            if (!appDir.exists()) {
//                appDir.mkdir();
//            }
//            String fileName = System.currentTimeMillis() + ".jpg";
//            File file1 = new File(appDir, fileName);


            file.createNewFile();


            if (degree != 0) {
                bm = rotateBitmapByDegree(bm, degree);
            }

            OutputStream outStream = new FileOutputStream(file);
            compressImage(bm, outStream);
            outStream.flush();
            outStream.close();
            bm.recycle();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //拍照保存正方形图片
    public static void saveLocalImageSquare(Bitmap bm, File f, int degree) {
        if (bm == null)
            return;
        File file = f;
        try {
//
//            File appDir = new File(Environment
//                    .getExternalStorageDirectory(), "carplay");
//            if (!appDir.exists()) {
//                appDir.mkdir();
//            }
//            String fileName = System.currentTimeMillis() + ".jpg";
//            File file1 = new File(appDir, fileName);


            file.createNewFile();


            if (degree != 0) {
                bm = rotateBitmapByDegree(bm, degree);
            }
            Bitmap squareBitmap = ImageCrop(bm);
            OutputStream outStream = new FileOutputStream(file);
            compressImage(squareBitmap, outStream);
            outStream.flush();
            outStream.close();
            bm.recycle();
            squareBitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compressImage(Bitmap image, OutputStream outStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        baos.reset();
        image.compress(Bitmap.CompressFormat.JPEG, options, outStream);

        //
        // ByteArrayInputStream isBm = new
        // ByteArrayInputStream(baos.toByteArray());//
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        //
        // // 把ByteArrayInputStream数据生成图片
        // return new SoftReference<Bitmap>(BitmapFactory.decodeStream(isBm,
        // null, null)).get();
    }

    /**
     * 由本地获取图片
     *
     * @param f
     * @return
     */
    public static Bitmap getLocalImage(File f) {
        File file = f;
        if (file.exists()) {
            try {
                file.setLastModified(System.currentTimeMillis());
                FileInputStream in = new FileInputStream(file);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(in, null, options);
                int sWidth = 500;
                int sHeight = 500;
                int mWidth = options.outWidth;
                int mHeight = options.outHeight;
                int s = 1;
                while ((mWidth / s > sWidth * 2) || (mHeight / s > sHeight * 2)) {
                    s *= 2;
                }
                options = new BitmapFactory.Options();
                options.inSampleSize = s;
                in.close();
                // 再次获取
                in = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                in.close();
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * aspectY Y对于X的比例 outputX X 的宽
     **/
    public static void photoZoom(Activity activity, Uri uri, Uri outUri,
                                 int photoResoultCode, int aspectX, int aspectY, int outputX) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        if (aspectY > 0) {
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
        }
        // outputX outputY 是裁剪图片宽高
        // intent.putExtra("outputX", outputX);
        // if (aspectY > 0) {
        // intent.putExtra("outputY", (int)(aspectY/(float)aspectX) * outputX);
        // }
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // 加入人脸识别
        activity.startActivityForResult(intent, photoResoultCode);
    }

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    public static Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

}
