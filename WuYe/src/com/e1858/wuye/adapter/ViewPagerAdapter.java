package com.e1858.wuye.adapter;

import java.util.LinkedList;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.e1858.wuye.R;
import com.three.widget.RecyclingPagerAdapter;

public class ViewPagerAdapter extends RecyclingPagerAdapter
{

	private Context context;
	private LinkedList<Bitmap> list;
	private boolean isInfiniteLoop;
	private int mChildCount = 0;
	public ViewPagerAdapter(Context context, LinkedList<Bitmap> list)
	{
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return isInfiniteLoop ? Integer.MAX_VALUE : list.size();
	}
    @Override
    public void notifyDataSetChanged() {         
          mChildCount = getCount();
          super.notifyDataSetChanged();
    }
    @Override
    public int getItemPosition(Object object)   {          
          if ( mChildCount > 0) {
          mChildCount --;
          return POSITION_NONE;
          }
          return super.getItemPosition(object);
    }
    
	private int getPosition(int position)
	{
		return isInfiniteLoop ? position % (list.size()) : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.view_pager_item, null);
			holder.imageView=(ImageView)convertView.findViewById(R.id.guide_image);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.imageView.setImageBitmap(list.get(getPosition(position)));
	
	    return convertView;
	}

	private static class ViewHolder
	{

		private ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop()
	{
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ViewPagerAdapter setInfiniteLoop(boolean isInfiniteLoop)
	{
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}


}
