public class Race
{
    private String name;
    private double regenmult;
    private double hpmult;
    private double atkmult;
    private double defmult;
    private double matkmult;
    private double manamult;
    public Race(String n,double r, double h, double a, double d, double matk, double mana) {
        name=n;
        regenmult=r;
        hpmult=h;
        atkmult=a;
        defmult=d;
        matkmult=matk;
        manamult=mana;
    }
    
    public String toString() {
        return "Name: "+name+"\n"+
               "Regen Mult: "+regenmult+"\n"+
               "Hp Mult: "+hpmult+"\n"+
               "Atk Mult: "+atkmult+"\n"+
               "Def Mult: "+defmult+"\n"+
               "MAtk Mult: "+matkmult+"\n"+
               "Mana Mult: "+manamult+"\n";
    }
    
    public String getName() {
        return name;
    }
    
    public double getRegenMult() {
        return regenmult;
    }
    
    public double getAtkMult() {
        return atkmult;
    }
    
    public double getDefMult() {
        return defmult;
    }
    
    public double getHpMult() {
        return hpmult;
    }
    
    public double getMAtkMult() {
        return matkmult;
    }
    
    public double getManaMult() {
        return manamult;
    }
}