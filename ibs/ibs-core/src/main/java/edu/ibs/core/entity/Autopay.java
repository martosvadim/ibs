package edu.ibs.core.entity;

import edu.ibs.common.enums.Period;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * Autopay ...
 *
 * @date Jan 10, 2013
 *
 * @author Vadim Martos
 * @version 1.0.0
 */
@Entity
@Table(name = "Autopay")
@XmlRootElement
public class Autopay implements Serializable, AbstractEntity {

    private static final long serialVersionUID = 521341231983123312L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private long amount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "period")
    private long period;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lastPayed")
    private long lastPayed;
    @JoinColumn(name = "currencyID", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    private Currency currency;
    @JoinColumn(name = "toCardBookID", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    private CardBook to;
    @JoinColumn(name = "fromCardBookID", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    private CardBook from;
    @Transient
    private Money money;

    public Autopay() {
    }

    public Autopay(CardBook from, CardBook to, Money money, long period) {
        this.period = period;
        this.to = to;
        this.from = from;
        this.currency = money.currency();
        this.amount = money.balance();
        this.money = money;
    }

    public Autopay(CardBook from, CardBook to, Money money, Period period, int periodMultiply) {
        this.period = Period.calculatePeriod(period, periodMultiply);
        this.to = to;
        this.from = from;
        this.currency = money.currency();
        this.amount = money.balance();
        this.money = money;
    }

    private void validateMoney() {
        if (money == null) {
            money = new Money(amount, currency);
        }
    }

    public Money getMoney() {
        validateMoney();
        return money;
    }

    public long getId() {
        return id;
    }

    public User getOwner() {
        return from == null ? null : from.getOwner();
    }

    public long getAmount() {
        return amount;
    }

    public long getPeriod() {
        return period;
    }

    public long getLastPayed() {
        return lastPayed;
    }

    public void setLastPayed(long lastPayed) {
        this.lastPayed = lastPayed;
    }

    public Currency getCurrency() {
        return currency;
    }

    public CardBook getFrom() {
        return from;
    }

    public CardBook getTo() {
        return to;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Autopay other = (Autopay) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
