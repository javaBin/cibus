package no.java.portal.domain.member;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public class MemberPerson {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String phoneNumber;
    private final String address;
    private final String email;
    private final MemberCompany memberCompany;

    public MemberPerson(int id, String firstName, String lastName, String password, String phoneNumber, String address, String email, MemberCompany memberCompany) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.memberCompany = memberCompany;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return firstName.trim() + " " + lastName.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
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

