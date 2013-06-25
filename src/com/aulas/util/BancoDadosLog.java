package com.aulas.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Grava log dos comandos em memória
 * @author Marco
 *
 */
public class BancoDadosLog {
	public static final int QTDE_REGISTROS = 5;
	
	private static ArrayList<String> sqls = new ArrayList<String>();
	private static ArrayList<String> times = new ArrayList<String>();
	
	public static synchronized void init() throws Exception {
		for (int i = 0; i < QTDE_REGISTROS; i++) {
			sqls.add("");
			times.add("");
		}
	}
	
	public static synchronized void log(String sql) throws Exception {
		sqls.remove(0);
		sqls.add(sql);
		times.remove(0);
		String data = SimpleDateFormat.getInstance().format(new Date());
		times.add(data);
	}
	
	public static String lastCommands() throws Exception {
		StringBuffer str = new StringBuffer();
		for (int i = QTDE_REGISTROS-1; i >=0; i--) {
			str.append("["+times.get(i) + "] - " + sqls.get(i) + "\n");
		}
		return str.toString();
		
	}
	

}
