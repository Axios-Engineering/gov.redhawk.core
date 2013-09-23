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

import gov.redhawk.model.sca.DomainConnectionState;
import gov.redhawk.model.sca.ScaDomainManager;
import gov.redhawk.sca.ui.ScaUiPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class InitHandler.
 */
public class DisconnectDomainHandler extends AbstractHandler implements IHandler {

	@Override
	public void setEnabled(final Object evaluationContext) {
		if ((evaluationContext != null) && (evaluationContext instanceof EvaluationContext)) {
			final EvaluationContext context = (EvaluationContext) evaluationContext;
			final Object sel = context.getVariable("selection");
			if (sel instanceof IStructuredSelection) {
				final IStructuredSelection ss = (IStructuredSelection) sel;
				boolean enabled = true;
				for (final Object obj : ss.toArray()) {
					if (obj instanceof ScaDomainManager) {
						final ScaDomainManager domMgr = (ScaDomainManager) obj;
						if (!domMgr.getState().equals(DomainConnectionState.CONNECTED)) {
							enabled = false;
						}
					}
				}
				this.setBaseEnabled(enabled);
			} else {
				super.setEnabled(evaluationContext);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		if (selection == null) {
			selection = HandlerUtil.getCurrentSelection(event);
		}
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			for (final Object obj : ss.toArray()) {
				if (obj instanceof ScaDomainManager) {
					final ScaDomainManager domMgr = (ScaDomainManager) obj;
					final Job job = new Job("Disconnecting Domain") {

						@Override
						protected IStatus run(final IProgressMonitor monitor) {
							monitor.beginTask("Disconnecting from domain " + domMgr.getName(), IProgressMonitor.UNKNOWN);
							try {
								domMgr.disconnect();
								return Status.OK_STATUS;
							} catch (final Exception e) {
								return new Status(IStatus.ERROR, ScaUiPlugin.PLUGIN_ID, "Failed to connect", e);
							}
						}

					};
					job.setPriority(Job.LONG);
					job.setUser(true);
					job.schedule();
				}
			}
		}
		// TODO Auto-generated method stub
		return null;
	}

}
