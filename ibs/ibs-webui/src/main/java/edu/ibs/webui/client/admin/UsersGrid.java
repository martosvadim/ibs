package edu.ibs.webui.client.admin;

import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import edu.ibs.webui.client.utils.Components;

/**
 * Created with IntelliJ IDEA.
 * User: Rimsky
 * Date: 12.01.13
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class UsersGrid extends ListGrid{
    public UsersGrid(){
        super();
        Components.prepareGrid(this);
        this.setShowAllRecords(true);
        ListGridField firstnameField = new ListGridField("firstname","Имя",100);
        ListGridField surnameField = new ListGridField("surname", "Фамилия", 100);
        ListGridField emailField = new ListGridField("email", "Email", 100);
        ListGridField passportField = new ListGridField("passport", "№ пасспорта", 100);

        ListGridField emptyField = new ListGridField("emptyField", " ");
        this.setFields(new ListGridField[]{firstnameField, surnameField, emailField, passportField, emptyField});
        this.setDataSource(new UsersDataSource());
    }
}
