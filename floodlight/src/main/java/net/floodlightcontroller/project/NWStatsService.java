package net.floodlightcontroller.project;

import net.floodlightcontroller.core.module.IFloodlightService;

import java.util.List;

public interface NWStatsService extends IFloodlightService {
    public void enableStats();
    public void disableStats();
    public List<TableEntry> collectStats(String src, String dst, int idx, int max);

}
