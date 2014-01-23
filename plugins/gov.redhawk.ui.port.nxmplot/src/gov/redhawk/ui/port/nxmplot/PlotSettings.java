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
package gov.redhawk.ui.port.nxmplot;


/** <b>INTERNAL USE ONLY</b>
 * These setting are intended ONLY for the PLOT widget (not it's data source Port(s)).
 * @noreference This class is not intended to be referenced by clients.
 * @since 4.2
 */
public class PlotSettings {

	private Integer  frameSize      = null; // null to use default (e.g. from SRI) - DEPRECATED (as this is for a source)
	private Double   sampleRate     = null; // null to use default (e.g. from SRI) - DEPRECATED (as this is for a source)
	private Boolean blockingOption = false; // null to use default - DEPRECATED (as this is for a source)

	private Double   minValue       = null; // null to use default (i.e. AutoMin)
	private Double   maxValue       = null; // null to use default (i.e. AutoMax)
	private PlotType plotType       = null; // null to not change plot type (line, raster, etc.)
	private PlotMode plotMode       = null; // plot mode (i.e. complex mode (CM=)) (null to use default / no change)
	private boolean  enablePlotMenu = true; // enable/disable middle-mouse-button (MMB) to bring up PLOT's configure menu
	private String   launchArgs     = null; // additional arguments when launching/running PLOT   
	private String   launchSwitches = null; // additional switches when launching/running PLOT   

	/** plot data mode / complex mode (i.e. CM= arg or MPlot.setMode(..)).
	 * @since 4.4
	 */
	public static enum PlotMode {
		MAGNITUDE {
			public String toModeString() {
				return "Mag";
			}
		},
		PHASE {
			public String toModeString() {
				return "Phase";
			}
		},
		REAL {
			public String toModeString() {
				return "Real";
			}
		},
		IMAGINARY {
			public String toModeString() {
				return "Imag";
			}
		},
		REAL_AND_IMAGINARY {
			public String toModeString() {
				return "RnI";
			}
		}, 
		REAL_VS_IMAGINARY {
			public String toModeString() {
				return "RvI";
			}
		},
		TEN_LOG {
			public String toModeString() {
				return "10Log";
			}
			@Override
			public String toString() {
				return toModeString();
			}
		},
		TWENTY_LOG {
			public String toModeString() {
				return "20Log";
			}
			@Override
			public String toString() {
				return toModeString();
			}
		},
		X {
			public String toModeString() {
				return "X";
			}
		},
		Y {
			public String toModeString() {
				return "Y";
			}
		},
		Z {
			public String toModeString() {
				return "Z";
			}
		};
		
		public abstract String toModeString();
		
		/** convert from plot's mode string (e.g. CM= arg) to this enum. */
		public static PlotMode of(String modeString) {
			for (PlotMode plotMode : PlotMode.values()) {
				if (plotMode.toModeString().equals(modeString)) {
					return plotMode; // found match
				}
			}
			throw new IllegalArgumentException("Invalid plot mode string: " + modeString);
		}
	}
	
	public PlotSettings() {
	}

	/** copy constructor */
	public PlotSettings(PlotSettings settings) {
		this.frameSize  = settings.frameSize;
		this.sampleRate = settings.sampleRate;
		this.blockingOption = settings.blockingOption;
		this.minValue = settings.minValue;
		this.maxValue = settings.maxValue;
		this.plotType = settings.plotType;
		this.plotMode = settings.plotMode;
		this.enablePlotMenu = settings.enablePlotMenu;
	}

	public PlotSettings(final PlotType plotType) {
		this.plotType = plotType;
	}

	/**
	 * @param plotType
	 * @param plotMode
	 * @since 4.4
	 */
	public PlotSettings(final PlotType plotType, PlotMode plotMode) {
		this(plotType, plotMode, null, null);
	}
	
	/**
	 * @param plotType
	 * @param launchArgs
	 * @since 4.4
	 * @param launchSwitches
	 */
	public PlotSettings(final PlotType plotType, PlotMode plotMode, String launchArgs, String launchSwitches) {
		this.plotType = plotType;
		this.plotMode = plotMode;
		this.launchArgs = launchArgs;
		this.launchSwitches = launchSwitches;
	}

	/**
	 * @deprecated since 4.3 (not used)
	 */
	@Deprecated
	public PlotSettings(final Integer frameSize, final Double minValue, final Double maxValue, final Double sampleRate, final PlotType plotType) {
		this(frameSize, minValue, maxValue, Boolean.TRUE, sampleRate, plotType);
	}
	
	PlotSettings(final Integer frameSize, final Double minValue, final Double maxValue, final Boolean blockingOption, final Double sampleRate, final PlotType plotType) {
		super();
		this.frameSize = frameSize;
		this.sampleRate = sampleRate;
		this.blockingOption = blockingOption;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.plotType = plotType;
	}

