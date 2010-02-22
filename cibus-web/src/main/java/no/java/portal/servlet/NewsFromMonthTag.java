package no.java.portal.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

import no.java.portal.domain.Category;
import no.java.portal.domain.Articles;

/**
 * @author Nina Heitmann
 *         Date: Feb 3, 2009
 */
public class NewsFromMonthTag extends SimpleTagSupport {

    private Category category;
    private int year;
    private int month;
    private String var;

    @Override
    public void doTag() throws JspException, IOException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(((PageContext) getJspContext()).getServletContext());

        Articles articles = (Articles) applicationContext.getBean("articles");

        getJspContext().setAttribute(var, articles.getNewsItems(category, year, month));
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
