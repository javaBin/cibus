package no.java.portal.servlet;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import no.java.portal.domain.ArticleMetadatas;
import no.java.portal.domain.Articles;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class StaticPageTag extends SimpleTagSupport {

    private int articleId;

    private String var;

    @Override
    public void doTag() throws JspException, IOException {

        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(((PageContext) getJspContext()).getServletContext());

        Articles articles = (Articles) applicationContext.getBean("articles");
        ArticleMetadatas articleMetaDatas = (ArticleMetadatas) applicationContext.getBean("articleMetadatas");

        getJspContext().setAttribute(var, articles.getStaticPage(articleMetaDatas.getArticle(articleId)));
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
