package net.floodlightcontroller.fcn;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using=NSEntryListSerializer.class)
public class NSEntryList {
	private List<NSEntry> list;

	public List<NSEntry> getList() {
		return list;
	}

	public NSEntryList(List<NSEntry> list) {
		this.list = list;
	}

}


