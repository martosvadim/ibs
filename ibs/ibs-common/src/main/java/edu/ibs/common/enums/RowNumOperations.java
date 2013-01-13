package edu.ibs.common.enums;

/**
 * User: Максим
 * Date: 13.01.13
 * Time: 20:18
 */
public enum RowNumOperations {
    /**
     * меньше
     */
    LT("<"),
    /**
     * меньше или равно
     */
    LE("<=");

    /**
     * текстовое представление операции
     */
    private String value;

    /**
     * конструктор
     *
     * @param value текстовое представление операции
     */
    RowNumOperations(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}