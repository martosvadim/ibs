package edu.ibs.webui.client.utils;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import edu.ibs.webui.client.controller.GenericController;

/**
 * @author EgoshinME
 */
public class Components {

    private static final int DEFAULT_TITLE_LABEL_WIDTH = 105;

    public static DynamicForm createForm(final FormItem... items) {
        DynamicForm form = new DynamicForm();
        form.setItems(items);
        form.setColWidths("*", 1);
        form.setWidth100();
        return form;
    }

    public static GenericController getTextItem() {
        TextItem textItem = new TextItem();
        textItem.setShowTitle(false);
        textItem.setWidth("100%");
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
