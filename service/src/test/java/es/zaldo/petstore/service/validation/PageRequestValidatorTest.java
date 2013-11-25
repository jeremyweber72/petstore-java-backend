package es.zaldo.petstore.service.validation;

import org.junit.Assert;
import org.junit.Test;


import es.zaldo.petstore.core.PageRequest;
import es.zaldo.petstore.service.validation.PageRequestValidator;

/**
 * Tests for the {@link PageRequestValidator} class.
 */
public class PageRequestValidatorTest {

    @Test
    public void testValid() {
        PageRequestValidator validatorUnderTest =
                new PageRequestValidator(10, 10);
        Assert.assertTrue("PageRequest is valid.",
                validatorUnderTest.isValid(new PageRequest(5, 5)));
    }

    @Test
    public void testInvalidMaxPage() {
        PageRequestValidator validatorUnderTest =
                new PageRequestValidator(1, 10);
        Assert.assertFalse("PageRequest is invalid.",
                validatorUnderTest.isValid(new PageRequest(5, 5)));
    }

    @Test
    public void testNegativeMaxPage() {
        PageRequestValidator validatorUnderTest =
                new PageRequestValidator(10, 10);
        Assert.assertFalse("PageRequest is invalid.",
                validatorUnderTest.isValid(new PageRequest(-5, 5)));
    }

    @Test
    public void testInvalidMaxSize() {
        PageRequestValidator validatorUnderTest =
                new PageRequestValidator(10, 1);
        Assert.assertFalse("PageRequest is invalid.",
                validatorUnderTest.isValid(new PageRequest(5, 5)));
    }

    @Test
    public void testNegativeMaxSize() {
        PageRequestValidator validatorUnderTest =
                new PageRequestValidator(10, 10);
        Assert.assertFalse("PageRequest is invalid.",
                validatorUnderTest.isValid(new PageRequest(5, -5)));
    }

    @Test
    public void testInvalidMaxPageAndMaxSize() {
        PageRequestValidator validatorUnderTest =
                new PageRequestValidator(10, 10);
        Assert.assertFalse("PageRequest is invalid.",
                validatorUnderTest.isValid(new PageRequest(-5, -5)));
    }

}
