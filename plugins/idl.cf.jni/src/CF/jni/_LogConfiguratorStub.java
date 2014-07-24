package CF.jni;
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

public class _LogConfiguratorStub extends omnijni.ObjectImpl implements CF.LogConfigurator
{
  public _LogConfiguratorStub ()
  {
  }

  protected _LogConfiguratorStub (long ref)
  {
    super(ref);
  }

  public int getLogLevel (String config_id)
  {
    return getLogLevel(this.ref_, config_id);
  }
  private static native int getLogLevel (long __ref__, String config_id);

  public String getLogConfig (String config_id)
  {
    return getLogConfig(this.ref_, config_id);
  }
  private static native String getLogConfig (long __ref__, String config_id);

  private static String __ids[] = {
    "IDL:CF/LogConfigurator:1.0",
  };

  public String[] _ids ()
  {
    return (String[])__ids.clone();
  }

  static {
    System.loadLibrary("ossiecfjni");
  }

  protected native long _get_object_ref(long ref);
  protected native long _narrow_object_ref(long ref);
}
