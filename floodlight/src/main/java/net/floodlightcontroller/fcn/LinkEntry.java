package net.floodlightcontroller.fcn;

public class  LinkEntry{
	public LinkEntry(int acquisitionInterval, long numberOfBytesTransmitted) {
		super();
		this.acquisitionInterval = acquisitionInterval;
		this.numberOfBytesTransmitted = numberOfBytesTransmitted;
	}
	int acquisitionInterval;
	long numberOfBytesTransmitted;
	
	
}
