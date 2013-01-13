package edu.ibs.webui.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Canvas;
import edu.ibs.common.dto.SearchCriteriaDTO;
import edu.ibs.common.enums.SearchCriteriaOperation;
import edu.ibs.webui.client.MyApp;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * JS Константы и хелперные методы
 */
public final class JS {
    /**
     * Период жизнедеятельности куки
     */
    private static final int COOKIE_TIME = 1000 * 60 * 60 * 24 * 7; //seven days
    /**
     * 1024
     */
    private static final int BYTES = 1024;
    /**
     * десять
     */
    private static final int TEN = 10;
    /**
     * Стиль CSS
     */
    private static final String HIDE_CSS_STYLE = "hideElement";
    /**
     * Стиль CSS
     */
    private static final String SHOW_CSS_STYLE = "showElement";
    /**
     * Логин в cookey
     */
    public static final String COOKEY_LOGIN = "cookey_kblogin";
    /**
     * Модуль в cookey
     */
    public static final String COOKEY_MODULE = "cookey_kbmodule";

    /**
     * Конструктор
     */
    private JS() {
    }

    /**
     * Закрываем окно
     */
    public static native void closeBrowser() /*-{
        $wnd.open('', '_self', '');
        $wnd.close();
    }-*/;

    /**
     * Получаем путь к контексту
     *
     * @return путь
     */
    public static native String getContextPath() /*-{
        return "/" + $wnd.location.pathname.substring(1).substring(0,
                $wnd.location.pathname.substring(1).indexOf("/"));
    }-*/;

    /**
     * Путь к серверу
     *
     * @return путь
     */
    public static native String getServerURL() /*-{
        return $wnd.location.protocol + "//" + window.location.host;
    }-*/;

    /**
     * Полный путь к приложению
     *
     * @return путь
     */
    public static native String getServerURLWithContextPath() /*-{
        return $wnd.location.protocol + "//" + window.location.host
                + "/" + $wnd.location.pathname.substring(1).substring(0,
                $wnd.location.pathname.substring(1).indexOf("/"));
    }-*/;

    /**
     * Загружаем страницу
     *
     * @param theURL урл
     */
    public static native void loadURL(final String theURL) /*-{
        $wnd.top.location.href = theURL;
    }-*/;
    
    
    /**
     * Загружаем файл
     *
     * @param theURL урл
     */
    public static native void downloadURL(final String theURL) /*-{
        $wnd.document.location = theURL;
    }-*/;


    /**
     * Что у нас в строке запроса?
     *
     * @return значение
     */
    public static native String getQueryString() /*-{
        return $wnd.location.search;
    }-*/;


    /**
     * Кодирование строки
     *
     * @param component строка
     * @return закодированная строка
     */
    public static native String encodeURI(final String component) /*-{
        return encodeURI(component);
    }-*/;

    /**
     * Получаем строку URL
     *
     * @return URL
     */
    public static native String getParamString() /*-{
        return $wnd.location.search;

    }-*/;

    /**
     * Получаем параметры из URL
     *
     * @return параметры
     */
    public static HashMap<String, String> getParams() {
        String string = getParamString();
        return parseParams(string);
    }

