package no.java.portal.servlet;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import no.java.portal.domain.ArticleMetadatas;
import no.java.portal.domain.Meetings;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author <a href="mailto:martebk@tihlde.org">Marte Kjenstad</a>
 *         Date: 19.des.2008 - Time: 15:02:45
 *
 * TODO Copy / paste fra NewsItemTag, eneste forskjell er at denne setter et Meetingobject i stedet for et NewItem - objekt...
 */
public class MeetingTag extends SimpleTagSupport{

    private int articleId;

    private String var;

    @Override
    public void doTag() throws JspException, IOException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(((PageContext) getJspContext()).getServletContext());

        Meetings meetings = (Meetings) applicationContext.getBean("meetings");
        ArticleMetadatas articleMetaDatas = (ArticleMetadatas) applicationContext.getBean("articleMetadatas");

        getJspContext().setAttribute(var, meetings.getMeeting(articleMetaDatas.getArticle(articleId)));
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
