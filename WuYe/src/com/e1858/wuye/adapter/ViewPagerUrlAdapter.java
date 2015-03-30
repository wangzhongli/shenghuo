package com.e1858.wuye.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.e1858.wuye.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.three.widget.RecyclingPagerAdapter;

public class ViewPagerUrlAdapter extends RecyclingPagerAdapter
{


	private Context context;
	private List<String> list;
	private boolean isInfiniteLoop;
	private DisplayImageOptions options;
	public ViewPagerUrlAdapter(Context context, List<String> list)
	{
		this.context = context;
		this.list = list;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.color.white)
		.showImageForEmptyUri(R.color.white)
		.showImageOnFail(R.color.white)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return isInfiniteLoop ? Integer.MAX_VALUE : list.size();
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
		ImageLoader.getInstance().displayImage(list.get(getPosition(position)), holder.imageView, options);
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
	public ViewPagerUrlAdapter setInfiniteLoop(boolean isInfiniteLoop)
	{
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}



}
