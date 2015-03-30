package com.e1858.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/**
 * 底图缩放，浮层不变
 *
 */
public class CropImageView extends View {
	
	//单点触摸的时候
	private float oldX=0;
	private float oldY=0;
	
	//多点触摸的时候
	private float oldx_0=0;
	private float oldy_0=0;
	
	private float oldx_1=0;
	private float oldy_1=0;
	//状态
	private final int STATUS_Touch_SINGLE=1;//单点
	private final int STATUS_TOUCH_MULTI_START=2;//多点开始
	private final int STATUS_TOUCH_MULTI_TOUCHING=3;//多点拖拽中
	
	private int mStatus=STATUS_Touch_SINGLE;
	
	//默认的裁剪图片宽度与高度
	private final int defaultCropWidth=312;
	private final int defaultCropHeight=360;
	private int cropWidth=defaultCropWidth;
	private int cropHeight=defaultCropHeight;
	
	protected float oriRationWH=0;//原始宽高比率
	protected final float maxZoomOut=5.0f;//最大扩大到多少倍
	protected final float minZoomIn=0.8f;//最小缩小到多少倍
	
	protected Drawable mDrawable;//原图
	protected FloatDrawable mFloatDrawable;//浮层
	protected Rect mDrawableSrc = new Rect();
	protected Rect mDrawableDst = new Rect();
	protected Rect mDrawableFloat = new Rect();//浮层选择框，就是头像选择框
	protected boolean isFrist=true;
	
	protected Context mContext;
    
	public CropImageView(Context context) {
		super(context);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
			
	}
	
