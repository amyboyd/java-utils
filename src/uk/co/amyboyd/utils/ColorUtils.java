package uk.co.amyboyd.utils;

/**
 * Color utilities.
 */
final public class ColorUtils {
    private ColorUtils() {
    }

    public static int rgb2int(final int red, final int green, final int blue) {
        return (red << 16) + (green << 8) + blue;
    }

    public static int rgb2int(final int[] color) {
        return (color[0] << 16) + (color[1] << 8) + color[2];
    }

    public static int[] int2rgb(final int color) {
        return new int[] { (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF };
    }

    /**
     * Return the "distance" between two colors. The rgb entries are taken
     * to be coordinates in a 3D space [0.0-1.0], and this method returnes
     * the distance between the coordinates for the first and second color.
     *
     * @return Distance between colors.
     */
    public static double colorDistance(final double r1, final double g1, final double b1,
            final double r2, final double g2, final double b2) {
        double a = r2 - r1;
        double b = g2 - g1;
        double c = b2 - b1;

        return Math.sqrt(a * a + b * b + c * c);
    }

    /**
     * Return the "distance" between two colors.
     * 
     * @param color1 First color [r,g,b].
     * @param color2 Second color [r,g,b].
     * @return Distance bwetween colors.
     */
    public static double colorDistance(final int color1, final int color2) {
        int[] rgb1 = int2rgb(color1), rgb2 = int2rgb(color2);

        return colorDistance(rgb1[0] / 256.0d, rgb1[1] / 256.0d, rgb1[2] / 256.0d, rgb2[0] / 256.0d, rgb2[1] / 256.0d, rgb2[2] / 256.0d);
    }

    /**
     * Convert from red, green and blue to a hex.
     *
     * <p>E.g. inputs (255, 0, 0) will return 0xff0000.
     *
     * @return The hex as a string (without any hash like in CSS).
     */
    public static String getHexName(final int red, final int green, final int blue) {
        String rHex = Integer.toString(red, 16);
        String gHex = Integer.toString(green, 16);
        String bHex = Integer.toString(blue, 16);

        return (rHex.length() == 2 ? rHex : "0" + rHex) + (gHex.length() == 2 ? gHex : "0" + gHex) + (bHex.length() == 2
                ? bHex : "0" + bHex);
    }

    /**
     * Convert from red, green and blue to a hex.
     *
     * <p>E.g. inputs (255, 0, 0) will return 0xff0000.
     *
     * @return The hex as a string (without any hash like in CSS).
     */
    public static String getHexName(final int color) {
        int[] rgb = int2rgb(color);

        return getHexName(rgb[0], rgb[1], rgb[2]);
    }
}
