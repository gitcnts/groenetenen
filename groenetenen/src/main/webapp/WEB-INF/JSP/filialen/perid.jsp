<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<%@taglib prefix='v' uri='http://vdab.be/tags'%>
<!doctype html>
<html lang='nl'>
<head>
<v:head title='Filialen per id' />
</head>
<body>
	<v:menu />
	<h1>Filialen per id</h1>
	<form id='zoekForm'>
		<label>Id: <input id='filiaalId' required type='number'
			min='1' autofocus /></label> <input type='submit' value='Zoeken'>
	</form>
	<dl>
		<dt>Naam</dt>
		<dd id='naam'></dd>
		<dt>Adres</dt>
		<dd id='adres'></dd>
	</dl>
	<c:url value='/filialen' var='url' />
	<!-- URL van alle filialen bijhouden in variabele “url” -->
	<script>
		<!-- bij submit van “zoekform” wordt de javascript functie “zoekFiliaal” uitgevoerd -->
		document.getElementById('zoekForm').onsubmit = zoekFiliaal; 
		function zoekFiliaal() {
			<!-- om HTTP requests te versturen in javascript -->
			var request = new XMLHttpRequest();
			<!-- je maakt een GET request naar de URL van het filiaal van de ingevulde id -->
			<!-- URL = URL van alle filialen, gevolgd door /, gevolgd door value van textbox “filiaalId” -->
			<!-- true zorgt ervoor dat de browser het request verstuurt als achtergrondtaak -->

			request.open(
				"GET", '${url}' + '/' + document.getElementById('filiaalId').value, true);
			<!-- Je plaats de request header accept op application/json -->
			request.setRequestHeader('accept', 'application/json'); 
			<!-- Als het response binnenkomt moet de javascript functie “responseIsBinnengekomen” -->
			<!-- uitgevoerd worden -->
			request.onload = responseIsBinnengekomen;
			request.send();<!-- request wordt verstuurd (=AJAX request) -->
			return false; <!-- Hiermee voorkom je dat de form gesubmit wordt -->
		}
		function responseIsBinnengekomen() {
			switch (this.status) {<!-- Je controleert de response status -->
				case 200: <!-- alles OK -->
					<!-- response body wordt geconverteerd naar javascript object -->
					var filiaalResource = JSON.parse(this.responseText);
					<!-- je zoekt in dit javascript object de eigenschap filiaal -->
					<!-- filiaal is ook een javascript object met filiaal eigenschappen (id, naam, -->
					var filiaal = filiaalResource.filiaal;
					<!-- je zoekt in de pagina het element met id “naam”  -->
					<!-- innerHTML stelt de tekst voor tussen <dd> en </dd> -->
					<!-- je vervangt deze tekts door de naam van het filiaal -->
					document.getElementById('naam').innerHTML = filiaal.naam;
					var adres = filiaal.adres;
					document.getElementById('adres').innerHTML = adres.straat + 
						+ adres.huisNr + ' ' + adres.postcode + ' ' 
						+ adres.gemeente;
					break;
				case 404: <!-- not found -->
					alert('Filiaal bestaat niet'); <!-- popup venster met foutmelding -->
					break;
				default: <!-- status niet 200 en niet 404 -->
					alert("Technisch probleem");
			}
		}
	</script>
</body>
</html>
