package davidsolutions.caixaferramentas.utilidade;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

public class XmlUtil<T> {

	private JAXBContext jaxbContext;

	public XmlUtil(Class<?> clazz) {
		try {
			this.jaxbContext = JAXBContext.newInstance(clazz);
		}
		catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public String convertToXml(T object, String tagRaiz) {
		try {
			Marshaller marshaller = this.jaxbContext.createMarshaller();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			marshaller.marshal(new JAXBElement<Object>(QName.valueOf(tagRaiz), Object.class, object), baos);
			return baos.toString();
		}
		catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public T convertToObject(String xml) {
		try {
			Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
			JAXBElement<Object> unmarshal = (JAXBElement<Object>) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
			return (T) unmarshal.getValue();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}