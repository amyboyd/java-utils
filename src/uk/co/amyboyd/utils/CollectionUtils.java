package uk.co.amyboyd.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link Collection} utilities.
 */
final public class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * Canonicalize a Map as a query string.
     *
     * @param params Parameter name-value pairs.
     * @return Canonical form of query string. May be an empty string, but never null.
     */
    public static String mapToQueryString(final Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        final StringBuilder buffer = new StringBuilder(350);
        final Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();

        while (iter.hasNext()) {
            final Map.Entry<String, String> pair = iter.next();
            buffer.append(StringUtils.percentEncodeRfc3986(pair.getKey()));
            buffer.append('=');
            buffer.append(StringUtils.percentEncodeRfc3986(pair.getValue()));
            if (iter.hasNext()) {
                buffer.append('&');
            }
        }

        return buffer.toString();
    }

    /**
     * Takes a query string and separates the constituent name-value pairs, and stores
     * them in a SortedMap ordered by lexicographical order.
     *
     * @return Null if there is no query string.
     */
    public static SortedMap<String, String> queryStringToMap(final String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return null;
        }

        final String[] pairs = queryString.split("&");
        final Map<String, String> map = new HashMap<String, String>(pairs.length);

        for (final String pair: pairs) {
            if (pair.length() < 1) {
                continue;
            }

            String[] tokens = pair.split("=", 2);
            for (int j = 0; j < tokens.length; j++) {
                try {
                    tokens[j] = URLDecoder.decode(tokens[j], "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(CollectionUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            switch (tokens.length) {
                case 1: {
                    if (pair.charAt(0) == '=') {
                        map.put("", tokens[0]);
                    } else {
                        map.put(tokens[0], "");
                    }
                    break;
                }
                case 2: {
                    map.put(tokens[0], tokens[1]);
                    break;
                }
            }
        }

        // Convert to TreeMap so the keys are sorted.
        return new TreeMap<String, String>(map);
    }
}
