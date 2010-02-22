package no.java.portal.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Mar 4, 2009
 */
public class SubscribedCategory implements Serializable {

    private static final long serialVersionUID = 8094595603696336819L;

    private final int categoryId;
	private final String description;
	private final boolean selected;

	public boolean isSelected() {
		return selected;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getDescription() {
		return description;
	}

	public SubscribedCategory(int categoryId, String description, boolean selected) {
		this.categoryId = categoryId;
		this.description = description;
		this.selected = selected;
	}

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
