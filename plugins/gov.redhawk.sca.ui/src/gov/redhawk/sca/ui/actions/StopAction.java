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
package gov.redhawk.sca.ui.actions;

import gov.redhawk.sca.ui.ScaUiPlugin;
import gov.redhawk.sca.util.PluginUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import CF.ResourceOperations;
import CF.ResourcePackage.StopError;

/**
 * @since 3.0
 * 
 */
public class StopAction extends Action {
	private Object context;

	public StopAction() {
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ScaUiPlugin.PLUGIN_ID, "icons/clcl16/stop.png"));
		setText("Stop");
		setToolTipText("Stop");
		this.setEnabled(false);
	}

	public void setContext(final Object obj) {
		this.context = obj;
		setEnabled(PluginUtil.adapt(ResourceOperations.class, obj) != null);
	}

	@Override
	public void run() {
		if (!this.isEnabled()) {
			return;
		}
		stop(this.context);
	}

	private void stop(final Object obj) {
		final ResourceOperations device = PluginUtil.adapt(ResourceOperations.class, obj);
		if (device != null) {
			final Job job = new Job("Stopping: " + device.identifier()) {

				@Override
				protected IStatus run(final IProgressMonitor monitor) {
					monitor.beginTask("Stopping: " + device.identifier(), IProgressMonitor.UNKNOWN);
					try {
						device.stop();
						return Status.OK_STATUS;
					} catch (final StopError e) {
						return new Status(IStatus.ERROR, ScaUiPlugin.PLUGIN_ID, "Failed to stop: " + device.identifier(), e);
					}
				}

			};
			job.setUser(true);
			job.schedule();
		}
	}
}
