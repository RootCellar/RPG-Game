public class Class
{
    private String name;
    private double bhp;
    private double bmana;
    private double batk;
    private double bdef;
    private double bmatk;
    public Class(String n,double hp, double mana, double atk, double def, double matk) {
        name=n;
        bhp=hp;
        bmana=mana;
        batk=atk;
        bdef=def;
        bmatk=matk;
    }
    
    public String toString() {
        return "Name: "+name+"\n"+
               "Base HP: "+bhp+"\n"+
               "Base Mana: "+bmana+"\n"+
               "Base Attack: "+batk+"\n"+
               "Base Defense: "+bdef+"\n"+
               "Base Magic Attack: "+bmatk+"\n";
    }
    
    public String getName() {
        return name;
    }
    
    public double getBHp() {
        return bhp;
    }
    
    public double getBMana() {
        return bmana;
    }
    
    public double getBAtk() {
        return batk;
    }
    
    public double getBDef() {
        return bdef;
    }
    
    public double getBMatk() {
        return bmatk;
    }
}