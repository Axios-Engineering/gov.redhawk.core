package org.omg.CosEventComm.jni;

public class _PullSupplierStub extends omnijni.ObjectImpl implements org.omg.CosEventComm.PullSupplier
{
  public _PullSupplierStub ()
  {
  }

  protected _PullSupplierStub (long ref)
  {
    super(ref);
  }

  public org.omg.CORBA.Any pull ()
  {
    return pull(this.ref_);
  }
  private static native org.omg.CORBA.Any pull (long __ref__);

  public org.omg.CORBA.Any try_pull (org.omg.CORBA.BooleanHolder has_event)
  {
    return try_pull(this.ref_, has_event);
  }
  private static native org.omg.CORBA.Any try_pull (long __ref__, org.omg.CORBA.BooleanHolder has_event);

  public void disconnect_pull_supplier ()
  {
    disconnect_pull_supplier(this.ref_);
  }
  private static native void disconnect_pull_supplier (long __ref__);

  private static String __ids[] = {
    "IDL:omg.org/CosEventComm/PullSupplier:1.0",
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
