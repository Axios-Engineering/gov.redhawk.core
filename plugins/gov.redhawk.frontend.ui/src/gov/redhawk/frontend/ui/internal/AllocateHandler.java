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
package gov.redhawk.frontend.ui.internal;

import gov.redhawk.frontend.Tuner;
import gov.redhawk.frontend.TunerContainer;
import gov.redhawk.frontend.ui.wizard.TunerAllocationWizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 *
 */
public class AllocateHandler extends AbstractHandler implements IHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveMenuSelection(event);
		if (selection == null) {
			selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		}
		if (selection == null) {
			return null;
		}

		if (selection.getFirstElement() instanceof Tuner && selection.size() > 1) {
			Object[] selObjects = selection.toArray();
			Tuner[] tuners = this.<Tuner>castArray(selObjects, new Tuner[0]);
			WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new TunerAllocationWizard(tuners));
			dialog.open(); 
		} else {
			Object obj = selection.getFirstElement();
			if (obj instanceof Tuner) {
				Tuner tuner = (Tuner) obj;
				WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new TunerAllocationWizard(tuner));
				dialog.open(); 
			} else if (obj instanceof TunerContainer) {
				TunerContainer container = (TunerContainer) obj;
				List<Tuner> tuners = new ArrayList<Tuner>();
				for (Tuner tuner : container.getTuners()) {
					if (tuner.getAllocationID() == null || "".equals(tuner.getAllocationID().trim())) {
						tuners.add(tuner);
					}
				}
				WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new TunerAllocationWizard(tuners.toArray(new Tuner[0])));
				dialog.open(); 
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T[] castArray(Object[] objects, T[] array) {
		List<T> list = new ArrayList<T>();
		for (Object obj : objects) {
			list.add((T) obj);
		}
		return (T[]) list.toArray(array);
	}

}
