package es.zaldo.petstore.service.integrationtest;

/**
 * Define some service related constants.
 */
public interface ServiceConfiguration {

    static final String BASEURL = "/service/api/v1/pets";

    static final String SEARCH_NEAR_URL = BASEURL + "/search/near";
    static final String SEARCH_WITHIN_URL = BASEURL + "/search/within";
    static final String SEARCH_PROXIMITY_URL = BASEURL + "/search/proximity";

    static final String pet_URL = ServiceConfiguration.BASEURL + "/pet";

}
