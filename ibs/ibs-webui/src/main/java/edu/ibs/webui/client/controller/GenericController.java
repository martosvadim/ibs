package edu.ibs.webui.client.controller;

import com.smartgwt.client.widgets.Canvas;

import java.util.LinkedList;
import java.util.List;

/**
 * User: EgoshinME
 * Date: 03.12.12
 * Time: 12:08
 */
public class GenericController {
    /**
     * Признак задизейблен контрол или нет
     */
    private boolean enabled = true;
    /**
     * Заголовок окна контроллера
     */
    private String title;
    /**
     * Интерфейс контроллера
     */
    private Canvas view;
    /**
     * Экшены обновления контрола
     */
    private List<IAction> onChangeList = new LinkedList<IAction>();

    /**
     * Выполнение событий на изменение
     *
     * @param data данные
     */
    public void fireOnChange(final Object data) {
        for (IAction action : onChangeList) {
            if (action != null) {
                action.execute(data);
            }
        }
    }

    /**
     * Добавляет событие на изменение контрола
     *
     * @param onChange событие
     */
    public void addOnChange(final IAction onChange) {
        this.onChangeList.add(onChange);
    }

    /**
     * Создание view части
     *
     * @return представление
     */
    protected Canvas createView() {
        return new Canvas(getTitle());
    }

    /**
     * Получение представления для контролла
     *
     * @return представление
     */
    public Canvas getView() {
        if (view == null) {
            view = createView();
        }
        return view;
    }

    public String getTitle() {
        if (title == null) {
            return getClass().getName();
        } else {
            return title;
        }
    }

    /**
     * Загрузка данных в контрол
     *
     * @param dto источник данных
     */
    public void bind(final Object dto) {
    }

    /**
     * Загружает в контрол данные
     */
    public void reload() {
    }

    /**
     * Выгружает данные с контрола
     *
     * @return данные
     */
    public Object unbind() {
        return null;
    }

    /**
     * Проверка на заполненность
     *
     * @return да если поле заполнено
     */
    public boolean isFilled() {
        return unbind() != null;
    }

    /**
     * Фокусировка на контроле
     */
    public void focus() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
