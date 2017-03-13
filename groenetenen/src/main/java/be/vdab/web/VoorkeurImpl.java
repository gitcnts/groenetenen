package be.vdab.web;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
class VoorkeurImpl implements Voorkeur, Serializable {
	private static final long serialVersionUID = 1L;
	
	private String foto;

	@Override
	public String getFoto() {
		return foto;
	}

	@Override
	public void setFoto(String foto) {
		this.foto = foto;
	}

}
