package edu.ibs.webui.client.utils;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tree.TreeGridField;
import edu.ibs.common.dto.VocDTO;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.SelectController;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author EgoshinME
 */
public class Components {

    private static final int DEFAULT_TITLE_LABEL_WIDTH = 105;
	public static final int STANDART_ICON_SIZE = 16;
	public static final int STANDART_ACTION_FIELD_WIDTH = 24;

    public static DynamicForm createForm(final FormItem... items) {
        DynamicForm form = new DynamicForm();
        form.setItems(items);
        form.setColWidths("*", 1);
        form.setWidth100();
        return form;
    }

    public static GenericController getTextItem() {
        return new GenericController() {
            /**
             * Поле для воода если контрол редактируемый
             */
            private TextItem textItem = new TextItem();
            /**
             * Поле для ввода если контрол задизейблен
             */
            private Label label = Components.getLabel();
            /**
             * Числовой источник данных?
             */
            private boolean isNumber = false;

            @Override
            protected Canvas createView() {
                Canvas view;
                if (isEnabled()) {
                    textItem.setShowTitle(false);
                    textItem.setWidth("*");
					textItem.setLength(64);
                    textItem.addChangedHandler(new ChangedHandler() {
                        public void onChanged(final ChangedEvent changedEvent) {
                            fireOnChange(null);
                        }
                    });
                    view = createForm(textItem);
                } else {
                    view = label;
                }
                return view;
            }

            @Override
            public void focus() {
                textItem.focusInItem();
            }

            @Override
            public void bind(final Object dto) {
                if (dto instanceof Number) {
                    isNumber = true;
                }
                if (dto != null) {
                    textItem.setValue("" + dto);
                    label.setContents("" + dto);
                }
            }

            @Override
            public Object unbind() {
                Object result;
                if (isEnabled()) {
                    result = textItem.getValue();
                } else {
                    result = label.getContents();
                    if ("&nbsp;".equals(result)) {
                        result = "";
                    }
                }
                if (isNumber) {
                    try {
                        return Integer.valueOf("" + result);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                } else {
                    return result;
                }
            }

            @Override
            public boolean isFilled() {
                return super.isFilled() && !"".equals(((String) unbind()).trim());
            }
        };
    }

    /**
     * Создаём поле для ввода
     *
     * @return контролл
     */
    public static GenericController getPasswordItem() {
        return new GenericController() {
            /**
             * Поле для воода если контрол редактируемый
             */
            private PasswordItem textItem = new PasswordItem();
            /**
             * Поле для ввода если контрол задизейблен
             */
            private Label label = Components.getLabel();

            @Override
            protected Canvas createView() {
                Canvas view;
                if (isEnabled()) {
                    textItem.setShowTitle(false);
                    textItem.setWidth("*");
                    textItem.addChangedHandler(new ChangedHandler() {
                        public void onChanged(final ChangedEvent changedEvent) {
                            fireOnChange(null);
                        }
                    });
                    view = createForm(textItem);
                } else {
                    view = label;
                }
                return view;
            }

            @Override
            public void focus() {
                textItem.focusInItem();
            }

            @Override
            public void bind(final Object dto) {
                textItem.setValue((String) dto);
                label.setContents((String) dto);
            }

            @Override
            public Object unbind() {
                Object result;
                if (isEnabled()) {
                    result = textItem.getValue();
                } else {
                    result = label.getContents();
                }
                return result;
            }

            @Override
            public boolean isFilled() {
                return super.isFilled() && !"".equals(((String) unbind()).trim());
            }
        };
    }

    /**
     * Создаем лэйбл
     *
     * @return лэйбл
     */
    public static Label getLabel() {
        return getLabel("");
    }

    /**
     * Создаем лэйбл со значением
     *
     * @param contents значение
     * @return лейбл
     */
    public static Label getLabel(final String contents) {
        return getLabel(contents, null, 0);
    }

    /**
     * Создаем лэйбл со значением
     *
     * @param contents  значение
     * @param styleName стиль
     * @return лейбл
     */
    public static Label getLabel(final String contents, final String styleName) {
        return getLabel(contents, styleName, 0);
    }

    /**
     * Создаем лэйбл со значением
     *
     * @param contents  значение
     * @param styleName стиль
     * @param width     ширина
     * @return лейбл
     */
    public static Label getLabel(final String contents, final String styleName, final int width) {
        Label label = new Label(contents);
        label.setAutoHeight();
        label.setCanSelectText(true);
        label.setCursor(Cursor.TEXT);
        if (styleName != null && styleName.length() > 0) {
            label.setStyleName(styleName);
        }
        if (width == 0) {
            label.setWidth100();
        } else {
            label.setWidth(width);
        }

        return label;
    }

	/**
	 * Заполняем комбобокс значениями
	 *
	 * @param values     значения
	 * @param selectItem комбобокс
	 */
	public static void fillComboBox(final List values,
									final SelectItem selectItem) {
		fillComboBox(values, selectItem, false);
	}

	/**
	 * Заполняем комбобокс значениями
	 *
	 * @param values          значения
	 * @param selectItem      комбобокс
	 * @param setDefaultFirst устонавливать ли первое значение по умолчанию
	 */
	public static void fillComboBox(final List values,
									final SelectItem selectItem, final boolean setDefaultFirst) {
		LinkedHashMap<String, String> svalues = new LinkedHashMap<String, String>();
		if (values != null) {

			for (Object o : values) {
				if (o instanceof VocDTO) {
					svalues.put("" + ((VocDTO) o).getId(), "" + ((VocDTO) o).getValue());
				} else {
					svalues.put("" + o, "" + o);
				}
			}
			if (setDefaultFirst && values.size() > 0) {
				Object o = values.get(0);
				if (o instanceof VocDTO) {
					selectItem.setValue("" + ((VocDTO) o).getValue());
				} else {
					selectItem.setValue("" + o);
				}
			}
		}
		selectItem.setValueMap(svalues);
	}

	/**
	 * Получаем таблицу с начальными настройками
	 *
	 * @return таблица
	 */
	public static ListGrid getGrid() {
		ListGrid grid = new ListGrid();
		return prepareGrid(grid);
	}

	/**
	 * Подготавливаем таблицу к использованию
	 *
	 * @param grid Таблица
	 * @return Таблица
	 */
	public static ListGrid prepareGrid(final ListGrid grid) {
		localizeGrid(grid);
		grid.setAutoFetchData(true);
		grid.setCanEdit(false);
		grid.setWidth100();
		grid.setHeight100();
		grid.setShowRollOver(false);
		grid.setAlternateRecordStyles(true);
		grid.setCanDragSelect(false);
		grid.setCanHover(false);
		grid.setSelectionType(SelectionStyle.SINGLE);
		return grid;
	}

	/**
	 * Локализация таблицы
	 *
	 * @param grid Таблица
	 */
	public static void localizeGrid(final ListGrid grid) {
		grid.setEmptyMessage("Нет данных");
		grid.setLoadingMessage("Загрузка...");
		grid.setLoadingDataMessage("Загрузка...");
		grid.setSortFieldAscendingText("Сортировать по возрастанию");
		grid.setSortFieldDescendingText("Сортировать по убыванию");
		grid.setFreezeFieldText("Зафиксировать ");
		grid.setUnfreezeFieldText("Убрать фиксацию по ");
		grid.setGroupByText("Групировать по ");
		grid.setClearFilterText("Убрать сортировку");
	}

	/**
	 * Иконка в таблице или дереве Отличается от IMAGE тем что всегда отображается
	 *
	 * @param element Идентификатор элемента
	 * @param hover Описание
	 * @param imgPath Путь к картинке
	 * @param handler обработчик клика на картинку
	 * @return колонка
	 */
	public static ListGridField getIconGridField(final String element, final String hover, final String imgPath, final RecordClickHandler handler) {
		return getIconGridField(element, hover, STANDART_ACTION_FIELD_WIDTH, STANDART_ICON_SIZE, imgPath, handler);
	}

	/**
	 * Иконка в таблице или дереве Отличается от IMAGE тем что всегда отображается
	 *
	 * @param element Идентификатор элемента
	 * @param hover Описание
	 * @param width Ширина колонки
	 * @param imageSize Размеры иконки
	 * @param imgPath Путь к картинке
	 * @param handler обработчик клика на картинку
	 * @return колонка
	 */
	public static ListGridField getIconGridField(final String element, final String hover, final int width, final int imageSize, final String imgPath,
												 final RecordClickHandler handler) {
		TreeGridField field = Components.getTreeGridFieldWithPrompt(element, null, width);
		field.setAlign(Alignment.CENTER);
		field.setType(ListGridFieldType.ICON);
		field.setCellIcon(imgPath);
		field.setImageHeight(imageSize);
		field.setImageWidth(imageSize);
		field.setCanFilter(false);
		field.setCanSort(false);
		field.setHoverCustomizer(Components.getHover(hover));
		field.addRecordClickHandler(handler);
		return field;
	}

	public static TreeGridField getTreeGridFieldWithPrompt(final String name, final String title, final int width) {
		TreeGridField result = new TreeGridField(name, title, width);
		result.setPrompt(title);
		return result;
	}

	public static HoverCustomizer getHover(final String hover) {
		return new HoverCustomizer() {

			public String hoverHTML(final Object value, final ListGridRecord record, final int rowNum, final int colNum) {
				return hover;
			}
		};
	}

	/**
	 * Создаём контрол для выбора элемента из списка
	 *
	 * @return контролл
	 */
	public static SelectController getComboBoxControll() {
		return new SelectController();
	}

    public static Canvas addTitle(final String title, final Canvas control) {

        final Label label = new Label(title);
        label.setWidth(DEFAULT_TITLE_LABEL_WIDTH);

        final HLayout layout = new HLayout();
        layout.setWidth100();
        layout.addMember(label);
        layout.addMember(control);
        layout.setAutoHeight();
        layout.setDefaultLayoutAlign(Alignment.CENTER);

        return layout;
    }

    public static Window getWindow() {
        final Window window = new Window();
        window.setShowFooter(false);
        window.setShowHeaderBackground(false);
        window.setTitle("");
        window.setIsModal(true);
        window.setAutoCenter(true);
        window.setShowModalMask(true);
        window.setShowMinimizeButton(false);
        window.setShowMaximizeButton(false);
        window.setShowCloseButton(true);
        window.setCanDragReposition(true);
        window.setCanDragResize(true);
        window.setWidth(310);
        window.setHeight(200);
        return window;
    }
}
