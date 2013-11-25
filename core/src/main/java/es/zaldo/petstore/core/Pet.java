package es.zaldo.petstore.core;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import es.zaldo.petstore.core.utils.PetUtils;

/**
 * This class represents a pet.
 */
@XmlRootElement(name="pet")
@XmlAccessorType(XmlAccessType.FIELD)
@CompoundIndexes({
    @CompoundIndex(name= "petnt_group_type_idx", def = "{ 'coords': 1, 'group': 1, 'type': 1 }"),
    @CompoundIndex(name= "petnt_group_idx", def = "{ 'coords': 1 , 'group': 1}"),
    @CompoundIndex(name= "petnt_type_idx", def = "{ 'coords': 1, 'type': 1 }")
})
@Document
public class Pet {

    /**
     * Internal and Unique Pet Id.
     */
    @Id
    @Field
    private String id;

    /**
     * Owner of the Pet.
     */
    @Indexed
    @Field
    private String owner;

    /**
     * Pet name.
     */
    @Field
    private String name;

    /**
     * Location of the pet.
     */
    @GeoSpatialIndexed
    protected double[] coords = new double[2];

    /**
     * pet objected extended attributes
     */
    private HashMap<String, Object> attributes = new HashMap<String, Object>();

    /**
     * pet group. Examples: Cat, dog, etc.
     */
    @Indexed
    @Field
    private String group;

    /**
     * Type of the pet. Examples: Caniche, Dogo, etc.
     */
    @Indexed
    @Field
    private String type;

    /**
     * This field is managed automatically by the pets service.
     */
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Field
    private Date lastUpdate;

    /**
     * Needed by JAXB marshaller.
     */
    public Pet() {
        this.id = PetUtils.generateUUID().toString();
    }

    /**
     * Constructor of the class.
     *
     * @param id
     */
    public Pet(String id) {
        this.id = id;
    }

    /**
     * Constructor of the class.
     *
     * @param owner
     * @param countryCode
     * @param latitude
     * @param longitude
     */
    public Pet(String name, Location location) {
        this.id = PetUtils.generateUUID().toString();
        this.name = name;
        setLocation(location.getLatitude(), location.getLongitude());
    }

    /**
     * Constructor of the class.
     *
     * @param id
     * @param owner
     * @param countryCode
     * @param latitude
     * @param longitude
     */
    public Pet(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        setLocation(location.getLatitude(), location.getLongitude());
    }

    /**
     * Constructor of the class.
     *
     * @param id
     * @param owner
     * @param countryCode
     * @param latitude
     * @param longitude
     */
    public Pet(String name, Location location, String owner, String group) {
        this.id = PetUtils.generateUUID().toString();
        this.name = name;
        this.owner = owner;
        this.group = group;
        setLocation(location.getLatitude(), location.getLongitude());
    }

    /**
     * Constructor of the class.
     *
     * @param id
     * @param owner
     * @param countryCode
     * @param latitude
     * @param longitude
     */
    public Pet(String id, String name, Location location, String owner,
            String group) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.group = group;
        setLocation(location.getLatitude(), location.getLongitude());
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public Pet setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public Pet setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public Pet setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return new Location(coords[1], coords[0]);
    }

    /**
     * @param the location
     */
    public Pet setLocation(double latitude, double longitude) {
        this.coords[0]= longitude;
        this.coords[1]= latitude;
        return this;
    }

    /**
     * @return the pet extended attributes
     */
    public HashMap<String,Object> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     */
    public Pet setAttributes(HashMap<String,Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public Pet setGroup(String group) {
        this.group = group;
        return this;
    }

    /**
     * @return the creationDate
     */
    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setLastUpdate() {
        this.lastUpdate = new Date();
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public Pet setType(String type) {
        this.type = type;
        return this;
    }

    // TODO Use Apache Commons to handle equals and hashcode

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + Arrays.hashCode(coords);
        result = prime
                * result
                + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
        Pet other = (Pet) obj;
        if (attributes == null) {
            if (other.attributes != null)
                return false;
        } else if (!attributes.equals(other.attributes))
            return false;
        if (!Arrays.equals(coords, other.coords))
            return false;
        if (lastUpdate == null) {
            if (other.lastUpdate != null)
                return false;
        } else if (!lastUpdate.equals(other.lastUpdate))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "pet [id=" + id + ", owner=" + owner + ", name=" + name
                + ", coords=" + Arrays.toString(coords) + ", attributes="
                + attributes + ", groups=" + group + ", type=" + type
                + ", lastUpdate=" + lastUpdate + "]";
    }

}
