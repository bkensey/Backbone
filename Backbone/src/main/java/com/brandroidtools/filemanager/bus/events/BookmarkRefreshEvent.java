package com.brandroidtools.filemanager.bus.events;

public class BookmarkRefreshEvent {

	public void BookmarkRefreshEvent() {
		
	}

	@Override
	public String toString() {
		return new StringBuilder("(")
		.append("bus event: refresh bookmarks")
		.append(")").toString();
	}
}