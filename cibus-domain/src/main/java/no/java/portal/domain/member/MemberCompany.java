package no.java.portal.domain.member;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public class MemberCompany {

    private final int id;
    private final String name;
    private final int contactPersonId;

    public MemberCompany(int id, String name, int contactPersonId) {
        this.id = id;
        this.name = name;
        this.contactPersonId = contactPersonId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getContactPersonId() {
        return contactPersonId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
