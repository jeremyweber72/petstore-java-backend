package es.zaldo.petstore.core;

/**
 * Holds pagination information.
 */
public class PageRequest {

    private final int page;
    private final int size;

    /**
     * Constructor of the class.
     *
     * @param page Page number
     * @param size Number of results per page
     */
    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + page;
        result = prime * result + size;
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PageRequest other = (PageRequest) obj;
        if (page != other.page)
            return false;
        if (size != other.size)
            return false;
        return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PageRequest [page=" + page + ", size=" + size + "]";
    }

}
