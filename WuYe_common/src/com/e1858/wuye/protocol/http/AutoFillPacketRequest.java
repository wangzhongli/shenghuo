package com.e1858.wuye.protocol.http;

import com.e1858.common.Constant;
import com.e1858.common.app.BaseApplication;
import com.e1858.utils.Encrypt;
import com.e1858.utils.PreferencesUtils;

@SuppressWarnings("serial")
public abstract class AutoFillPacketRequest extends PacketRequest {

	public AutoFillPacketRequest(int command) {
		super();
		this.command = command;

		if (BaseApplication.getBaseInstance() == null) {
			return;
		}
		this.setKey(PreferencesUtils.getLoginKey());
		this.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
		if (PreferencesUtils.getCommunity() != null)
			this.setCommunityID(PreferencesUtils.getCommunity().getID());
	}
}
