package es.zaldo.petstore.core.dao.mixed;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.geo.GeoLocation;

import es.zaldo.petstore.core.Pet;

/**
 * pet used to persist data in Solr when using the mixed Dao.
 */
public class PetMixed {

    /**
     * Internal and Unique pet Id.
     */
    @Id
    @Field
    private String id;

    @Field
    private String owner;

    @Field
    private String group;

    @Field
    private String type;


    /**
     * Stored
     */
    @Field
    private GeoLocation place;

    /**
     * Constructor of the class.
     */
    @SuppressWarnings("unused")
    private PetMixed() { /* NEEDED BY SOLR UNMARSHALLER */ }

    /**
     * Constructor of the class.
     *
     * @param id ID of the pet
     * @param place Location of the pet
     */
    public PetMixed(String id, GeoLocation place, String owner, String group, String type) {
        this.id = id;
        this.owner = owner;
        this.place = place;
        this.group = group;
        this.type = type;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the place
     */
    public GeoLocation getPlace() {
        return place;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Parses a pet.
     *
     * @param pet pet to parse
     *
     * @return A brand new <code>PetMixed</code> with the data from the given
     * pet.
     */
    public static PetMixed parsePet(Pet pet) {
        return new PetMixed(pet.getId(), new GeoLocation(
            pet.getLocation().getLatitude(), pet.getLocation().getLongitude()),
                pet.getOwner(), pet.getGroup(), pet.getType());
    }

    // TODO Use Apache Commons to implement equals and hashcode

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((place == null) ? 0 : place.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /* (non-Javadoc)
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
        PetMixed other = (PetMixed) obj;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id)) {
            return false;
        } else if (place.getLatitude() != other.getPlace().getLatitude()) {
            return false;
        } else if (place.getLongitude() != other.getPlace().getLongitude()) {
            return false;
        }
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
