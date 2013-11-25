package es.zaldo.petstore.service.validation;

import org.junit.Assert;
import org.junit.Test;


import es.zaldo.petstore.core.Box;
import es.zaldo.petstore.core.Location;
import es.zaldo.petstore.service.Constants;
import es.zaldo.petstore.service.validation.BoxValidator;
import es.zaldo.petstore.service.validation.LocationValidator;

/**
 * Tests for the {@link Box} class.
 */
public class BoxValidatorTest {

    @Test
    public void testValidBox() {
        Box box = new Box(
                new Location(4, 6), new Location(2, 8));
        Assert.assertTrue(new BoxValidator(new LocationValidator()).isValid(box));
    }

    @Test
    public void testInvalidParameterValue() {
        Box box = new Box(
                new Location(Double.valueOf(Constants.INVALID_COORDINATE_VALUE), 1), new Location(1, 1));
        Assert.assertFalse(new BoxValidator(new LocationValidator()).isValid(box));

        box = new Box(
                new Location(1, Double.valueOf(Constants.INVALID_COORDINATE_VALUE)), new Location(1, 1));
        Assert.assertFalse(new BoxValidator(new LocationValidator()).isValid(box));

        box = new Box(
                new Location(1, 1), new Location(Double.valueOf(Constants.INVALID_COORDINATE_VALUE), 1));
        Assert.assertFalse(new BoxValidator(new LocationValidator()).isValid(box));

        box = new Box(
                new Location(1, 1), new Location(1, Double.valueOf(Constants.INVALID_COORDINATE_VALUE)));
        Assert.assertFalse(new BoxValidator(new LocationValidator()).isValid(box));
    }

    @Test
    public void testInvalidLats() {
        Box box = new Box(new Location(2, 6), new Location(4, 8));
        Assert.assertFalse(new BoxValidator(new LocationValidator()).isValid(box));
    }

    @Test
    public void testInvalidLon() {
        Box box = new Box(new Location(4, 8), new Location(3, 6));
        Assert.assertFalse(new BoxValidator(new LocationValidator()).isValid(box));
    }

}
