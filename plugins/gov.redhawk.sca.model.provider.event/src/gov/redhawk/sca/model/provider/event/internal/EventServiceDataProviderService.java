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
package gov.redhawk.sca.model.provider.event.internal;

import gov.redhawk.model.sca.services.AbstractDataProviderService;
import gov.redhawk.model.sca.services.IScaDataProvider;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;

/**
 * 
 */
public class EventServiceDataProviderService extends AbstractDataProviderService {

	private final EventServiceProviderSwitch s = new EventServiceProviderSwitch();

	@Override
	protected IScaDataProvider createDataProvider(final EObject object) {
		return this.s.doSwitch(object);
	}

	public void refresh(final EObject object, final IProgressMonitor monitor) {
		// Pass...this data provider has no refresh concept
	}

}
