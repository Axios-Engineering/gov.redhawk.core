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
package gov.redhawk.ui.port.nxmblocks;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * Used before custom converter in data bindings (e.g. afterGetValidator when value from widget is still a String).
 * @NonNullByDefault
 * @since 4.4 (package-private for now)
 */
class StringToDoubleValidator extends AllowableStringValidator {
	
	public StringToDoubleValidator(String fieldName, String... allowableStrings) {
		this(fieldName, true, true, allowableStrings);
	}
	
	public StringToDoubleValidator(String fieldName, boolean allowNull, boolean allowEmpty, String... allowableStrings) {
		super(fieldName, allowNull, allowEmpty, allowableStrings);
	}
	
//	@Override
//	public IStatus validate(Object value) {
//		if (value == null) {
//			if (!allowNull) {
//				return ValidationStatus.error(fieldName + " cannot be null.");
//			}
//		} else if ("".equals(value)) {
//			if (!allowEmpty) {
//				return ValidationStatus.error(fieldName + " cannot be empty.");
//			}
//		} else {
//			for (String str : allowableStrings) {
//				if (str.equals(value)) {
//					return ValidationStatus.ok();
//				}
//			}
//			try {
//				Double.parseDouble((String) value);
//			} catch (NumberFormatException nfe) {
//				return ValidationStatus.error(fieldName + " must be a floating point number.");
//			}
//		}
//		return ValidationStatus.ok(); // passed all checks
//	}
	
	@Override
	public IStatus doValidate(String value) {
		try {
			Double.parseDouble((String) value);
		} catch (NumberFormatException nfe) {
			return ValidationStatus.error(getFieldName() + " must be a floating point number.");
		}
		return ValidationStatus.ok();
	}
}
