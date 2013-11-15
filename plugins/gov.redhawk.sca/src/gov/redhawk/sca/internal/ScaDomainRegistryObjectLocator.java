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
package gov.redhawk.sca.internal;

import gov.redhawk.model.sca.CorbaObjWrapper;
import gov.redhawk.model.sca.services.AbstractScaObjectLocator;
import gov.redhawk.sca.ScaPlugin;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * 
 */
public class ScaDomainRegistryObjectLocator extends AbstractScaObjectLocator {

	@Override
	protected TreeIterator<EObject> getContentIterator(final Class< ? extends CorbaObjWrapper< ? >> type, final String ior) {
		//If this method is invoked in RAP, the domain registry will not be retrieved, because a Display
		//should be provided. To avoid creating a dependency on the UI, we don't provide one.
		return EcoreUtil.getAllContents(ScaPlugin.getDefault().getDomainManagerRegistry(), false);
	}

}
