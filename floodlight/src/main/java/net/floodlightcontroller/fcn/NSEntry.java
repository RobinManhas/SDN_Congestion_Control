package net.floodlightcontroller.fcn;

import java.util.List;

public class NSEntry {
	public NSEntry(int i, int l, List<LinkEntry> linkEntries) {
		super();
		this.i = i;
		L = l;
		this.linkEntries = linkEntries;
	}
	int i; //index
	int L;// number of links
	List<LinkEntry> linkEntries;
	

}

