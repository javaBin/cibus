package no.java.portal.domain.member;

import java.util.List;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public interface MemberPeople {

    List<MemberPerson> getCurrentMemberPeople();

    MemberPerson findByNameAndPassword(String userName, String password);
    
}
