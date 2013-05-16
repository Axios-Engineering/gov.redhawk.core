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

 // BEGIN GENERATED CODE
package gov.redhawk.model.sca;

import mil.jpeojtrs.sca.prf.SimpleSequence;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Sequence Property</b></em>'.
 *  @since 9.0
 *  @noimplement This interface is not intended to be implemented by clients.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.redhawk.model.sca.ScaSimpleSequenceProperty#getValues <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.redhawk.model.sca.ScaPackage#getScaSimpleSequenceProperty()
 * @model extendedMetaData="name='ScaSimpleSequenceProperty' kind='empty'"
 * @generated
 */
public interface ScaSimpleSequenceProperty extends ScaAbstractProperty<SimpleSequence> {

	/**
	 * Returns the value of the '<em><b>Values</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Object}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Values</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' attribute list.
	 * @see gov.redhawk.model.sca.ScaPackage#getScaSimpleSequenceProperty_Values()
	 * @model unique="false" transient="true"
	 *        extendedMetaData="kind='attribute' name='currentValue'"
	 * @generated
	 */
	EList<Object> getValues();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model newValueDataType="gov.redhawk.model.sca.ObjectArray"
	 * @generated
	 */
	void setValue(Object[] newValue);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="gov.redhawk.model.sca.ObjectArray"
	 * @generated
	 */
	Object[] getValue();

} // ScaSimpleSequenceProperty
