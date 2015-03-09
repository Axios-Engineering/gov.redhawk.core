package CF.jni;

public class _EventRegistrantIteratorStub extends omnijni.ObjectImpl implements CF.EventRegistrantIterator
{
  public _EventRegistrantIteratorStub ()
  {
  }

  protected _EventRegistrantIteratorStub (long ref)
  {
    super(ref);
  }

  public boolean next_one (CF.EventChannelManagerPackage.EventRegistrantHolder er)
  {
    return next_one(this.ref_, er);
  }
  private static native boolean next_one (long __ref__, CF.EventChannelManagerPackage.EventRegistrantHolder er);

  public boolean next_n (int how_many, CF.EventChannelManagerPackage.EventRegistrantListHolder erl)
  {
    return next_n(this.ref_, how_many, erl);
  }
  private static native boolean next_n (long __ref__, int how_many, CF.EventChannelManagerPackage.EventRegistrantListHolder erl);

  public void destroy ()
  {
    destroy(this.ref_);
  }
  private static native void destroy (long __ref__);

  private static String __ids[] = {
    "IDL:CF/EventRegistrantIterator:1.0",
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
