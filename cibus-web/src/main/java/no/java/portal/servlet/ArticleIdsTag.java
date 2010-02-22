package no.java.portal.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;

import no.java.portal.domain.ArticleMetadata;
import no.java.portal.domain.ArticleMetadatas;
import no.java.portal.domain.Category;

/**
 * Get article id list for category, offset and length
 *
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since 6. jan. 2009
 */
public class ArticleIdsTag extends CibusSimpleTagSupport {

    private int offset = 0;
    private int length = 1;
    private String var;
    private Category category;

    @Override
    public void doTag() throws JspException, IOException {
    	ArticleMetadatas articleMetaDatas = getBean("articleMetadatas", ArticleMetadatas.class);
        List<ArticleMetadata> amds = articleMetaDatas.getActiveArticleIdsByCategory(
            Arrays.asList(category), offset, length);
        List<Integer> aIds = new ArrayList<Integer>();
        for (ArticleMetadata amd : amds) {
            aIds.add(amd.getId());
        }
        getJspContext().setAttribute(var, aIds);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLength(int length) {
        this.length = length;
    }
}