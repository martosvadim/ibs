package edu.ibs.webui.client.controller;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import edu.ibs.common.dto.VocDTO;
import edu.ibs.webui.client.utils.Components;

import java.util.List;

/**
 * Контрол для выбора элемента из списка
 */
public class SelectController extends GenericController {
    /**
     * Поле для ввода если контрол редактируемый
     */
    private ComboBoxItem selectItem = new ComboBoxItem();
    /**
     * Поле для ввода если контрол задизейблен
     */
    private Label label = Components.getLabel();
    /**
     * Данные контрола
     */
    private VocDTO<String, String> data;
    /**
     * Пустое значение в начале списка?
     */
    private boolean hasEmptyValue;
    /**
     * Ширина контрола
     */
    private int width = 0;

    /**
     * Конструктор
     */
    public SelectController() {
        this(0);
    }

    /**
     * Конструктор
     * 
     * @param width ширина
     */
    public SelectController(final int width) {
        this.width = width;
    }

    @Override
    protected Canvas createView() {
        Canvas view;
        if (isEnabled()) {
            selectItem.setDisabled(!isEnabled());
            selectItem.setShowTitle(false);
            if (width > 0) {
                selectItem.setWidth(width);
            } else {
                selectItem.setWidth("*");
            }
            selectItem.addChangedHandler(new ChangedHandler() {
                public void onChanged(final ChangedEvent changedEvent) {
                    fireOnChange(null);
                }
            });
            view = Components.createForm(selectItem);
            //view.setAutoWidth();
        } else {
            label.setValign(VerticalAlignment.CENTER);
            view = label;
        }
        return view;
    }

    @Override
    public void bind(final Object dto) {
        if (dto instanceof List) {
            Components.fillComboBox((List) dto, selectItem);
        }
    }

    /**
     * Устонавливет значение в комбобоксе
     * 
     * @param value значение
     */
    public void setSelectedValue(final VocDTO<String, String> value) {
        this.data = value;
        if (value == null) {
            label.setContents("");
            selectItem.setValue("");
        } else {
            selectItem.setValue(value.getId());
            label.setContents(value.getValue());
        }

    }

    @Override
    public Object unbind() {
        VocDTO<String, String> result = null;
        if (isEnabled()) {
            result = new VocDTO<String, String>((String) selectItem.getValue(), selectItem.getDisplayValue());
        } else {
            result = this.data;
        }
        return result;
    }

    @Override
    public void setEnabled(final boolean enable) {
        super.setEnabled(enable);
        selectItem.setDisabled(!isEnabled());
    }

    /**
     * Добавление обработчика на изменения значения
     * 
     * @param handler обработчик
     */
    public void addChangedHandler(final ChangedHandler handler) {
        selectItem.addChangedHandler(handler);
    }

    public boolean isHasEmptyValue() {
        return hasEmptyValue;
    }

    public void setHasEmptyValue(final boolean hasEmptyValue) {
        this.hasEmptyValue = hasEmptyValue;
    }

    @Override
    public boolean isFilled() {
        VocDTO vocDTO = (VocDTO) unbind();
        return vocDTO != null && vocDTO.getId() != null && !"".equals(vocDTO.getId());
    }

    @Override
    public void focus() {
        if (isEnabled()) {
            selectItem.focusInItem();
        } else {
            label.focus();
        }
    }

    public ComboBoxItem getSelectItem() {
        return selectItem;
    }
}
