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
package gov.redhawk.core.resourcefactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;

public abstract class AbstractResourceFactoryProvider implements IResourceFactoryProvider, IExecutableExtension {
	private int priority;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private List<ResourceDesc> descriptors = new ArrayList<ResourceDesc>();

	/**
	 * @since 2.0
	 */
	public int getPriority() {
		return this.priority;
	}

	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data) throws CoreException {
		try {
			this.priority = Integer.valueOf(config.getAttribute("priority"));
		} catch (final Exception e) {
			this.priority = Integer.MAX_VALUE;
		}
	}

	/**
	 * @since 2.0
	 */
	public void addResourceDesc(ResourceDesc desc) {
		descriptors.add(desc);
		fireAddResourceDescriptor(desc);
	}

	/**
	 * @since 2.0
	 */
	public void removeResourceDesc(ResourceDesc desc) {
		descriptors.remove(desc);
		fireRemoveResourceDescriptor(desc);
	}

	/**
	 * @since 2.0
	 */
	protected void fireRemoveResourceDescriptor(ResourceDesc desc) {
		pcs.firePropertyChange(PROPERTY_RESOURCE_DESCRIPTORS, desc, null);
	}

	/**
	 * @since 2.0
	 */
	protected void fireAddResourceDescriptor(ResourceDesc desc) {
		pcs.firePropertyChange(PROPERTY_RESOURCE_DESCRIPTORS, null, desc);
	}

	/**
	 * @since 2.0
	 */
	public List<ResourceDesc> getResourceDescriptors() {
		return Collections.unmodifiableList(new ArrayList<ResourceDesc>(descriptors));
	}

	/**
	 * @since 2.0
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	/**
	 * @since 2.0
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}
