package no.java.portal.domain;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class NewsItem implements Serializable, Identifiable {

    private static final long serialVersionUID = 5241550286945817354L;

    private final ArticleMetadata metadata;
    private final String title;
    private final String author;
    private final String ingress;
    private final String text;
    private final String source;
    private final String seeAlso;
    private final String publicUrl;
    private final List<String> otherUrls;

    public NewsItem(ArticleMetadata metadata, String title, String author, String ingress, String text, String source,
            String seeAlso, String publicUrl, List<String> otherUrls) {
        this.metadata = metadata;
        this.title = title;
        this.author = author;
        this.ingress = ingress;
        this.text = text;
        this.source = source;
        this.seeAlso = seeAlso;
        this.publicUrl = publicUrl;
        this.otherUrls = otherUrls;
    }

    public ArticleMetadata getMetadata() {
        return metadata;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIngress() {
        return ingress;
    }

    public String getText() {
        return text;
    }

    public String getSource() {
        return source;
    }

    public String getSeeAlso() {
        return seeAlso;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("metadata", metadata).
            append("title", title).
            append("author", author).
            append("ingress", ingress).
            append("source", source).
            append("seeAlso", seeAlso).
            append("publicUrl", publicUrl).
            append("otherUrls", otherUrls).
            toString();
    }

    @Override
    public int hashCode() {
        return metadata.hashCode() ^ (int) serialVersionUID;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NewsItem && ((NewsItem) obj).metadata.equals(metadata);
    }

    public int getId() {
        return metadata.getId();
    }

    public List<String> getOtherUrls() {
        return otherUrls;
    }

}
