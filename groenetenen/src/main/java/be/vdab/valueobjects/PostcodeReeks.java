package be.vdab.valueobjects;

public class PostcodeReeks {

	private final static int MIN_POSTCODE = 1000;
	private final static int MAX_POSTCODE = 9999;

	private Integer vanpostcode;
	private Integer totpostcode;

	// Je maakt getters en setters voor de private variabelen
	public Integer getVanpostcode() {
		return vanpostcode;
	}

	public void setVanpostcode(Integer vanpostcode) {
		valideer(vanpostcode);
		this.vanpostcode = vanpostcode;
	}

	public Integer getTotpostcode() {
		return totpostcode;
	}

	public void setTotpostcode(Integer totpostcode) {
		valideer(totpostcode);
		this.totpostcode = totpostcode;
	}

	private void valideer(Integer postcode) {
		if (postcode < MIN_POSTCODE || postcode > MAX_POSTCODE) {
			throw new IllegalArgumentException("Ongeldige waarde voor postcode");
		}
	}

	public boolean bevat(Integer postcode) {
		// bevat reeks een bepaalde postcode ? (gebruikt in repository layer)
		return postcode >= vanpostcode && postcode <= totpostcode;
	}

}
