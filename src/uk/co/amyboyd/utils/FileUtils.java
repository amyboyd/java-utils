package uk.co.amyboyd.utils;

import java.io.*;
import java.net.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 * File, URL and IO utilities.
 */
final public class FileUtils {
    private FileUtils() {
    }

    /**
     * Copy the content of one file to another.
     *
     * <p>Optionally the source can be appended to the destination file.
     *
     * @throws IOException if the URL doesn't respond "200 OK", or another error.
     */
    public static void copy(final URL source, final File destination, final boolean append)
            throws IOException {
        InputStream in = null;
        FileOutputStream out = null;

        try {
            if (!append) {
                destination.mkdirs();
                destination.delete();
                destination.createNewFile();
            }

            in = source.openStream();
            out = new FileOutputStream(destination, append);

            byte[] buf = new byte[10000];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (final IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "Exception copying URL to file: " + ex.
                    getMessage(), ex);
            destination.delete();

            throw ex;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (final IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Copy the content of one file to another.
     *
     * Optionally the source can be appended to the destination file.
     *
     * @throws IOException
     */
    public static void copy(final File source, final File destination, final boolean append)
            throws IOException {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(destination, append);

            byte[] buf = new byte[10000];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (final IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            destination.delete();

            throw ex;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (final IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Write to a file.
     *
     * Optionally the source can be appended to the destination file.
     *
     * @throws IOException
     */
    public static void write(final String source, final File destination, final boolean append)
            throws IOException {
        OutputStream out = null;

        try {
            out = new FileOutputStream(destination, append);
            out.write(source.getBytes("UTF-8"));
        } catch (final IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            destination.delete();

            throw ex;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (final IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Read the content of a URL to a string.
     *
     * @throws IOException
     */
    public static String read(final URL source)
            throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(source.openStream()));
        final StringBuilder response = new StringBuilder(5000);
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }

        in.close();

        return response.toString();
    }

    /**
     * @link http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
     *
     * @return Content of the file as a string.
     * @throws IOException if there is a file-system error.
     */
    public static String read(final File source) throws IOException {
        final FileInputStream stream = new FileInputStream(source);
        try {
            final FileChannel fc = stream.getChannel();
            final MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            // Instead of using default, pass in a decoder.
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }

    /**
     * Change every occurence of <tt>replace</tt> to <tt>with</tt> in a file's content, then overwrite the file.
     *
     * @throws IOException if the file does not exist.
     */
    public static void replaceLiteralInFile(final File file, final String replace, final String with)
            throws IOException {
        if (!file.exists()) {
            throw new IOException("File does not exist: " + file.getAbsolutePath());
        }

        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            final StringBuffer oldText = new StringBuffer((int) file.length());
            String line = "";

            while ((line = reader.readLine()) != null) {
                oldText.append(line).append("\n");
            }
            reader.close();

            final FileWriter writer = new FileWriter(file);
            writer.write(oldText.toString().replace(replace, with));
            writer.close();
        } catch (final IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return The fully-redirected URL, or the original if this fails.
     */
    public static URL followRedirects(final URL original) {
        InputStream is = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) original.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.connect();
            is = conn.getInputStream();

            return conn.getURL();
        } catch (final Exception ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);

            // Just return the original and forget about following redirects.
            return original;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException ex) {
                    Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * GZIP the "input" file and save to "output".
     *
     * @param input
     * @param output Will be overwritten if it already exists.
     */
    public static void gzip(final File input, final File output) {
        try {
            output.delete();
            output.createNewFile();
        } catch (final IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "File not created: {0}", ex.getMessage());
        }

        try {
            final InputStream in = new FileInputStream(input);
            final OutputStream out = new GZIPOutputStream(new FileOutputStream(output));

            final byte[] buf = new byte[5000];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
        } catch (final IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "GZIP failed: " + ex.getMessage(), ex);
        }
    }
}
