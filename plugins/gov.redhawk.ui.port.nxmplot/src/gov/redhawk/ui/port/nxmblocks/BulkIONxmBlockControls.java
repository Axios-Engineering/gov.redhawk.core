/**
 * This file is protected by Copyright.
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package gov.redhawk.ui.port.nxmblocks;

import gov.redhawk.sca.util.ArrayUtil;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.StringToNumberConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Adjust/override BULKIO NXM block settings user interface/entry control widgets.
 * @NonNullByDefault
 * @noreference This class is provisional/beta and is subject to API changes
 * @since 4.3
 */
public class BulkIONxmBlockControls {

	private static final String VALUE_USE_DEFAULT = "default";
	private static final String SAMPLE_RATE_FIELD_NAME = "Sample Rate";
	
	private final BulkIONxmBlockSettings settings;
	private final DataBindingContext dataBindingCtx;
	
	// widgets
	private Text connectionIDField;
	private ComboViewer sampleRateField;
	private Button blockingField;

	public BulkIONxmBlockControls(BulkIONxmBlockSettings settings, DataBindingContext dataBindingCtx) {
		this.settings = settings;
		this.dataBindingCtx = dataBindingCtx;
	}

	public void createControls(final Composite container) {
		container.setLayout(new GridLayout(2, false));
		Label label;
		
		// === connection ID ==
		label = new Label(container, SWT.None);
		label.setText("Connection ID:");
		connectionIDField = new Text(container, SWT.BORDER);
		connectionIDField.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		connectionIDField.setToolTipText("Custom Port connection ID to use vs a generated one.");
		if (this.settings.getConnectionID() != null) {
			connectionIDField.setEditable(false); // cannot change custom connection ID after it has been set at this time
		}

		// === sample rate ===
		label = new Label(container, SWT.NONE);
		label.setText(SAMPLE_RATE_FIELD_NAME + ":");
		this.sampleRateField = new ComboViewer(container, SWT.BORDER); // writable
		this.sampleRateField.getCombo().setLayoutData(GridDataFactory.fillDefaults().grab(true,  false).create());
		this.sampleRateField.getCombo().setToolTipText("Custom sample rate to override value in StreamSRI. Default uses value from StreamSRI.");
		this.sampleRateField.setContentProvider(ArrayContentProvider.getInstance()); // ArrayContentProvider does not store any state, therefore can re-use instances
		this.sampleRateField.setLabelProvider(new LabelProvider());
		Object[] inputValues = ArrayUtil.copyAndPrependIfNotNull(this.settings.getSampleRate(), VALUE_USE_DEFAULT);
		this.sampleRateField.setInput(inputValues);
		this.sampleRateField.setSelection(new StructuredSelection(inputValues[0])); // select first value (which is current value or default)

		// === blocking option ===
		label = new Label(container, SWT.NONE);
		label.setText("Blocking:");
		this.blockingField = new Button(container, SWT.CHECK);
		this.blockingField.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		this.blockingField.setToolTipText("On/checked to block pushPacket when Plot is not able to keep up; Off to drop packets in this scenario.");

		initDataBindings();
	}

	private void initDataBindings() {
		Binding bindingValue;
		
		IObservableValue connIdWidgetValue = WidgetProperties.text(SWT.Modify).observe(connectionIDField);
		IObservableValue connIdModelValue = PojoProperties.value(BulkIONxmBlockSettings.PROP_CONNECTION_ID).observe(settings);
		bindingValue = dataBindingCtx.bindValue(connIdWidgetValue, connIdModelValue);
		ControlDecorationSupport.create(bindingValue, SWT.TOP | SWT.LEFT);
		
		IObservableValue srWidgetValue = WidgetProperties.selection().observe(sampleRateField.getCombo());
		IObservableValue srModelValue = PojoProperties.value(BulkIONxmBlockSettings.PROP_SAMPLE_RATE).observe(settings);
		UpdateValueStrategy srModelToTarget = new UpdateValueStrategy();
		srModelToTarget.setConverter(new ObjectToNullConverter()); // converts null to null, otherwise uses toString()
		bindingValue = dataBindingCtx.bindValue(srWidgetValue, srModelValue, createTargetToModelForSampleRate(), srModelToTarget);
		ControlDecorationSupport.create(bindingValue, SWT.TOP | SWT.LEFT);
		
		IObservableValue boWidgetValue = WidgetProperties.selection().observe(blockingField); 
		IObservableValue boModelValue = PojoProperties.value(BulkIONxmBlockSettings.PROP_BLOCKING_OPTION).observe(settings);
		bindingValue = dataBindingCtx.bindValue(boWidgetValue, boModelValue);
		ControlDecorationSupport.create(bindingValue, SWT.TOP | SWT.LEFT);
	}

	private UpdateValueStrategy createTargetToModelForSampleRate() {
		UpdateValueStrategy updateValueStrategy = new UpdateValueStrategy();
		
		updateValueStrategy.setAfterGetValidator(new StringToDoubleValidator(SAMPLE_RATE_FIELD_NAME, VALUE_USE_DEFAULT));
		updateValueStrategy.setConverter(new ObjectToNullConverter(StringToNumberConverter.toDouble(false), true, true, VALUE_USE_DEFAULT));
		updateValueStrategy.setAfterConvertValidator(new NumberRangeValidator<Double>(SAMPLE_RATE_FIELD_NAME, Double.class, 0.0, false));
		return updateValueStrategy;
	}

}
