package br.leitor.dou.util;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.MaskFormatter;

/**
 * Classe que mantem operacoes utilitarias.
 * 
 * @author kalil.peixoto
 * @date 12/01/2015
 */
public class Util {
	public static final String MASCARA_PROCESSO_ANTIGO = "#####.######/##-##";
	public static final String MASCARA_PROCESSO = "#####.######/####-##";
	
	/**
	 * Formata o texto conforme a máscara informada.
	 * 
	 * @param texto {@link String}.
	 * @param mascara {@link String}.
	 * @return {@link String} valor formatado.
	 * @throws ParseException Excecao a ser lancada.
	 */
	public static String formatarString(String texto, String mascara) throws ParseException {
		MaskFormatter mf = new MaskFormatter(mascara);
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(texto);
	}
	
	/**
	 * <dl>
	 * Verifica se um objeto È nulo. O mÈtodo pode ser usado para inst?ncias das classes:
	 * <dd>- <code>java.lang.String</code><dd>
	 * <dd>- <code>java.util.Collection</code><dd>
	 * <dd>- <code>java.util.Map</code><dd>
	 * <dd>- <code>java.util.List</code><dd>
	 * <dd>- <code>java.util.Set</code><dd>
	 * </dl>
	 * @param objeto
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNull(final Object objeto) {
		boolean nulo;
		
		if (objeto == null) {
			nulo = true;
		} else if (objeto instanceof String) {
			nulo = objeto.toString().trim().length() == 0;
		} else if (objeto instanceof Collection) {
			nulo = ((Collection)objeto).isEmpty();
		} else if (objeto instanceof Map) {
			nulo = ((Map)objeto).isEmpty();
		} else if (objeto instanceof List) {
			nulo = ((List)objeto).isEmpty();
		} else if (objeto instanceof Set) {
			nulo = ((Set)objeto).isEmpty();
		} else {
			nulo = false;
		} //end if
		
		return nulo;
	} //end method
}
