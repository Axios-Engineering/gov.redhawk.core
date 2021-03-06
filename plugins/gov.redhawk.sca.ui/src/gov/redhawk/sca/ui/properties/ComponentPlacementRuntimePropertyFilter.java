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
package gov.redhawk.sca.ui.properties;

import gov.redhawk.model.sca.ScaComponent;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IFilter;

/**
 * @since 9.1
 */
public class ComponentPlacementRuntimePropertyFilter implements IFilter {

	@Override
	public boolean select(final Object toTest) {
		final Object adapter = Platform.getAdapterManager().getAdapter(toTest, ScaComponent.class);
		return adapter instanceof ScaComponent;
	}

}
