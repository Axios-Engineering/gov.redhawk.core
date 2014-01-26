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
package gov.redhawk.ui.port.nxmblocks;

import gov.redhawk.ui.port.nxmplot.AbstractNxmPlotWidget;

import java.nio.ByteOrder;
import java.text.MessageFormat;

import nxm.redhawk.prim.sourcenic;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Composite;

/**
 * SDDS (UDP/Multicast) source NXM block.
 * @noreference This class is provisional/beta and is subject to API changes
 * @since 4.4
 */
public class SddsNxmBlock extends AbstractNxmBlock<sourcenic> {

	private SddsNxmBlockSettings settings;

	public SddsNxmBlock(@NonNull AbstractNxmPlotWidget plotWidget, @NonNull SddsNxmBlockSettings settings) {
		super(sourcenic.class, SddsNxmBlockSettings.class, "SDDS", plotWidget);
		this.settings = settings;
	}

	@Override
	public int getMaxInputs() {
		return 0; // SDDS source is starting point (so it has no inputs)
	}

	@Override
	@NonNull
	protected String formCmdLine(@NonNull AbstractNxmPlotWidget plotWidget, String streamID) {
		final String outputName = AbstractNxmPlotWidget.createUniqueName(true);
		putOutputNameMapping(0, streamID, outputName); // save output name mapping

		final StringBuilder switches = new StringBuilder("");
		final int pipeSize = settings.getPipeSize(); // in bytes
		if (pipeSize > 0) {
			switches.append("/PS=").append(pipeSize);
		}
		ByteOrder byteOrder = settings.getDataByteOrder();
		String outputFormat = settings.getOutputFormat();
		if (outputFormat == null) {
			outputFormat = "";
		}

		String pattern = "SOURCENIC{0}/BG/BYTEORDER={1}/FC={2}/MGRP={3}/PORT={4,number,#}/VLAN={5,number,#} OUT={6}";
		String cmdLine = MessageFormat.format(pattern, switches, byteOrder, outputFormat,
				settings.getMcastAddress(), settings.getPort(), settings.getVlan(), outputName);

		return cmdLine;
	}

	@Override
	@NonNull
	public SddsNxmBlockSettings getSettings() {
		SddsNxmBlockSettings clone;
		if (settings != null) {
			clone = settings.clone();
		} else {
			clone = new SddsNxmBlockSettings();
		}
		return clone;
	}

	@Override
	protected void applySettingsTo(sourcenic cmd, Object settings, String streamId) {
		if (settings instanceof SddsNxmBlockSettings) {
			SddsNxmBlockSettings newSettings = (SddsNxmBlockSettings) settings;
			ByteOrder byteOrder = newSettings.getDataByteOrder();
			cmd.setDataByteOrder(byteOrder);
		}
	}

	@Override
	public boolean hasControls() {
		return true;
	}

	@Override
	public void createControls(Composite parent, Object settings, DataBindingContext dataBindingContext) {
		SddsNxmBlockSettings blockSettings = null;
		if (settings instanceof SddsNxmBlockSettings) {
			blockSettings = (SddsNxmBlockSettings) settings;
		}
		new SddsNxmBlockControls(blockSettings, dataBindingContext).createControls(parent);
	}
}
