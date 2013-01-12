package edu.ibs.core.entity;

import edu.ibs.common.enums.ProviderField;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @date Jan 12, 2013
 *
 * @author Vadim Martos
 */
@Entity
@Table(name = "Transaction")
@XmlRootElement
public class Provider implements Serializable, AbstractEntity {
	
	public static final int NUMBER_OF_FIELDS = 5;
	private static final long serialVersionUID = 642421389802413412L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, updatable = false)
	private long id;
	@JoinColumn(name = "cardBookID", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = false)
	private CardBook card;
	@Basic(optional = true)
	@Column(name = "field1", updatable = false)
	@Enumerated(EnumType.STRING)
	private ProviderField field1;
	@Basic(optional = true)
	@Column(name = "field2", updatable = false)
	@Enumerated(EnumType.STRING)
	private ProviderField field2;
	@Basic(optional = true)
	@Column(name = "field3", updatable = false)
	@Enumerated(EnumType.STRING)
	private ProviderField field3;
	@Basic(optional = true)
	@Column(name = "field4", updatable = false)
	@Enumerated(EnumType.STRING)
	private ProviderField field4;
	@Basic(optional = true)
	@Column(name = "field5", updatable = false)
	@Enumerated(EnumType.STRING)
	private ProviderField field5;
	
	public Provider() {
	}
	
	public Provider(CardBook card, ProviderField... fields) {
		this.card = card;
		if (fields != null) {
			if (fields.length > NUMBER_OF_FIELDS) {
				throw new IllegalArgumentException(String.format("Number of fields [%s] is too huge, expected %s", fields.length, NUMBER_OF_FIELDS));
			} else {
				int index = 0;
				if (fields.length > index) {
					field1 = fields[index++];
				}
				if (fields.length > index) {
					field2 = fields[index++];
				}
				if (fields.length > index) {
					field3 = fields[index++];
				}
				if (fields.length > index) {
					field4 = fields[index++];
				}
				if (fields.length > index) {
					field5 = fields[index++];
				}
			}
		}
	}
	
	public List<ProviderField> getFields() {
		List<ProviderField> fields = new LinkedList<ProviderField>();
		if (field1 != null) {
			fields.add(field1);
			if (field2 != null) {
				fields.add(field2);
				if (field3 != null) {
					fields.add(field3);
					if (field4 != null) {
						fields.add(field4);
						if (field5 != null) {
							fields.add(field5);
						}
					}
				}
			}
		}
		return fields;
	}
	
	public CardBook getCard() {
		return card;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Provider other = (Provider) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}
	
	@Override
	public String toString() {
		return "Provider{" + "card=" + card + '}';
	}
}
