package es.zaldo.petstore.service.marshalling;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.jettison.json.JSONObject;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.utils.PetUtils;

/**
 * Marshalls a pet into a JSON object.
 */
public class PetMarshaller implements Marshaller<Pet, JSONObject> {

    private PetUtils petUtils;

    /**
     * Constructor of the class.
     *
     * @param petUtils Utility class
     */
    public PetMarshaller(PetUtils petUtils)    {
        this.petUtils= petUtils;
    }

    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject marshall(Pet dbPet) throws MarshallerException {
        JSONObject json= new JSONObject();
        try {
            json.put("id", dbPet.getId().toString());
            // Adding the url attribute
            json.put("url", petUtils.getUrlBase() + "/" + dbPet.getId());

            json.put("name", dbPet.getName().toString());
            json.put("owner", dbPet.getOwner().toString());
            if (dbPet.getGroup() != null)
                json.put("group", dbPet.getGroup().toString());
            if (dbPet.getType() != null)
                json.put("type", dbPet.getType().toString());

            JSONObject jsLocation= new JSONObject();
            jsLocation.put("latitude", dbPet.getLocation().getLatitude());
            jsLocation.put("longitude", dbPet.getLocation().getLongitude());
            json.put("coords", jsLocation);

            if (dbPet.getLastUpdate() != null &&
                    !dbPet.getLastUpdate().toString().trim().isEmpty())
                json.put("lastUpdate", dbPet.getLastUpdate());

            HashMap<String,Object> attr= dbPet.getAttributes();
            Set<String> keySet = attr.keySet();

            Iterator<String> itr= keySet.iterator();
            while (itr.hasNext())    {
                String key= itr.next();

                if (HashMap.class.isAssignableFrom(attr.get(key).getClass())) {
                    HashMap<String, String> map= (HashMap<String, String>) attr.get(key);
                    Set<String> hashKeys = map.keySet();
                    Iterator<String> hashItr= hashKeys.iterator();
                    JSONObject jsMap= new JSONObject();
                    while (hashItr.hasNext())    {
                        String mapElementKey= hashItr.next();
                        jsMap.put(mapElementKey, map.get(mapElementKey));
                    }
                    json.put(key, jsMap);
                } else {
                    json.put(key, attr.get(key));
                }

            }

        } catch (NullPointerException ex)            {
            throw new RuntimeException("Unable read mandatory attributes: " + ex.getMessage(), ex.getCause());
        } catch (Exception ex)    {
            throw new RuntimeException("Server error: " + ex.getMessage(), ex.getCause());
        }
        return json;
	}

}
