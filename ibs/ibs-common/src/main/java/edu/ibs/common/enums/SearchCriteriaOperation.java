package edu.ibs.common.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Максим
 * Date: 13.01.13
 * Time: 20:17
 */
public enum SearchCriteriaOperation implements IsSerializable {
    /**
     * Содержит
     */
    HAS {
        /**
         * {@inheritDoc}
         */
        public String sql(final String attr, final String value) {
            return "LOWER(" + attr + ") like '%" + value.toLowerCase() + "%'";
        }
        /**
         * {@inheritDoc}
         */
        public String toString() {
            return "HAS";
        }
    },
    /**
     * Начинается с
     */
    START {
        /**
         * {@inheritDoc}
         */
        public String sql(final String attr, final String value) {
            return "LOWER(" + attr + ") like '" + value.toLowerCase() + "%'";
        }
        /**
         * {@inheritDoc}
         */
        public String toString() {
            return "START";
        }
    },
    /**
     * Равно
     */
    EQUALS {
        /**
         * {@inheritDoc}
         */
        public String sql(final String attr, final String value) {
            return "LOWER(" + attr + ") like '" + value.toLowerCase() + "'";
        }
        /**
         * {@inheritDoc}
         */
        public String toString() {
            return "EQUALS";
        }
    },
    /**
     * Не равно
     */
    NOT_EQUALS {
        /**
         * {@inheritDoc}
         */
        public String sql(final String attr, final String value) {
            return "NOT(" + attr + " like '" + value + "')";
        }
        /**
         * {@inheritDoc}
         */
        public String toString() {
            return "NOT_EQUALS";
        }
    },
    /**
     * число элементов
     */
    ROWNUM {
        /**
         * {@inheritDoc}
         */
        public String sql(final String attr, final String value) {
            // Throws IllegalArgumentException. See documentation for RowNumOperations.valueOf(String) method.
            return "ROWNUM" + RowNumOperations.valueOf(attr).getValue() + value;
        }

        /**
         * {@inheritDoc}
         */
        public String toString() {
            return "ROWNUM";
        }
    };

    /**
     * Возвращает SQL код для условия операции
     *
     * @param attr  Аттрибут
     * @param value Значение для условия
     * @return SQL код
     */
    public String sql(final String attr, final String value) {
        return "";
    }

    /**
     * Возвращает SQL код для условия операции
     *
     * @param attr     Аттрибут
     * @param value    Значение для условия
     * @param isRepeat Аттрибут является ли repeat
     * @return SQL код
     */
    public String sql(final String attr, final String value, final Boolean isRepeat) {
        /**
         * удаляем символ ' из запроса, иначе можно провести sql injection
         */
        String searchValue = value;
        if (searchValue.contains("'")) {
            searchValue = searchValue.replaceAll("'", "");
        }
        String result = sql(attr, searchValue);
        if (isRepeat) {
            result = "any " + result;
        }
        return result;
    }

    /**
     * Описание операции для условия
     *
     * @return Описание
     */
    public String toString() {
        return super.toString();
    }

}
