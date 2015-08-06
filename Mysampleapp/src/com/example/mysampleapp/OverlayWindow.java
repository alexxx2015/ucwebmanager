/**
 * 
 */
package com.example.mysampleapp;

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Button;

/**
 * @author Student
 *
 */
public class OverlayWindow extends AbstractExtension  {

	/**
	 * 
	 */
	public OverlayWindow() {
		// TODO Auto-generated constructor stub
	}

	 public OverlayWindow(Button field) {
	        super.extend(field);
	    }

	    // Or in an extend() method
	    public void extend(Button field) {
	        super.extend(field);
	    }

	    // Or with a static helper
	    public static void addTo(Button field)
	    {
	        new OverlayWindow().extend(field);
	    }
}
