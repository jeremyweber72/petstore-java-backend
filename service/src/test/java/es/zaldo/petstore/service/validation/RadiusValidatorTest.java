package es.zaldo.petstore.service.validation;

import org.junit.Assert;
import org.junit.Test;

import es.zaldo.petstore.service.validation.RadiusValidator;

/**
 * Tests for the {@link RadiusValidator} class.
 */
public class RadiusValidatorTest {

    @Test
    public void testValidRadius() {
        Assert.assertTrue(
                "Radius is valid!", new RadiusValidator(10).isValid(5d));
    }

    @Test
    public void testValidMaximumRadius() {
        Assert.assertTrue(
                "Radius is valid!", new RadiusValidator(5).isValid(5d));
    }

    @Test
    public void testInvalidRadius() {
        Assert.assertFalse(
                "Radius is invalid!", new RadiusValidator(5).isValid(10d));
    }

    @Test
    public void testInvalidNegativeRadius() {
        Assert.assertFalse(
                "Radius is invalid!", new RadiusValidator(5).isValid(-1d));
    }

    @Test
    public void testInvalidZeroRadius() {
        Assert.assertFalse(
                "Radius is invalid!", new RadiusValidator(5).isValid(0d));
    }

}
