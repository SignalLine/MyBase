
package com.rilin.lzy.mybase.util;

import java.io.ByteArrayOutputStream;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.view.View;

@SuppressWarnings("deprecation")
public class FzImage
{
	
	public static final float TRANSFORMROUND_CIRCLE = -1;
	public static final int NO_COLOR = -5;
	private static final int DEFAULT_BACKGROUNDCOLOR = Color.parseColor("#D8D8D8");
	
	private Bitmap bitmap = null;
	private byte[] bytes = null;
	
	public FzImage(Drawable drawable)
	{
		drawableToBitmap(drawable);
	}
	
	public FzImage(Resources res, int resId)
	{
		this(res.getDrawable(resId));
	}
	
	public FzImage(Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}
	
	public FzImage transformRound(float radius)
	{
		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();
		final int color = 0xff888888;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);
		if (radius == TRANSFORMROUND_CIRCLE)
		{
			radius = width > height ? width / 2 : height / 2;
		}
		Bitmap mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(mBitmap);
		paint.setAntiAlias(true);
		paint.setColor(color);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		this.bitmap = mBitmap;
		return this;
	}
	
	public Bitmap toBitmap()
	{
		return bitmap;
	}
	
	public Drawable toDrawable()
	{
		return bitmapToDrawable();
	}
	
	public byte[] toByteArray()
	{
		if (bytes == null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		}
		else
		{
			return bytes;
		}
	}
	
	private Drawable bitmapToDrawable()
	{
		return new BitmapDrawable(this.bitmap);
	}
	
	private void drawableToBitmap(Drawable drawable)
	{
		final int width = drawable.getIntrinsicWidth();
		final int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		this.bitmap = bitmap;
	}
	
	public FzImage addFilterToGray()
	{
		Drawable drawable = bitmapToDrawable();
		drawable.mutate();
		ColorMatrix matrix = new ColorMatrix();
		matrix.setSaturation(0);
		drawable.setColorFilter(new ColorMatrixColorFilter(matrix));
		drawableToBitmap(drawable);
		return this;
	}
	
	public FzImage compressOnlyQuality(int quality)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		bytes = baos.toByteArray();
		return this;
	}
	
	public FzImage compress(int quality)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > quality)
		{
			baos.reset();
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 10;
		}
		bytes = baos.toByteArray();
		return this;
	}
	
	public FzImage resize(int width, int height)
	{
		float scaleWidth = ((float) width) / bitmap.getWidth();
		float scaleHeight = ((float) height) / bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return this;
	}
	
	/**
	 * 生成图片
	 */
	public static ShapeDrawable createBackground(int color, float radius)
	{
		return createBackground(color, 255, radius);
	}
	
	/**
	 * 生成图片
	 */
	public static ShapeDrawable createBackground(int color, int alpha, float radius)
	{
		float[] outerR = new float[]
		{
				radius, radius, radius, radius, radius, radius, radius, radius
		};
		RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);
		ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
		shapeDrawable.getPaint().setColor(color);
		shapeDrawable.getPaint().setAlpha(alpha);
		shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
		
		// GradientDrawable gradientDrawable = new GradientDrawable();
		// gradientDrawable.setCornerRadii(outerR);
		// gradientDrawable.setColor(color);
		// gradientDrawable.setAlpha(alpha);
		
		return shapeDrawable;
	}
	
	/**
	 * 生成图片
	 */
	public static Drawable createBackgroundDrawable(int color, float radius)
	{
		return createBackgroundDrawable(color, 255, radius);
	}
	
	public static Drawable createBackgroundDrawable(int color, int alpha, float radius)
	{
		float[] outerR = new float[]
		{
				radius, radius, radius, radius, radius, radius, radius, radius
		};
		// RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);
		// ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
		// shapeDrawable.getPaint().setColor(color);
		// shapeDrawable.getPaint().setAlpha(alpha);
		// shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
		
		GradientDrawable gradientDrawable = new GradientDrawable();
		gradientDrawable.setCornerRadii(outerR);
		gradientDrawable.setColor(color);
		gradientDrawable.setAlpha(alpha);
		
		return gradientDrawable;
	}
	
	public static Drawable generateDrawable(int normalColor, int pressColor, int unableColor, float leftTopRadius,
			float leftBottomRadius, float rightTopRadius, float rightBottomRadius, float customRadius)
	{
		StateListDrawable stateDrawable = new StateListDrawable();
		
		GradientDrawable normalDrawble = new GradientDrawable();
		GradientDrawable pressDrawable = new GradientDrawable();
		GradientDrawable unableDrawble = new GradientDrawable();
		if (customRadius != -1)
		{
			pressDrawable.setCornerRadius(customRadius);
			normalDrawble.setCornerRadius(customRadius);
			unableDrawble.setCornerRadius(customRadius);
		}
		else
		{
			pressDrawable.setCornerRadii(getnerateRadius(leftTopRadius, leftBottomRadius, rightTopRadius,
					rightBottomRadius));
			normalDrawble.setCornerRadii(getnerateRadius(leftTopRadius, leftBottomRadius, rightTopRadius,
					rightBottomRadius));
			unableDrawble.setCornerRadii(getnerateRadius(leftTopRadius, leftBottomRadius, rightTopRadius,
					rightBottomRadius));
		}
		
		normalDrawble.setColor(normalColor);
		
		if (NO_COLOR == pressColor)
		{
			pressDrawable.setColor(normalColor);
			
			
//			pressDrawable = normalDrawble;
//			pressDrawable.setColorFilter(Color.parseColor("#33000000"), PorterDuff.Mode.SRC_OVER);
//			pressDrawable.setColorFilter(Color.parseColor("#33000000"), PorterDuff.Mode.MULTIPLY);
			pressDrawable.setColorFilter(Color.RED, Mode.SRC_OVER);
			
			pressDrawable.setColorFilter(new LightingColorFilter(0xFFFFFFFF,0xFFAA0000));
		}
		else
		{
			pressDrawable.setColor(pressColor);
		}
		if (NO_COLOR == unableColor)
		{
			unableDrawble.setColor(DEFAULT_BACKGROUNDCOLOR);
		}
		else
		{
			unableDrawble.setColor(unableColor);
		}
		stateDrawable.addState(new int[]
		{
				android.R.attr.state_pressed
		}, pressDrawable);
		stateDrawable.addState(new int[]
		{
				android.R.attr.state_focused
		}, pressDrawable);
		stateDrawable.addState(new int[]
		{
			-android.R.attr.state_enabled
		}, unableDrawble);
		stateDrawable.addState(new int[] {}, normalDrawble);
		
		return stateDrawable;
	}
	
	private static float[] getnerateRadius(float leftTop, float leftBottom, float rightTop, float rightBottom)
	{
		return new float[]
		{
				leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom
		};
	}
	
	/**
	 * 设置视图的背景图片
	 * 
	 * @param view
	 * @param drawable
	 */
	@SuppressLint("NewApi")
	public static void setDrawble(View view, Drawable drawable)
	{
		if (Build.VERSION.SDK_INT >= 16)
		{
			view.setBackground(drawable);
		}
		else
		{
			view.setBackgroundDrawable(drawable);
		}
	}
	
}
