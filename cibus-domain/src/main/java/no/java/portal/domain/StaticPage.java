package no.java.portal.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Nina Heitmann
 */
public class StaticPage implements Serializable, Identifiable {
	
	private static final long serialVersionUID = -4293725537195277663L;

	private ArticleMetadata metadata;
    private String title;
    private String body;

    public StaticPage(String body, String title, ArticleMetadata metadata) {
        this.body = body;
        this.title = title;
        this.metadata = metadata;
    }

    public ArticleMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ArticleMetadata metadata) {
        this.metadata = metadata;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("metadata", metadata).
            append("title", title).
            append("body", body).toString();
    }
    
    @Override
    public int hashCode() {
    	return metadata.hashCode() ^ (int) serialVersionUID;
    }

    @Override
    public boolean equals(Object obj) {
    	return obj instanceof StaticPage && ((StaticPage) obj).metadata.equals(metadata);
    }

	public int getId() {
		return metadata.getId();
	}

}
