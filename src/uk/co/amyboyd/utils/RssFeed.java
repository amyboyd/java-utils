package uk.co.amyboyd.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Unknown
 */
public class RssFeed {
    final private String title;

    final private String link;

    final private String description;

    final private String language;

    final private String copyright;

    final private String pubDate;

    final private List<RssMessage> messages = new ArrayList<RssMessage>(16);

    public RssFeed(final String title, final String link, final String description,
            final String language, final String copyright, final String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
    }

    public boolean addMessage(final RssMessage message) {
        return this.messages.add(message);
    }

    public List<RssMessage> getMessages() {
        return messages;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    @Override
    public String toString() {
        return "Feed [copyright=" + copyright + ", description=" + description + ", language="
                + language + ", link=" + link + ", pubDate=" + pubDate + ", title=" + title + "]";
    }
}
