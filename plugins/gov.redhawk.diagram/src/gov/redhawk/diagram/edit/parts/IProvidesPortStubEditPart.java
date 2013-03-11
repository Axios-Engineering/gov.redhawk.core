/** 
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;

/**
 * @since 3.0
 * 
 */
public interface IProvidesPortStubEditPart extends IGraphicalEditPart {

	IFigure basicCreateNodeShape();

	boolean isInstanceofProvidesPortStubNameEditPart(IBorderItemEditPart borderItemEditPart);

	void basicAddBorderItem(IFigure borderItemContainer, IBorderItemEditPart borderItemEditPart);

	IFigure getPrimaryShape();

	IFigure getMainFigure();

	void basicAddSemanticListeners();

	/**
	 * @since 4.0
	 */
	void setVisibility(boolean b);

	void basicRemoveSemanticListeners();

}
