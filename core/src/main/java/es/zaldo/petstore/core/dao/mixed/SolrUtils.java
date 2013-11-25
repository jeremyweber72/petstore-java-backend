package es.zaldo.petstore.core.dao.mixed;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.geo.BoundingBox;
import org.springframework.data.solr.core.geo.Distance;
import org.springframework.data.solr.core.geo.Distance.Unit;
import org.springframework.data.solr.core.geo.GeoLocation;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;

import es.zaldo.petstore.core.Box;
import es.zaldo.petstore.core.Location;
import es.zaldo.petstore.core.PageRequest;

/**
 * Provides some utility methods to deal with Solr.
 */
public abstract class SolrUtils {

    private static final String TYPE_FIELD_NAME = "type";
    private static final String GROUP_FIELD_NAME = "group";
    private static final String COORDS_FIELD_NAME = "place";

    /**
     * Builds a proximity query with pagination.
     *
     * @param groups Groups filter
     * @param types Types filter
     * @param center Location filter
     * @param radius Radius filter
     * @param pagination Pagination info
     *
     * @return A proximity query ready to be executed against Solr
     */
    public static Query buildProximityQuery( List<String> groups, List<String> types,
            Location center, double radius, PageRequest pagination, Unit unit) {

        GeoLocation petnt= new GeoLocation(center.getLatitude(), center.getLongitude());
        Distance distance= new Distance(radius, unit);

        FilterQuery fq= new SimpleFilterQuery();

        if (groups != null && !groups.isEmpty()) {
            fq.addCriteria(getListCriteria(GROUP_FIELD_NAME, groups));
        }

        if (types != null && !types.isEmpty()) {
            fq.addCriteria(getListCriteria(TYPE_FIELD_NAME, types));
        }

        fq.addCriteria(new Criteria(COORDS_FIELD_NAME).within(petnt, distance));
        return getQueryWithPagination(fq.getCriteria(), pagination);
    }

    /**
     * Builds a within query with pagination.
     *
     * @param groups Groups filter
     * @param types Types filter
     * @param bounds Bounding box
     * @param pagination Pagination info
     *
     * @return A proximity query ready to be executed against Solr
     */
    public static Query buildWithinQuery(List<String> groups,
            List<String> types, Box bounds, PageRequest pagination) {
        GeoLocation upperLeft = new GeoLocation(bounds.getLowerRight().getLatitude(),
                bounds.getUpperLeft().getLongitude());
        GeoLocation lowerRight = new GeoLocation(bounds.getUpperLeft().getLatitude(),
                bounds.getLowerRight().getLongitude());

        BoundingBox box= BoundingBox.startingAt(upperLeft).endingAt(lowerRight);

        //FilterQuery fq = new SimpleFilterQuery(new Criteria(COORDS_FIELD_NAME).near(box));
        FilterQuery fq= new SimpleFilterQuery();

        if (groups != null && !groups.isEmpty()) {
            Criteria groupCriteria= getListCriteria(GROUP_FIELD_NAME, groups);
            if (groupCriteria != null)
                fq.addCriteria(groupCriteria);
        }

        if (types != null && !types.isEmpty()) {
        	Criteria typeCriteria= getListCriteria(TYPE_FIELD_NAME, types);
            if (typeCriteria != null)
                fq.addCriteria(typeCriteria);
        }

        fq.addCriteria(new Criteria(COORDS_FIELD_NAME).near(box));
        return getQueryWithPagination(fq.getCriteria(), pagination);
    }

    /**
     * Getting a fieldName and a list of items the method returns a Criteria a
     * composed object build with "OR" conditions.
     *
     * @param fieldName
     * @param list
     *
     * @return
     */
    public static Criteria getListCriteria(String fieldName, List<String> list) {
        Criteria criteria = null;
        if (list != null && !list.isEmpty())	{
            for (String element: list)  {
                if (criteria == null) {
                    criteria = new Criteria(fieldName).contains(element);
                } else {
                    criteria = criteria.or(
                            new Criteria(fieldName).is(element));
                }
            }
        }
        return criteria;
    }

    /**
     * Creates a new query object with the given criteria and pagination values.
     *
     * @param pagination
     * @param criteria
     *
     * @return A new query object ready to use
     */
    public static Query getQueryWithPagination(Criteria criteria, PageRequest pagination) {
        return new SimpleQuery(criteria).setPageRequest(
            new org.springframework.data.domain.PageRequest(
            pagination.getPage(), pagination.getSize()))
                .addSort(new Sort(Sort.Direction.ASC, "score"));
    }

}
