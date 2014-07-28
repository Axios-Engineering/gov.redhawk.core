package CF.jni;

public class _TestableObjectStub extends omnijni.ObjectImpl implements CF.TestableObject
{
  public _TestableObjectStub ()
  {
  }

  protected _TestableObjectStub (long ref)
  {
    super(ref);
  }

  public void runTest (int testid, CF.PropertiesHolder testValues)
  {
    runTest(this.ref_, testid, testValues);
  }
  private static native void runTest (long __ref__, int testid, CF.PropertiesHolder testValues);

  private static String __ids[] = {
    "IDL:CF/TestableObject:1.0",
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
