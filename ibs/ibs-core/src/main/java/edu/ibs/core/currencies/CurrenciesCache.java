package edu.ibs.core.currencies;

import edu.ibs.core.entity.Currency;
import edu.ibs.core.operation.AdminOperations;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: EgoshinME Date: 10.01.13 Time: 13:19
 */
public class CurrenciesCache {

	private List<Currency> list = new ArrayList<Currency>();
	private AdminOperations adminLogic;

	public CurrenciesCache() {
	}

	public void fillCurrencies() {
		SOAPConnectionFactory sfc = null;
		try {
			sfc = SOAPConnectionFactory.newInstance();
			SOAPConnection connection = sfc.createConnection();

			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage sm = mf.createMessage();

			MimeHeaders mimeHeader = sm.getMimeHeaders();
			mimeHeader.setHeader("SOAPAction", "http://www.nbrb.by/ExRatesDaily");

			SOAPPart soapPart = sm.getSOAPPart();

			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration("SOAP-ENC", "http://schemas.xmlsoap.org/soap/encoding/");
			envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
			envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance-instance");
			envelope.getHeader().detachNode();


			SOAPBody body = envelope.getBody();
			SOAPBodyElement item = body.addBodyElement(envelope.createName("ExRatesDaily", "", "http://www.nbrb.by/"));
			SOAPElement onDate = item.addChildElement("onDate");
			onDate.setValue(ISO8601DateParser.toString(new Date()));

			URL endpoint = new URL("http://www.nbrb.by/Services/ExRates.asmx");
			SOAPMessage response = connection.call(sm, endpoint);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.writeTo(out);

			SAXReader reader = new SAXReader();
			Document doc = reader.read(new StringReader(out.toString()));
			List<org.dom4j.Node> list = doc.selectNodes("//DailyExRatesOnDate");
			if (list != null && list.size() > 0) {
				for (org.dom4j.Node node : list) {
					String rate = node.selectSingleNode("Cur_OfficialRate").getText();
					String abbr = node.selectSingleNode("Cur_Abbreviation").getText();

					if (abbr != null) {
						for (Currency c : getList()) {
							if (abbr.equals(c.getName())) {
								BigDecimal bd = new BigDecimal(rate).divide(new BigDecimal(c.getFraction().multiply()), 2 * c.getFraction().multiply(), RoundingMode.DOWN);
//								c.setFactor(Float.parseFloat(rate) / c.getFraction().multiply());
								c.setFactor(bd.floatValue());
								c.setLastUpdated(System.currentTimeMillis());
							}
						}
					}
				}
				adminLogic.update(getList());
			}
		} catch (SOAPException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (MalformedURLException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (DocumentException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	public AdminOperations getAdminLogic() {
		return adminLogic;
	}

	public void setAdminLogic(AdminOperations adminLogic) {
		this.adminLogic = adminLogic;
	}

	public List<Currency> getList() {
		return list;
	}

	public void setList(List<Currency> list) {
		this.list = list;
	}
}
