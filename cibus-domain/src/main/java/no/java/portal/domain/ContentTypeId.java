package no.java.portal.domain;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public enum ContentTypeId {
    on_article(1),
    menuitem(2),
    imgbagitem(3),
    folderitem(4),
    on_template_def(5),
    on_template_impl(6),
    on_template_elements(7),
    on_module(8),
    on_element_module(9),
    categoryitem(10),
    on_image(11),
    on_document(12),
    on_url(13),
    segmentitem(14),
    textlabel(15),
    onp_image(16),
    documentfolder(17);

    public final int id;

    ContentTypeId(int id) {
        this.id = id;
    }
}
