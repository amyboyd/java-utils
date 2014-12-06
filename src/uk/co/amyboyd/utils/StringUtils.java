package uk.co.amyboyd.utils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * String utilities.
 */
final public class StringUtils {
    private final static String BYTE_ORDER_MARK = getByteOrderMark();

    private StringUtils() {
    }

    public static String getByteOrderMark() {
        try {
            return new String("\uFEFF".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Remove byte-order-mark from the start of a UTF-8 string.
     */
    public static String removeByteOrderMark(final String in) {
        if (in.startsWith(BYTE_ORDER_MARK)) {
            return in.replace(BYTE_ORDER_MARK, "");
        }
        return in;
    }

    /**
     * Percent-encode values according the RFC 3986. The built-in Java URLEncoder does not encode
     * according to the RFC, so we make the extra replacements.
     *
     * @param string Decoded string.
     * @return Encoded string per RFC 3986.
     */
    public static String percentEncodeRfc3986(final String string) {
        try {
            return URLEncoder.encode(string, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (final UnsupportedEncodingException e) {
            return string;
        }
    }

    /**
     * @see CollectionUtils#queryStringToMap(java.lang.String)
     */
    public static Map<String, String> queryStringToMap(final String queryString) {
        return CollectionUtils.queryStringToMap(queryString);
    }

    /**
     * Normalizes a URL.
     * 
     * <p>If the URL points to a HTML page and the page has a "canonical" link, it is
     * better to use that instead. However this provides a fallback solution when the URL does not
     * point to a HTML page, or if there is no canonical link.
     *
     * <ol>
     * <li>Covert the protocol and host to lowercase
     * <li>Remove the port number if it is the default for the protocol.
     * <li>Remove the fragment (everything after #).
     * <li>Remove trailing slash.
     * <li>Sort the query string params.
     * <li>Remove some query string params like "utm_*" and "*session*".
     * </ol>
     */
    public static String normalize(final String taintedURL) {
        final URL url;
        try {
            url = new URL(taintedURL);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL: " + taintedURL);
        }

        final StringBuilder result = new StringBuilder(taintedURL.length());

        // Convert the protocol and host to lowercase.
        result.append(url.getProtocol().toLowerCase()).
                append("://").
                append(url.getHost().toLowerCase());

        // Remove the port number if it is the default for the protocol.
        final int port = url.getPort();
        if (port != -1 && port != 80) {
            result.append(':').append(port);
        }

        // Remove trailing slash.
        final String path = url.getPath().replace("/$", "");
        result.append(path);

        // Sort the query string params.
        final SortedMap<String, String> params = CollectionUtils.queryStringToMap(url.getQuery());

        // Remove Google Analytics tracking and session-dependant params.
        if (params != null) {
            // Some params are only relevant for user tracking, so remove the most commons ones.
            for (final Map.Entry<String, String> entry: params.entrySet()) {
                final String key = entry.getKey();
                if (key.startsWith("utm_") || key.contains("session")) {
                    params.remove(key);
                }
            }
        }

        final String queryString = CollectionUtils.mapToQueryString(params);
        if (queryString != null && !queryString.isEmpty()) {
            result.append('?').append(queryString);
        }

        return result.toString();
    }
}
