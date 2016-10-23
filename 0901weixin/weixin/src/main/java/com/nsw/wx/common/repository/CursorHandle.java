package com.nsw.wx.common.repository;

import com.mongodb.Cursor;

public interface CursorHandle {
	  public void handle(Cursor cursor);
}
