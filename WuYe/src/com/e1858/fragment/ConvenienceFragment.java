package com.e1858.fragment;

import java.util.ArrayList;
import java.util.List;

import com.e1858.utils.DataFileUtils;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.ConvenienceGridViewAdapter;
import com.e1858.wuye.protocol.http.ConvenientType;
import com.e1858.wuye.protocol.http.GetConvenientTypesResp;
import com.hg.android.widget.MyGridView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 便民Fragment
 * @author jiajia 2014年8月22日
 *
 */
public class ConvenienceFragment extends Fragment
{
	private MyGridView convenience_cmd_gridview;
	private static final String ARG_POSITION = "type";
	private byte type;
	private List<ConvenientType> lists = new ArrayList<ConvenientType>();
	private ConvenienceGridViewAdapter adapter = null;
	private String object_key;

	public static ConvenienceFragment newInstance(byte type)
	{
		ConvenienceFragment f = new ConvenienceFragment();
		Bundle b = new Bundle();
		b.putByte(ARG_POSITION, type);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		type = getArguments().getByte(ARG_POSITION);
		object_key = ConvenienceFragment.class.getSimpleName()+"_"+ type;
		adapter = new ConvenienceGridViewAdapter(MainApplication.getInstance(), getActivity(), lists);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.convenience_fragment, container, false);
		convenience_cmd_gridview = (MyGridView)view.findViewById(R.id.convenience_cmd_gridview);
		loadData(type, true);
		return view;
	}


	private void loadData(byte type, boolean isCache)
	{
		if (isCache)
		{
			GetConvenientTypesResp resp = (GetConvenientTypesResp) DataFileUtils.readObject(object_key);
			if (null != resp)
			{
				lists.addAll(resp.getTypes());
				initData(lists);
			}
		}
		List<ConvenientType> convenientTypes = MainApplication.getInstance().getConvenientTypes();
		GetConvenientTypesResp resp = new GetConvenientTypesResp();
		lists.clear();
		if(null!=convenientTypes&&convenientTypes.size()>0){
			for (int i = 0; i < convenientTypes.size(); i++)
			{
				if (convenientTypes.get(i).getCatalogueID() == type)
				{
					lists.add(convenientTypes.get(i));
				}
			}
			DataFileUtils.saveObject(resp, object_key);
			initData(lists);

		}
	}
	private void initData(List<ConvenientType> list)
	{
		adapter = new ConvenienceGridViewAdapter(MainApplication.getInstance(), getActivity(), lists);
		convenience_cmd_gridview.setAdapter(adapter);
	}
}
