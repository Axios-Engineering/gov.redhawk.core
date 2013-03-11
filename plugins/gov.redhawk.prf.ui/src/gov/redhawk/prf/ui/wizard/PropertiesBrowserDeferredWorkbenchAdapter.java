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
package gov.redhawk.prf.ui.wizard;

import gov.redhawk.sca.ScaPlugin;
import gov.redhawk.sca.properties.IPropertiesProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.progress.IElementCollector;

public class PropertiesBrowserDeferredWorkbenchAdapter implements IDeferredWorkbenchAdapter {
	
	private final Map<Object, List<IPropertiesProvider>> map;;
	
	public PropertiesBrowserDeferredWorkbenchAdapter(final Map<Object, List<IPropertiesProvider>> map) {
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getChildren(Object o) {
		return Collections.EMPTY_LIST.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	public ImageDescriptor getImageDescriptor(Object object) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLabel(Object o) {
		return "Browse Properties";
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getParent(Object o) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void fetchDeferredChildren(Object object, IElementCollector collector, IProgressMonitor monitor) {
		List<IPropertiesProvider> providers = loadPropertiesProviders(monitor);
		collector.add(providers, monitor);
		this.map.put(object, providers);
		collector.done();
		monitor.done();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isContainer() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public ISchedulingRule getRule(Object object) {
		return null;
	}
	
	public List<IPropertiesProvider> loadPropertiesProviders(final IProgressMonitor monitor) {
		return ScaPlugin.getPropertiesProviderRegistry().getPropertiesProviders();
	}

}
