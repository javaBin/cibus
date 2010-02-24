package no.java.portal.domain.service;

import no.java.portal.domain.member.Member.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public interface CompanyService {
    void addCompany(Company company);

    void addAdministrator(CompanyId companyId, MembershipNo member);

    void removeAdministrator(CompanyId companyId, MembershipNo member);

    class Company {
    }

    class CompanyId {
    }
}
