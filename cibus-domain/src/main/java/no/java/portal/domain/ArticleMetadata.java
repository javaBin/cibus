package no.java.portal.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.LocalDateTime;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class ArticleMetadata implements Serializable, Identifiable {

	private static final long serialVersionUID = 2002186875187838487L;

	private final int id;
    private final LocalDateTime created;
    private final LocalDateTime modified;

    public ArticleMetadata(int id, LocalDateTime created, LocalDateTime modified) {
        this.id = id;
        this.created = created;
        this.modified = modified;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("id", id).
            append("created", created).
            toString();
    }
    
    @Override
    public int hashCode() {
    	return id ^ (int) serialVersionUID;
    }
    
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof ArticleMetadata && ((ArticleMetadata) obj).id == id;
    }
    
}
