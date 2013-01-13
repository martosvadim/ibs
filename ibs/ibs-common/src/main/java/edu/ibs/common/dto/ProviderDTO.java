package edu.ibs.common.dto;

import edu.ibs.common.enums.ProviderField;

import java.util.List;

/**
 * User: Максим
 * Date: 13.01.13
 * Time: 16:30
 */
public class ProviderDTO implements IBaseDTO {
    private long id;
    private CardBookDTO card;
    private List<ProviderField> fieldList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CardBookDTO getCard() {
        return card;
    }

    public void setCard(CardBookDTO card) {
        this.card = card;
    }

    public List<ProviderField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<ProviderField> fieldList) {
        this.fieldList = fieldList;
    }
}
