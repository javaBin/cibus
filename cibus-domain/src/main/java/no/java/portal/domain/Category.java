package no.java.portal.domain;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public enum Category {

    nyheter(26, null),
    moter_oslo(35, "Oslo"),
    moter_trondheim(261, "Trondheim"),
    moter_bergen(260, "Bergen"),
    moter_sorlandet(778, "Sørlandet"),
    moter_tromso(806, "Tromsø"),
    moter_stavanger(39, "Stavanger"),
    nyheter_partnere(105, null),
    styret_stavanger(347, null),
    styret_bergen(281, null),
    styret_trondheim(282, null),
    styret_tromso(808, null),
    styret_sorlandet(804, null),
    styret_admin(218, null),
    styret_mote(192, null),
    javazone_program(314, null),
    javazone_admin(204, null),
    foredragsholdere(70, null),
    styret_portal(193, null), 
    partnerinfo(1128, null),
    stillingsannonser(234, null);

    private int id;
	private String name;

    Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
