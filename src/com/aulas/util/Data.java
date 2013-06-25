package com.aulas.util;

/* ----------------------------------------------- *
 *				Marco Aurélio Gerosa               *
 * ----------------------------------------------- *
 
 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Importação e adaptação da classe
 * 
 */
 
import java.util.*;

public class Data {
	
public static String agora () throws Exception {
	// retorna a data e hora atual no formato dd/mm/aaaa hh:mm:ss

	String ret = "";

	// PS: Gambiarra para dar certo: somar um no mes e subtrair um na hora
	
	GregorianCalendar calendar = new GregorianCalendar ();

	String dia = Integer.toString(calendar.get(calendar.DAY_OF_MONTH));
	if (dia.length() == 1) {dia = "0" + dia;}

	String mes = Integer.toString(calendar.get(calendar.MONTH)+1);
	if (mes.length() == 1) {mes = "0" + mes;}

	String ano = Integer.toString(calendar.get(calendar.YEAR));

	String hora = Integer.toString(calendar.get(calendar.HOUR_OF_DAY));
	if (hora.length() == 1) {hora = "0" + hora;}
		
	String min = Integer.toString(calendar.get(calendar.MINUTE));
	if (min.length() == 1) {min = "0" + min;}

	String seg = Integer.toString(calendar.get(calendar.SECOND));
	if (seg.length() == 1) {seg = "0" + seg;}

	ret = dia + "/" + mes + "/" + ano + " " + hora + ":" + min + ":" + seg;

	return ret;
	
}
private static boolean ehnumero (String n) throws Exception {
	return  n.equals("0") ||
			n.equals("1") ||
			n.equals("2") ||
			n.equals("3") ||
			n.equals("4") ||
			n.equals("5") ||
			n.equals("6") ||
			n.equals("7") ||
			n.equals("8") ||
			n.equals("9");
}
public static String formataData(String data) throws Exception {
	// retira os segundos da data e completa com zeros
	// a data de entrada deve estar no formato "dd/mm/aaaa hh:mm:ss" com qq separador simples

	int pos = 0;
	String dia, mes, ano, hor, min, seg;

	if (data == null) {
		return "";
	}
	
	// dia
	dia = "";
	while (ehnumero(data.substring(pos,pos+1))) {
		dia += data.charAt(pos);
		pos++;
	}
	if (dia.length() <= 1) {dia = "0" + dia;}

	// mes
	mes = ""; pos++;
	while (ehnumero(data.substring(pos,pos+1))) {
		mes += data.charAt(pos);
		pos++;
	}
	if (mes.length() <= 1) {mes = "0" + mes;}
	
	// ano
	ano = ""; pos++;
	while (ehnumero(data.substring(pos,pos+1))) {
		ano += data.charAt(pos);
		pos++;
	}

	// hor
	hor = ""; pos++;
	while (ehnumero(data.substring(pos,pos+1))) {
		hor += data.charAt(pos);
		pos++;
	}
	if (hor.length() <= 1) {hor = "0" + hor;}

	// min
	min = ""; pos++;
	while (ehnumero(data.substring(pos,pos+1))) {
		min += data.charAt(pos);
		pos++;
	}
	if (min.length() <= 1) {min = "0" + min;}

	// seg
	/* // esta pegando fora do limite
	seg = ""; pos++;
	while (ehnumero(data.substring(pos,pos+1))) {
		seg += data.charAt(pos);
		pos++;
	}
	if (seg.length() <= 1) {seg = "0" + seg;}*/

	return dia + "/" + mes + "/" + ano + " " + hor + ":" + min;
}
public static String formataData(Date data) throws Exception {
	// retira os segundos da data e completa com zeros

	if (data == null) {
		return "";
	}

	GregorianCalendar calendar = new GregorianCalendar ();
	calendar.setTime(data);

	String dia = Integer.toString(calendar.get(calendar.DAY_OF_MONTH));
	if (dia.length() == 1) {dia = "0" + dia;}

	String mes = Integer.toString(calendar.get(calendar.MONTH)+1);
	if (mes.length() == 1) {mes = "0" + mes;}

	String ano = Integer.toString(calendar.get(calendar.YEAR));

	//String hora = Integer.toString(calendar.get(calendar.HOUR_OF_DAY));
	//if (hora.length() == 1) {hora = "0" + hora;}
		
	//String min = Integer.toString(calendar.get(calendar.MINUTE));
	//if (min.length() == 1) {min = "0" + min;}

	//String seg = Integer.toString(calendar.get(calendar.SECOND));
	//if (seg.length() == 1) {seg = "0" + seg;}

	//return dia + "/" + mes + "/" + ano + " " + hora + ":" + min + ":" + seg;
	return dia + "/" + mes + "/" + ano;

}
public static String formataDataHora(Date data) throws Exception {

	if (data == null) {
		return "";
	}

	GregorianCalendar calendar = new GregorianCalendar ();
	calendar.setTime(data);

	String dia = Integer.toString(calendar.get(calendar.DAY_OF_MONTH));
	if (dia.length() == 1) {dia = "0" + dia;}

	String mes = Integer.toString(calendar.get(calendar.MONTH)+1);
	if (mes.length() == 1) {mes = "0" + mes;}

	String ano = Integer.toString(calendar.get(calendar.YEAR));

	String hora = Integer.toString(calendar.get(calendar.HOUR_OF_DAY));
	if (hora.length() == 1) {hora = "0" + hora;}
		
	String min = Integer.toString(calendar.get(calendar.MINUTE));
	if (min.length() == 1) {min = "0" + min;}

	String seg = Integer.toString(calendar.get(calendar.SECOND));
	if (seg.length() == 1) {seg = "0" + seg;}

	return dia + "/" + mes + "/" + ano + " " + hora + ":" + min + ":" + seg;

}
public static String getAno(Date data) throws Exception {

	if (data == null) {
		return "";
	}

	GregorianCalendar calendar = new GregorianCalendar ();
	calendar.setTime(data);

	String ano = Integer.toString(calendar.get(calendar.YEAR));

	return ano;

}
public static String getDia(Date data) throws Exception {

	if (data == null) {
		return "";
	}

	GregorianCalendar calendar = new GregorianCalendar ();
	calendar.setTime(data);

	String dia = Integer.toString(calendar.get(calendar.DAY_OF_MONTH));
	if (dia.length() == 1) {dia = "0" + dia;}

	return dia;

}
public static String getMes(Date data) throws Exception {
	// retira os segundos da data e completa com zeros

	if (data == null) {
		return "";
	}

	GregorianCalendar calendar = new GregorianCalendar ();
	calendar.setTime(data);

	String mes = Integer.toString(calendar.get(calendar.MONTH)+1);
	if (mes.length() == 1) {mes = "0" + mes;}

	return mes;

}
public static Date novaData(int dia, int mes, int ano) throws Exception {

	if ((dia == 0) || (mes == 0) || (ano == 0)) {
		return null;
	}
	
	GregorianCalendar calendar = new GregorianCalendar (ano,mes-1,dia);
	return calendar.getTime();

}
public static Date novaData(String dia, String mes, String ano) throws Exception {

	if ((dia == null) || (dia.equals("")) || (mes == null) || (mes.equals("")) || (ano == null) || (ano.equals(""))) {
		return null;
	}

	int diaInt = Integer.parseInt(dia);
	int mesInt = Integer.parseInt(mes);
	int anoInt = Integer.parseInt(ano);
	
	return novaData(diaInt,mesInt,anoInt);

}

public static boolean verificaPrazoSemHoras(Date deadline, Date data) throws Exception {

	if (deadline == null) return true;
	
	GregorianCalendar dt1 = new GregorianCalendar ();
	dt1.setTime(deadline);
	int ano1 = dt1.get(dt1.YEAR);
	int mes1 = dt1.get(dt1.MONTH);
	int dia1 = dt1.get(dt1.DAY_OF_MONTH);
	GregorianCalendar dt2 = new GregorianCalendar ();
	dt2.setTime(data);
	int ano2 = dt2.get(dt2.YEAR);
	int mes2 = dt2.get(dt2.MONTH);
	int dia2 = dt2.get(dt2.DAY_OF_MONTH);

	// Ano
	if (ano2 > ano1) {
		return false;
	} else if (ano2 == ano1) {

		// Mes
		if (mes2 > mes1) {
			return false;
		} else if (mes2 == mes1) {
		
			// Dia
			if (dia2 > dia1) {
				return false;
			}
		}
	}
	
	return true;
}
}