	/**
	 * @deprecated since 4.3 as this should apply to the source Port(s).
	 */
	@Deprecated
	public Integer getFrameSize() {
		return this.frameSize;
	}

	/**
	 * @deprecated since 4.3 as this should apply to the source Port(s).
	 */
	@Deprecated
	public void setFrameSize(final Integer frameSize) {
		this.frameSize = frameSize;
	}

	public Double getMinValue() {
		return this.minValue;
	}

	public void setMinValue(final Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @deprecated since 4.3 as this should apply to the source Port(s).
	 */
	@Deprecated
	public Double getSampleRate() {
		return sampleRate;
	}

	/**
	 * @deprecated since 4.3 as this should apply to the source Port(s).
	 */
	@Deprecated
	public void setSampleRate(Double sampleRate) {
		this.sampleRate = sampleRate;
	}

	public PlotType getPlotType() {
		return plotType;
	}

	public void setPlotType(PlotType plotType) {
		this.plotType = plotType;
	}

	/**
	 * @deprecated since 4.3 as this should apply to the source Port(s).
	 */
	@Deprecated
	public Boolean getBlockingOption() {
		return blockingOption;
	}

	/**
	 * @deprecated since 4.3 as this should apply to the source Port(s).
	 */
	@Deprecated
	public void setBlockingOption(Boolean blockingOption) {
		this.blockingOption = blockingOption;
	}

	/**
	 * @since 4.4
	 */
	public PlotMode getPlotMode() {
		return plotMode;
	}

	/** 
	 * @since 4.4
	 */
	public void setPlotMode(PlotMode plotMode) {
		this.plotMode = plotMode;
	}

	/** 
	 * @since 4.4
	 */
	public boolean isEnablePlotMenu() {
		return enablePlotMenu;
	}

	/** 
	 * @since 4.4
	 */
	public void setEnablePlotMenu(boolean enablePlotMenu) {
		this.enablePlotMenu = enablePlotMenu;
	}

	/** 
	 * @since 4.4
	 */
	public String getLaunchArgs() {
		return launchArgs;
	}

	/** 
	 * @since 4.4
	 */
	public void setLaunchArgs(String launchArgs) {
		this.launchArgs = launchArgs;
	}

	/** 
	 * @since 4.4
	 */
	public String getLaunchSwitches() {
		return launchSwitches;
	}

	/** 
	 * @since 4.4
	 */
	public void setLaunchSwitches(String launchSwitches) {
		this.launchSwitches = launchSwitches;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((frameSize == null) ? 0 : frameSize.hashCode());
		result = prime * result + ((sampleRate == null) ? 0 : sampleRate.hashCode());
		result = prime * result + ((maxValue == null) ? 0 : maxValue.hashCode());
		result = prime * result + ((minValue == null) ? 0 : minValue.hashCode());
		result = prime * result + ((plotType == null) ? 0 : plotType.hashCode());
		result = prime * result + ((blockingOption == null) ? 0 : blockingOption.hashCode());
		result = prime * result + ((plotMode == null) ? 0 : plotMode.hashCode());
		result = prime * result + ((enablePlotMenu) ? 1231 : 1237);
		result = prime * result + ((launchArgs == null) ? 0 : launchArgs.hashCode());
		result = prime * result + ((launchSwitches == null) ? 0 : launchSwitches.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PlotSettings)) {
			return false;
		}
		PlotSettings other = (PlotSettings) obj;
		if (frameSize == null) {
			if (other.frameSize != null) {
				return false;
			}
		} else if (!frameSize.equals(other.frameSize)) {
			return false;
		}
		if (sampleRate == null) {
			if (other.sampleRate != null) {
				return false;
			}
		} else if (!sampleRate.equals(other.sampleRate)) {
			return false;
		}
		if (blockingOption == null) {
			if (other.blockingOption != null) {
				return false;
			}
		} else if (!blockingOption.equals(other.blockingOption)) {
			return false;
		}
		if (maxValue == null) {
			if (other.maxValue != null) {
				return false;
			}
		} else if (!maxValue.equals(other.maxValue)) {
			return false;
		}
		if (minValue == null) {
			if (other.minValue != null) {
				return false;
			}
		} else if (!minValue.equals(other.minValue)) {
			return false;
		}
		if (plotType != other.plotType) {
			return false;
		}
		if (plotMode == null) {
			if (other.plotMode != null) {
				return false;
			}
		} else if (!plotMode.equals(other.plotMode)) {
			return false;
		}
		if (enablePlotMenu != other.enablePlotMenu) {
			return false;
		}
		if (launchArgs == null) {
			if (other.launchArgs != null) {
				return false;
			}
		} else if (!launchArgs.equals(other.launchArgs)) {
			return false;
		}
		if (launchSwitches == null) {
			if (other.launchSwitches != null) {
				return false;
			}
		} else if (!launchSwitches.equals(other.launchSwitches)) {
			return false;
		}
		return true;
	}
	
}
