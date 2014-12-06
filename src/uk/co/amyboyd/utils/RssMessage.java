package uk.co.amyboyd.utils;

/**
 * @author Unknown
 */
public class RssMessage {
    private String title;

    private String description;

    private String link;

    private String author;

    private String guid;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }
}
