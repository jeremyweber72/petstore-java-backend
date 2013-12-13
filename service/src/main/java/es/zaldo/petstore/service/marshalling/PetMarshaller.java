package es.zaldo.petstore.service.marshalling;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.jettison.json.JSONObject;

import es.zaldo.petstore.core.Pet;

/**
 * Marshalls a pet into a JSON object.
 */
public class PetMarshaller implements Marshaller<Pet, JSONObject> {

    private static final String FIELD_LAST_UPDATE = "lastUpdate";
    private static final String FIELD_COORDS = "coords";
    private static final String FIELD_LONGITUDE = "longitude";
    private static final String FIELD_LATITUDE = "latitude";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_GROUP = "group";
    private static final String FIELD_OWNER = "owner";
    private static final String FIELD_NAME = "name";
    private static final String SEPARATOR = "/";
    private static final String FIELD_URL = "url";
    private static final String FIELD_ID = "id";

    private final String urlBase;

    /**
     * Constructor of the class.
     * 
     * @param petUtils
     *            Utility class
     */
    public PetMarshaller(final String urlBase) {
        this.urlBase = urlBase;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public JSONObject marshall(Pet dbPet) throws MarshallerException {
        JSONObject json = new JSONObject();
        try {
            json.put(FIELD_ID, dbPet.getId().toString());

            // Adding the url attribute
            json.put(FIELD_URL, urlBase + SEPARATOR + dbPet.getId());

            json.put(FIELD_NAME, dbPet.getName().toString());
            json.put(FIELD_OWNER, dbPet.getOwner().toString());
            if (dbPet.getGroup() != null)
                json.put(FIELD_GROUP, dbPet.getGroup().toString());
            if (dbPet.getType() != null)
                json.put(FIELD_TYPE, dbPet.getType().toString());

            JSONObject jsLocation = new JSONObject();
            jsLocation.put(FIELD_LATITUDE, dbPet.getLocation().getLatitude());
            jsLocation.put(FIELD_LONGITUDE, dbPet.getLocation().getLongitude());
            json.put(FIELD_COORDS, jsLocation);

            if (dbPet.getLastUpdate() != null && !dbPet.getLastUpdate().toString().trim().isEmpty())
                json.put(FIELD_LAST_UPDATE, dbPet.getLastUpdate());

            HashMap<String, Object> attr = dbPet.getAttributes();
            Set<String> keySet = attr.keySet();

            Iterator<String> itr = keySet.iterator();
            while (itr.hasNext()) {
                String key = itr.next();

                if (HashMap.class.isAssignableFrom(attr.get(key).getClass())) {
                    HashMap<String, String> map = (HashMap<String, String>) attr.get(key);
                    Set<String> hashKeys = map.keySet();
                    Iterator<String> hashItr = hashKeys.iterator();
                    JSONObject jsMap = new JSONObject();
                    while (hashItr.hasNext()) {
                        String mapElementKey = hashItr.next();
                        jsMap.put(mapElementKey, map.get(mapElementKey));
                    }
                    json.put(key, jsMap);
                } else {
                    json.put(key, attr.get(key));
                }

            }

            return json;
        } catch (NullPointerException ex) {
            throw new MarshallerException("Unable read mandatory attributes: " + ex.getMessage(),
                    ex.getCause());
        } catch (Exception ex) {
            throw new MarshallerException("Server error: " + ex.getMessage(), ex.getCause());
        }
    }

}
