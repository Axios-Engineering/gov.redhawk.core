/*******************************************************************************
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package gov.redhawk.bulkio.util;

import gov.redhawk.bulkio.util.internal.ConnectionManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import mil.jpeojtrs.sca.util.NamedThreadFactory;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BulkIOUtilActivator extends Plugin {
	
	private static final ExecutorService EXECUTOR_POOL = Executors.newSingleThreadExecutor(new NamedThreadFactory(BulkIOUtilActivator.class.getName()));

	// The plug-in ID
	public static final String PLUGIN_ID = "gov.redhawk.bulkio.util"; //$NON-NLS-1$

	// The shared instance
	private static BulkIOUtilActivator plugin;

	/**
	 * The constructor
	 */
	public BulkIOUtilActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		Future< ? > future = EXECUTOR_POOL.submit(new Runnable() {

			@Override
			public void run() {
				ConnectionManager.INSTANCE.dispose();
			}
			
		});
		try {
			future.get(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// PASS
		} catch (ExecutionException e) {
			// PASS
		} catch (TimeoutException e) {
			// PASS
		}
		
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BulkIOUtilActivator getDefault() {
		return plugin;
	}
	
	public static IBulkIOPortConnectionManager getBulkIOPortConnectionManager() {
		return ConnectionManager.INSTANCE;
	}

}
