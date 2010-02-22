package no.java.portal.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author <a href="mailto:martebk@tihlde.org">Marte Kjenstad</a>
 *         Date: 17.des.2008 - Time: 14:24:04
 */
public class Meeting implements Serializable, Identifiable {

	private static final long serialVersionUID = 591767662861881429L;

	private final ArticleMetadata metadata;
    private final String title;
    private final String time;
    private final String ingress;
    private final String text;
    private final String place;
    private final String publicUrl;

    public Meeting(ArticleMetadata metadata, String title, String time, String ingress, String text, String place, String publicUrl) {
        this.metadata = metadata;
        this.title = title;
        this.time = time;
        this.ingress = ingress;
        this.text = text;
        this.place = place;
        this.publicUrl = publicUrl;
    }

    public ArticleMetadata getMetadata() {
        return metadata;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getIngress() {
        return ingress;
    }

    public String getText() {
        return text;
    }

    public String getPlace() {
        return place;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("metadata", metadata).
            append("title", title).
            append("time", time).
            append("ingress", ingress).
            append("text", text).
            append("place", place).
            toString();
    }
    
    @Override
    public int hashCode() {
    	return metadata.hashCode() ^ (int) serialVersionUID;
    }

    @Override
    public boolean equals(Object obj) {
    	return obj instanceof Meeting && ((Meeting) obj).metadata.equals(metadata);
    }
    
	public int getId() {
		return metadata.getId();
	}

}
