package be.vdab.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "werknemers")
public class Werknemer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String voornaam;
	private String familienaam;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "filiaalId")
	private Filiaal filiaal;
	private BigDecimal wedde;
	private long rijksregisterNr;

	// je maakt getters voor de niet-static private variabelen
	public long getId() {
		return id;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public String getFamilienaam() {
		return familienaam;
	}

	public Filiaal getFiliaal() {
		return filiaal;
	}

	public BigDecimal getWedde() {
		return wedde;
	}

	public long getRijksregisterNr() {
		return rijksregisterNr;
	}

	// je maakt met het Eclipse menu Source, Generate hashcode() and equals()
	// hashCode en equals op basis van rijksregisterNr
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (rijksregisterNr ^ (rijksregisterNr >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Werknemer)) {
			return false;
		}
		Werknemer other = (Werknemer) obj;
		return rijksregisterNr == other.rijksregisterNr;
	}

}
