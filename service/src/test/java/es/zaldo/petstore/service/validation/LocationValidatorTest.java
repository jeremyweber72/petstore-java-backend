package es.zaldo.petstore.service.validation;

import org.junit.Assert;
import org.junit.Test;


import es.zaldo.petstore.core.Location;
import es.zaldo.petstore.service.Constants;
import es.zaldo.petstore.service.validation.LocationValidator;

/**
 * Tests for the {@link LocationValidator} class.
 */
public class LocationValidatorTest {

    @Test
    public void testValidCoordinates() {
        Assert.assertTrue(new LocationValidator().isValid(new Location(4, 6)));
    }

    @Test
    public void testInvalidLat() {
        Assert.assertFalse(new LocationValidator().isValid(
                new Location(Double.valueOf(Constants.INVALID_COORDINATE_VALUE), 6)));
    }

    @Test
    public void testInvalidLon() {
        Assert.assertFalse(new LocationValidator().isValid(
                new Location(6, Double.valueOf(Constants.INVALID_COORDINATE_VALUE))));
    }

    @Test
    public void testInvalidLatLon() {
        Assert.assertFalse(new LocationValidator().isValid(new Location(181, -91)));
    }

}
