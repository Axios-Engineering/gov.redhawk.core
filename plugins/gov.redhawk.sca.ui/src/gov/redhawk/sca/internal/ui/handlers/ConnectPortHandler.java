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
package gov.redhawk.sca.internal.ui.handlers;

import gov.redhawk.model.sca.ScaComponent;
import gov.redhawk.model.sca.ScaProvidesPort;
import gov.redhawk.model.sca.ScaUsesPort;
import gov.redhawk.sca.ui.ScaUiPlugin;
import mil.jpeojtrs.sca.scd.SupportsInterface;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.omg.CORBA.SystemException;

import CF.PortPackage.InvalidPort;
import CF.PortPackage.OccupiedPort;

/**
 * 
 */
public class ConnectPortHandler extends AbstractHandler implements IHandler {

	private static class ConnectJob extends Job {
		private static int number = 1;

		private final org.omg.CORBA.Object target;
		private final ScaUsesPort usesPort;

		public ConnectJob(final ScaUsesPort usesPort, final org.omg.CORBA.Object target) {
			super("Connecting");
			this.usesPort = usesPort;
			this.target = target;
			setPriority(Job.LONG);
			setUser(true);
		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			monitor.beginTask("Connecting", IProgressMonitor.UNKNOWN);
			final String username = System.getProperty("user.name", "user");
			final String connectionId = username + "_" + (ConnectJob.number++);
			try {
				this.usesPort.connectPort(this.target, connectionId);
			} catch (final InvalidPort e) {
				return new Status(IStatus.ERROR, ScaUiPlugin.PLUGIN_ID, "Failed to connect " + e.msg, e);
			} catch (final OccupiedPort e) {
				return new Status(IStatus.ERROR, ScaUiPlugin.PLUGIN_ID, "Failed to connect", e);
			} catch (final SystemException e) {
				return new Status(IStatus.ERROR, ScaUiPlugin.PLUGIN_ID, "Failed to connect", e);
			}
			return Status.OK_STATUS;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		if (sel instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) sel;
			ScaUsesPort usesPort = null;
			org.omg.CORBA.Object target = null;
			for (final Object obj : ss.toArray()) {
				if (obj instanceof ScaUsesPort) {
					usesPort = (ScaUsesPort) obj;
				} else if (obj instanceof ScaProvidesPort) {
					target = ((ScaProvidesPort) obj).getCorbaObj();
				} else if (obj instanceof ScaComponent) {
					target = ((ScaComponent) obj).getCorbaObj();
				}
			}
			if (usesPort != null && target != null) {
				new ConnectJob(usesPort, target).schedule();
			}
		}
		return null;
	}

	@Override
	public void setEnabled(final Object evaluationContext) {
		// TODO Auto-generated method stub
		if ((evaluationContext != null) && (evaluationContext instanceof EvaluationContext)) {
			final EvaluationContext context = (EvaluationContext) evaluationContext;
			final Object sel = context.getVariable("selection");
			if (sel instanceof IStructuredSelection) {
				final IStructuredSelection ss = (IStructuredSelection) sel;
				ScaUsesPort usesPort = null;
				EObject target = null;
				for (final Object obj : ss.toArray()) {
					if (obj instanceof ScaUsesPort) {
						usesPort = (ScaUsesPort) obj;
					} else if (obj instanceof ScaProvidesPort) {
						target = (EObject) obj;
					} else if (obj instanceof ScaComponent) {
						target = (EObject) obj;
					}
				}
				if (usesPort != null && usesPort.getProfileObj() != null && usesPort.getProfileObj().getInterface() != null) {
					if (target instanceof ScaProvidesPort) {
						final ScaProvidesPort providesPort = (ScaProvidesPort) target;
						setBaseEnabled(usesPort.getProfileObj().getInterface().isInstance(providesPort.getProfileObj().getInterface()));
					} else if (target instanceof ScaComponent) {
						final ScaComponent component = (ScaComponent) target;
						for (final SupportsInterface i : component.getProfileObj().getDescriptor().getComponent().getComponentFeatures().getSupportsInterface()) {
							if (usesPort.getProfileObj().getInterface().isInstance(i.getInterface())) {
								setBaseEnabled(true);
								return;
							}
						}
						setBaseEnabled(false);
					} else {
						setBaseEnabled(false);
					}
				} else {
					setBaseEnabled(false);
				}
			} else {
				super.setEnabled(evaluationContext);
			}
		}
	}

}
