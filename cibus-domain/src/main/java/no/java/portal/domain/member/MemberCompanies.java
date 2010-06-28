package no.java.portal.domain.member;

import java.util.Map;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public interface MemberCompanies {

    Map<Integer, MemberCompany> getMemberCompanyMap();

    MemberCompany find(long id);
    
}
