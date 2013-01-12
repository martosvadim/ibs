package edu.ibs.common.dto;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:27
 */
public class TransactionDTO implements IBaseDTO {
	private long id;
	private String amount;
	private TransactionType type;
	private CurrencyDTO currency;
	private CardBookDTO to;
	private CardBookDTO from;
	private MoneyDTO money;
    private long date;
    private String desc;

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public CurrencyDTO getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyDTO currency) {
		this.currency = currency;
	}

	public CardBookDTO getTo() {
		return to;
	}

	public void setTo(CardBookDTO to) {
		this.to = to;
	}

	public CardBookDTO getFrom() {
		return from;
	}

	public void setFrom(CardBookDTO from) {
		this.from = from;
	}

	public MoneyDTO getMoney() {
		return money;
	}

	public void setMoney(MoneyDTO money) {
		this.money = money;
	}

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
