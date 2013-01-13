package edu.ibs.common.dto;

/**
 * User: Максим
 * Date: 13.01.13
 * Time: 20:14
 */
public final class SearchCriteriaDTO implements IBaseDTO {
    /**
     * Аттрибут для поиска
     */
    private String attribute;
    /**
     * Операция для условия
     */
    private String operation;
    /**
     * Значение для условия
     */
    private String value;

    /**
     * Конструктор по умолчанию (нужен для всех объектов которые isSerializable)
     */
    public SearchCriteriaDTO() {
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(final String attribute) {
        this.attribute = attribute;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SearchCriteriaDTO{"
                + "attribute='" + attribute + '\''
                + ", operation='" + operation + '\''
                + ", value='" + value + '\'' + '}';
    }
}
