package br.leitor.dou;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import br.leitor.dou.util.Constants;
import br.leitor.dou.util.Util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

/**
 * Classe para busca dos links por processo.
 * 
 * @author kalil.peixoto
 * @date 12/01/2015
 */
public class SearchLinks {
	private static final String FILE_NAME = "links.txt";
	
	private static final Logger LOGGER = Logger.getLogger(SearchLinks.class);
	
	private WebDriver driver;
	private List<String> processos;
	
	static {
		Constants.initVariables();
	}
	
	/**
	 * Antes de tudo.
	 */
	@BeforeClass
	public static void init() {
		LOGGER.info("\t        log.dir:   " + System.getProperty("log.dir"));
		LOGGER.info("Iniciou classe para obtenção dos processos a serem manipulados e URL´s dos jornais do DOU");
	}

    /**
     * Antes de cada metodo de teste.
     */
    @Before
    public void openBrowser() {
    	LOGGER.info("Iniciou a conexão com o driver embedded HTMLUnitDriver");
    	this.driver = new HtmlUnitDriver();
    	this.driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
    	
    	try {
    		LOGGER.info("Iniciou a leitura dos processos do arquivo");
    		this.processos = Files.readLines(new File(Resources.getResource(FILE_NAME).getPath()), Charsets.UTF_8);
    		LOGGER.info("Criação de lista de processos para manipulação");
		} catch (IOException e) {
			LOGGER.error("Erro no processo de leitura do arquivo", e);
		}
    }
    
    /**
     * Obtem os links a partir dos processos.
     */
    @Test
    public void searchProcessesAndGetLinks() {
    	int qtdProcessoComDOU = 0;
    	int qtdProcessoSemDOU = 0;
    	List<String> processosSemDOU = new ArrayList<String>();
    	
    	LOGGER.info("Iniciou processo de log de URL´s dos jornais por processo\n");
    	try {
    		for (String processo: this.processos) {
    			String processoFormatado = this.formataProcesso(processo);
    			
    			LOGGER.info("-------------- Buscando jornais para o processo: " + processoFormatado);
    			
    			int ano = 2014;
    			
    			while (ano >= 2011 && ano <= 2014) {
			    	this.driver.get(this.montaURLJornais(processoFormatado, ano));
			    	
			        List<WebElement> elementos = this.driver.findElements(By.xpath("//th[@class='data']/a"));
			        
			        if (!Util.isNull(elementos)) {
				        for (WebElement link : elementos) {
				        	LOGGER.info("\t" + link.getAttribute("href"));
					    }
				        qtdProcessoComDOU++;
				        break;
	    			} else {
	    				LOGGER.info(processoFormatado + " não possui jornal no ano " + ano);
	    				ano--;
	    				
	    				if (ano < 2011) {
	    					processosSemDOU.add(processoFormatado);
		    				qtdProcessoSemDOU++;
	    				}
	    			}
    			}
		        LOGGER.info("\n");
    		}
	        
    		LOGGER.info("-------Total de processos com DOU: " + qtdProcessoComDOU);
    		LOGGER.info("-------Total de processos sem DOU: " + qtdProcessoSemDOU);
    		
    		StringBuilder sbProcessosSemDOU = new StringBuilder();
    		for (String pSemDou : processosSemDOU) {
    			sbProcessosSemDOU.append(pSemDou);
    			sbProcessosSemDOU.append("--");
    		}
    		
    		LOGGER.info("Processos SEM DOU: " + 
    				sbProcessosSemDOU.toString().substring(0, sbProcessosSemDOU.toString().lastIndexOf("--")));
    		LOGGER.info("-------FINALIZADO processo de obtenção de links");
    		
	        this.driver.quit();
    	} catch (Exception e) {
    		LOGGER.error("Erro no processo de obtenção dos jornais", e);
		}
    }

	/**
	 * Monta URL do jornal e realiza a conexao.
	 * 
	 * @param processo {@link String}.
	 * @param ano <code>int</code>.
	 * @return {@link String}.
	 * @throws ParseException Excecao a ser lancada.
	 */
	private String montaURLJornais(String processo, int ano) throws ParseException {
		StringBuilder url = new StringBuilder();
		url.append("http://pesquisa.in.gov.br/imprensa/core/consulta.action?edicao.txtPesquisa=");
		url.append(processo);
		url.append("&edicao.tipoPesquisa=pesquisa_avancada&edicao.paginaAtual=1");
		url.append("&edicao.dtInicio=01/01&edicao.dtFim=31/12");
		url.append("&edicao.ano=");
		url.append(ano);
		url.append("&edicao.fonetica=N");
		return url.toString();
	}
	
    /**
     * Formata o processo com os caracteres necessarios para busca.
     * 
     * @param processo {@link String}.
     * @return {@link String} formatado.
     * @throws ParseException Excecao a ser lancada.
     */
    private String formataProcesso(String processo) throws ParseException {
    	if (processo.length() == 15) {
    		return Util.formatarString(processo, Util.MASCARA_PROCESSO_ANTIGO);
    	} else {
    		return Util.formatarString(processo, Util.MASCARA_PROCESSO);
    	} 
    } 
}