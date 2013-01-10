package edu.ibs.webui.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.webui.client.cards.ContextAreaListGrid;

/**
 * User: Максим
 * Date: 29.10.12
 * Time: 23:01
 */
public class AccountView extends VLayout {

	private ContextAreaListGrid contextAreaListGrid;

    public AccountView() {
        super();
        GWT.log("init AccountView()...", null);
        this.setStyleName("crm-ContextArea");
        this.setWidth("*");
        this.addMember(getContextAreaListGrid());
    }

	public ContextAreaListGrid getContextAreaListGrid() {
		if (contextAreaListGrid == null) {
			contextAreaListGrid = new ContextAreaListGrid();
		}
		return contextAreaListGrid;
	}
}