    /**
     * Парсим строку с параметрами
     *
     * @param string строка
     * @return параметры
     */
    public static HashMap<String, String> parseParams(final String string) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (string != null && string.length() > 0) {
            String[] ray = string.substring(1, string.length()).split("&");
            for (String aRay : ray) {
                String[] substrRay = aRay.split("=");
                if (substrRay.length > 1) {
                    map.put(substrRay[0], substrRay[1]);
                }
            }
        }
        return map;
    }

    /**
     * Получаем токен переданный при старте приложения
     * @return
     */
    public static native String getToken() /*-{
        return $wnd.webToken;
    }-*/;

    /**
     * Получаем вид документа в CRM
     * @return
     */
    public static native String getCRMDocTypeCode() /*-{
        return $wnd.crmDocTypeCode;
    }-*/;

    /**
     * Получаем ид профиля
     * @return
     */
    public static native String getCRMProfile() /*-{
        return $wnd.crmProfile;
    }-*/;

    /**
     * Получаем команду из CRM
     * @return
     */
    public static native String getCRMCommand() /*-{
        return $wnd.crmCommand;
    }-*/;

    /**
     * Получаем название шаблона из CRM
     * @return
     */
    public static native String getCRMTemplateName() /*-{
        return $wnd.crmTemplateName;
    }-*/;

    /**
     * Получаем сообщение об ошибке
     * @return
     */
    public static native String getCRMErrorMessage() /*-{
        return $wnd.crmErrorMessage;
    }-*/;

    /**
     * Получаем полный путь к картинкам
     *
     * @param path относительный путь
     * @return путь
     */
    public static native String getImgPath(final String path) /*-{
        return $wnd.webResourcesSkinDir + '/custom/' + path;
    }-*/;

    /**
     * Выводим alert
     *
     * @param message текст
     */
    public static native void alert(final String message) /*-{
        alert(message);
    }-*/;


    /**
     * Получаем полный путь к иконке
     *
     * @param fileName Имя файла с иконкой
     * @return путь
     */
    public static native String getIconPath(final String fileName) /*-{
        return $wnd.webResourcesDir + 'icons/' + fileName;
    }-*/;

    /**
     * Получаемполный путь к папке
     *
     * @param path путь
     * @return полный путь с хостом
     */
    public static String getURL(final String path) {
        return getServerURL() + getContextPath() + path;
    }

    /**
     * Строку с браузером
     *
     * @return браузер
     */
    public static native String getUserAgent() /*-{
        return navigator.userAgent.toLowerCase();
    }-*/;

    /**
     * Это IE ?
     *
     * @return да/нет
     */
    public static boolean isIE() {
        return /*false; */getUserAgent().contains("msie");
    }

    /**
     * Это мозила?
     *
     * @return да/нет
     */
    public static native boolean isMoz() /*-{
        return $wnd.isc.Browser.isMoz;
    }-*/;

    /**
     * Показывать версию?
     *
     * @return да / нет
     */
    public static boolean isShowVersion() {
        return getParams().containsKey("version")
                && "true".equals(getParams().get("version"));
    }

    /**
     * Показывать разработческую версию?
     *
     * @return да / нет
     */
    public static boolean isDev() {
        return getParams().containsKey("dev")
                && "true".equals(getParams().get("dev"));
    }

      /**
     * Устанавливаем заголовок для окна
     *
     * @param title заголовок
     */
    public static void setTitle(final String title) {
        Window.setTitle(title);
    }

    /**
     * Открытие документа в новом окне
     *
     * @param id       идентификатор выбранной задачи
     * @param docId    идентификатор документа
     * @param category текущая категория
     */
    public static void openDocument(final String id, final String docId, final String category) {
        String url = JS.getServerURLWithContextPath()
                + "/application.jsp?docid=" + docId + "&taskid=" + id + "&category=" + category;
        Window.open(url, "_blank", "");
    }

    /**
     * Читаем куку
     *
     * @param name Имя
     * @return значение
     */
    public static native String getCookie(final String name) /*-{
        var arg = name + "=";
        var alen = arg.length;
        var clen = $wnd.document.cookie.length;
        var i = 0;
        while (i < clen) {
            var j = i + alen;
            if ($wnd.document.cookie.substring(i, j) == arg) {
                var endstr = $wnd.document.cookie.indexOf(";", j);
                if (endstr == -1) {
                    endstr = $wnd.document.cookie.length;
                }
                return unescape($wnd.document.cookie.substring(j, endstr));
            }
            i = $wnd.document.cookie.indexOf(" ", i) + 1;
            if (i == 0) break;
        }
        return null;
    }-*/;

    /**
     * Обновить страницу
     */
    public static native void refreshPage() /*-{
        $wnd.location.reload(true);
    }-*/;

    /**
     * Установка кука
     *
     * @param name  Имя
     * @param value Значение
     */
    public static native void setCookie(final String name, final String value) /*-{
        $wnd.document.cookie = name + "=" + escape(value) + ";";
    }-*/;

    /**
     * Установка кука в GWT
     *
     * @param name  Имя
     * @param value Значение
     */
    public static void setGWTCookie(final String name, final String value) {
        Date now = new Date();
        long nowLong = now.getTime();
        nowLong = nowLong + COOKIE_TIME; //seven days
        now.setTime(nowLong);
        Cookies.setCookie(name, value, now);
    }
    /**
     * Читаем куку в GWT
     *
     * @param name Имя
     * @return значение
     */
    public static String getGWTCookie(final String name) {
        return Cookies.getCookie(name);
    }


    /**
     * 1.если размер больше 1024КБ, то переводит и отображает в МБ
     * 2.если размер меньше 1КБ, то переводит и отображает в Б
     * 3 иначе в КБ
     *
     * @param size размер в байтах
     * @return результат
     */
    public static String parseFileSize(final String size) {
        String rezult;
        try {
            int b = Integer.parseInt(size);
            if (b > BYTES * BYTES) {
                double d = (double) b / (BYTES * BYTES);
                b = (int) (d * TEN);
                rezult = (double) b / TEN + " МБ";
            } else if (b > BYTES) {
                int kb = b / BYTES;
                rezult = kb + " КБ";
            } else {
                rezult = b + " Б";
            }
        } catch (NumberFormatException e) {
            rezult = size;
        }
        return rezult;

    }

    /**
     * Строковое значение текущей даты
     *
     * @return значение
     */
    public static native String getDate() /*-{
        var myDate = new Date();
        return myDate.toLocaleDateString();
    }-*/;

    /**
     * Загрузка файла
     *
     * @param url урл
     */
    public static native void downloadFile(final String url) /*-{
        var iframe = document.createElement("iframe");
        iframe.src = url;
        iframe.style.display = "none";
        document.body.appendChild(iframe);
    }-*/;

    /**
     * Переход к окну авторизации
     */
    public static void goToLoginPage() {
        JS.setCookie(MyApp.LOGIN_COOKIE_NAME, "");
        Window.open(JS.getURL(""), "_self", "");
    }

    /**
     * Печать pdf документа.
     *
     * @param pdfUrl - url к pdf документу
     */
    public static native void printPdfDocument(final String pdfUrl) /*-{
        var pdfFrame = document.getElementById("pdfFrame");
        if (pdfFrame == null) {
            pdfFrame = document.createElement("iframe");
            pdfFrame.setAttribute("id", "pdfFrame");
            document.body.appendChild(pdfFrame);
            pdfFrameStyle = document.getElementById("pdfFrame").style;
            pdfFrameStyle.position = "absolute";
            pdfFrameStyle.top = "0px";
            pdfFrameStyle.left = "0px";
            pdfFrameStyle.border = "none";
            pdfFrameStyle.filter = "alpha(opacity=0)";
            pdfFrameStyle.visibility = "hidden";
        }
        pdfFrame.src = pdfUrl;
    }-*/;

    /**
     * Получение заголовка списка документов
     *
     * @param count    количество элементов
     * @param category категория
     * @return заголовок
     */
    public static String createCaptionTitle(final int count, final String category) {
        return category.replace("<br/>", " ") + "<span style='color:#003366'>&nbsp;" + count + "</span>";
    }

    /**
     * Скрываем элемент на странице
     *
     * @param element элемент
     */
    public static void invisibleElement(final Canvas element) {
        String curStyle = element.getStyleName();
        String newStyle = HIDE_CSS_STYLE;
        if (curStyle != null && curStyle.trim().length() > 0) {
            newStyle = curStyle.replace(SHOW_CSS_STYLE, "") + " " + newStyle;
        }
        element.setStyleName(newStyle);
    }

    /**
     * Показываем элемент на странице
     *
     * @param element элемент
     */
    public static void visibleElement(final Canvas element) {
        String curStyle = element.getStyleName();
        String newStyle = "";
        if (curStyle != null && curStyle.trim().length() > 0) {
            newStyle = curStyle.replace(HIDE_CSS_STYLE, "");
            if (newStyle.trim().length() < 1) {
                newStyle = SHOW_CSS_STYLE;
            }
        }
        element.setStyleName(newStyle);
    }

    /**
     * Получаем полный путь к рессурсам
     *
     * @return путь
     */
    public static native String getRessurcePath() /*-{
        return $wnd.webResourcesDir;
    }-*/;


    /**
     * Получаем идентификатор браузера
     *
     * @return идентификатор браузера
     */
    public static native String getBrowser() /*-{
        return $wnd.BrowserDetect.browser;
    }-*/;

    /**
     * Получаем идентификатор версии браузера
     *
     * @return идентификатор версии браузера
     */
    public static native String getBrowserVersion() /*-{
        return $wnd.BrowserDetect.version;
    }-*/;

    /**
     * Получаем идентификатор ОС
     *
     * @return идентификатор ОС
     */
    public static native String getOS() /*-{
        return $wnd.BrowserDetect.OS;
    }-*/;

    /**
     * Получаем идентификатор мажорной версии флеш
     *
     * @return идентификатор
     */
    public static native String getFlashMajorVersion() /*-{
        if (typeof $wnd.deconcept.SWFObjectUtil != undefined) {
            var version = $wnd.deconcept.SWFObjectUtil.getPlayerVersion();
            return "" + version["major"];
        }
        return "";
    }-*/;

    /**
     * Получаем идентификатор минорной версии флеш
     *
     * @return идентификатор
     */
    public static native String getFlashMinorVersion() /*-{
        if (typeof $wnd.deconcept.SWFObjectUtil != undefined) {
            var version = $wnd.deconcept.SWFObjectUtil.getPlayerVersion();
            return "" + version["minor"];
        }
        return "";
    }-*/;

    public static List<SearchCriteriaDTO> getSearchCriterias(final DSRequest request) {
        List<SearchCriteriaDTO> searchCriterias = new LinkedList<SearchCriteriaDTO>();
        JavaScriptObject data = request.getData();
        for (String property : JSOHelper.getProperties(data)) {
            String value = JSOHelper.getAttribute(data, property);
            if (value != null && value.length() > 0 && !"__gwt_ObjectId".equals(property)) {
                SearchCriteriaDTO criteriaDTO = new SearchCriteriaDTO();
                criteriaDTO.setAttribute(property);
                criteriaDTO.setOperation(SearchCriteriaOperation.HAS.toString());
                criteriaDTO.setValue(value);
                searchCriterias.add(criteriaDTO);
            }
        }
        return searchCriterias;
    }
    
}

