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
package gov.redhawk.sca.sad.diagram.edit.parts;

import gov.redhawk.diagram.edit.parts.IProvidesPortStubNameEditPart;
import gov.redhawk.diagram.edit.parts.ProvidesPortStubNameEditPartHelper;
import gov.redhawk.sca.sad.diagram.RedhawkSadDiagramPlugin;

import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;

public class ProvidesPortStubNameEditPart extends mil.jpeojtrs.sca.sad.diagram.edit.parts.ProvidesPortStubNameEditPart implements IProvidesPortStubNameEditPart {

	private final ProvidesPortStubNameEditPartHelper editPartHelper = new ProvidesPortStubNameEditPartHelper(this);

	public ProvidesPortStubNameEditPart(final View view) {
		super(view);
		disableEditMode();
	}

	@Override
	protected boolean isEditable() {
		return this.editPartHelper.isEditable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addNotationalListeners() {
		this.editPartHelper.addNotationalListeners();
	}

	@Override
	protected void removeNotationalListeners() {
		this.editPartHelper.removeNotationalListeners();
	}

	@Override
	public void enableEditMode() {
		this.editPartHelper.enableEditMode();
	}

	@Override
	protected void refreshVisibility() {
		this.editPartHelper.refreshVisibility();
	}

	@Override
	public boolean isSelectable() {
		return this.editPartHelper.isSelectable();
	}

	@Override
	public void basicAddNotationalListeners() {
		super.addNotationalListeners();
	}

	@Override
	public IPreferenceStore getPreferenceStore() {
		return RedhawkSadDiagramPlugin.getDefault().getPreferenceStore();
	}

	@Override
	public void basicRemoveNotationalListeners() {
		super.removeNotationalListeners();
	}

	@Override
	public void setVisibility(final boolean vis) {
		super.setVisibility(vis);
	}
}
