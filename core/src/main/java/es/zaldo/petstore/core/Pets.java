package es.zaldo.petstore.core;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents list of {@link Pet}s.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pets")
public class Pets {

    @XmlElement(name = "pet", type = Pet.class)
	protected List<Pet> pets = new ArrayList<Pet>();

    protected int currentPage;
    protected int numberOfResults;
    protected int totalNumberOfResults;

    /**
     * Needed by JAXB!
     */
    protected Pets() {}

    /**
     * Constructor of the class.
     *
     * @param pets List of petS returned
     * @param currentPage
     * @param numberOfResults
     * @param totalNumberOfResults
     */
    public Pets(List<Pet> pets, int currentPage, int numberOfResults,
            int totalNumberOfResults) {
        this.pets = pets;
        this.totalNumberOfResults = totalNumberOfResults;
        this.currentPage = currentPage;
        this.numberOfResults = numberOfResults;
    }


    /**
     * @return the list of pets
     */
    public List<Pet> getPets() {
        return pets;
    }

    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @return the numberOfResults
     */
    public int getNumberOfResults() {
        return numberOfResults;
    }

    /**
     * @return the totalNumberOfResults
     */
    public int getTotalNumberOfResults() {
        return totalNumberOfResults;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Pets [pets=" + pets + ", currentPage=" + currentPage
                + ", numberOfResults=" + numberOfResults
                + ", totalNumberOfResults=" + totalNumberOfResults + "]";
    }

}
