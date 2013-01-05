package edu.ibs.common.dto;

/**
 * Элемент словаря (пара ключ, значение)
 *
 * @param <K> Класс ключа
 * @param <V> Класс данных
 */
public final class VocDTO<K, V> implements IBaseDTO {
    /**
     * Ключ
     */
    private K id;
    /**
     * Значение
     */
    private V value;

    /**
     * Конструктор
     */
    public VocDTO() {
    }

    /**
     * Конструктор
     *
     * @param id    Ключ
     * @param value Значение
     */
    public VocDTO(final K id, final V value) {
        this.id = id;
        this.value = value;
    }

    public K getId() {
        return id;
    }

    public void setId(final K id) {
        this.id = id;
    }

    public V getValue() {
        return value;
    }

    public void setValue(final V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + getValue();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        if (getId() != null) {
            hash *= getId().hashCode();
        }
        if (getValue() != null) {
            hash *= getValue().hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof VocDTO)) {
            return false;
        }
        VocDTO other = (VocDTO) obj;
        boolean bid = (other.getId() != null && getId() != null
                && other.getId().equals(getId())) || (other.getId() == null && getId() == null);
        boolean bvalue = (other.getValue() != null && getValue() != null
                && other.getValue().equals(getValue())) || (other.getValue() == null && getValue() == null);
        return bid && bvalue;
    }
}
