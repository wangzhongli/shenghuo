package com.e1858.monitor;

import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class IncomingCallMonitor extends BroadcastReceiver
{
	private static Vector<IncomingCallListener> incomingCallListeners = new Vector<IncomingCallListener>();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals("android.intent.action.PHONE_STATE"))
		{
			String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			for (IncomingCallListener incomingCallListener : incomingCallListeners)
			{
				if (null != incomingCallListener)
				{
					incomingCallListener.onIncomingCall(number);
				}
			}
		}
	}

	public static void addIncomingCallListener(IncomingCallListener incomingCallListener)
	{
		if (null != incomingCallListener)
		{
			incomingCallListeners.add(incomingCallListener);
		}
	}

	public static void removeIncomingCallListener(IncomingCallListener incomingCallListener)
	{
		if (null != incomingCallListener)
		{
			incomingCallListeners.remove(incomingCallListener);
		}
	}
}
