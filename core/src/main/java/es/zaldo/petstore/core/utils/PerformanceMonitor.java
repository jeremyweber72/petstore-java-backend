package es.zaldo.petstore.core.utils;

/**
 * Defines all the operations needed to monitor the performance of specific
 * parts of the application.
 */
public interface PerformanceMonitor {

    enum AvailableMonitors {
        Marshal_Pet, Marshal_List, Unmarshal_Pet, Get_Pets_Core, By_Owner_Core,
        Load_Pet_Core, Update_Pet_Core, Create_Pet_Core, Delete_Pet_Core,
        Within_Core, Near_Core, Proximity_Core;

        /**
         * {@inheritDoc}
         */
        public String toString() {
            return this.name().replace("_", "-");
        }
    }

    /**
     * Starts a monitor.
     *
     * @param monitor Monitor to start
     */
    void start(AvailableMonitors performanceMonitor);

    /**
     * Stops and stores the value of a monitor.
     *
     * @return The value of the monitor.
     */
    Double stop(AvailableMonitors performanceMonitor)
            throws MonitorNotReadyException;

    /**
     * Reset all monitors.
     */
    void resetAll();

    /**
     * @param performanceMonitor Selected monitor
     *
     * @return The last gathered value of a monitor.
     */
    Double getValue(AvailableMonitors performanceMonitor);

    /**
     * @param performanceMonitor Selected monitor
     *
     * @return All data gathered by monitor.
     */
    MonitorData getData(AvailableMonitors performanceMonitor);

    /**
     * @param performanceMonitor Selected monitor
     *
     * @return Basic data gathered by monitor.
     */
    MonitorShortData getShortData(AvailableMonitors performanceMonitor);

}
