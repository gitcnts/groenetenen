package be.vdab.restservices;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

	@Override
	public LocalDate unmarshal(String string) throws Exception {
		return LocalDate.parse(string);
	}

	@Override
	public String marshal(LocalDate date) throws Exception {
		return date.toString();
	}

}
