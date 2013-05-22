/** 
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.ui.port.nxmplot;

import org.eclipse.swt.widgets.Composite;

/**
 * @since 3.0
 */
public interface INxmPlotWidgetFactory {
	
	/**
	 * Creates a plot
	 * Pass configuration paramters to the plot in the init call.  Recommend to pass arguments to init via a job.
	 * @param parent
	 * @param style
	 * @return
	 */
	public AbstractNxmPlotWidget createPlotWidget(final Composite parent, int style);
}
