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
package gov.redhawk.ui.views.domainbrowser.extend;

import gov.redhawk.model.sca.ScaComponent;
import gov.redhawk.model.sca.ScaPackage;
import gov.redhawk.model.sca.ScaWaveform;
import gov.redhawk.model.sca.provider.ScaWaveformItemProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ViewerNotification;

public class ScaWaveformItemProviderExtended extends ScaWaveformItemProvider {

	private ScaPropertyContainerItemProviderExtended propertyItemProvider;

	public ScaWaveformItemProviderExtended(final AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public Collection< ? > getChildren(final Object object) {
		final Collection< ? > superChildren = super.getChildren(object);

		if (superChildren != null && superChildren instanceof List< ? > && object instanceof ScaWaveform) {
			final Set<Object> set = new HashSet<Object>(superChildren);
			final ScaWaveform wave = (ScaWaveform) object;

			if (!wave.isDisposed()) {
				set.add(getPropertyContainterItemProvider());
				return set;
			}
		}

		return superChildren;
	}

	@Override
	public Collection< ? extends EStructuralFeature> getChildrenFeatures(final Object object) {
		final Collection< ? extends EStructuralFeature> superFeatures = super.getChildrenFeatures(object);

		if (superFeatures != null && superFeatures instanceof List< ? >) {
			final Set<EStructuralFeature> set = new HashSet<EStructuralFeature>(superFeatures);
			set.add(ScaPackage.Literals.PROFILE_OBJECT_WRAPPER__PROFILE_OBJ);

			return new ArrayList<EStructuralFeature>(set);
		}

		return superFeatures;
	}

	@Override
	public void notifyChanged(final Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ScaComponent.class)) {
		case ScaPackage.SCA_WAVEFORM__IDENTIFIER:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		case ScaPackage.PROFILE_OBJECT_WRAPPER__PROFILE_OBJ:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
			return;
		default:
			break;
		}
		super.notifyChanged(notification);
	}

	@Override
	public void dispose() {
		super.dispose();

		if (this.propertyItemProvider != null) {
			this.propertyItemProvider.dispose();
			this.propertyItemProvider = null;
		}
	}

	private ScaPropertyContainerItemProviderExtended getPropertyContainterItemProvider() {
		if (this.propertyItemProvider == null) {
			this.propertyItemProvider = new ScaPropertyContainerItemProviderExtended(this.adapterFactory, (EObject) getTarget());
		}

		return this.propertyItemProvider;
	}
}
