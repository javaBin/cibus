package no.java.portal.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

//Må finnes et bedre navn på helter in english???
public class Hero implements Serializable, Identifiable {

	private static final long serialVersionUID = -5510303652399232001L;
	
	private ArticleMetadata metadata;
    private String navn;
    private String email;
    private String kallenavn;
    private String firmaURL;
    private String firma;
    private String javaInteresser;
    private String hjemmeside;
    private String profesjon;
    private String bilde;
    private String heltedato;
    
    private String favorittURL1;
    private String favorittURL1Tekst;
    private String favorittURL2;
    private String favorittURL2Tekst;
    private String favorittURL3;
    private String favorittURL3Tekst;

    private String beskrivelse;

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public Hero(ArticleMetadata metadata, String navn, String email, String kallenavn, String firmaURL, String firma,
                String javaInteresser, String hjemmeside, String profesjon, String bilde, String heltedato,
                String favorittURL1, String favorittURL1Tekst, String favorittURL2, String favorittURL2Tekst,
                String favorittURL3, String favorittURL3Tekst, String beskrivelse) {
        this.metadata = metadata;
        this.navn = navn;
        this.email = email;
        this.kallenavn = kallenavn;
        this.firmaURL = firmaURL;
        this.firma = firma;
        this.javaInteresser = javaInteresser;
        this.hjemmeside = hjemmeside;
        this.profesjon = profesjon;
        this.bilde = bilde;
        this.heltedato = heltedato;
        this.favorittURL1 = favorittURL1;
        this.favorittURL1Tekst = favorittURL1Tekst;
        this.favorittURL2 = favorittURL2;
        this.favorittURL2Tekst = favorittURL2Tekst;
        this.favorittURL3 = favorittURL3;
        this.favorittURL3Tekst = favorittURL3Tekst;
        this.beskrivelse = beskrivelse;
    }

    public ArticleMetadata getMetadata() {
        return metadata;

    }

    public void setMetadata(ArticleMetadata metadata) {
        this.metadata = metadata;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKallenavn() {
        return kallenavn;
    }

    public void setKallenavn(String kallenavn) {
        this.kallenavn = kallenavn;
    }

    public String getFirmaURL() {
        return firmaURL;
    }

    public void setFirmaURL(String firmaURL) {
        this.firmaURL = firmaURL;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getJavaInteresser() {
        return javaInteresser;
    }

    public void setJavaInteresser(String javaInteresser) {
        this.javaInteresser = javaInteresser;
    }

    public String getHjemmeside() {
        return hjemmeside;
    }

    public void setHjemmeside(String hjemmeside) {
        this.hjemmeside = hjemmeside;
    }

    public String getProfesjon() {
        return profesjon;
    }

    public void setProfesjon(String profesjon) {
        this.profesjon = profesjon;
    }

    public String getBilde() {
        return bilde;
    }

    public void setBilde(String bilde) {
        this.bilde = bilde;
    }

    public String getHeltedato() {
        return heltedato;
    }

    public void setHeltedato(String heltedato) {
        this.heltedato = heltedato;
    }

    public String getFavorittURL1() {
        return favorittURL1;
    }

    public void setFavorittURL1(String favorittURL1) {
        this.favorittURL1 = favorittURL1;
    }

    public String getFavorittURL1Tekst() {
        return favorittURL1Tekst;
    }

    public void setFavorittURL1Tekst(String favorittURL1Tekst) {
        this.favorittURL1Tekst = favorittURL1Tekst;
    }

    public String getFavorittURL2() {
        return favorittURL2;
    }

    public void setFavorittURL2(String favorittURL2) {
        this.favorittURL2 = favorittURL2;
    }

    public String getFavorittURL2Tekst() {
        return favorittURL2Tekst;
    }

    public void setFavorittURL2Tekst(String favorittURL2Tekst) {
        this.favorittURL2Tekst = favorittURL2Tekst;
    }

    public String getFavorittURL3() {
        return favorittURL3;
    }

    public void setFavorittURL3(String favorittURL3) {
        this.favorittURL3 = favorittURL3;
    }

    public String getFavorittURL3Tekst() {
        return favorittURL3Tekst;
    }

    public void setFavorittURL3Tekst(String favorittURL3Tekst) {
        this.favorittURL3Tekst = favorittURL3Tekst;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
    	return metadata.hashCode() ^ (int) serialVersionUID;
    }

    @Override
    public boolean equals(Object obj) {
    	return obj instanceof Hero && ((Hero) obj).metadata.equals(metadata);
    }

	public int getId() {
		return metadata.getId();
	}

}
