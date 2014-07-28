package CF.jni;

public class _PortStub extends omnijni.ObjectImpl implements CF.Port
{
  public _PortStub ()
  {
  }

  protected _PortStub (long ref)
  {
    super(ref);
  }

  public void connectPort (org.omg.CORBA.Object connection, String connectionId)
  {
    connectPort(this.ref_, connection, connectionId);
  }
  private static native void connectPort (long __ref__, org.omg.CORBA.Object connection, String connectionId);

  public void disconnectPort (String connectionId)
  {
    disconnectPort(this.ref_, connectionId);
  }
  private static native void disconnectPort (long __ref__, String connectionId);

  private static String __ids[] = {
    "IDL:CF/Port:1.0",
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
