package CF.jni;

public class _FileSystemStub extends omnijni.ObjectImpl implements CF.FileSystem
{
  public _FileSystemStub ()
  {
  }

  protected _FileSystemStub (long ref)
  {
    super(ref);
  }

  public void remove (String fileName)
  {
    remove(this.ref_, fileName);
  }
  private static native void remove (long __ref__, String fileName);

  public void copy (String sourceFileName, String destinationFileName)
  {
    copy(this.ref_, sourceFileName, destinationFileName);
  }
  private static native void copy (long __ref__, String sourceFileName, String destinationFileName);

  public void move (String sourceFileName, String destinationFileName)
  {
    move(this.ref_, sourceFileName, destinationFileName);
  }
  private static native void move (long __ref__, String sourceFileName, String destinationFileName);

  public boolean exists (String fileName)
  {
    return exists(this.ref_, fileName);
  }
  private static native boolean exists (long __ref__, String fileName);

  public CF.FileSystemPackage.FileInformationType[] list (String pattern)
  {
    return list(this.ref_, pattern);
  }
  private static native CF.FileSystemPackage.FileInformationType[] list (long __ref__, String pattern);

  public CF.File create (String fileName)
  {
    return create(this.ref_, fileName);
  }
  private static native CF.File create (long __ref__, String fileName);

  public CF.File open (String fileName, boolean read_Only)
  {
    return open(this.ref_, fileName, read_Only);
  }
  private static native CF.File open (long __ref__, String fileName, boolean read_Only);

  public void mkdir (String directoryName)
  {
    mkdir(this.ref_, directoryName);
  }
  private static native void mkdir (long __ref__, String directoryName);

  public void rmdir (String directoryName)
  {
    rmdir(this.ref_, directoryName);
  }
  private static native void rmdir (long __ref__, String directoryName);

  public void query (CF.PropertiesHolder fileSystemProperties)
  {
    query(this.ref_, fileSystemProperties);
  }
  private static native void query (long __ref__, CF.PropertiesHolder fileSystemProperties);

  private static String __ids[] = {
    "IDL:CF/FileSystem:1.0",
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
