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

import gov.redhawk.frontend.FrontendFactory;
import gov.redhawk.frontend.TunerContainer;
import gov.redhawk.frontend.TunerStatus;
import gov.redhawk.frontend.UnallocatedTunerContainer;
import gov.redhawk.frontend.ui.wizard.TunerAllocationWizard;
import gov.redhawk.frontend.util.TunerUtils;
import gov.redhawk.model.sca.ScaDevice;
import gov.redhawk.sca.util.PluginUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
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
				} else {
					ScaDevice< ? > device = (ScaDevice< ? >) container.eContainer().eContainer();
					WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new TunerAllocationWizard(FrontendFactory.eINSTANCE.createTunerStatus(), device));
					warnNoTuners(event);
					dialog.open();
				}
			} else if (obj instanceof ScaDevice< ? >) {
				ScaDevice< ? > device = (ScaDevice< ? >) obj;
				TunerContainer container = TunerUtils.INSTANCE.getTunerContainer(device);
				if (container == null) {
					MessageDialog warning = new MessageDialog(HandlerUtil.getActiveShell(event), "Error - No Tuner Container Found", null, 
						"The device's tuner container could not be found.  Make sure the device's \"Data Providers Enabled\" property is set to \"true\".", 
						MessageDialog.ERROR, new String[] {"OK"}, 0);
					warning.open();
					return null;
				}
				TunerStatus[] tuners = container.getTunerStatus().toArray(new TunerStatus[0]);
				if (tuners.length > 0) {
					WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new TunerAllocationWizard(tuners[0]));
					dialog.open();
				} else {
					WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new TunerAllocationWizard(FrontendFactory.eINSTANCE.createTunerStatus(), device));
					warnNoTuners(event);
					dialog.open();
				}
			}
		}

		return null;
	}

	private void warnNoTuners(ExecutionEvent event) {
		MessageDialog warning = new MessageDialog(HandlerUtil.getActiveShell(event), "Warning - No Tuners Available", null,
			"The selected device has no tuners.  Dynamic tuner creation may not be supported.", MessageDialog.WARNING, new String[] { "OK" }, 0);
		warning.open();		
	}
	
	private TunerStatus[] getUnallocatedTunersOfType(TunerContainer container, String tunerType) {
		List<TunerStatus> tuners = new ArrayList<TunerStatus>();
		for (TunerStatus tuner : container.getTunerStatus()) {
			if ((tuner.getAllocationID() == null || tuner.getAllocationID().length() == 0) && PluginUtil.equals(tuner.getTunerType(), tunerType)) {
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
		return list.toArray(array);
	}

}
