package com.ustclin.petchicken.bean;

public class ChatMessage
{
	/**
	 * 由机器人回答的消息：in
	 */
	public final static String MESSAGE_IN = "in";
	/**
	 * 由用户发出的消息：out
	 */
	public final static String MESSAGE_OUT = "out";
	
	private int id;
	/**
	 * 消息类型 : 
	 * in : 表示由机器人发来的消息，进来
	 * out ：表示由用户发出去的消息，出去
	 */
	private String type ;
	/**
	 * 消息内容
	 */
	private String msg;
	/**
	 * 日期
	 */
	private String date;
	/**
	 * 发送人
	 */
	private String name;

	//构造函数1
	public ChatMessage(int id ,String chater , String date , String content)
	{
		this.id = id;
		this.type = chater;
		setDate(date);
		this.msg = content;
	}
	//构造函数2
	public ChatMessage(String chater , String date , String content)
	{
		this.type = chater;
		setDate(date);
		this.msg = content;
	}


	public ChatMessage() {
		// TODO Auto-generated constructor stub
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
