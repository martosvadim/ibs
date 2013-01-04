package edu.ibs.webui.client.controller;

import com.smartgwt.client.widgets.Window;
import edu.ibs.webui.client.utils.Components;

/**
 * User: EgoshinME
 * Date: 04.01.13
 * Time: 12:29
 */
public class GenericWindowController extends GenericController {

	private Window window = Components.getWindow();

	public Window getWindow() {
		return window;
	}
}
