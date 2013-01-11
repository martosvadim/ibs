package edu.ibs.webui.client.controller;

import edu.ibs.common.dto.CardBookDTO;

/**
 * User: Максим
 * Date: 11.01.13
 * Time: 5:26
 */
public class HistoryController extends GenericWindowController {
    private CardBookDTO cardBookDto;

    public HistoryController() {
        getWindow().setTitle("Выписка по карте");
    }

    public void setCardBookDTO(CardBookDTO cardBookDTO) {
        this.cardBookDto = cardBookDTO;
    }
}
