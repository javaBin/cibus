package no.java.portal.domain.member;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public class MemberPerson {

    private final int id;
    private final Integer onpId;
    private final String name;
    private final boolean hidden;
    private final MemberCompany memberCompany;

    public MemberPerson(int id, Integer onpId, MemberCompany memberCompany, String name, boolean hidden) {
        this.id = id;
        this.onpId = onpId;
        this.memberCompany = memberCompany;
        this.name = name;
        this.hidden = hidden;
    }

    public int getId() {
        return id;
    }

    public Integer getOnpId() {
        return onpId;
    }

    public String getName() {
        return name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public MemberCompany getMemberCompany() {
        return memberCompany;
    }

    public boolean isContactPerson() {
        return memberCompany != null && id == memberCompany.getContactPersonId();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

