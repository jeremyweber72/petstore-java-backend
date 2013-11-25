package es.zaldo.petstore.service.validation;

import es.zaldo.petstore.core.Location;

/**
 * Validates <code>Location</code>s.
 */
public class LocationValidator implements Validator<Location> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Location validable) {
        return isValidLat(validable.getLatitude())
                && isValidLon(validable.getLongitude());
    }

    /**
     * @param coord Coordenates to validate
     *
     * @return <code>True</code> if the coordenate is valid or
     * <code>false</code> otherwise.
     */
    private boolean isValidLat(double lat) {
        return lat >= -90 && lat <= 90;
    }

    /**
     * @param coord Coordenates to validate
     *
     * @return <code>True</code> if the coordenate is valid or
     * <code>false</code> otherwise.
     */
    private boolean isValidLon(double lon) {
        return lon >= -180 && lon <= 180;
    }

}
