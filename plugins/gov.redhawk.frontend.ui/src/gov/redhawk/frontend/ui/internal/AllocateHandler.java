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

import gov.redhawk.frontend.TunerContainer;
import gov.redhawk.frontend.TunerStatus;
import gov.redhawk.frontend.UnallocatedTunerContainer;
import gov.redhawk.frontend.edit.utils.TunerUtils;
import gov.redhawk.frontend.impl.TunerContainerImpl;
import gov.redhawk.frontend.provider.TunerStatusItemProvider;
import gov.redhawk.frontend.ui.FrontEndUIActivator;
import gov.redhawk.frontend.ui.wizard.TunerAllocationDetailWizard;
import gov.redhawk.frontend.ui.wizard.TunerAllocationWizard;
import gov.redhawk.model.sca.ScaDevice;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
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

		// This check is used to tie the allocation wizard into the view toolbar buttons
		if (selection == null || selection.getFirstElement() == null) {
			TunerStatus currentSelection = TunerStatusItemProvider.getCurrentSelection();
			if (currentSelection != null) {
				selection = new StructuredSelection(new Object[] { currentSelection });
			}

			if (currentSelection.getAllocationID() != null && !currentSelection.getAllocationID().equals("")) {
				Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
				MessageBox alreadyAllocated = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				alreadyAllocated.setMessage("Tuner is already allocated");
				alreadyAllocated.setText("Allocation Failed");
				alreadyAllocated.open();
				return null;
			}
		}

		if (selection.getFirstElement() instanceof TunerStatus && selection.size() > 1) {
			Object[] items = selection.toArray();
			TunerStatus[] tuners = castArray(items, new TunerStatus[0]);
			if (tuners.length > 0) {
				WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new TunerAllocationWizard(tuners[0]));
				dialog.open();
			}
		} else {
			Object obj = selection.getFirstElement();
			if (obj instanceof UnallocatedTunerContainer) {
				UnallocatedTunerContainer container = (UnallocatedTunerContainer) obj;
				TunerStatus[] tuners = getUnallocatedTunersOfType(container.getTunerContainer(), container.getTunerType());
				if (tuners.length > 0) {
					WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new TunerAllocationWizard(tuners[0]));
					dialog.open();
				}
			} else if (obj instanceof TunerContainer) {
				TunerContainer container = (TunerContainer) obj;
				TunerStatus[] tuners = container.getTunerStatus().toArray(new TunerStatus[0]);
				if (tuners.length > 0) {
					WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new TunerAllocationWizard(tuners[0]));
					dialog.open();
				}
			}
		}

		return null;
	}

	private TunerStatus[] getUnallocatedTunersOfType(TunerContainer container, String tunerType) {
		List <TunerStatus> tuners = new ArrayList <TunerStatus>();
		for (TunerStatus tuner: container.getTunerStatus()) {
			if ((tuner.getAllocationID() == null || tuner.getAllocationID().length() == 0) &&
					tuner.getTunerType().equals(tunerType)) {
				tuners.add(tuner);
			}
		}
		return tuners.toArray(new TunerStatus[0]);
	}

	@SuppressWarnings("unchecked")
	private < T > T[] castArray(Object[] objects, T[] array) {
		List<T> list = new ArrayList<T>();
		for (Object obj : objects) {
			list.add((T) obj);
		}
		return (T[]) list.toArray(array);
	}

}
