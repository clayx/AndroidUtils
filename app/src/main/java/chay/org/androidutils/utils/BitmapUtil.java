package chay.org.androidutils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 图形/图像 操作，读取,缩放，变换
 * </p>
 */
public class BitmapUtil {

    //默认压缩最大的图片大小
    public static int IMAGE_MAX_DEFAULT_SIZE = 512;

    public static String getSDPath() {
        File sdDir = null;
        if (hasSdcard()) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }

    public static boolean hasSdcard() {
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        return sdCardExist;
    }

    /**
     * 读取uri所在的图片
     *
     * @param uri      图片对应的Uri
     * @param mContext 上下文对象
     * @return 获取图像的Bitmap
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据存储路径获取Bitmap
     */
    public static Bitmap getBitmapFromPath(String srcImg) {
        return BitmapFactory.decodeFile(srcImg);
    }

    /**
     * 压缩图片（质量限制）
     *
     * @param srcImg
     * @param quality
     * @return
     */
    public static Bitmap compressImage4Quality(String srcImg, int quality) {
        return compressImage(srcImg, 0, quality);
    }

    /**
     * 压缩图片（大小限制）
     *
     * @param srcImg
     * @param bitmapSize
     * @return
     */
    public static Bitmap compressImage4MaxSize(String srcImg, int bitmapSize) {
        return compressImage(srcImg, bitmapSize, 0);
    }

    /**
     * 压缩图片，通过标准采样率，对图片进行压缩，对图片大小（or质量进行限制）
     *
     * @param srcImg     图片路径
     * @param bitmapSize 压缩大小
     * @param quality    质量（PS：当quality > 0 时，根据quality进行压缩，否则根据bitmapSize进行压缩）
     * @return
     */
    private static Bitmap compressImage(String srcImg, int bitmapSize, int quality) {
        if (srcImg == null || srcImg.length() == 0) {
            return null;
        }
        Bitmap tagBitmap = BitmapFactory.decodeFile(srcImg, getStandardOpt(srcImg));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        tagBitmap = rotatingImage(tagBitmap, readPictureDegree(srcImg));

        if (quality <= 0) {
            int options = 100;
            tagBitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);
            while (stream.toByteArray().length / 1024 > bitmapSize) {
                options -= 5;
                stream.reset();
                tagBitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);
            }
        } else {
            tagBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        }

        ByteArrayInputStream ins = new ByteArrayInputStream(stream.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(ins, null, null);

        try {
            ins.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    /**
     * 图片质量压缩,压缩后自动保存到指定的路径
     *
     * @param srcImg
     * @param saveFile
     * @return
     */
    public static File compressImage(String srcImg, File saveFile) {
        if (srcImg == null || saveFile == null) {
            return null;
        }

        Bitmap tagBitmap = BitmapFactory.decodeFile(srcImg, getStandardOpt(srcImg));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        tagBitmap = rotatingImage(tagBitmap, readPictureDegree(srcImg));
        tagBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        tagBitmap.recycle();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFile);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveFile;
    }

    public static BitmapFactory.Options getStandardOpt(String srcImg) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(srcImg, options);
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        options.inSampleSize = computeSize(srcWidth, srcHeight);
        options.inJustDecodeBounds = false;
        return options;
    }

    /**
     * 根据图片的尺寸 比例计算 采样率 压缩
     *
     * @param srcWidth
     * @param srcHeight
     * @return
     */
    private static int computeSize(final int srcWidth, final int srcHeight) {
        int resizesrcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        int resizesrcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(resizesrcWidth, resizesrcHeight);
        int shortSide = Math.min(resizesrcWidth, resizesrcHeight);

        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide >= 1664 && longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280 == 0 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

    /**
     * 对imageview gc
     *
     * @param imageView
     */
    public static void gcImageView(ImageView imageView) {
        if (imageView != null && imageView.getDrawable() != null) {
            if (imageView.getDrawable() instanceof BitmapDrawable) {
                Bitmap oldBitmap = ((BitmapDrawable) imageView.getDrawable())
                        .getBitmap();

                imageView.setImageDrawable(null);

                if (oldBitmap != null && oldBitmap.isRecycled()) {

                    oldBitmap.recycle();

                    oldBitmap = null;

                }
            }


        }
    }

    // 缩放图片
    public static Bitmap zoomImage(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap dest = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        if (dest != bm) {
            bm.recycle();
        }
        return dest;

    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            double scale = heightRatio < widthRatio ? heightRatio : widthRatio;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            inSampleSize = (int) Math.pow(2, logCeil);
        }

        return inSampleSize;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
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

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param angle
     * @return
     */
    public static Bitmap rotatingImage(Bitmap bitmap, float angle) {

        Matrix matrix = new Matrix();

        matrix.postRotate(angle);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
