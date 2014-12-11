package com.ducksboard.gridster;

public class LogUtils
{
	public static native void log( String message, Object obj )
	/*-{
		console.log(message, obj);
		//JSON.stringify(obj, null, 4)
	}-*/;
}