//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.09.08 at 10:12:16 AM IDT 
//


package engine.LoadData.jaxbGeneratedEx2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="5">
 *         &lt;element ref="{}CTE-Reflector"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cteReflector"
})
@XmlRootElement(name = "CTE-Reflectors")
public class CTEReflectors {

    @XmlElement(name = "CTE-Reflector", required = true)
    protected List<CTEReflector> cteReflector;

    /**
     * Gets the value of the cteReflector property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cteReflector property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCTEReflector().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CTEReflector }
     * 
     * 
     */
    public List<CTEReflector> getCTEReflector() {
        if (cteReflector == null) {
            cteReflector = new ArrayList<CTEReflector>();
        }
        return this.cteReflector;
    }

}
