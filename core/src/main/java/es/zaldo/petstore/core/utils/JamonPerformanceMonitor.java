package es.zaldo.petstore.core.utils;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * JAMon based performance monitor.
 */
public class JamonPerformanceMonitor implements PerformanceMonitor {

    private static final Logger LOGGER =
            Logger.getLogger(JamonPerformanceMonitor.class);

    private static final String SEPARATOR = ";";

    private HashMap<AvailableMonitors, Monitor> monitors =
            new HashMap<AvailableMonitors, Monitor>();
    private HashMap<AvailableMonitors, Double> values =
            new HashMap<AvailableMonitors, Double>();

    private boolean isMonitoringEnabled = false;
    private boolean writeStatsToFile = false;

    /**
     * Constructor of the class.
     *
     * @param isMonitoringEnabled <code>true</code> if calls have to be logged
     * @param writeStatsToFile <code>true</code> if calls have to be saved to
     * disk
     */
    public JamonPerformanceMonitor(
            boolean isMonitoringEnabled, boolean writeStatsToFile) {
        this.isMonitoringEnabled = isMonitoringEnabled;
        this.writeStatsToFile = writeStatsToFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(AvailableMonitors performanceMonitor) {
        if (isMonitoringEnabled) {
            monitors.put(performanceMonitor,
                    MonitorFactory.start(performanceMonitor.toString()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double stop(AvailableMonitors performanceMonitor)
            throws MonitorNotReadyException {
        Double result = -1d;
        if (isMonitoringEnabled) {
            Monitor monitor = monitors.get(performanceMonitor);
            if (monitor == null) {
                throw new MonitorNotReadyException();
            }
            synchronized (this) {
                monitor.stop();
                result = monitor.getLastValue();
                if (writeStatsToFile) {
                    LOGGER.debug(formatData(monitor));
                }
            }
            values.put(performanceMonitor, result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getValue(AvailableMonitors performanceMonitor) {
        Double result = values.get(performanceMonitor);
        if (result == null) {
            return 0d;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MonitorData getData(AvailableMonitors performanceMonitor) {
        Monitor timeMonitor = MonitorFactory.getTimeMonitor(
                performanceMonitor.toString());
        MonitorData result = new MonitorData();
        result.setName(timeMonitor.getLabel());
        result.setAverage(timeMonitor.getAvg());
        result.setHits(timeMonitor.getHits());
        result.setLast(timeMonitor.getLastValue());
        result.setMax(timeMonitor.getMax());
        result.setMin(timeMonitor.getMin());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MonitorShortData getShortData(AvailableMonitors performanceMonitor) {
        Monitor timeMonitor = MonitorFactory.getTimeMonitor(
                performanceMonitor.toString());
        MonitorShortData result = new MonitorShortData();
        result.setAverage(timeMonitor.getAvg());
        result.setHits(timeMonitor.getHits());
        result.setLast(timeMonitor.getLastValue());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetAll() {
        MonitorFactory.reset();
    }

    /**
     * {@inheritDoc}
     */
    public String formatData(Monitor monitor) {
        return monitor.getLabel() + SEPARATOR
                + String.valueOf(monitor.getLastValue())
                + SEPARATOR
                + String.valueOf(monitor.getHits())
                + SEPARATOR
                + String.valueOf(monitor.getAvg())
                + SEPARATOR
                + String.valueOf(monitor.getTotal())
                + SEPARATOR
                + String.valueOf(monitor.getMin())
                + SEPARATOR
                + String.valueOf(monitor.getMax())
                + SEPARATOR
                + String.valueOf(monitor.getActive())
                + SEPARATOR
                + String.valueOf(monitor.getAvgActive())
                + SEPARATOR
                + String.valueOf(monitor.getMaxActive())
                + SEPARATOR
                + String.valueOf(monitor.getFirstAccess())
                + SEPARATOR
                + String.valueOf(monitor.getLastAccess());
    }

}
