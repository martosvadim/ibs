package edu.ibs.webui.client.controller;

public interface IAction<T> {
    /**
     * Обработка действия
     *
     * @param data Данные
     */
    void execute(T data);
}
