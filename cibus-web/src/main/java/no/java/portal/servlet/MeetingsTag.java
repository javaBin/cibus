package no.java.portal.servlet;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import no.java.portal.domain.Category;
import no.java.portal.domain.Meetings;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author <a href="mailto:martebk@tihlde.org">Marte Kjenstad</a>
 *         Date: 19.des.2008 - Time: 15:02:45
 *
 * TODO Copy / paste fra NewsItemTag, eneste forskjell er at denne setter et Meetingobject i stedet for et NewItem - objekt...
 */
public class MeetingsTag extends SimpleTagSupport{

    private int year;

    private String var;

	private Category category;

    @Override
    public void doTag() throws JspException, IOException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(((PageContext) getJspContext()).getServletContext());

        Meetings meetings = (Meetings) applicationContext.getBean("meetings");

        getJspContext().setAttribute(var, meetings.getMeetings(category, year));
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setVar(String var) {
        this.var = var;
    }

	public void setCategory(Category category) {
		this.category = category;
	}
}
