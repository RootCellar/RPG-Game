public class Character
{
    private String name;
    private int xp;
    private int level;
    private double hp;
    private double mana;
    private double mmana;
    private double mHp;
    private double atk;
    private double def;
    private double matk;
    private double mult;
    private Class type;
    private boolean isAlive;
    public Character(String n,Class c,double m) {
        name=n;
        type=c;
        mult=m;
    }
    
    public void heal(double n) {
        hp+=n;
    }
    
    public void heal() {
        hp=mHp;
    }
    
    public void damage(double n) {
        hp-=n;
    }
    
    public void die() {
        isAlive=false;
    }
    
    public void revive() {
        isAlive=true;
        hp=mHp;
    }
    
    public boolean isAlive() {
        return isAlive;
    }

    public void setClass(Class t) {
        type=t;
    }

    public void setXp(int x) {
        xp=x;
        checkXp();
    }

    public void addXp(int x) {
        xp+=x;
        checkXp();
    }

    public void calcStats() {
        calcLevel();
        calcAtk();
        calcDef();
        calcMAtk();
        calcHp();
        calcMana();
    }
    
    public void calcMana() {
        mmana=type.getBMana()*Math.pow(mult,level);
    }

    public void calcHp() {
        mHp=type.getBHp()*Math.pow(mult,level);
    }

    public void calcAtk() {
        atk=type.getBAtk()*Math.pow(mult,level);
    }

    public void calcDef() {
        def=type.getBDef()*Math.pow(mult,level);
    }

    public void calcMAtk() {
        matk=type.getBMatk()*Math.pow(mult,level);
    }

    public void calcLevel() {
        int lvl = (int)Math.sqrt(Math.sqrt(xp));
        if(lvl<1) {
            lvl=1;
        }
        level=lvl;
        checkXp();
    }
    
    public void checkXp() {
        if(xp>100000000) {
            xp=100000000;
        }
    }
}