package MODELS;

public class TABLE {
    public enum availability
    {
        occupy,free
    }
    private int id;
    private int capasity;
    private availability status;
    public TABLE(int id,int capasity)
    {
        this.id=id;
        this.capasity=capasity;
        this.status=availability.free;
    }
    public int getid()
    {
        return id;
    }
    public int getCapasity()
    {
        return capasity;
    }
    public availability getStatus()
    {
        return status;
    }
    public void booking()
    {
        status=availability.occupy;
    }
    public void freenow()
    {
        status=availability.free;
    }

}
