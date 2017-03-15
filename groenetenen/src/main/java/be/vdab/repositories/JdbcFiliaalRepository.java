package be.vdab.repositories;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import be.vdab.entities.Filiaal;
import be.vdab.valueobjects.Adres;
import be.vdab.valueobjects.PostcodeReeks;

@Repository
class JdbcFiliaalRepository implements FiliaalRepository {
	// private final Map<Long, Filiaal> filialen = new ConcurrentHashMap<>();
	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private final SimpleJdbcInsert simpleJdbcInsert;

	private final RowMapper<Filiaal> rowMapper = (resultSet, rowNum) -> new Filiaal(resultSet.getLong("id"),
			resultSet.getString("naam"), resultSet.getBoolean("hoofdFiliaal"), resultSet.getBigDecimal("waardeGebouw"),
			resultSet.getDate("inGebruikName").toLocalDate(), new Adres(resultSet.getString("straat"),
					resultSet.getString("huisNr"), resultSet.getInt("postcode"), resultSet.getString("gemeente")));

	private static final String BEGIN_SQL = "select id, naam, hoofdFiliaal, straat, huisNr, postcode, gemeente,"
			+ "inGebruikName, waardeGebouw from filialen ";
	private static final String SQL_FIND_ALL = BEGIN_SQL + "order by naam";
	private static final String SQL_FIND_BY_POSTCODE = BEGIN_SQL + "where postcode between :van and :tot order by naam";
	private static final String SQL_READ = BEGIN_SQL + " where id = :id";
	private static final String SQL_FIND_AANTAL_FILIALEN = "select count(*) from filialen";
	private static final String SQL_FIND_AANTAL_WERKNEMERS = "select count(*) from werknemers where filiaalId = ?";
	private static final String SQL_DELETE = "delete from filialen where id = ?";
	private static final String SQL_UPDATE = "update filialen set naam=:naam,hoofdFiliaal=:hoofdFiliaal, straat=:straat,"
			+ "huisNr=:huisNr, postcode=:postcode, gemeente=:gemeente, "
			+ "inGebruikName=:inGebruikName, waardeGebouw=:waardeGebouw where id = :id";

	// InMemoryFiliaalRepository() {
	// filialen.put(1L, new Filiaal(1, "Andros", true, BigDecimal.valueOf(1000),
	// LocalDate.now(),
	// new Adres("Keizerslaan", "11", 1000, "Brussel")));
	// filialen.put(2L, new Filiaal(2, "Delos", false, BigDecimal.valueOf(2000),
	// LocalDate.now(),
	// new Adres("Gasthuisstraat", "31", 1000, "Brussel")));
	// filialen.put(3L, new Filiaal(3, "Gavdos", false,
	// BigDecimal.valueOf(3000), LocalDate.now(),
	// new Adres("Koestraat", "44", 9700, "Oudenaarde")));
	// }

	JdbcFiliaalRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		simpleJdbcInsert.withTableName("filialen");
		simpleJdbcInsert.usingGeneratedKeyColumns("id");
	}

	// @Override
	// public void create(Filiaal filiaal) {
	// filiaal.setId(Collections.max(filialen.keySet()) + 1);
	// filialen.put(filiaal.getId(), filiaal);
	// }
	@Override
	public void create(Filiaal filiaal) {
		Map<String, Object> kolomWaarden = new HashMap<>();
		kolomWaarden.put("naam", filiaal.getNaam());
		kolomWaarden.put("hoofdFiliaal", filiaal.isHoofdFiliaal());
		kolomWaarden.put("straat", filiaal.getAdres().getStraat());
		kolomWaarden.put("huisNr", filiaal.getAdres().getHuisNr());
		kolomWaarden.put("postcode", filiaal.getAdres().getPostcode());
		kolomWaarden.put("gemeente", filiaal.getAdres().getGemeente());
		kolomWaarden.put("inGebruikName", Date.valueOf(filiaal.getInGebruikName()));
		kolomWaarden.put("waardeGebouw", filiaal.getWaardeGebouw());
		Number id = simpleJdbcInsert.executeAndReturnKey(kolomWaarden);
		filiaal.setId(id.longValue());
	}

	// @Override
	// public Optional<Filiaal> read(long id) {
	// return Optional.ofNullable(filialen.get(id));
	// }

	@Override
	public Optional<Filiaal> read(long id) {
		Map<String, Long> parameters = Collections.singletonMap("id", id);
		try {
			return Optional.of(namedParameterJdbcTemplate.queryForObject(SQL_READ, parameters, rowMapper));
		} catch (IncorrectResultSizeDataAccessException ex) {
			return Optional.empty(); // record niet gevonden
		}
	}

	// @Override
	// public void update(Filiaal filiaal) {
	// filialen.put(filiaal.getId(), filiaal);
	// }

	@Override
	public void update(Filiaal filiaal) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("id", filiaal.getId());
		parameters.put("naam", filiaal.getNaam());
		parameters.put("hoofdFiliaal", filiaal.isHoofdFiliaal());
		parameters.put("straat", filiaal.getAdres().getStraat());
		parameters.put("huisNr", filiaal.getAdres().getHuisNr());
		parameters.put("postcode", filiaal.getAdres().getPostcode());
		parameters.put("gemeente", filiaal.getAdres().getGemeente());
		parameters.put("inGebruikName", Date.valueOf(filiaal.getInGebruikName()));
		parameters.put("waardeGebouw", filiaal.getWaardeGebouw());
		namedParameterJdbcTemplate.update(SQL_UPDATE, parameters);
	}

	// @Override
	// public void delete(long id) {
	// filialen.remove(id);
	// }

	@Override
	public void delete(long id) {
		jdbcTemplate.update(SQL_DELETE, id);
	}

	// @Override
	// public List<Filiaal> findAll() {
	// return new ArrayList<>(filialen.values());
	// }

	@Override
	public List<Filiaal> findAll() {
		return jdbcTemplate.query(SQL_FIND_ALL, rowMapper);
	}

	// @Override
	// public long findAantalFilialen() {
	// return filialen.size();
	// }
	@Override
	public long findAantalFilialen() {
		return jdbcTemplate.queryForObject(SQL_FIND_AANTAL_FILIALEN, Long.class);
	}

	// @Override
	// public long findAantalWerknemers(long id) {
	// return id == 1L ? 7L : 0L;
	// }
	@Override
	public long findAantalWerknemers(long id) {
		return jdbcTemplate.queryForObject(SQL_FIND_AANTAL_WERKNEMERS, Long.class, id);
	}

	// bevat() geeft true als postcode binnen de reeks valt (method in class
	// PostcodeReeks)
	// @Override
	// public List<Filiaal> findByPostcodeReeks(PostcodeReeks reeks) {
	// return filialen.values().stream().filter(filiaal ->
	// reeks.bevat(filiaal.getAdres().getPostcode()))
	// .collect(Collectors.toList());
	// }

	@Override
	public List<Filiaal> findByPostcodeReeks(PostcodeReeks reeks) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("van", reeks.getVanpostcode());
		parameters.put("tot", reeks.getTotpostcode());
		return namedParameterJdbcTemplate.query(SQL_FIND_BY_POSTCODE, parameters, rowMapper);
	}

}