	@SuppressLint("NewApi")
	private void init(Context context)
	{
		this.mContext=context;
		try {  
            if(android.os.Build.VERSION.SDK_INT>=11)  
            {  
                this.setLayerType(LAYER_TYPE_SOFTWARE, null);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		mFloatDrawable=new FloatDrawable(context);//头像选择框
	}

	public void setDrawable(Drawable mDrawable,int cropWidth,int cropHeight)
	{
		this.mDrawable=mDrawable;
		this.cropWidth=cropWidth;
		this.cropHeight=cropHeight;
		this.isFrist=true;
		invalidate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(event.getPointerCount()>1)
		{
			if(mStatus==STATUS_Touch_SINGLE)
			{
				mStatus=STATUS_TOUCH_MULTI_START;
				
				oldx_0=event.getX(0);
				oldy_0=event.getY(0);
				
				oldx_1=event.getX(1);
				oldy_1=event.getY(1);
			}
			else if(mStatus==STATUS_TOUCH_MULTI_START)
			{
				mStatus=STATUS_TOUCH_MULTI_TOUCHING;
			}
		}
		else
		{
			if(mStatus==STATUS_TOUCH_MULTI_START||mStatus==STATUS_TOUCH_MULTI_TOUCHING)
			{
				oldx_0=0;
				oldy_0=0;
				
				oldx_1=0;
				oldy_1=0;
				
				oldX=event.getX();
				oldY=event.getY();
			}
			
			mStatus=STATUS_Touch_SINGLE;
		}
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				oldX = event.getX();
				oldY = event.getY();
	            break;
	            
			case MotionEvent.ACTION_UP:
				checkBounds();
	            break;
			case MotionEvent.ACTION_MOVE:
				if(mStatus==STATUS_TOUCH_MULTI_TOUCHING)
				{
					float newx_0=event.getX(0);
					float newy_0=event.getY(0);
					
					float newx_1=event.getX(1);
					float newy_1=event.getY(1);
					
					float oldWidth=Math.abs(oldx_1-oldx_0);
					float oldHeight=Math.abs(oldy_1-oldy_0);
					
					float newWidth=Math.abs(newx_1-newx_0);
					float newHeight=Math.abs(newy_1-newy_0);
					
					boolean isDependHeight=Math.abs(newHeight-oldHeight)>Math.abs(newWidth-oldWidth);
					
			        float ration=isDependHeight?((float)newHeight/(float)oldHeight):((float)newWidth/(float)oldWidth);
			        int centerX=mDrawableDst.centerX();
			        int centerY=mDrawableDst.centerY();
			        int _newWidth=(int) (mDrawableDst.width()*ration);
			        int _newHeight=(int) ((float)_newWidth/oriRationWH);
			        
			        float tmpZoomRation=(float)_newWidth/(float)mDrawableSrc.width();
			        if(tmpZoomRation>=maxZoomOut)
			        {
			        	_newWidth=(int) (maxZoomOut*mDrawableSrc.width());
			        	_newHeight=(int) ((float)_newWidth/oriRationWH);
			        }
			        else if(tmpZoomRation<=minZoomIn)
			        {
			        	_newWidth=(int) (minZoomIn*mDrawableSrc.width());
			        	_newHeight=(int) ((float)_newWidth/oriRationWH);
			        }
			        
			        mDrawableDst.set(centerX-_newWidth/2, centerY-_newHeight/2, centerX+_newWidth/2, centerY+_newHeight/2);
			        invalidate();
			        
					Log.v("width():"+(mDrawableSrc.width())+"height():"+(mDrawableSrc.height()), "new width():"+(mDrawableDst.width())+"new height():"+(mDrawableDst.height()));
					Log.v(""+(float)mDrawableSrc.height()/(float)mDrawableSrc.width(), "mDrawableDst:"+(float)mDrawableDst.height()/(float)mDrawableDst.width());
					
					oldx_0=newx_0;
					oldy_0=newy_0;
					
					oldx_1=newx_1;
					oldy_1=newy_1;
				}
				else if(mStatus==STATUS_Touch_SINGLE)
				{
					int dx=(int)(event.getX()-oldX);
					int dy=(int)(event.getY()-oldY);
					
					oldX=event.getX();
					oldY=event.getY();
					
					if(!(dx==0&&dy==0))
					{
						mDrawableDst.offset((int)dx, (int)dy);
						invalidate();
					}
				}
	            break;
		}
		
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		if (mDrawable == null) {
            return; // couldn't resolve the URI
        }

        if (mDrawable.getIntrinsicWidth() == 0 || mDrawable.getIntrinsicHeight() == 0) {
            return;     // nothing to draw (empty bounds)
        }
        
        configureBounds();

		mDrawable.draw(canvas);
        canvas.save();
        canvas.clipRect(mDrawableFloat, Region.Op.DIFFERENCE);
        canvas.drawColor(Color.parseColor("#a0000000"));
        canvas.restore();
        mFloatDrawable.draw(canvas);
	}
	
	
	protected void configureBounds()
	{
		if(isFrist)
		{
			//获取资源文件图片的大小 通过计算得到  原始高宽比率
			oriRationWH=((float)mDrawable.getIntrinsicWidth())/((float)mDrawable.getIntrinsicHeight());
			
			final float scale = mContext.getResources().getDisplayMetrics().density;
			int w=Math.min(getWidth(), (int)(mDrawable.getIntrinsicWidth()*scale+0.5f));
			int h=(int) (w/oriRationWH);
			
			int left = (getWidth()-w)/2;
			int top = (getHeight()-h)/2;
			int right=left+w;
			int bottom=top+h;
			
			mDrawableSrc.set(left,top,right,bottom);
			mDrawableDst.set(mDrawableSrc);
			
			int floatWidth=dipTopx(mContext, cropWidth);
			int floatHeight=dipTopx(mContext, cropHeight);
			
			if(floatWidth>getWidth())
			{
				floatWidth=getWidth();
				floatHeight=cropHeight*floatWidth/cropWidth;
			}
			
			if(floatHeight>getHeight())
			{
				floatHeight=getHeight();
				floatWidth=cropWidth*floatHeight/cropHeight;
			}
			
			int floatLeft=(getWidth()-floatWidth)/2;
			int floatTop = (getHeight()-floatHeight)/2;
			mDrawableFloat.set(floatLeft, floatTop,floatLeft+floatWidth, floatTop+floatHeight);
	        isFrist=false;
		}
        
		mDrawable.setBounds(mDrawableDst);
		mFloatDrawable.setBounds(mDrawableFloat);
	}
	
	protected void checkBounds()
	{
		int newLeft = mDrawableDst.left;
		int newTop = mDrawableDst.top;
		
		boolean isChange=false;
		if(mDrawableDst.left<-mDrawableDst.width()+mDrawableFloat.right)
		{
			newLeft=-mDrawableDst.width()+mDrawableFloat.right;
			isChange=true;
		}
		
		if(mDrawableDst.top<-mDrawableDst.height()+mDrawableFloat.bottom)
		{
			newTop=-mDrawableDst.height()+mDrawableFloat.bottom;
			isChange=true;
		}
		
		if(mDrawableDst.left>0+mDrawableFloat.left)
		{
			newLeft=mDrawableFloat.left;
			isChange=true;
		}
		
		if(mDrawableDst.top>0+mDrawableFloat.top)
		{
			newTop=mDrawableFloat.top;
			isChange=true;
		}
		
		mDrawableDst.offsetTo(newLeft, newTop);
		if(isChange)
		{
			invalidate();
		}
	}
	public Bitmap getCropImage()
	{
		Bitmap tmpBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(tmpBitmap);
		mDrawable.draw(canvas);

		Matrix matrix=new Matrix();
		float scale=(float)(mDrawableSrc.width())/(float)(mDrawableDst.width());
		matrix.postScale(scale, scale);
		
	    Bitmap ret = Bitmap.createBitmap(tmpBitmap, mDrawableFloat.left, mDrawableFloat.top, mDrawableFloat.width(), mDrawableFloat.height(), matrix, true);
	    tmpBitmap.recycle();
	    tmpBitmap=null;
	    
	    Bitmap newRet=Bitmap.createScaledBitmap(ret, cropWidth, cropHeight, false);
    	ret.recycle();
    	ret=newRet;
		return ret;
	}
    public int dipTopx(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
       return (int) (dpValue * scale + 0.5f);  
    }
}
