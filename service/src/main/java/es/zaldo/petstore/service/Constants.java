package es.zaldo.petstore.service;
/**
* This class holds the list of constants used in the application.
*
*/
public abstract class Constants {

    public static final String DECODE_TYPE = "UTF-8";

    public static final String CHARSET_UTF8 = ";charset=utf-8";

    public static final int INVALID_PARAM_ERROR_CODE = 400;

    /**
    *  HttpHeaders Constants
    */
    public static final String CACHE_CONTROL_KEY = "Cache-Control";

    public static final String CACHE_CONTROL= "no_cache";

    public static final String VARY_KEY ="Vary";

    public static final String VARY = "Accept";

    public static final String ACCESS_CONTROL_KEY = "Access-Control-Allow-Origin";

    public static final String ACCESS_CONTROL = "*";

    /**
     *  Pagination Constants
     */

    public static final String DEFAULT_START_PAGE = "0";

    public static final String DEFAULT_PAGE_NUMROWS = "20";

    /**
     * Search relate constants
     */

    public static final String DEFAULT_SEARCH_RADIUS = "1000";

    public static final String INVALID_COORDINATE_VALUE = "1000";



}
