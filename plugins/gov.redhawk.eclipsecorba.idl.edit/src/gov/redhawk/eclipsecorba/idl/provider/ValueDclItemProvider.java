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

 // BEGIN GENERATED CODE
package gov.redhawk.eclipsecorba.idl.provider;


import gov.redhawk.eclipsecorba.idl.IdlFactory;
import gov.redhawk.eclipsecorba.idl.IdlPackage;
import gov.redhawk.eclipsecorba.idl.ValueDcl;
import gov.redhawk.eclipsecorba.idl.operations.OperationsFactory;
import gov.redhawk.eclipsecorba.idl.types.TypesFactory;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link gov.redhawk.eclipsecorba.idl.ValueDcl} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ValueDclItemProvider
	extends ValueTypeDclItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueDclItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addInheritedValuesPropertyDescriptor(object);
			addSupportsInterfacePropertyDescriptor(object);
			addCustomPropertyDescriptor(object);
			addForwardDclPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Inherited Values feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInheritedValuesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ValueDcl_inheritedValues_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ValueDcl_inheritedValues_feature", "_UI_ValueDcl_type"),
				 IdlPackage.Literals.VALUE_DCL__INHERITED_VALUES,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Supports Interface feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSupportsInterfacePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ValueDcl_supportsInterface_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ValueDcl_supportsInterface_feature", "_UI_ValueDcl_type"),
				 IdlPackage.Literals.VALUE_DCL__SUPPORTS_INTERFACE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Custom feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCustomPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ValueDcl_custom_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ValueDcl_custom_feature", "_UI_ValueDcl_type"),
				 IdlPackage.Literals.VALUE_DCL__CUSTOM,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Forward Dcl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addForwardDclPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ValueDcl_forwardDcl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ValueDcl_forwardDcl_feature", "_UI_ValueDcl_type"),
				 IdlPackage.Literals.VALUE_DCL__FORWARD_DCL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(IdlPackage.Literals.EXPORT_CONTAINER__BODY);
			childrenFeatures.add(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns ValueDcl.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ValueDcl"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		ValueDcl p = (ValueDcl) object;
		IItemLabelProvider lp = (IItemLabelProvider) getRootAdapterFactory().adapt(p.getType(), IItemLabelProvider.class);
		String label = p.getName() + " : " + lp.getText(p.getType());
		return label == null || label.length() == 0 ?
			getString("_UI_ValueDcl_type") :
			label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ValueDcl.class)) {
			case IdlPackage.VALUE_DCL__CUSTOM:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case IdlPackage.VALUE_DCL__BODY:
			case IdlPackage.VALUE_DCL__DEFINITIONS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 IdlFactory.eINSTANCE.createIdlConstDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 IdlFactory.eINSTANCE.createIdlException()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 IdlFactory.eINSTANCE.createForwardDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 IdlFactory.eINSTANCE.createIdlInterfaceDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 IdlFactory.eINSTANCE.createNativeTypeDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 IdlFactory.eINSTANCE.createValueTypeDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 IdlFactory.eINSTANCE.createValueForwardDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 IdlFactory.eINSTANCE.createValueDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 IdlFactory.eINSTANCE.createValueBoxDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 OperationsFactory.eINSTANCE.createOperation()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 OperationsFactory.eINSTANCE.createAttribute()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 TypesFactory.eINSTANCE.createTypeDef()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 TypesFactory.eINSTANCE.createUnionType()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 TypesFactory.eINSTANCE.createEnumType()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 TypesFactory.eINSTANCE.createStructType()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 TypesFactory.eINSTANCE.createUnionForwardDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 TypesFactory.eINSTANCE.createStructForwardDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.EXPORT_CONTAINER__BODY,
				 TypesFactory.eINSTANCE.createEnumeration()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createModule()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createIdlConstDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createIdlException()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createForwardDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createIdlInterfaceDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createNativeTypeDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createValueTypeDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createValueForwardDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createValueDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 IdlFactory.eINSTANCE.createValueBoxDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 TypesFactory.eINSTANCE.createTypeDef()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 TypesFactory.eINSTANCE.createUnionType()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 TypesFactory.eINSTANCE.createEnumType()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 TypesFactory.eINSTANCE.createStructType()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 TypesFactory.eINSTANCE.createUnionForwardDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 TypesFactory.eINSTANCE.createStructForwardDcl()));

		newChildDescriptors.add
			(createChildParameter
				(IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS,
				 TypesFactory.eINSTANCE.createEnumeration()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == IdlPackage.Literals.EXPORT_CONTAINER__BODY ||
			childFeature == IdlPackage.Literals.DEFINITION_CONTAINER__DEFINITIONS;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
