package com.example.mysampleapp;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.shared.ui.Connect;

@Connect(OverlayWindow.class)
public class OverlayConnector extends AbstractExtensionConnector {

	// TODO Auto-generated constructor stub
	@Override
	protected void extend(ServerConnector target) {
		// Get the extended widget
		final Widget pw = ((ComponentConnector) target).getWidget();

		// Preparations for the added feature
		final VOverlay warning = new VOverlay();
		warning.setOwner(pw);
		warning.add(new HTML("Caps Lock is enabled!"));

		// Add an event handler
		pw.addDomHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (isEnabled() && isCapsLockOn(event)) {
//					warning.showRelativeTo(passwordWidget);
					warning.show();
				} else {
					warning.hide();
				}
			}
		}, KeyPressEvent.getType());
	}

	private boolean isCapsLockOn(KeyPressEvent e) {
		return e.isShiftKeyDown() ^ Character.isUpperCase(e.getCharCode());
	}
}
