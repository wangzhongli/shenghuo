package com.e1858.monitor;

import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
public class IncomingSmsMonitor extends BroadcastReceiver
{
	private static Vector<IncomingSmsListener>	incomingSmsListeners	= new Vector<IncomingSmsListener>();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			String number = null;// 短信发件人
			StringBuilder messageBody = new StringBuilder();// 短信内容
			Bundle bundle = intent.getExtras();
			boolean handledFlag = false;
			if (bundle != null)
			{
				Object[] _pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] _message = new SmsMessage[_pdus.length];
				for (int i = 0; i < _pdus.length; i++)
				{
					_message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
				}
				for (SmsMessage currentMessage : _message)
				{
					messageBody.append(currentMessage.getDisplayMessageBody());
					if (null == number)
					{
						number = currentMessage.getDisplayOriginatingAddress();
					}
				}
				String message = messageBody.toString();
				if (number.contains("+86"))
				{
					number = number.substring(3);
				}
				for (IncomingSmsListener incomingSmsListener : incomingSmsListeners)
				{
					if (null != incomingSmsListener)
					{
						if (incomingSmsListener.onIncomingSms(number, message))
						{
							handledFlag = true;
						}
					}
				}
				if (handledFlag)
				{
					abortBroadcast();
				}
			}
		}
	}

	public static void addIncomingSmsListener(IncomingSmsListener incomingSmsListener)
	{
		if (null != incomingSmsListener)
		{
			incomingSmsListeners.add(incomingSmsListener);
		}
	}

	public static void removeIncomingSmsListener(IncomingSmsListener incomingSmsListener)
	{
		if (null != incomingSmsListener)
		{
			incomingSmsListeners.remove(incomingSmsListener);
		}
	}

}
