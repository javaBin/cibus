package no.java.portal.domain.member;

import java.util.List;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public interface MemberPeople {

    List<MemberPerson> getCurrentMemberPeople();

    MemberPerson findByEMailAndPassword(String userName, String password);

    MemberPerson findByEMail(String email);

    void save(MemberPerson memberPerson);
}
