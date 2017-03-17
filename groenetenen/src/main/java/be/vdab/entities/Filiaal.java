package be.vdab.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import be.vdab.valueobjects.Adres;

@Entity
@Table(name = "filialen")
public class Filiaal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank
	@Length(min = 1, max = 50)
	@SafeHtml
	private String naam;
	private boolean hoofdFiliaal;
	@NumberFormat(style = Style.NUMBER)
	@NotNull
	@Min(0)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal waardeGebouw;
	@DateTimeFormat(style = "S-") // dd-mm-jj
	@NotNull
	private LocalDate inGebruikName;
	@Valid
	@Embedded
	private Adres adres;
	@OneToMany(mappedBy = "filiaal")
	private Set<Werknemer> werknemers;
	@Version
	private long versie;

	public Filiaal() {
	}

	public Filiaal(String naam, boolean hoofdFiliaal, BigDecimal waardeGebouw, LocalDate inGebruikName, Adres adres) {
		this.naam = naam;
		this.hoofdFiliaal = hoofdFiliaal;
		this.waardeGebouw = waardeGebouw;
		this.inGebruikName = inGebruikName;
		this.adres = adres;
	}

	public Filiaal(long id, String naam, boolean hoofdFiliaal, BigDecimal waardeGebouw, LocalDate inGebruikName,
			Adres adres) {
		this(naam, hoofdFiliaal, waardeGebouw, inGebruikName, adres);
		this.id = id;
	}

	// Je maakt getters en setters voor de private variabelen
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public boolean isHoofdFiliaal() {
		return hoofdFiliaal;
	}

	public void setHoofdFiliaal(boolean hoofdFiliaal) {
		this.hoofdFiliaal = hoofdFiliaal;
	}

	public BigDecimal getWaardeGebouw() {
		return waardeGebouw;
	}

	public void setWaardeGebouw(BigDecimal waardeGebouw) {
		this.waardeGebouw = waardeGebouw;
	}

	public LocalDate getInGebruikName() {
		return inGebruikName;
	}

	public void setInGebruikName(LocalDate inGebruikName) {
		this.inGebruikName = inGebruikName;
	}

	public Adres getAdres() {
		return adres;
	}

	public void setAdres(Adres adres) {
		this.adres = adres;
	}

	public Set<Werknemer> getWerknemers() {
		return Collections.unmodifiableSet(werknemers);
	}

}