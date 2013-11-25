package es.zaldo.petstore.service.validation;

import es.zaldo.petstore.core.PageRequest;

/**
 * Validates {@link PageRequest}s.
 */
public class PageRequestValidator implements Validator<PageRequest> {

    private int maxPage;
    private int maxSize;

    /**
     * Constructor of the class.
     *
     * @param maxPage Maximum page to request
     * @param maxSize Maximum page size
     */
    public PageRequestValidator(int maxPage, int maxSize) {
        super();
        this.maxPage = maxPage;
        this.maxSize = maxSize;
    }

    @Override
    public boolean isValid(PageRequest validable) {
        return validable.getPage() >= 0 && validable.getPage() <= maxPage &&
                validable.getSize() >= 0 && validable.getSize() <= maxSize;
    }

    /**
     * @return the maxPage
     */
    public int getMaxPage() {
        return maxPage;
    }

    /**
     * @return the maxSize
     */
    public int getMaxSize() {
        return maxSize;
    }

}
