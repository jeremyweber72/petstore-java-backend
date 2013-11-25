package es.zaldo.petstore.core.utils;

/**
 * Exception thrown when trying to stop or read a monitor that has not been
 * started.
 */
@SuppressWarnings({ "serial" })
public class MonitorNotReadyException extends RuntimeException {}
