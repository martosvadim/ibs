package edu.ibs.core.currencies;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * User: EgoshinME
 * Date: 08.01.13
 * Time: 11:43
 */
public class CurrenciesServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
		try {
			SOAPConnectionFactory sfc = SOAPConnectionFactory.newInstance();
			SOAPConnection connection = sfc.createConnection();

			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage sm = mf.createMessage();

			MimeHeaders mimeHeader = sm.getMimeHeaders();
			mimeHeader.setHeader("SOAPAction", "http://www.nbrb.by/ExRatesDaily");

			SOAPPart soapPart= sm.getSOAPPart();

			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration("SOAP-ENC","http://schemas.xmlsoap.org/soap/encoding/");
			envelope.addNamespaceDeclaration("xsd","http://www.w3.org/2001/XMLSchema") ;
			envelope.addNamespaceDeclaration("xsi","http://www.w3.org/2001/XMLSchema-instance-instance");
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
					String name = new String(node.selectSingleNode("Cur_QuotName").getText().getBytes(), "UTF-8");

					System.out.println(name + " \t" + abbr + " \t" + rate);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}
}
