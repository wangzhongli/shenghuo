package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public abstract class Packet implements Serializable
{
	@Expose
	protected int	command;//命令字

	public int getCommand() {
		return this.command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

}
