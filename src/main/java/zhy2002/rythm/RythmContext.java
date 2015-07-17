package zhy2002.rythm;


import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrap the hash map so that Rythm template can access.
 */
public class RythmContext {

    private final HashMap<Comparable, RythmContext> valueCache;
    private final Object value;

    /**
     * Wrap the argument object to allow uniform access in Rythm template.
     *
     * @param value any Object.
     */
    public RythmContext(Object value) {

        this.value = value;
        valueCache = value == null ? null : new HashMap<Comparable, RythmContext>();
    }

    /**
     * If the underlying object is a a list or array then return the element at index. Otherwise return null.
     *
     * @param index the index of the element to get.
     * @return list or array element wrapped in a RythmContext. Return null if underlying object is not list or array OR index if not valid.
     */
    @SuppressWarnings("unchecked")
    public RythmContext get(int index) {

        if (value == null)
            return null;

        RythmContext result = valueCache.get(index);
        if (result != null)
            return result;

        if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (index >= list.size())
                return null;
            result = new RythmContext(list.get(index));
            valueCache.put(index, result);
            return result;
        }

        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            if (index >= length)
                return null;
            result = new RythmContext(Array.get(value, index));
            valueCache.put(index, result);
            return result;
        }

        return null;
    }

    /**
     * If the underlying object is a hash map, which is converted from a json object, then access its property value with this method.
     * @param propertyName the property name of the json object.
     * @return property value wrapped in a RythmContext.
     */
    @SuppressWarnings("unchecked")
    public RythmContext get(String propertyName) {
        if(value == null)
            return null;

        RythmContext result = valueCache.get(propertyName);
        if (result != null)
            return result;

        if (value instanceof Map) {
            Map<String,Object> map = (Map<String,Object>)value;
            if(!map.containsKey(propertyName))
                return null;

            result = new RythmContext(map.get(propertyName));
            valueCache.put(propertyName, result);
            return result;
        }

        return null;
    }

    /**
     * Get the underlying data object.
     *
     * @return the object wrapped by this RythmContext.
     */
    public Object getValue() {
        return value;
    }


    @Override
    public String toString() {
        return value == null ? null : value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RythmContext that = (RythmContext) o;

        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
