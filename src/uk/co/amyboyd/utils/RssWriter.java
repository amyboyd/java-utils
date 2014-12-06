package uk.co.amyboyd.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * @author Unknown
 */
public class RssWriter {
    private static final XMLEventFactory eventFactory = XMLEventFactory.newInstance();

    private RssFeed feed;

    private XMLEvent lineEnd;

    private XMLEvent tab;

    public RssWriter(final RssFeed feed) {
        this.feed = feed;
        setWhitespace(true);
    }

    public void setWhitespace(final boolean enable) {
        if (enable) {
            lineEnd = eventFactory.createDTD("\n");
            tab = eventFactory.createDTD("\t");
        } else {
            lineEnd = eventFactory.createDTD("");
            tab = eventFactory.createDTD("");
        }
    }

    public void setFeed(final RssFeed feed) {
        this.feed = feed;
    }

    public void write(final File outputFile) throws Exception {
        final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        final XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(outputFile));

        write(eventWriter);
    }

    public void write(final OutputStream stream) throws Exception {
        final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        final XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(stream);

        write(eventWriter);
    }

    private void write(final XMLEventWriter eventWriter) throws Exception {
        // Create and write Start Tag.
        final StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        eventWriter.add(lineEnd);

        final StartElement rssStart = eventFactory.createStartElement("", "", "rss");
        eventWriter.add(rssStart);
        eventWriter.add(eventFactory.createAttribute("version", "2.0"));
        eventWriter.add(lineEnd);

        eventWriter.add(eventFactory.createStartElement("", "", "channel"));
        eventWriter.add(lineEnd);

        createNode(eventWriter, "title", feed.getTitle());
        createNode(eventWriter, "link", feed.getLink());
        createNode(eventWriter, "description", feed.getDescription());
        createNode(eventWriter, "language", feed.getLanguage());
        createNode(eventWriter, "copyright", feed.getCopyright());
        createNode(eventWriter, "pubdate", feed.getPubDate());

        for (final RssMessage message: feed.getMessages()) {
            eventWriter.add(eventFactory.createStartElement("", "", "item"));
            eventWriter.add(lineEnd);
            createNode(eventWriter, "title", message.getTitle());
            createNode(eventWriter, "description", message.getDescription());
            createNode(eventWriter, "link", message.getLink());
            createNode(eventWriter, "author", message.getAuthor());
            createNode(eventWriter, "guid", message.getGuid());
            eventWriter.add(lineEnd);
            eventWriter.add(eventFactory.createEndElement("", "", "item"));
            eventWriter.add(lineEnd);

        }

        eventWriter.add(lineEnd);
        eventWriter.add(eventFactory.createEndElement("", "", "channel"));
        eventWriter.add(lineEnd);
        eventWriter.add(eventFactory.createEndElement("", "", "rss"));
        eventWriter.add(lineEnd);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }

    private void createNode(final XMLEventWriter eventWriter, final String name, final String value)
            throws XMLStreamException {
        final StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);

        final Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);

        final EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(lineEnd);
    }
}
