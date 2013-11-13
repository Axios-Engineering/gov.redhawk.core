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
package gov.redhawk.sca.rcp.internal;

import gov.redhawk.sca.IScaDomainManagerRegistryContainer;
import gov.redhawk.sca.IScaDomainManagerRegistryFactoryService;

public class ScaDomainManagerRegistryFactoryServiceImpl implements
		IScaDomainManagerRegistryFactoryService {

	@Override
	public ScaDomainManagerRegistryContainerImpl getRegistryContainer() {
		return (ScaDomainManagerRegistryContainerImpl) getRegistryContainer(null);
	}
	
	@Override
	public IScaDomainManagerRegistryContainer getRegistryContainer(Object context) {
		return ScaDomainManagerRegistryContainerImpl.getInstance();
	}
	
	public void activate() {
		getRegistryContainer().activate();
	}
	
	public void deactivate() {
		getRegistryContainer().dispose();
	}
}
